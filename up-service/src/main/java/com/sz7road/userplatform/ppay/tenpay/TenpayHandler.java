/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.tenpay;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.ppay.GenericPayHandler;
import com.sz7road.userplatform.ppay.PayBean;
import com.sz7road.userplatform.pay.tenpay.tenpayUtil.RequestHandler;
import com.sz7road.userplatform.pay.tenpay.tenpayUtil.client.ClientResponseHandler;
import com.sz7road.userplatform.pay.tenpay.tenpayUtil.client.TenpayHttpClient;
import com.sz7road.userplatform.pojos.ProductOrder;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.ProductPayService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.*;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-20
 * Time: 上午3:59
 * Description: 财付通充值处理类
 */
@RequestScoped
public class TenpayHandler extends GenericPayHandler {

    private static final Logger log = LoggerFactory.getLogger(TenpayHandler.class.getName());

    private static final int MAGNIFICATION = 100; //倍率  1块钱=100分

    @Inject
    private Provider<RequestHandler> requestHandlerProvider;
    @Inject
    private Provider<TenpayHttpClient> tenpayHttpClientProvider;
    @Inject
    private Provider<ClientResponseHandler> clientResponseHandlerProvider;

    @Override
    public void process(final HeadlessServletRequest request, final HeadlessServletResponse response) {
        try {
            final PayBean payBean = getInstance(TenpayPayBean.class);
            if (payBean.isAvailableForSubmit()) {
                final TenpayPostBean postBean = getInstance(TenpayPostBean.class);
                final ProductPayService payService = getInstance(ProductPayService.class);
                final UserService userService = getInstance(UserService.class);
                final UserAccount account = userService.findAccountByUserName(payBean.getUserName());

                ProductOrder order;
                if (null != account && null != (order = payService.submitOrder(account, payBean))) { //生成订单之后，请求充值url,并且传递参数。
                    postBean.setBank_type(payBean.getSubTag());//银行
                    postBean.setSpbill_create_ip(payBean.getClientIp());
                    postBean.setOut_trade_no(payBean.getOrderId()); //订单号
                    String characterEncoding = getCharacterEncoding(request, response);
                    postBean.setInput_charset(characterEncoding);
                    postBean.setBody(getProductName(payBean));
                    postBean.setTotal_fee((int) payBean.getPayAmount() * MAGNIFICATION);
                    postBean.setProduct_fee((int) payBean.getPayAmount() * MAGNIFICATION);
                    postBean.setReturn_url(ConfigurationUtils.get("tenpay.returnUri") + payBean.getChannelTag());
                    postBean.setNotify_url(ConfigurationUtils.get("tenpay.notifyUri") + payBean.getChannelTag());
                    postBean.createSign();

                    Headend.redirectFormForTenPay(response, postBean.getGateWayUrl(), "GET", postBean.getParameters(), true);
                } else {
                    // 未找到用户
                    response.sendError(500);
                }
            } else {
                response.sendError(404);
            }
        } catch (final Exception e) {
            log.error("财付通交易常：{}", e.getMessage(), e);
            response.setStatus(404);
        }
    }

