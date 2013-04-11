package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.utils.MailMsg;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.CommonDateUtils;
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


/**
 * @author jiangfan.zhou
 */
@Singleton
class SendCodeBindEmailServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SendCodeBindEmailServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Inject
    public SendCodeBindEmailServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _uname = AppHelper.getUserName(request);
        String userName = _uname;
        String type = request.getParameter("type");
        String email = request.getParameter("email");
        String time = request.getParameter("time");
        int id = AppHelper.getUserId(request);
        if (Strings.isNullOrEmpty(_uname) || Strings.isNullOrEmpty(type) || Strings.isNullOrEmpty(email)
                || Strings.isNullOrEmpty(time) || id == 0) {
            response.setStatus(404);
            return;
        }

        Msg msg = new Msg(1, "邮箱验证已发送成功");

        String emailNameFrom = "深圳第七大道科技有限公司";
        String emailSubject = "邮箱绑定";

        try {
            StringBuilder content = new StringBuilder("亲爱的" + userName + "，您好：");
            if ("bind".equals(type)) {
                content.append("<br><br>欢迎您使用邮箱绑定服务。");
            } else if ("unbind".equals(type)) {
                content.append("<br><br>欢迎您使用邮箱解绑服务。");
                emailSubject = "解除绑定";

                UserService userService = userServiceProvider.get();
                UserAccount userAccount = userService.findAccountById(id);
                if ( userAccount == null || userAccount.getEmail() == null || userAccount.getEmail().equals("")) {
                    throw new IllegalArgumentException("已经解除绑定");
                }

                if (!userAccount.getEmail().equals(email)) {
                    throw new IllegalArgumentException("旧邮箱错误:" + userAccount.getEmail());
                }
            } else {
                response.setStatus(404);
                return ;
            }
            //String code = DigitUtils.getRandomDigit(6);
            String code = AppHelper.RandomCode(6);
            long expiration = System.currentTimeMillis() + Constant.TIME_30_MINUTES;
            VerifyCode verifyCode = new VerifyCode("verify_" + time + "_" + userName, code, expiration);
            VerifyCodeProvider verifyCodeService = verifyCodeProvider.get();
            verifyCodeService.add(verifyCode);

            content.append("<br><br>验证码30分钟内有效，请在" + CommonDateUtils.getDate(expiration) + " 前点使用以下验证码：");
            content.append("<br>验证码:" + code);
            content.append("<br><br>请勿直接回复该邮件，有关的更多帮助信息，请访问：").append(ConfigurationUtils.get("gateway.domain"));
            content.append("<br><br>第七大道用户中心");

            String host = ConfigurationUtils.get("mail.host");
            String account = ConfigurationUtils.get("mail.account");
            String password = ConfigurationUtils.get("mail.password");

            MailMsg mail = new MailMsg();
            mail.setHostName(host);
            mail.setAuthName(account);
            mail.setAuthPwd(password);
            mail.setNameTo(userName);
            mail.setMailTo(email);
            mail.setMailFrom(account);
            mail.setNameFrom(emailNameFrom);
            mail.setSubJect(emailSubject);
            mail.setHtmlMsg(content.toString());

            mail.sendMail(mail);

            log.info("发送绑定邮件,用户[{}],绑定邮箱:{},验证码:{}",new Object[]{userName, email, code});
        } catch (Exception ex) {
            msg.setCode(0);
            msg.setMsg(ex.getMessage());
            log.error("用户{}发送邮件出现异常,{},输入邮箱地址:" + email, userName, ex.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        PrintWriter out = response.getWriter();
        out.write(resp);
        out.flush();
        out.close();
    }
}
