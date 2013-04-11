/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.w99bill;

import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.ppay.GenericPayBean;
import com.sz7road.userplatform.ppay.GenericPayHandler;
import com.sz7road.userplatform.ppay.PayBean;
import com.sz7road.userplatform.pojos.ProductOrder;
import com.sz7road.userplatform.service.ProductPayService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.Headend;
import com.sz7road.web.SimplePostBean;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 快钱充值接口处理器。
 *
 * @author jeremy
 */
@RequestScoped
public class W99billHandler extends GenericPayHandler {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(W99billHandler.class);

    @Override
    public void process(final HeadlessServletRequest request, final HeadlessServletResponse response) {
        final PayBean payBean = getInstance(GenericPayBean.class);

        try {

            final ProductPayService payService = getInstance(ProductPayService.class);

            ProductOrder order;
            if (payBean.isAvailableForSubmit() && null != (order = payService.submitOrder(payBean))) {
                final W99billPostBean bean = (W99billPostBean) getPostBean();
                bean.setOrderId(payBean.getOrderId());
                bean.setProductName(getProductName(payBean));
                bean.setProductDesc(getProductName(payBean));
                //bean.setProductDesc(payBean.getDescription());
                //bean.setProductDesc(payBean.getDescription());
                bean.setExt1(payService.getOrderSign(order));
                bean.setBgUrl(getAssertUrl(ConfigurationUtils.get("99bill.bgUri")) + payBean.getChannelTag());
                bean.setPageUrl(getAssertUrl(ConfigurationUtils.get("99bill.pageUri")) + payBean.getChannelTag());

                setupExtras(request, response, bean, payBean);

                bean.updateSign();

                Headend.redirectForm(response, bean.getGatewayUrl(), "POST", bean.toMap());
            } else {
                log.error("快钱充值确认接口 - 发现篡改请求，非法提交支付确认信息：IP:{},UserName:{},OrderId:{},Amount:{}", new Object[]{payBean.getClientIp(),payBean.getUserName(),payBean.getOrderId(), payBean.getPayAmount()});
                response.sendError(404);
            }
        } catch (final Exception e) {
            log.error("快钱充值接口后端调用异常：{}", e.getMessage());
        }
    }

    /**
     * 获取提交到后端网关的表单对象。
     *
     * @return 表单对象
     */
    protected SimplePostBean getPostBean() {
        return getInstance(W99billPostBean.class);
    }

    /**
     * 设置额外配置。
     *
     * @param request
     * @param response
     * @param bean
     * @param payBean
     */
    protected void setupExtras(HeadlessServletRequest request, HeadlessServletResponse response, W99billPostBean bean, PayBean payBean) throws Exception {
        bean.setAmount((long) (payBean.getPayAmount() * 100));
//        bean.setAmount((long) (payBean.getPayAmount())); // test
        bean.setBankId(payBean.getSubTag());
        bean.setPayType("10");
    }

    @Override
    public void callback(HeadlessServletRequest request, HeadlessServletResponse response) {
        String orderId = "";
        try {
            final W99billCallbackBean callbackBean = getInstance(W99billCallbackBean.class);
            orderId = callbackBean.getOrderId();

            if (callbackBean.isValid()) {
                switch (callbackBean.getPayResult()) {
                    case 10:
                        final ProductPayService payService = getInstance(ProductPayService.class);
                        final ProductOrder order = payService.getOrder(orderId);

                        if (null != order && payService.isValidOrderSign(order, callbackBean.getOrderSign())) {
                            if (payService.isWaitForClosed(order)) {
                                order.setEndOrder(callbackBean.getDealId());
                                order.setAssertTime(new Timestamp(new Date().getTime()));
                                payService.assertOrder(order);

                                // 发放游戏币
                                payService.rechargeGolds(order);
                            }
                        } else {
                            log.warn("快钱：{}", callbackBean.getSTR());
                        }
                        break;
                    default:
                        break;
                }
            } else {
                log.warn("快钱网关回调验证失败：{}", callbackBean.getSTR());
            }

        } catch (final Exception e) {
            log.error("快钱充值确认回调网关异常：{}", e.getMessage());
            response.setStatus(404);
        }

        try {
            response.setContentType("text/html");
            final PrintWriter out = response.getWriter();
            //String redirecturl = ConfigurationUtils.get("gateway.domain") + ConfigurationUtils.get("pay.common.result.pageUri");
            String redirecturl = ConfigurationUtils.get("pay.common.result.pageUri");
            redirecturl = String.format(redirecturl, orderId);
            out.write("<result>1</result><redirecturl>" + redirecturl + "</redirecturl>");
        } catch (final IOException e) {
        }
    }
}
