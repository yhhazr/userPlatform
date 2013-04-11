package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Random;

/**
 * @author jiangfan.zhou
 */
@Singleton
class SendCodeBindEmailSubmitServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SendCodeBindEmailSubmitServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;
    @Inject
    private Provider<LogService> logServiceProvider;

    @Inject
    public SendCodeBindEmailSubmitServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _uname = AppHelper.getUserName(request);
        int id = AppHelper.getUserId(request);
        String userName = _uname;
        String type = request.getParameter("type");
        String email = request.getParameter("email");
        String time = request.getParameter("time");
        String code = request.getParameter("code");
        if (Strings.isNullOrEmpty(_uname) || Strings.isNullOrEmpty(type) || Strings.isNullOrEmpty(email) ||
                Strings.isNullOrEmpty(time) || Strings.isNullOrEmpty(code) || id == 0) {
            response.setStatus(404);
            return;
        }

        int ret = 0;
        Msg msg = new Msg(1, "邮箱绑定成功");
        try {
            VerifyCodeProvider verifyCodeService = verifyCodeProvider.get();
            String verify = "verify_" + time + "_" + userName;
            if (!verifyCodeService.checkVerifyCode(verify, code)) {
                throw new IllegalArgumentException("验证码错误");
            }
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = new UserAccount(id, userName);
            //UserAccount userAccount = userService.findAccountById(id);

            if ("bind".equals(type)) {
                userAccount.setEmail(email);
                ret = userService.updateEmail(userAccount);
                log(userName, "", email);
            } else if ("unbind".equals(type)) {
                userAccount.setEmail("");
                ret = userService.updateEmail(userAccount);
                log(userName, email, "");
            } else {
                response.setStatus(404);
                return ;
            }
        } catch (Exception e) {
            msg.setCode(0);
            msg.setMsg(e.getMessage());
            log.error(e.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        PrintWriter out = response.getWriter();
        out.write(resp);
        out.flush();
        out.close();
    }

    private void log(String userName, String oldEmail, String email) {
        Log l = new Log();
        l.setUserName(userName);
        l.setLogType(Log.LogType.EMAIL_BIND);
        l.setLog_time(new Timestamp(System.currentTimeMillis()));
        l.setContent("oldEmail=" + oldEmail + "|email=" + email);
        l.setExt1(oldEmail);
        l.setExt2(email);
        logServiceProvider.get().addTask(l);
    }
}
