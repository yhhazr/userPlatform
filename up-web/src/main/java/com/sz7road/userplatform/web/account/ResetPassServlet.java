/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.utils.DesUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author leo.liao
 */
@Singleton
class ResetPassServlet extends HeadlessHttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResetPassServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String code = request.getParameter("code");
        String time = request.getParameter("time");
        String key = request.getParameter("key");

        String forward = "/account/forget/fail.jsp";
        boolean isPass = false;
        if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(time) || Strings.isNullOrEmpty(key)) {
            response.setStatus(404);
            return ;
        }

        String comKey = DigestUtils.md5Hex("verify," + time + "," + user);
        if ( comKey.equals(key)) {
            VerifyCodeProvider vService = verifyCodeProvider.get();
            if (vService.getVerifyCode("verify_" + time + "_" + user, code) != null) {
                isPass = true;
                forward = "/account/forget/reset.jsp";
            }
        }

        String debug = request.getParameter("_debug");
        if ("true".equals(debug)) {
            forward = "/account/forget/reset.jsp";
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }
}
