package com.sz7road.userplatform.ws;

import java.io.IOException;

import javax.servlet.ServletException;

import com.google.inject.Singleton;
import com.sz7road.userplatform.ppay.GenericPayLocatorBean;
import com.sz7road.userplatform.ppay.PayHandler;
import com.sz7road.userplatform.ppay.PayLocatorBean;
import com.sz7road.userplatform.ppay.PayManager;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

@Singleton
public class AssertPayServlet extends HeadlessServlet
{
    @Override
    protected void doService(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {

        final String servletPath = request.getServletPath();
        if (servletPath.startsWith("/AssertPay")) {
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