    public void callbackReWrite(HeadlessServletRequest request, HeadlessServletResponse response) {
        try {
            final TenpayCallbackBean callbackBean = getInstance(TenpayCallbackBean.class);
            boolean notify = callbackBean.isNotify(); //确定是查询结果还是后台通知
            callbackBean.updateSign(); //根据得到的参数，重新进行签名
            final String mysign = callbackBean.getMySign();
            final Map<String, Object> params = callbackBean.getParameters();
            String sign = "";
            if (params.get("sign") != null && !Strings.isNullOrEmpty(params.get("sign").toString())) {
                sign = params.get("sign").toString();
            }
            //验证签名
            if (mysign.equalsIgnoreCase(sign)) {
                String responseTxt = null;
                if (notify) {//如果是notify_url
                    final ProductPayService payService = getInstance(ProductPayService.class);
                    final ProductOrder orderObject = payService.getOrder((String) params.get("out_trade_no"));
                    if (0 == orderObject.getStatus()) {
                        Map map = null;
                        String newSign = callbackBean.updateAndGetSign();
                        if (params.get("notify_id") != null && !Strings.isNullOrEmpty(params.get("notify_id").toString()) && !Strings.isNullOrEmpty(newSign)) {
                            responseTxt = verifyResponse(params.get("notify_id").toString(), newSign);//确认是从财付通端过来的
                            try {
                                map = XMLUtil.doXMLParse(responseTxt);//得到查询的结果
                                //获取返回参数
                                if (map != null && !map.isEmpty()) {
                                    String retcode = (String) map.get("retcode");
                                    String trade_state = (String) map.get("trade_state");
                                    String trade_mode = (String) map.get("trade_mode");
                                    if (isTenpaySign(map, callbackBean.getKey()) && "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
                                        // 进行处理
                                        if (null != orderObject && payService.isValidOrderSign(orderObject, callbackBean.getSign())) {
                                            boolean flag = payService.isWaitForClosed(orderObject);
                                            flag = flag && orderObject.getAmount() * MAGNIFICATION == callbackBean.getTotal_fee();
                                            if (flag) {
                                                orderObject.setEndOrder(callbackBean.getTransaction_id());
                                                orderObject.setAssertTime(new Timestamp(new Date().getTime()));
                                                payService.assertOrder(orderObject);
                                                // 发送游戏金币并异步记录发放日志。
                                                payService.rechargeGolds(orderObject);
                                                response.getWriter().println("success");
                                            }
                                        }
                                    } else {
                                        log.info("通知查询结果为失败!" + map.get("retmsg"));
                                        response.getWriter().println("fail");
                                    }
                                } else {
                                    log.info("通知查询失败!");
                                    response.getWriter().println("fail");
                                }
                            } catch (Exception ex) {
                                log.error("读取财付通通知信息异常或者业务功能出现异常!通知查询失败!");
                                response.getWriter().println("fail");
                                ex.printStackTrace();
                            }
                        }
                    } else if (1 == orderObject.getStatus()) {
                        response.getWriter().println("success");
                    }
                } else {//如果是return_url
                    response.sendRedirect(getResultUrl((String) params.get("out_trade_no")));
                }
            } else {

                if (notify) {
                    log.info("通知认证签名失败!");
                    response.getWriter().println("fail");
                } else {
                    log.info("return_url 认证签名失败!");
                    response.sendRedirect(getResultUrl((String) params.get("out_trade_no")));
                }
            }
        } catch (final Exception e) {
            log.error("财付通充值确认回调网关异常：{}", e.getMessage());
            response.setStatus(404);
            e.printStackTrace();
        }
    }

    @Override
    public void callback(HeadlessServletRequest request, HeadlessServletResponse response) {
        try {
            final TenpayCallbackBean callbackBean = getInstance(TenpayCallbackBean.class);
            boolean notify = callbackBean.isNotify(); //确定是查询结果还是后台通知
            callbackBean.updateSign(); //根据得到的参数，重新进行签名
            final String mysign = callbackBean.getMySign();
            final Map<String, Object> params = callbackBean.getParameters();
            String sign = "";
            if (params.get("sign") != null && !Strings.isNullOrEmpty(params.get("sign").toString())) {
                sign = params.get("sign").toString();
            }
            //验证签名
            if (mysign.equalsIgnoreCase(sign)) {
                if (notify) {//如果是notify_url
                    final ProductPayService payService = getInstance(ProductPayService.class);
                    final ProductOrder orderObject = payService.getOrder((String) params.get("out_trade_no"));
                    if (ProductOrder.PAY_FAIL == orderObject.getStatus()) {
                        String notify_id = (String) params.get("notify_id");
                        String key = (String) params.get("key");
                        //创建请求对象
                        RequestHandler queryReq = requestHandlerProvider.get();
                        //通信对象
                        TenpayHttpClient httpClient = tenpayHttpClientProvider.get();
                        //应答对象
                        ClientResponseHandler queryRes = clientResponseHandlerProvider.get();
                        //通过通知ID查询，确保通知来至财付通
                        queryReq.setKey(key);
                        queryReq.setGateUrl(ConfigurationUtils.get("tenpay.verifyUrl"));
                        queryReq.setParameter("partner", (String) params.get("partner"));
                        queryReq.setParameter("notify_id", notify_id);
                        httpClient.setTimeOut(5);
                        httpClient.setReqContent(queryReq.getRequestURL());
                        if (httpClient.call()) {
                            //设置结果参数
                            queryRes.setContent(httpClient.getResContent());
                            queryRes.setKey(key);
                            //获取返回参数
                            String retcode = queryRes.getParameter("retcode");
                            String trade_state = queryRes.getParameter("trade_state");
                            String trade_mode = queryRes.getParameter("trade_mode");
                            //判断签名及结果
                            if (queryRes.isTenpaySign() && "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
                                //处理业务开始
                                if (null != orderObject) {
                                    boolean flag = payService.isWaitForClosed(orderObject);
                                    flag = flag && orderObject.getAmount() * MAGNIFICATION == Integer.parseInt(queryRes.getParameter("total_fee"));
                                    if (flag) {
                                        orderObject.setEndOrder(queryRes.getParameter("transaction_id"));
                                        orderObject.setAssertTime(new Timestamp(new Date().getTime()));
                                        payService.assertOrder(orderObject);
                                        // 发送游戏金币并异步记录发放日志。
                                        payService.rechargeGolds(orderObject);
                                        response.getWriter().println("success");
                                    } else {
                                        response.getWriter().println("fail");
                                    }
                                }
                            } else {
                                //错误时，返回结果未签名，记录retcode、retmsg看失败详情。
                                log.info("查询验证签名失败或业务错误 :retcode:" + queryRes.getParameter("retcode") +
                                        " retmsg:" + queryRes.getParameter("retmsg"));
                                response.getWriter().println("fail");
                            }
                        }
                    } else if (ProductOrder.PAY_SUCCESS == orderObject.getStatus()) {
                        response.getWriter().println("success");
                    }
                } else {
                    //有可能因为网络原因，请求已经处理，但未收到应答。
                    response.sendRedirect(getResultUrl((String) params.get("out_trade_no")));
                }
            } else {
                if (notify) {
                    log.info("通知认证签名失败!");
                    response.getWriter().println("fail");
                } else {
                    log.info("return_url 认证签名失败!");
                    response.sendRedirect(getResultUrl((String) params.get("out_trade_no")));
                }
            }
        } catch (final Exception e) {
            log.error("财付通充值确认回调网关异常：{}", e.getMessage());
            response.setStatus(404);
            e.printStackTrace();
        }
    }


