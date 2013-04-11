/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.sz7road.userplatform.pay.GenericPayLocatorBean;
import com.sz7road.userplatform.pay.PayHandler;
import com.sz7road.userplatform.pay.PayLocatorBean;
import com.sz7road.userplatform.pay.PayManager;
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
class AssertServlet extends HeadlessServlet {

    @Override
    protected void doService(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {

        final String servletPath = request.getServletPath();
        if (servletPath.startsWith("/Assert")) {
            final char[] chars = {'0', '0'};
            servletPath.getChars(servletPath.length() - 2, servletPath.length(), chars, 0);
            final PayLocatorBean bean = new GenericPayLocatorBean(getInstance(PayManager.class), chars[0], Integer.parseInt(Character.toString(chars[1])));
            final PayHandler handler = bean.getHandler();
            if (null != handler) {
                handler.callback(request, response);
            }
        }
    }
}
