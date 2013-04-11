/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
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
import com.sz7road.userplatform.utils.MailMsg;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DesUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 * @author leo.liao
 */
@Singleton
class ForgetPwdSubmitServlet extends HeadlessHttpServlet {

    private final static String URL_FORGETPASSWORD = "/resetpwd.html";
    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Msg msg = new Msg(0, "申请邮件已经发送成功，请您登陆到您绑定邮箱去查收邮件");
        PrintWriter writer = response.getWriter();
        try {
            validation(request, response, msg);
            ObjectMapper mapper = new ObjectMapper();
            String resp = mapper.writeValueAsString(msg);
            writer.write(resp);
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private void validation(HttpServletRequest request, HttpServletResponse response, Msg msg) throws ServletException, IOException {
        try {
            String userName = null;
            userName = request.getParameter("_u");
            String verify = request.getParameter("_v");
            String code = request.getParameter("_c");
            if (Strings.isNullOrEmpty(userName)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            if (Strings.isNullOrEmpty(verify)) {
                throw new IllegalArgumentException("不合法的请求");
            }
            if (Strings.isNullOrEmpty(code)) {
                throw new IllegalArgumentException("请输入验证码");
            }
            VerifyCodeProvider verifyCodeService = verifyCodeProvider.get();
            if (!verifyCodeService.checkVerifyCode(verify, code)) {
                throw new IllegalArgumentException("验证码输入有误或者已经失效");
            }
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(userName);
            if (userAccount == null) {
                userAccount = userService.findByEmail(userName);
            }
            if (userAccount == null) {
                throw new IllegalArgumentException("您输入的用户名或者邮箱尚未注册或绑定");
            }
            String email = userAccount.getEmail();
            if (Strings.isNullOrEmpty(email)) {
                throw new IllegalArgumentException("此帐号没绑定邮箱");
            }
            String url = URL_FORGETPASSWORD + userService.generateGetBackPasswordURL(userAccount);
            url = ConfigurationUtils.get("gateway.domain") + url;
            sendEmail(userName, email, msg, url);
            StringTokenizer stz = new StringTokenizer(email, "@");
            String prex = stz.nextToken();
            String suf = stz.nextToken();
            int len = 1;
            if (prex.length() >= 3) {
                len = 3;
            }
            String mail = prex.substring(0, len) + "*****@" + suf;
            msg.setObject(mail);
        } catch (Exception e) {
            msg.setCode(1);
            msg.setMsg(e.getMessage());
        }
    }

    private void sendEmail(String userName, String email, Msg msg, String url) {
        try {
            String host = ConfigurationUtils.get("mail.host");
            String account = ConfigurationUtils.get("mail.account");
            String password = ConfigurationUtils.get("mail.password");
            MailMsg mail = new MailMsg();
            mail.setHostName(host);
            mail.setAuthName(account);
            mail.setAuthPwd(password);

            mail.setNameTo(userName);
            mail.setMailTo(email);

            String _u = URLEncoder.encode(DesUtils.encrypt(userName), Constant.UTF_8);
            String _r = URLEncoder.encode(DesUtils.encrypt("redirect"), Constant.UTF_8);
            String _e = URLEncoder.encode(DesUtils.encrypt(email), Constant.UTF_8);

            mail.setMailFrom(account);
            mail.setNameFrom("深圳第七大道科技有限公司");
            mail.setSubJect("密码重置");
            StringBuilder content = new StringBuilder("亲爱的用户" + userName + "，您好：");
            content.append("<br>您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。");
            content.append("<br>要使用新的密码, 请使用以下链接启用密码。<br>");
            content.append(url);
            content.append("<br>(如果无法点击该URL链接地址，请将它复制并粘帖到浏览器的地址输入框，然后单击回车即可。该链接使用后将立即失效。)");
            content.append("<br>注意:请您在收到邮件1个小时内使用，否则该链接将会失效。");
            content.append("<br>请勿直接回复该邮件，有关的更多帮助信息，请访问：").append(ConfigurationUtils.get("gateway.domain"));
            content.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第七大道用户中心");
            mail.setHtmlMsg(content.toString());
            try {
                mail.sendMail(mail);
            } catch (Exception ex) {
                throw new Exception("发送邮件出现异常");
            }
        } catch (Exception ex) {
            msg.setCode(1);
            msg.setMsg(ex.getMessage());
        }
    }
}
