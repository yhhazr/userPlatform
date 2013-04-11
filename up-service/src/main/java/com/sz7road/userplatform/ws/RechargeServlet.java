/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.google.common.base.Strings;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.service.PayService;
import com.sz7road.utils.Backend;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author jeremy
 */
@Singleton
public class RechargeServlet extends HeadlessServlet {

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        final String orderId = request.getNotNullParameter("order");

        if (Strings.isNullOrEmpty(orderId)) {
            response.sendError(404);
            return;
        }

        log.info("接收的订单号："+orderId);

        PayService instance = getInstance(PayService.class);
        OrderObject order = instance.getOrder(orderId);
         log.info("订单信息："+order.toString());
        final Backend.BackendResponse response1 = instance.rechargeGolds(order, 0);

        if (null != response1) {
            response.getWriter().println(String.format("%d => %s", response1.getResponseCode(), response1.getResponseContent()));
            response.getWriter().flush();
        }
    }
}
