package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.LogDao;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.utils.IPUtil.IPSeeker;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.Base64;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-18
 * Time: 上午11:40
 * 平台登录的servlet
 */
@Singleton
public class LoginServlet extends HeadlessHttpServlet {
    static final Logger logger = LoggerFactory.getLogger(LoginServlet.class.getName());
    @Inject
    Provider<UserService> userServiceProvider;
    @Inject
    Provider<VerifyCodeService> verifyCodeServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String passWord = request.getParameter("psw");
        String verifyCode = request.getParameter("verifyCode");
        String useVerifyCode = request.getParameter("useVerifyCode");
        String timeStr = request.getParameter("timeStr");

        userName = HtmlUtils.html2EntityAndCheckMaxLength(userName, 20);
        passWord = HtmlUtils.html2EntityAndCheckMaxLength(passWord, 20);
        timeStr = HtmlUtils.html2Entity(timeStr);
        //获得登录的IP　同神曲官网
        String ip = CommonDateUtils.getRemoteIPAddress(request);
        Msg msg = new Msg();
        msg.setCode(204);
        msg.setMsg("用户不存在或密码不正确");
        verifyCode = HtmlUtils.html2Entity(verifyCode);
        VerifyCodeService verifyCodeService = verifyCodeServiceProvider.get();
        String very = "captcha_" + timeStr + "_rl";
        if ("true".equals(useVerifyCode) && !verifyCodeService.checkVerifyCode(very, verifyCode)) {
            msg.setCode(202);
            msg.setMsg("验证码错误!");
        } else {
            try {
                final UserService userService = userServiceProvider.get();
                UserAccount account = userService.authenticated(userName, passWord);
                if (null == account) {
                    // 该用户不存在或密码不正确
                    msg.setCode(204);
                    msg.setMsg("用户不存在或密码不正确");
                } else {
                    // 验证通过
                    final UserObject user = userService.findByAccount(account);
                    if (null != user) {
                        long time = System.currentTimeMillis();
                        String userId = String.valueOf(user.getId());
                        String USERINFO = userId + "," + userName + "," + time;
                        String u = Base64.encode(USERINFO + "," +
                                AppHelper.buildUserInfoSign(String.valueOf(userId), userName, String.valueOf(time)));
                        u = URLEncoder.encode(u, "utf-8");
                        String key = URLEncoder.encode(Base64.encode("USERINFO"), "UTF-8");
                        Cookie cookie = new Cookie(key, u);
                        String cookieDomain = ConfigurationUtils.get("7road.cookie.domain");
                        if (Strings.isNullOrEmpty(cookieDomain)) {
                            cookieDomain = ".7road.com"; //设置为7道的域cookie，方便同步七道游戏的登录信息
                        } else {
                            if ("/".equals(cookieDomain)) { //本地不设置cookie域
                            } else {
                                cookie.setDomain(cookieDomain);
                            }
                        }
                        cookie.setMaxAge(1800);
                        response.addCookie(cookie);



                        msg.setCode(200);
                        msg.setMsg("欢迎登录运营平台!");
                        // 异步运算用户登录后的数据逻辑及日志记录。
                        user.setLoginSum(user.getLoginSum() + 1);
                        user.setLastIp(ip);
                        int pswStrength = RuleUtil.getPswStrength(passWord);
                        user.setPswStrength(pswStrength);
                        user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
                        userService.runLoginAsyncHooks(user);

                        String fromUrl=request.getParameter("fromUrl");
                        if(!Strings.isNullOrEmpty(fromUrl))
                        {
                            msg.setCode(400);
                            msg.setMsg("跳转到来源页面");
                            msg.setObject(fromUrl);
                        }
                    }
                }
            } catch (final Exception e) {
                logger.error("登录验证接口异常：{}", e.getMessage());
                e.printStackTrace();
            } finally {

                if (msg.getCode() == 204) {
                    msg.setObject("showVerifyCode");
                }

            }
        }
        DataUtil.returnJson(response, msg);
    }
}
