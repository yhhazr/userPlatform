package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.dao.AppealDao;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.AppealService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.utils.MailMsg;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DigitUtils;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Singleton
public class AppealManageServlet extends BaseServlet {

    @Inject
    private Provider<AppealService> appealServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/appeal.jsp").forward(request, response);
    }

    public void query(HttpServletRequest request, HttpServletResponse response){
        String strRows = request.getParameter("rows");
        String strPage = request.getParameter("page");
        String userName = request.getParameter("userName");
        String strPay = request.getParameter("pay");
        String strStatus = request.getParameter("status");
        String strFromDate = request.getParameter("fromDate");
        String strToDate = request.getParameter("toDate");

        if(Strings.isNullOrEmpty(userName)){
            userName = "";
        }
        if(Strings.isNullOrEmpty(strPay)){
            strPay = "-1";
        }
        if(Strings.isNullOrEmpty(strStatus)){
            strStatus = "-1";
        }

        Date fromDate = null;
        Date toDate = CommonDateUtils.string2Date(strToDate, "yyyy-MM-dd");
        if (!Strings.isNullOrEmpty(strFromDate)) {
            fromDate = CommonDateUtils.string2Date(strFromDate, "yyyy-MM-dd");
        }
        if (!Strings.isNullOrEmpty(strToDate)) {
            toDate = CommonDateUtils.string2Date(strToDate, "yyyy-MM-dd");
        }

        Pager<Appeal> pager = new Pager<Appeal>();
        try {
            int rows = Integer.parseInt(strRows);
            int page = Integer.parseInt(strPage);
            int pay = Integer.parseInt(strPay);
            int status = Integer.parseInt(strStatus);

            AppealService service = appealServiceProvider.get();
            pager = service.query(userName, pay, status, fromDate, toDate, page, rows);
            pager.setPage(page);
            pager.setPageSize(rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServletUtil.returnJson(response, pager);
    }

    public void modify(HttpServletRequest request, HttpServletResponse response){
        String strAppealId = request.getParameter("appealId");
        String strStatus = request.getParameter("status");
        String strAuditor = request.getParameter("auditor");
        Msg msg = new Msg(0, "操作失败");
        try{
            int appealId = Integer.parseInt(strAppealId);
            int status = Integer.parseInt(strStatus);
            if (!Strings.isNullOrEmpty(strAuditor)) {
                AppealService service = appealServiceProvider.get();
                Appeal entity = service.updateStatus(appealId, status, strAuditor);
                if (entity != null && status == AppealDao.STATUS_AUDIT_PASS) {
                    sendMail(entity.getUserName(), entity.getEmail());
                }
                if (entity != null && status == AppealDao.STATUS_AUDIT_NOT_PASS) {
                    sendMailAppealFail(entity.getUserName(), entity.getEmail());
                }
                msg.setCode(1);
                msg.setMsg("操作成功");
            }
        } catch (Exception e) {
            log.error("用户[{}]，申诉管理," ,e.getMessage());
            msg.setObject(e.getMessage());
        }
        ServletUtil.returnJson(response, msg);
    }

    private final static String URL_RESET_PASS = "/resetPass";

    private void sendMail(String userName, String mailTo) {
        long time = System.currentTimeMillis();
        String emailNameFrom = "深圳第七大道科技有限公司";
        String emailSubject = "申诉成功-密码重置";

        String code = DigitUtils.getRandomDigit(6);
        long expiration = time + Constant.TIME_7_DAYS;
        VerifyCode verifyCode = new VerifyCode("verify_" + time + "_" + userName, code, expiration);
        VerifyCodeProvider verifyCodeService = verifyCodeProvider.get();
        verifyCodeService.add(verifyCode);

        String url = ConfigurationUtils.get("gateway.domain") + URL_RESET_PASS
                + "?user=" + userName + "&code=" + code + "&time=" + time + "&key=" + DigestUtils.md5Hex("verify," + time + "," + userName);

        StringBuilder content = new StringBuilder("尊敬的第七大道用户:");
        content.append("<br>您好！");
        content.append("<br>");
        content.append("<br>恭喜您，您提交的帐号为 [" + userName + "] 的申诉已成功通过审核！");
        content.append("<br>请点这里重新设置号码资料（本链接一周内仅一次有效）。请在 "  + CommonDateUtils.getDate(expiration) + " 前使用下面链接:");
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
        mail.setNameTo(userName);
        mail.setMailTo(mailTo);
        mail.setMailFrom(user);
        mail.setNameFrom(emailNameFrom);
        mail.setSubJect(emailSubject);
        mail.setHtmlMsg(content.toString());

        try {
            mail.sendMail(mail);
            log.info("用户{}申诉重置密码发送邮件URL:{}", userName, url);
        } catch (Exception e) {
            log.error("用户{}申诉重置密码发送邮件错误:{}" , userName, e.getMessage());
        }
    }

    private void sendMailAppealFail(String userName, String mailTo) {
        long time = System.currentTimeMillis();
        String emailNameFrom = "深圳第七大道科技有限公司";
        String emailSubject = "申诉失败";

        StringBuilder content = new StringBuilder("尊敬的第七大道用户：<br/>您好！");
        content.append("<br><br>很抱歉，您所提交的帐号为 [" + userName + "] 的申诉没有通过审核！");
        content.append("<br><br>可能是您提交的以下资料不完整或不正确：");
        content.append("<br>1、基本信息：即您使用该帐号时填写的证件，详细的游戏基本信息");
        content.append("<br>2、申诉基本资料：即该帐号注册时间、城市、异常登录时间");
        content.append("<br>3、充值资料：即该帐号使用时充值记录明细");
        content.append("<br>4、其它补充资料：即该帐号其它相关信息内容（如：购买记录、公会信息、物品操作信息等），所补充的资料时间越早、越详细越完整，则申诉效果就会越好");
        content.append("<br>建议您仔细回忆以上资料后，到<a href='" + ConfigurationUtils.get("gateway.domain") + "/forget.html'>个人中心</a>重新提交申诉。");
        content.append("<br>");
        content.append("<br>温馨提示：");
        content.append("<br>1、第七大道统一申诉回复邮箱为 noreply@7road.com ，请注意邮件发送者，谨防假冒！");
        content.append("<br>2、密码保护资料非常重要，请注意保密且一定牢记！只要您能够准确的记住您所填写的密码保护资料，即使您忘记了密码或帐号被盗，通过客服中心都可轻松找回账号。");
        content.append("<br>3、为了您帐号的安全，建议不要使用第三方软件！谨防账号被盗。");
        content.append("<br>4、本邮件为系统自动发出，请勿回复。");
        content.append("<br>");
        content.append("<br>感谢您使用第七大道服务，有任何问题您都可以登录网站:").append(ConfigurationUtils.get("gateway.domain"));
        content.append("<br><br>第七大道用户中心");

        String host = ConfigurationUtils.get("mail.host");
        String user = ConfigurationUtils.get("mail.account");
        String password = ConfigurationUtils.get("mail.password");

        MailMsg mail = new MailMsg();
        mail.setHostName(host);
        mail.setAuthName(user);
        mail.setAuthPwd(password);
        mail.setNameTo(userName);
        mail.setMailTo(mailTo);
        mail.setMailFrom(user);
        mail.setNameFrom(emailNameFrom);
        mail.setSubJect(emailSubject);
        mail.setHtmlMsg(content.toString());

        try {
            mail.sendMail(mail);
            log.info("邮件发送:用户{}申述失败", userName);
        } catch (Exception e) {
            log.error("邮件发送:用户{}申述失败,错误信息:{}" , userName, e.getMessage());
        }
    }
}
