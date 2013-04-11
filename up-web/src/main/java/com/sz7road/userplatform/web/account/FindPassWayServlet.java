package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.Question;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.utils.MailMsg;
import com.sz7road.userplatform.web.utils.HideMobileEmailUtils;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DigitUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Singleton
class FindPassWayServlet extends HeadlessHttpServlet {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FindPassWayServlet.class.getName());
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;
    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String type = request.getParameter("type");

        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(type)) {
            response.getWriter().println("<script>location.href='forget.html';</script>");
            return;
        }

        UserService userService = userServiceProvider.get();
        UserObject userObject = userService.getUserObject(userName);
        if (userObject == null) {
            response.getWriter().println("<script>location.href='forget.html';</script>");
            return;
        }
        String email = userObject.getEmail();
        String mobile = null;

        String forward = "/account/forget/index.jsp";
        if ("question".equals(type)) {
            forward = "/account/forget/findquestion.jsp";
            List<Question> list = userService.getLastQuestions(userObject.getId(), 3);
            Collections.shuffle(list);
            request.setAttribute("list", list);
        } else if ("email".equals(type)) {
            if (null != email && !email.equals("") && email.indexOf("@") != -1) {
                email = HideMobileEmailUtils.getWildEmail(email);
                forward = "/account/forget/findemail.jsp";
                sendMail(userObject.getAccount());
            }
        } else if ("mobile".equals(type)){
            mobile = HideMobileEmailUtils.getWildMobile(userObject.getMobile());
            forward = "/account/forget/findmobile.jsp";
        } else if ("appeal".equals(type)) {
            forward = "/account/forget/appeal.jsp";
        } else {
            response.getWriter().println("<script>location.href='forget.html';</script>");
            return;
        }
        request.setAttribute("showEmail", email);
        request.setAttribute("showMobile", mobile);
        request.setAttribute("userObject", userObject);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    private final static String URL_RESET_PASS = "/resetPass";

    private void sendMail(UserAccount account) {
        long time = System.currentTimeMillis();
        String emailNameFrom = "深圳第七大道科技有限公司";
        String emailSubject = "密码重置";

        String code = DigitUtils.getRandomDigit(6);
        long expiration = time + Constant.TIME_24_HOURS;
        VerifyCode verifyCode = new VerifyCode("verify_" + time + "_" + account.getUserName(), code, expiration);
        VerifyCodeProvider verifyCodeService = verifyCodeProvider.get();
        verifyCodeService.add(verifyCode);

        String url = ConfigurationUtils.get("gateway.domain") + URL_RESET_PASS
                + "?user=" + account.getUserName() + "&code=" + code + "&time=" + time + "&key=" + DigestUtils.md5Hex("verify," + time + "," + account.getUserName());

        StringBuilder content = new StringBuilder("尊敬的第七大道用户:");
        content.append("<br>您好！");
        content.append("<br>");
        content.append("<br>欢迎您使用密码重置服务！");
        content.append("<br>请点这里重新设置号码资料（本链接一天内仅一次有效）。请在 "  + CommonDateUtils.getDate(expiration) + " 前使用下面链接:");
        content.append("<br>");
        content.append("<br>" + url);
        content.append("<br>");
        content.append("<br>如上面的链接无法点击，请复制上面链接，粘贴到浏览器的地址栏内访问。");
        content.append("<br>");
        content.append("<br>温馨提示：");
        content.append("<br>1、第七大道统一申诉回复邮箱为 noreply@7road.com ，请注意邮件发送者，谨防假冒！");
        content.append("<br>2、密码保护资料非常重要，请注意保密且一定牢记！只要您能够准确的记住您所填写的密码保护资料，即使您忘记了密码或帐号被盗，通过客服中心都可轻松找回账号。");
        content.append("<br>3、为了您帐号的安全，建议不要使用第三方软件！谨防账号被盗。");
        content.append("<br>4、本邮件为系统自动发出，请勿回复。");
        content.append("<br>感谢您使用第七大道服务，有任何问题您都可以登录网站:").append(ConfigurationUtils.get("gateway.domain"));
        content.append("<br><br>第七大道用户中心");

        String host = ConfigurationUtils.get("mail.host");
        String user = ConfigurationUtils.get("mail.account");
        String password = ConfigurationUtils.get("mail.password");

        MailMsg mail = new MailMsg();
        mail.setHostName(host);
        mail.setAuthName(user);
        mail.setAuthPwd(password);
        mail.setNameTo(account.getUserName());
        mail.setMailTo(account.getEmail());
        mail.setMailFrom(user);
        mail.setNameFrom(emailNameFrom);
        mail.setSubJect(emailSubject);
        mail.setHtmlMsg(content.toString());

        try {
            mail.sendMail(mail);
            log.info("用户{}找回重置密码发送邮件URL:{}", account.getUserName(), url);
        } catch (Exception e) {
            log.error("用户{}找回重置密码发送邮件错误:{}" , account.getUserName(), e.getMessage());
        }
    }
}