package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Singleton
class GetUuidServlet extends HeadlessHttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PayServlet.class.getName());

    @Inject
    private Provider<PayManager> payManmagerProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //游戏id
        String gameId_str = request.getParameter("gameId");
        if (Strings.isNullOrEmpty(gameId_str)) {
            gameId_str = "1";
        }
        //设置游戏id
        int gid = Integer.valueOf(gameId_str);
        request.setAttribute("gameId", gid);

        try {
            String channel = request.getParameter("channel");
            if (Strings.isNullOrEmpty(channel)) {
                logger.error("channel error");
            }
            final PayManager payManager = payManmagerProvider.get();
            String orderId = payManager.nextOrder(channel.charAt(0));
            out.print(orderId);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("system error");
        }
    }
}
