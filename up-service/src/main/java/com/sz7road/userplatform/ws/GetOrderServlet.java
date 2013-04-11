/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.sz7road.userplatform.service.OrderService;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.apache.http.protocol.HTTP;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 获取订单号网关服务。
 *
 * @author jeremy
 */
@Singleton
@Parameter({"_c:", "_s:"})
class GetOrderServlet extends HeadlessServlet {

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        final char channelId = request.getCharParameter("_c");
        final int subChannelId = request.getIntParameter("_s");

        if (channelId == 0 || subChannelId == 0) {
            notFound(response);
            return;
        }

        final OrderService orderDecorateService = getInstance(OrderService.class);
        if (orderDecorateService.isValidChannel(channelId, subChannelId)) {
            response.setContentType(HTTP.PLAIN_TEXT_TYPE);
            final PrintWriter out = response.getWriter();
            try {
                out.println(orderDecorateService.nextOrder(channelId, subChannelId));
                return;
            } finally {
                out.flush();
                out.close();
            }
        }
    }
}
