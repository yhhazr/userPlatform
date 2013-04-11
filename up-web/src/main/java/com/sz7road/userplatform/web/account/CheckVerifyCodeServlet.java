package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.utils.Base64;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 * @author jiangfan.zhou
 */

@Singleton
class CheckVerifyCodeServlet extends HeadlessHttpServlet {

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String verify = request.getParameter("verifyCode");
        String code = request.getParameter("code");
        String userName = request.getParameter("userName");

        if (Strings.isNullOrEmpty(verify) || Strings.isNullOrEmpty(code)
                || Strings.isNullOrEmpty(userName)) {
            response.setStatus(404);
            return;
        }

        Msg msg = new Msg(0, "验证成功");
        try {
            VerifyCodeProvider verifyCodeService = verifyCodeProvider.get();

            if (verifyCodeService.getVerifyCode("verify_" + verify, code) == null) {
                msg.setCode(-1);
                msg.setMsg("验证码错误");
                throw new IllegalArgumentException("验证码输入有误或者已经失效");
            }

            UserService userService = userServiceProvider.get();
            UserAccount account = userService.findAccountByUserName(userName);
            if (account == null) {
                msg.setCode(-2);
                msg.setMsg("用户名不存在");
                throw new IllegalArgumentException("用户名不存在");
            }

            /*
            if (!verifyCodeService.checkVerifyCode(verify, code)) {
                throw new IllegalArgumentException("验证码输入有误或者已经失效");
            }
            */
        } catch (Exception e) {
            msg.setMsg(e.getMessage());
        }

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        writer.write(resp);
        writer.flush();
        writer.close();
    }

}
