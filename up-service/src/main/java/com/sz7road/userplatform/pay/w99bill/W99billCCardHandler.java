package com.sz7road.userplatform.pay.w99bill;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pay.GenericPayBean;
import com.sz7road.userplatform.pay.PayBean;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.service.PayService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.Headend;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author jeremy
 */
@RequestScoped
public class W99billCCardHandler extends W99billHandler {

    private static final Logger log = LoggerFactory.getLogger(W99billCCardHandler.class);

    @Override
    protected W99billCCardPostBean getPostBean() {
        return getInstance(W99billCCardPostBean.class);
    }

    @Override
    protected void setupExtras(HeadlessServletRequest request, HeadlessServletResponse response, W99billPostBean bean, PayBean payBean) throws Exception {
        super.setupExtras(request, response, bean, payBean);
        bean.setPayerName(payBean.getUserName());
        //bean.setPayType(payBean.getSubTag());
        bean.setPayType("42");  //固定值
        bean.setSignType("1");  //固定值
        //bean.setExt2("");
        //bean.setExt2(payBean.getSubTag());
        ((W99billCCardPostBean)bean).setBossType(payBean.getSubTag());
    }

    @Override
    public void process(final HeadlessServletRequest request, final HeadlessServletResponse response) {
        final PayBean payBean = getInstance(GenericPayBean.class);

        try {

            final PayService payService = getInstance(PayService.class);

            OrderObject order;
            if (payBean.isAvailableForSubmit() && null != (order = payService.submitOrder(payBean))) {
                final W99billPostBean bean = (W99billPostBean) getPostBean();
                bean.setOrderId(payBean.getOrderId());
                bean.setProductName(getProductName(payBean));
                bean.setProductDesc(getProductName(payBean));
                //bean.setProductDesc(payBean.getDescription());
                bean.setExt1(payService.getOrderSign(order));
                bean.setExt2(getProductName(payBean));
                bean.setBgUrl(getAssertUrl(ConfigurationUtils.get("99bill.bgUri")) + payBean.getChannelTag());
                bean.setPageUrl(getAssertUrl(ConfigurationUtils.get("99bill.pageUri")) + payBean.getChannelTag());

                setupExtras(request, response, bean, payBean);

                bean.updateSign();

                Headend.redirectForm(response, bean.getGatewayUrl(), "POST", bean.toMap());
            } else {
                response.sendError(404);
            }
        } catch (final Exception e) {
            log.error("快钱充值接口后端调用异常：{}", e.getMessage());
        }
    }

    @Override
    public void callback(HeadlessServletRequest request, HeadlessServletResponse response) {
        String orderId = "";
        String strResultUrl = "";
        try {
            final W99billCCardCallbackBean callbackBean = getInstance(W99billCCardCallbackBean.class);
            orderId = callbackBean.getOrderId();
            if (!callbackBean.isNotify()) {
                strResultUrl = getResultUrl(callbackBean.getOrderId());
            }

            log.info("订单通知:" + callbackBean.getSTR());
            log.info("合法:" + callbackBean.isValid());
            log.info("结果(10):" + callbackBean.getPayResult());

            if (callbackBean.isValid()) {
                switch (callbackBean.getPayResult()) {
                    case 10:
                        final PayService payService = getInstance(PayService.class);
                        final OrderObject order = payService.getOrder(orderId);

                        if (null != order && payService.isValidOrderSign(order, callbackBean.getOrderSign())) {
                            if (payService.isWaitForClosed(order)) {
                                order.setEndOrder(callbackBean.getDealId());
                                order.setAssertTime(new Timestamp(new Date().getTime()));
                                payService.assertOrder(order);

                                // 发放游戏币
                                payService.rechargeGolds(order);
                            }
                        }
                        break;
                    default:
                        break;
                }
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

            if (!Strings.isNullOrEmpty(strResultUrl))
                response.sendRedirect(strResultUrl);
        } catch (final IOException e) {
        }
    }

}
