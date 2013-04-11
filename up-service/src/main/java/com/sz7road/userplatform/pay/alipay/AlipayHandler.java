/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay.alipay;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pay.GenericPayHandler;
import com.sz7road.userplatform.pay.PayBean;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.PayService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.Headend;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * @author jeremy
 */
@RequestScoped
public class AlipayHandler extends GenericPayHandler {

    private static final Logger log = LoggerFactory.getLogger(AlipayHandler.class.getName());

    @Override
    public void process(final HeadlessServletRequest request, final HeadlessServletResponse response) {
        try {
            final PayBean payBean = getInstance(AlipayPayBean.class);

            if (payBean.isAvailableForSubmit()) {
                final AlipayPostBean postBean = getInstance(AlipayPostBean.class);
                final PayService payService = getInstance(PayService.class);
                final UserService userService = getInstance(UserService.class);
                final UserAccount account = userService.findAccountByUserName(payBean.getUserName());

                OrderObject order;
                if (null != account && null != (order = payService.submitOrder(account, payBean))) {
                    postBean.setDefaultBank(payBean.getSubTag());
                    postBean.setTotalFree(payBean.getPayAmount());
                    postBean.setOutTradeNo(payBean.getOrderId());
                    postBean.setBody(payBean.getDescription());
                    postBean.setPaymentType("1");
                    postBean.setExtraCommonParam(payService.getOrderSign(order));

                    if (!Strings.isNullOrEmpty(payBean.getSubTag())) {
                        postBean.setPayMethod("bankPay");
                    }
                    postBean.setShowUrl(ConfigurationUtils.get("alipay.showUrl"));
                    postBean.setReturnUrl(getAssertUrl(ConfigurationUtils.get("alipay.returnUri")) + payBean.getChannelTag());
                    postBean.setNotifyUrl(getAssertUrl(ConfigurationUtils.get("alipay.notifyUri")) + payBean.getChannelTag());
                    postBean.setSubject(getServerName(payBean));
                    postBean.updateSign();
                    Headend.redirectForm(response, postBean.getGateWayURL(), "GET", postBean.toMap(), true); //组装成一个form，然后response写过去。就转到充值渠道那边了。
                } else {
                    // 未找到用户
                    response.sendError(500);
                }
            } else {
                response.sendError(404);
            }
        } catch (final Exception e) {
            log.error("支付宝交易常：{}", e.getMessage(), e);
            response.setStatus(404);
        }
    }

    @Override
    public void callback(HeadlessServletRequest request, HeadlessServletResponse response) {
        try {
            final AlipayCallbackBean callbackBean = getInstance(AlipayCallbackBean.class);
            boolean notify = callbackBean.isNotify();

            callbackBean.updateSign();

            final String mysign = callbackBean.getSign();
            final Map<String, Object> params = callbackBean.toMap();

            String responseTxt = "true";
            if (params.get("notify_id") != null && !Strings.isNullOrEmpty(params.get("notify_id").toString())) {
                responseTxt = verifyResponse(params.get("notify_id").toString());
            }

            String sign = "";
            if (params.get("sign") != null && !Strings.isNullOrEmpty(params.get("sign").toString())) {
                sign = params.get("sign").toString();
            }

            //验证
            //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
            //mysign与sign不等，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
            if (mysign.equals(sign) && responseTxt.equals("true")) {
                if (callbackBean.getTradeStatus().equals("TRADE_FINISHED") || callbackBean.getTradeStatus().equals("TRADE_SUCCESS")) {
                    // 进行处理
                    final PayService payService = getInstance(PayService.class);
                    final OrderObject orderObject = payService.getOrder(callbackBean.getOrder());

                    if (null != orderObject && payService.isValidOrderSign(orderObject, callbackBean.getOrderSign())) {
                        boolean flag = payService.isWaitForClosed(orderObject);
                        flag = flag && orderObject.getAmount() == callbackBean.getAmount();

                        if (flag) {
                            orderObject.setEndOrder(callbackBean.getEndOrder());
                            orderObject.setAssertTime(new Timestamp(new Date().getTime()));
                            payService.assertOrder(orderObject);
                            // 发送游戏金币并异步记录发放日志。
                            payService.rechargeGolds(orderObject);
                        }
                    } else {
                        log.error("支付宝充值确认回调网关确认异常：{}", callbackBean.getSTR());
                    }
                }

                if (notify) {
                    response.getWriter().println("success");
                }

            } else {
                // 跳转到失败页面。
                if (notify) {
                    response.getWriter().println("fail");
                }
            }

            if (!notify)
                response.sendRedirect(getResultUrl(callbackBean.getOrder()));

        } catch (final Exception e) {
            log.error("支付宝充值确认回调网关异常：{}", e.getMessage());
            response.setStatus(404);
        }
    }

    private String verifyResponse(String notifyId) {
        final String partner = ConfigurationUtils.get("alipay.merchantAcctId");
        final String verifyUrl = ConfigurationUtils.get("alipay.verifyUrl") + "partner=" + partner + "&notify_id=" + notifyId;

        try {
            URL url = new URL(verifyUrl);
            log.info("VerifyUrl: {}", verifyUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return reader.readLine();
        } catch (final IOException e) {
            log.error("支付宝充值确认回调对方验证地址异常：{}", e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

}
