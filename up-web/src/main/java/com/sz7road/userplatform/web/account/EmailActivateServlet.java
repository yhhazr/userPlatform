/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Log.LogType;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DesUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;

/**
 * @author leo.liao
 */

@Singleton
class EmailActivateServlet extends HeadlessHttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailActivateServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProviderProvider;
    @Inject
    private Provider<LogService> logServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ui = request.getParameter("ui");
        String un = request.getParameter("un");
        String ue = request.getParameter("ue");
        String ut = request.getParameter("ut");
        String _c = request.getParameter("c");
        String t = request.getParameter("t");
        String accountUri = ConfigurationUtils.get("host.accountUri");
        if (Strings.isNullOrEmpty(ui) || Strings.isNullOrEmpty(un) || Strings.isNullOrEmpty(ue)
                || Strings.isNullOrEmpty(ut) || Strings.isNullOrEmpty(_c) || Strings.isNullOrEmpty(t)) {
            response.setStatus(404);
            return;
        }
        try {
            int id = Integer.parseInt(DesUtils.decrypt(ui));
            String userName = DesUtils.decrypt(un);
            String email = DesUtils.decrypt(ue);
            String time = DesUtils.decrypt(ut);
            String code = DesUtils.decrypt(_c);
            String type = DesUtils.decrypt(t);

            long timestamp = Long.parseLong(time);
            if (System.currentTimeMillis() > timestamp) {
                throw new IllegalArgumentException("链接已过期");
            }
            VerifyCodeProvider verifyCodeProvider = verifyCodeProviderProvider.get();
            VerifyCode verifyCode = verifyCodeProvider.getVerifyCode("verify_" + timestamp + "_" + userName, code);
            if (verifyCode != null) {
                verifyCodeProvider.delete(verifyCode);
            }
            if (verifyCode == null) {
                throw new IllegalArgumentException("链接失效");
            }
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(userName);
            if (userAccount == null) {
                throw new NullPointerException("帐号不存在");
            }

            int ret = 0;
            if ("bind".equals(type)) {
                UserAccount userAccount2 = userService.findByEmail(email);
                if (userAccount2 != null) {
                    throw new IllegalArgumentException("邮箱已被绑定");
                }
                UserAccount account = new UserAccount(id, userName, email);
                ret = userService.updateEmail(account);
            }

            if ("unbind".equals(type)) {
                UserAccount account = new UserAccount(id, userName, "");
                ret = userService.updateEmail(account);
            }

            if (ret > 0) {
                log(userName, email);
                String _u = URLEncoder.encode(DesUtils.encrypt(userName), Constant.UTF_8);
                response.sendRedirect(accountUri);
            } else {
                response.sendRedirect(accountUri);
            }
        } catch (Exception ex) {
            LOGGER.error("邮箱绑定解绑出现异常,{}", ex.getMessage());
            response.sendRedirect(accountUri);
        }
    }

    private void log(String userName, String email) {
        Log l = new Log();
        l.setUserName(userName);
        l.setLogType(LogType.EMAIL_ACTIVATE);
        l.setLog_time(new Timestamp(System.currentTimeMillis()));
        l.setContent("email=" + email);
        l.setExt1(email);
        logServiceProvider.get().addTask(l);
    }

}
