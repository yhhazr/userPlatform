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
class ResetPwServlet extends HeadlessHttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResetPwServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProviderProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ui = request.getParameter("ui");
        String un = request.getParameter("un");
        String ue = request.getParameter("ue");
        String ut = request.getParameter("ut");
        String _c = request.getParameter("_c");
        if (Strings.isNullOrEmpty(ui) || Strings.isNullOrEmpty(un) || Strings.isNullOrEmpty(ue) || Strings.isNullOrEmpty(ut) || Strings.isNullOrEmpty(_c)) {
            response.setStatus(404);
            return;
        }
        try {
            int id = Integer.parseInt(DesUtils.decrypt(ui));
            String userName = DesUtils.decrypt(un);
            String email = DesUtils.decrypt(ue);
            String time = DesUtils.decrypt(ut);
            String code = DesUtils.decrypt(_c);
            if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(time) || Strings.isNullOrEmpty(code)) {
                throw new IllegalArgumentException("不可解析的请求");
            }
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(userName);
            if (userAccount == null) {
                throw new NullPointerException("帐号不存在");
            }
            String mail = userAccount.getEmail();
            if (Strings.isNullOrEmpty(mail)) {
                throw new IllegalArgumentException("未绑定邮箱");
            }
            if (!mail.equals(email)) {
                throw new IllegalArgumentException("输入的邮箱与绑定的邮箱不一致");
            }
            long timestamp = Long.parseLong(time);
            if (System.currentTimeMillis() > timestamp) {
                throw new IllegalArgumentException("链接已过期");
            }
            VerifyCodeProvider verifyCodeProvider = verifyCodeProviderProvider.get();
            VerifyCode verifyCode = verifyCodeProvider.getVerifyCode("verify_" + timestamp + "_" + userName, code);
            if (verifyCode == null) {
                throw new IllegalArgumentException("链接失效");
            }
            request.setAttribute("_u", userName);
            request.setAttribute("ui", ui);
            request.setAttribute("un", un);
            request.setAttribute("ue", ue);
            request.setAttribute("ut", ut);
            request.setAttribute("_c", _c);
            request.setAttribute("isPass", true);
        } catch (Exception ex) {
            LOGGER.error("重置密码验证出现异常,{}", ex.getMessage());
            request.setAttribute("isPass", false);
        }
        request.getRequestDispatcher("/account/resetpwd.jsp").forward(request, response);
    }
}
