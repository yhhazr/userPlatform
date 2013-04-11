package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Log.LogType;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.utils.MailMsg;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.ConfigurationUtils;
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
import java.net.URLEncoder;
import java.sql.Timestamp;

/**
 * @author leo.liao
 */
@Singleton
class EmailVerifySendServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EmailVerifySendServlet.class.getName());
    static final String EMAIL_ACTIVATE = "/EmailActivate";

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<LogService> logServiceProvider;

    @Inject
    public EmailVerifySendServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String r = null;
        String userName = null;
        String oldEmail = null;
        String email = null;
        Msg msg = new Msg(1, "邮箱验证已发送成功");
        try {
            r = request.getParameter("_r");
            if (!Strings.isNullOrEmpty(r)) {
                r = DesUtils.decrypt(r);
                if ("redirect".equals(r)) {
                    userName = DesUtils.decrypt(request.getParameter("_u"));
                    oldEmail = DesUtils.decrypt(request.getParameter("_o"));
                    email = DesUtils.decrypt(request.getParameter("_e"));
                }
            } else {
                String _uname = AppHelper.getUserName(request);
                userName = request.getParameter("userName");
                if (Strings.isNullOrEmpty(_uname) || !_uname.equals(userName)) {
                    response.setStatus(404);
                    return;
                }
                oldEmail = request.getParameter("oldEmail");
                email = request.getParameter("email");
            }
            if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(email)) {
                throw new IllegalArgumentException("参数错误");
            }
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(userName);
            if (userAccount == null) {
                throw new IllegalArgumentException("账户不存在");
            }
            UserAccount ua = userService.findByEmail(email);
            if (ua != null) {
                throw new IllegalArgumentException("邮箱已经被绑定");
            }
            oldEmail = userAccount.getEmail();
            userAccount.setEmail(email);
            String url = EMAIL_ACTIVATE + userService.generateEmailVerifyURL(userAccount);

            url = ConfigurationUtils.get("gateway.domain") + url;
            String host = ConfigurationUtils.get("mail.host");
            String account = ConfigurationUtils.get("mail.account");
            String password = ConfigurationUtils.get("mail.password");
            MailMsg mail = new MailMsg();
            mail.setHostName(host);
            mail.setAuthName(account);
            mail.setAuthPwd(password);

            mail.setNameTo(userName);
            String currEmail = null;
            if (Strings.isNullOrEmpty(oldEmail)) {
                currEmail = email;
            } else {
                currEmail = oldEmail;
            }
            mail.setMailTo(currEmail);

            log.info("发送到用户【{}】的邮箱地址是：{}，链接是：" + url, userName, currEmail);

            String _u = URLEncoder.encode(DesUtils.encrypt(userName), Constant.UTF_8);
            String _r = URLEncoder.encode(DesUtils.encrypt("redirect"), Constant.UTF_8);
            String _o = URLEncoder.encode(DesUtils.encrypt(currEmail), Constant.UTF_8);
            String _e = URLEncoder.encode(DesUtils.encrypt(email), Constant.UTF_8);

            mail.setMailFrom(account);
            mail.setNameFrom("深圳第七大道科技有限公司");
            mail.setSubJect("邮箱激活");
            StringBuilder content = new StringBuilder("亲爱的" + userName + "，您好：");
            content.append("<br>请您点击下面链接来绑定您的账号邮箱:<br>");
            content.append("<a href=\"").append(url).append("\">").append("立即验证").append("</a>");
            content.append("<br>为了确保您的帐号安全，该链接仅7天内访问有效。");
            content.append("<br>如果该链接已经失效，请您点击下面链接来重新获取激活保密邮箱的邮件:");
            content.append("<br><a href=\"").append(ConfigurationUtils.get("gateway.domain"))
                    .append("/EmailVerifySend?_r=").append(_r).append("&_u=").append(_u).append("&_o=").append(_o)
                    .append("&_e=").append(_e);
            content.append("\">重新发送验证</a>");
            content.append("<br>如果点击链接不工作...");
            content.append("<br>请您选择并复制整个链接，打开浏览器窗口并将其粘贴到地址栏中。然后单击\"转到\"按钮或按键盘上的 Enter 键。");
            content.append("<br>请勿直接回复该邮件，有关的更多帮助信息，请访问：").append(ConfigurationUtils.get("gateway.domain"));
            content.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第七大道用户中心");
            mail.setHtmlMsg(content.toString());
            try {
                mail.sendMail(mail);
            } catch (Exception ex) {
                log.error("用户{}发送邮件出现异常,{}", userName, ex.getMessage());
                throw new Exception("发送邮件出现异常");
            }
        } catch (Exception ex) {
            msg.setCode(0);
            msg.setMsg(ex.getMessage());
            log.error(ex.getMessage());
        }
        if (msg.getCode() > 0) {
            log(userName, oldEmail, email);
        }
        if ("redirect".equals(r)) {
            response.sendRedirect(ConfigurationUtils.get("gateway.domain"));
        } else {
            ObjectMapper mapper = new ObjectMapper();
            String resp = mapper.writeValueAsString(msg);
            PrintWriter out = response.getWriter();
            out.write(resp);
            out.flush();
            out.close();
        }
    }

    private void log(String userName, String oldEmail, String email) {
        Log l = new Log();
        l.setUserName(userName);
        l.setLogType(LogType.EMAIL_BIND);
        l.setLog_time(new Timestamp(System.currentTimeMillis()));
        l.setContent("oldEmail=" + oldEmail + "|email=" + email);
        l.setExt1(oldEmail);
        l.setExt2(email);
        logServiceProvider.get().addTask(l);
    }
}