    /**
     * 再次判断前面是不是一样的
     *
     * @param map
     * @return
     */
    private boolean isTenpaySign(Map map, String key) {
        map = Maps.filterValues(map, new Predicate<Object>() {
            @Override
            public boolean apply(@Nullable Object value) {
                if (null == value || Strings.isNullOrEmpty(value.toString()))
                    return false;
                return true;
            }
        });
        final List<String> keys = Lists.newArrayList(map.keySet());
        Collections.sort(keys);
        final StringBuilder sb = new StringBuilder();
        for (String keyStr : keys) {
            if (!Strings.isNullOrEmpty(keyStr) && !keyStr.equals("sign") && !keyStr.equals("key"))
                appendQueryStringForTenPay(sb, keyStr, map.get(keyStr));
        }
        sb.append("&key=" + key);
        String sign = MD5Utils.MD5Encode(sb.toString(), (String) map.get("input_charset")).toLowerCase();
        return sign.equalsIgnoreCase((String) map.get("sign"));
    }


    private String verifyResponse(String notifyId, String sign) {
        final String partner = ConfigurationUtils.get("tenpay.merchantAcctId");
        final String verifyUrl = ConfigurationUtils.get("tenpay.verifyUrl") + "?notify_id="
                + notifyId + "&partner=" + partner + "&sign=" + sign;
        log.info("VerifyUrl: {}", verifyUrl);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notify_id", notifyId);
        map.put("partner", partner);
        map.put("sign", sign);
        String resultStr = "";
        try {
            Backend.BackendResponse back = Backend.post(ConfigurationUtils.get("tenpay.verifyUrl"), map);
            if (back == null) {
                return null;
            }
            resultStr = back.getResponseContent();
        } catch (final Exception e) {
            log.error("财付通充值确认回调对方验证地址异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return resultStr;
    }


    private String getCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {
        if (null == request || null == response) {
            return "GBK";
        }
        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc)) {
            enc = response.getCharacterEncoding();
        }
        if (null == enc || "".equals(enc)) {
            enc = "GBK";
        }
        return enc;
    }

    /**
     * 追加请求参数。
     *
     * @param returnStr
     * @param paramId
     * @param paramValue
     * @return <code>*returnStr</code>
     */
    public static    StringBuilder appendQueryStringForTenPay(StringBuilder returnStr, String paramId, Object paramValue) {
        if (null != returnStr && returnStr.length() > 0) {
            if (null != paramValue && null != paramValue.toString()) {
                returnStr.append('&').append(paramId).append('=').append(paramValue);
            }
        } else {
            if (null != paramValue && null != paramValue.toString()) {
                returnStr.append(paramId).append('=').append(paramValue);
            }
        }
        return returnStr;
    }



}
