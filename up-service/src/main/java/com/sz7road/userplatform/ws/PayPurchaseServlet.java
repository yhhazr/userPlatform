package com.sz7road.userplatform.ws;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;

import com.sz7road.userplatform.ppay.GenericPayBean;
import com.sz7road.userplatform.ppay.PayHandler;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

/**
 * 支付购买的商品
 * @author leo.liao
 *
 */
@Singleton
@Parameter(method = "POST", value = {"_c:", "_s:"})
public class PayPurchaseServlet extends HeadlessServlet
{
    @Override
    protected void doPost(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        final GenericPayBean payBean = getInstance(GenericPayBean.class);

        if (null != payBean) {
            // the PayBean had already finished parsing the request parameters.
            final PayHandler handler = payBean.getHandler();
            if (null != handler) {
                handler.process(request, response);
                return;
            }
        }

        // something caught wrong, just response that we don't have the service now.
        response.setStatus(404);
    }
}
