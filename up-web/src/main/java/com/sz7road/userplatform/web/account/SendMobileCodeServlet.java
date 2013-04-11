package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.MobileMsgService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DigitUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


/**
 * @author jiangfan.zhou
 */
@Singleton
class SendMobileCodeServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SendMobileCodeServlet.class.getName());

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Inject
    private Provider<MobileMsgService> mobileMsgServiceProvider;

    @Inject
    public SendMobileCodeServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Msg msg = new Msg(1, "发送成功");
        try {
            String userName = request.getParameter("user");
            String mobile = request.getParameter("mobile");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String debug = request.getParameter("_debug");
            if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(mobile) || Strings.isNullOrEmpty(time) || Strings.isNullOrEmpty(type)) {
                response.setStatus(404);
                return;
            }

            String strEt = ConfigurationUtils.get("sendMsg.expiryTime");
            String strCount = ConfigurationUtils.get("sendMsg.Count");
            long expiryTime = Strings.isNullOrEmpty(strEt) ? Constant.TIME_15_MINUTES : Long.parseLong(strEt);
            int count = Strings.isNullOrEmpty(strCount) ? 5 : Integer.parseInt(strCount);

            UserService userService = userServiceProvider.get();
            UserObject userObject = userService.getUserObject(userName);
            if (userObject == null) {
                throw new IllegalArgumentException("账户不存在");
            }
            if (userObject.getLastMessageTime() != null) {
                long lastMessageTime = userObject.getLastMessageTime().getTime();
                long firstMomentTimeOfToday = CommonDateUtils.getOneDayFirstMoment(new Date());

                if (firstMomentTimeOfToday <= lastMessageTime && userObject.getMessageCount() >= count) {
                    throw new IllegalArgumentException("发送失败,今天还可以发0次短信");
                }
            }

            if ("bind".equals(type) && (userObject.getMobile() == null || userObject.getMobile().equals("")) ){

            } else if ("unbind".equals(type) || "reset".equals(type)) {
                mobile = userObject.getMobile();
            }else {
                throw new IllegalArgumentException("非法参数");
            }

            VerifyCodeProvider vcProvider = verifyCodeProvider.get();
            String randCode = DigitUtils.getRandomDigit(6);
            //String randCode = AppHelper.RandomCode(6);
            VerifyCode code = new VerifyCode("message_" + time + "_" + userName, randCode, System.currentTimeMillis() + expiryTime);
            vcProvider.add(code);

            userService.updateMessageCount(userObject);
            msg.setMsg("发送成功,今天还可以发" + (count - userObject.getMessageCount()) + "次短信");

            String content = ConfigurationUtils.get("sendMsg.content");
            //content = Strings.isNullOrEmpty(content) ? String.format("亲爱的用户，您的验证码是:%s。请在15分钟内完成验证[第七大道]", randCode) : String.format(content, randCode);
            content = Strings.isNullOrEmpty(content) ? String.format("亲爱的玩家，您绑定手机的验证码是：%s，请您在15分钟内完成验证，如有疑问，请致电客服0755-61886777【第七大道用户中心】", randCode) : String.format(content, randCode);
            MobileMsgService mobileMsgService = mobileMsgServiceProvider.get();
            //系统测试暂停使用短信
            String strResult = null;
            if ("true".equals(debug)) {
                strResult = "测试暂时不发送短信";
            } else {
                strResult = mobileMsgService.send(mobile, content);
            }
            log.info("发送到用户【{}】的手机短信是：{}，返回内容是：" + strResult, userName, content);

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
