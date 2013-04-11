/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.yeepay;

import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.ppay.GenericPayBean;
import com.sz7road.userplatform.ppay.GenericPayHandler;
import com.sz7road.userplatform.ppay.PayBean;
import com.sz7road.userplatform.pojos.ProductOrder;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.ProductPayService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.Headend;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 易宝充值处理组件。
 *
 * @author jeremy
 */
@RequestScoped
public class YeepayPayHandler extends GenericPayHandler {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(YeepayPayHandler.class.getName());

    @Override
    public void process(final HeadlessServletRequest request, final HeadlessServletResponse response) {
        try {
            final PayBean bean = getInstance(GenericPayBean.class);

            if (null != bean && bean.isAvailableForSubmit()) {
                final YeepayPostBean postBean = getInstance(YeepayPostBean.class);
                final UserService userService = getInstance(UserService.class);
                final ProductPayService payService = getInstance(ProductPayService.class);

                final UserAccount account = userService.findAccountByUserName(bean.getUserName());

                ProductOrder order;
                if (null != account && null != (order = payService.submitOrder(account, bean))) {
                    String assertUrl = getAssertUrl();
                    postBean.setBankId(bean.getSubTag());
                    //postBean.setProductName(bean.getDescription());
                    postBean.setProductName(getProductName(bean));
                    postBean.setProductDescription(bean.getDescription());
                    postBean.setOrder(bean.getOrderId());
                    postBean.setAmount(bean.getPayAmount());
                    postBean.setMp(payService.getOrderSign(order));
                    postBean.setCallbackUrl(assertUrl + bean.getChannelTag());
                    postBean.setNeedResponse(true);
                    postBean.updateSign();

                    response.setContentType("text/html;charset=" + ConfigurationUtils.get("yeepay.encoding"));
                    Headend.redirectForm(response, postBean.getPayUrl(), "POST", postBean.toMap());
                } else {
                    response.sendError(404);
                }
            } else {
                response.sendError(404);
            }
        } catch (final Exception e) {
            log.error("易宝充值异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void callback(final HeadlessServletRequest request, final HeadlessServletResponse response) {
        response.setContentType("text/html");
        try {
            final PrintWriter out = response.getWriter();
            final CallbackBean bean = getInstance(CallbackBean.class);
            if (null != bean && bean.isValid()) {
                if (bean.getCmd().equals("Buy") && bean.getCode().equals("1")) {
                    // 确认充值成功
                    final ProductPayService payService = getInstance(ProductPayService.class);
                    final ProductOrder order = payService.getOrder(bean.getOrder());

                    if (null != order && payService.isValidOrderSign(order, bean.getMp())) {
                        if (payService.isWaitForClosed(order)) {
                            // 如果还没有关闭该订单
                            order.setEndOrder(bean.getEndOrder());
                            order.setAssertTime(new Timestamp(new Date().getTime()));
                            payService.assertOrder(order);

                            // 发放游戏币
                            payService.rechargeGolds(order);
                        }

                        if (bean.getBtype().equals("1")) {
                            // 重定向
                            response.sendRedirect(getResultUrl(bean.getOrder()));
                        } else {
                            // 输出信息
                            out.print("SUCCESS");
                        }

                        return;
                    }
                }
            }
            // 篡改请求，非法提交支付确认信息。
            if (bean != null) {
                log.warn("易宝支付确认接口 - 发现篡改请求，非法提交支付确认信息：{}", bean.getSTR());
            }

            response.sendError(404);

        } catch (final Exception e) {
            log.error("易宝充值确认异常：{}", e.getMessage());
        }
    }

}
