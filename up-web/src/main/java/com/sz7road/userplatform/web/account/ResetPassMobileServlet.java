/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author leo.liao
 */
@Singleton
class ResetPassMobileServlet extends HeadlessHttpServlet {

    private final static Logger log = LoggerFactory.getLogger(ResetPassMobileServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String code = request.getParameter("code");
        String time = request.getParameter("time");

        Msg msg = new Msg(0, "验证码错误");
        boolean isPass = false;
        if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(time)) {
            response.setStatus(404);
            return ;
        }

        VerifyCodeProvider vService = verifyCodeProvider.get();
        if (vService.getVerifyCode("message_" + time + "_" + user, code) != null ) {
            msg.setCode(1);
            msg.setMsg("验证码正确");
        }

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        writer.write(resp);
        writer.flush();
        writer.close();
    }
}
