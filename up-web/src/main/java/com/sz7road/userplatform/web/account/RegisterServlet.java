package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.Base64;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-18
 * Time: 下午4:11
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class RegisterServlet extends HeadlessHttpServlet {
    private static final Logger log = LoggerFactory.getLogger(RegisterServlet.class.getName());
    @Inject
    private Provider<FilterCharacterProvider> dirtyProvider;
    @Inject
    Provider<UserService> userServiceProvider;
    @Inject
    Provider<VerifyCodeService> verifyCodeServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("user").trim();
        String passWord = request.getParameter("psw").trim();
        String verifyCode = request.getParameter("verifyCode").trim();
        String timeStr = request.getParameter("timeStr").trim();

        userName = HtmlUtils.html2EntityAndCheckMaxLength(userName, 20);
        passWord = HtmlUtils.html2EntityAndCheckMaxLength(passWord, 20);
        timeStr = HtmlUtils.html2Entity(timeStr);
        verifyCode = HtmlUtils.html2Entity(verifyCode);
        final long time = System.currentTimeMillis();
        Msg msg = new Msg();
        msg.setCode(200);
        try {
            final UserService userService = userServiceProvider.get();
            final FilterCharacterProvider dirtyService = dirtyProvider.get();
            final String very = "captcha_" + timeStr + "_rl";

            VerifyCodeService verifyCodeService = verifyCodeServiceProvider.get();
            if (!verifyCodeService.checkVerifyCode(very, verifyCode)) {
                msg.setCode(202);
                msg.setMsg("验证码错误!");
                return;
            }
            // 验证用户名
            if (!userService.isLegalNaming(userName)) {
                msg.setCode(199);
                msg.setMsg("用户名必须以字母开头!");
                return;
            }
            if (!userService.isNameCanReg(userName)) {
                msg.setCode(199);
                msg.setMsg("已经存在的用户名!");
                return;
            }
            if (dirtyService.isContainKey(userName)) {
                msg.setCode(199);
                msg.setMsg("用户名含有不雅字符!");
                return;
            }
            if (null != userService.findAccountByUserName(userName)) {
                msg.setCode(199);
                msg.setMsg("已经存在的用户名!");
                return;
            }
            if (!HtmlUtils.isNotChinese(userName)) {
                msg.setCode(199);
                msg.setMsg("用户名不能含有中文!");
                return;
            }
            if (!userService.isLegalPassword(passWord)) {
                msg.setCode(201);
                msg.setMsg("密码含有空格或长度不够!");
                return;
            }
            if (msg.getCode() == 200) {
                final UserObject userObject = new UserObject();
                userObject.setUserName(userName);
                userObject.getAccount().setPassWord(passWord);
                userObject.getAccount().setEmail("");
                final Timestamp createTime = new Timestamp(time);
                userObject.setCreateTime(createTime);
                userObject.setLoginSum(0);
                int pswStrength = RuleUtil.getPswStrength(passWord);
                userObject.setPswStrength(pswStrength);
                userObject.setLastLoginTime(createTime);
                Map<String, Integer> map = userService.saveData_Register(userObject);
                log.info("注册ID: " + map.get("id") + " , 结果 ：" + map.get("rel"));
                if (map != null && map.size() == 2 && map.get("id") > 0 && map.get("rel") > 0) {
                    msg.setMsg("注册成功!");
                    final String userId = String.valueOf(map.get("id").intValue());
                    final String USERINFO = userId + "," + userName + "," + time;
                    String u = Base64.encode(USERINFO + "," +
                            AppHelper.buildUserInfoSign(String.valueOf(userId), userName, String.valueOf(time)));
                    u = URLEncoder.encode(u, "utf-8");
                    final String key = URLEncoder.encode(Base64.encode("USERINFO"), "UTF-8");
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null && cookies.length > 0)
                        //移除之前的cookie
                        for (Cookie cookie : cookies) {
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    Cookie cookie = new Cookie(key, u);
                    String cookieDomain = ConfigurationUtils.get("7road.cookie.domain");
                    if (Strings.isNullOrEmpty(cookieDomain)) {
                        cookieDomain = ".7road.com";
                    } else {
                        if ("/".equals(cookieDomain)) { //本地不设置cookie域
                        } else {
                            cookie.setDomain(cookieDomain);
                        }
                    }
                    cookie.setMaxAge(1800);
                    response.addCookie(cookie);

                    String fromUrl=request.getParameter("fromUrl");
                    if(!Strings.isNullOrEmpty(fromUrl))
                    {
                        msg.setCode(400);
                        msg.setMsg("跳转到来源页面");
                        msg.setObject(fromUrl);
                    }
                } else {
                    msg.setCode(204);
                    msg.setMsg("注册失败!");
                }
            }
        } catch (final Exception e) {
            log.error("用户注册接口异常：{}", e.getMessage());
            response.sendError(404);
            e.printStackTrace();
        } finally {
            DataUtil.returnJson(response, msg);
        }
    }
}
