package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.pojos.FormItem;
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
import com.sz7road.utils.VerifyFormItem;
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
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-16
 * Time: 下午12:05
 * 处理防沉迷的servlet
 */
@Singleton
public class Register_fcmServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(Register_fcmServlet.class.getName());
    @Inject
    private Provider<FilterCharacterProvider> dirtyProvider;
    @Inject
    Provider<UserService> userServiceProvider;
    @Inject
    Provider<VerifyCodeService> verifyCodeServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1，接收参数
        String _name = request.getParameter("uName");
        String _psw = request.getParameter("uPsw");
        String _pswC = request.getParameter("uPswConfirm");
        String _email = request.getParameter("uEmail");
        String _rName = request.getParameter("rName");
        String _icn = request.getParameter("uIcn");
        String _code = request.getParameter("code");
        String _timeStr = request.getParameter("timeStr").trim();
        Msg msg = new Msg();
        msg.setCode(200);
        //2，xss过滤,限制最大长度
        _name = HtmlUtils.html2EntityAndCheckMaxLength(_name, 20);
        _psw = HtmlUtils.html2EntityAndCheckMaxLength(_psw, 20);
        _pswC = HtmlUtils.html2EntityAndCheckMaxLength(_pswC, 20);
        _email = HtmlUtils.html2EntityAndCheckMaxLength(_email, 30);
        _rName = HtmlUtils.html2EntityAndCheckMaxLength(_rName, 8);
        _icn = HtmlUtils.html2EntityAndCheckMaxLength(_icn, 18);
        _code = HtmlUtils.html2EntityAndCheckMaxLength(_code, 4);
        _timeStr = HtmlUtils.html2Entity(_timeStr);
        //3，服务器端判断用户输入的合法性
        try {
            List<FormItem> items = new LinkedList<FormItem>();
            items.add(new FormItem("uName", _name, "用户名", 201));
            items.add(new FormItem("uPsw", _psw, "密码", 202));
            items.add(new FormItem("uPswConfirm", _pswC, "确认密码", 203));
            items.add(new FormItem("uEmail", _email, "邮箱", 204));
            items.add(new FormItem("code", _code, "验证码", 205));
            items.add(new FormItem("timeStr", _timeStr, "时间戳", 205));
            items.add(new FormItem("rName", _rName, "真实姓名", 206));
            items.add(new FormItem("uIcn", _icn, "身份证号码", 207));
            Map<String, Object> checkResult = HtmlUtils.checkItemAndReturnMsg(items);
            if (checkResult != null && !checkResult.isEmpty() && (Boolean) checkResult.get("result")) {
                int code = (Integer) checkResult.get("code");
                String msgInfo = (String) checkResult.get("msg");
                msg.setCode(code);
                msg.setMsg(msgInfo);
                return;
            } else {
                UserService userService = userServiceProvider.get();
                // 验证用户名
                if (!userService.isLegalNaming(_name)) {
                    msg.setCode(201);
                    msg.setMsg("用户名必须以字母开头!");
                    return;
                }
                if (!HtmlUtils.isNotChinese(_name)) {
                    msg.setCode(201);
                    msg.setMsg("用户名不能含有中文!");
                    return;
                }
                if (!userService.isNameCanReg(_name) || null != userService.findAccountByUserName(_name)) {
                    msg.setCode(201);
                    msg.setMsg("已经存在的用户名!");
                    return;
                }
                FilterCharacterProvider dirtyService = dirtyProvider.get();
                if (dirtyService.isContainKey(_name)) {
                    msg.setCode(201);
                    msg.setMsg("用户名含有不雅字符!");
                    return;
                }
                if (!userService.isLegalPassword(_psw)) {
                    msg.setCode(202);
                    msg.setMsg("密码含有空格或长度不够!");
                    return;
                }
                if (!_psw.equals(_pswC)) {
                    msg.setCode(203);
                    msg.setMsg("两次输入的密码不一致!");
                    return;
                }
                if (!userService.isLegalEmail(_email)) {
                    msg.setCode(204);
                    msg.setMsg("非法邮箱地址!");
                    return;
                }
                VerifyCodeService verifyCodeService = verifyCodeServiceProvider.get();
                String very = "captcha_" + _timeStr + "_rl";
                if (!verifyCodeService.checkVerifyCode(very, _code)) {
                    msg.setCode(205);
                    msg.setMsg("验证码错误!");
                    return;
                }
                if (!HtmlUtils.isChinese(_rName)) {
                    msg.setCode(206);
                    msg.setMsg("请输入正确的中文名字!");
                    return;
                }

                if (!userService.isLegalIcn(_icn)) {
                    msg.setCode(207);
                    msg.setMsg("请输入正确的身份证号码!");
                    return;
                }


                if (msg.getCode() == 200) {
                    //4，插入数据
                    UserAccount account = new UserAccount();
                    account.setUserName(_name);
                    account.setPassWord(_psw);
                    account.setEmail(_email);
                    UserObject userObject = new UserObject();
                    userObject.setAccount(account);
                    userObject.setRealName(_rName);
                    userObject.setIcn(_icn);
                    userObject.setLoginSum(0);
                    Timestamp now = new Timestamp(new Date().getTime());
                    userObject.setLastLoginTime(now);
                    userObject.setCreateTime(now);
                    userObject.setPswStrength(RuleUtil.getPswStrength(_psw));
                    Map<String, Integer> map = userService.saveData_Register_fcm(userObject);
                    log.info("注册ID: " + map.get("id") + " , 结果 ：" + map.get("rel"));
                    if (map != null && map.size() == 2 && map.get("id") > 0 && map.get("rel") > 0) {
                        msg.setCode(200);
                        msg.setMsg("注册成功!");
                        long time = System.currentTimeMillis();
                        String userId = String.valueOf(map.get("id").intValue());
                        String USERINFO = userId + "," + _name + "," + time;
                        String u = Base64.encode(USERINFO + "," +
                                AppHelper.buildUserInfoSign(String.valueOf(userId), _name, String.valueOf(time)));
                        u = URLEncoder.encode(u, "utf-8");
                        String key = URLEncoder.encode(Base64.encode("USERINFO"), "UTF-8");
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
                    } else {
                        msg.setCode(204);
                        msg.setMsg("注册失败!");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //5，返回结果
            DataUtil.returnJson(response, msg);
        }

    }
}
