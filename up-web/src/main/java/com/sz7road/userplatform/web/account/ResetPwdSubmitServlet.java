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
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.utils.DesUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
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
class ResetPwdSubmitServlet extends HeadlessHttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResetPwdSubmitServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProviderProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String un = request.getParameter("un");
        String ut = request.getParameter("ut");
        String _c = request.getParameter("_c");
        String pwd = request.getParameter("pwd");
        String r_pwd = request.getParameter("r_pwd");
        Msg msg = new Msg(0, "重置密码成功");
        try {
            if (Strings.isNullOrEmpty(un) || Strings.isNullOrEmpty(ut) || Strings.isNullOrEmpty(_c)) {
                throw new IllegalArgumentException("不可解析的请求");
            }
            String _un = DesUtils.decrypt(un);
            String _ut = DesUtils.decrypt(ut);
            String code = DesUtils.decrypt(_c);
            if (Strings.isNullOrEmpty(_un) || Strings.isNullOrEmpty(_ut) || Strings.isNullOrEmpty(code)) {
                throw new IllegalArgumentException("不可解析的请求");
            }
            long timestamp = Long.parseLong(_ut);
            if (System.currentTimeMillis() > timestamp) {
                throw new IllegalArgumentException("链接已过期");
            }
            VerifyCodeProvider verifyCodeProvider = verifyCodeProviderProvider.get();
            VerifyCode verifyCode = verifyCodeProvider.getVerifyCode("verify_" + timestamp + "_" + _un, code);
            if (verifyCode == null) {
                throw new IllegalArgumentException("链接失效");
            }
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(_un);
            if (userAccount == null) {
                throw new IllegalArgumentException("用户不存在");
            }
            if (Strings.isNullOrEmpty(pwd) || Strings.isNullOrEmpty(r_pwd)) {
                throw new IllegalArgumentException("密码不能为空");
            }
            if (!pwd.equals(r_pwd)) {
                throw new IllegalArgumentException("前后输入密码不一致");
            }
            int ret = userService.resetPwd(userAccount, pwd);
            if (ret <= 0) {
                msg.setCode(0);
                msg.setMsg("重置密码时出现异常");
            }
            verifyCodeProvider.delete(verifyCode);
        } catch (Exception e) {
            msg.setCode(1);
            msg.setMsg(e.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        PrintWriter writer = response.getWriter();
        writer.write(resp);
        writer.flush();
        writer.close();
    }
}
