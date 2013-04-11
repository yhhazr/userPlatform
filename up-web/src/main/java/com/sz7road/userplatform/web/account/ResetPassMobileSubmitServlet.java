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
class ResetPassMobileSubmitServlet extends HeadlessHttpServlet {

    private final static Logger log = LoggerFactory.getLogger(ResetPassMobileSubmitServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String code = request.getParameter("code");
        String time = request.getParameter("time");
        String pass = request.getParameter("newPass");

        Msg msg = new Msg(0, "验证码已过期");
        boolean isPass = false;
        if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(time)
                || Strings.isNullOrEmpty(pass)) {
            response.setStatus(404);
            return ;
        }

        VerifyCodeProvider vService = verifyCodeProvider.get();
        UserService uService = userServiceProvider.get();
        UserAccount account = null;
        if (vService.checkVerifyCode("message_" + time + "_" + user, code)) {
            account = uService.findAccountByUserName(user);
            if (account != null) {
                isPass = true;
            } else {
                msg.setMsg("用户不存在");
            }
        }

        if (isPass) {
            int ret = uService.resetPwd(account, pass);
            if (ret > 0) {
                msg.setCode(1);
                msg.setMsg("修改成功");
            }
        }

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        writer.write(resp);
        writer.flush();
        writer.close();
    }
}
