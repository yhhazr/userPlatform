package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.pojos.VerifyCode;
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
import java.util.Random;

/**
 * @author jiangfan.zhou
 */
@Singleton
class SendMobileCodeSubmitServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SendMobileCodeSubmitServlet.class.getName());

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Inject
    private Provider<LogService> logServiceProvider;

    @Inject
    public SendMobileCodeSubmitServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Msg msg = new Msg(1, "操作成功");
        try {
            String userName = request.getParameter("user");
            String mobile = request.getParameter("mobile");
            String code = request.getParameter("code");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(mobile) || Strings.isNullOrEmpty(code) ||
                   Strings.isNullOrEmpty(time) || Strings.isNullOrEmpty(type)) {
                response.setStatus(404);
                return;
            }

            UserService userService = userServiceProvider.get();
            UserObject user = userService.getUserObject(userName);
            if (user == null) {
                throw new IllegalArgumentException("账户不存在");
            }

            VerifyCodeProvider vcProvider = verifyCodeProvider.get();
            if (!vcProvider.checkVerifyCode("message_" + time + "_" + userName, code)) {
                throw new IllegalArgumentException("重新输入验证码");
            }

            if ("bind".equals(type)) {
                user.setMobile(mobile);
                userService.updateMobile(user);
                log.info("用户【{}】绑定手机：{}" , userName, mobile);
            } else if ("unbind".equals(type)) {
                if (!mobile.equals(user.getMobile())) {
                    throw new IllegalArgumentException("原手机号码错误");
                }
                user.setMobile("");
                userService.updateMobile(user);
                log.info("用户[{}]解除绑定手机：{}" , userName, mobile);
            }
        } catch (Exception ex) {
            msg.setCode(0);
            msg.setMsg(ex.getMessage());
            log.error(ex.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        PrintWriter out = response.getWriter();
        out.write(resp);
        out.flush();
        out.close();
    }
}
