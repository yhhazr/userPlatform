package com.sz7road.userplatform.web.rr;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.*;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.userplatform.web.utils.RegexUtils;
import com.sz7road.utils.Backend;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author jiangfan.zhou
 */

@Singleton
class UpdateUsernameSubmitServlet extends HeadlessHttpServlet {

    private final static Logger log = LoggerFactory.getLogger(UpdateUsernameSubmitServlet.class.getName());
    @Inject
    private Provider<FilterCharacterProvider> dirtyProvider;
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeService> verifyCodeServiceProvider;
    @Inject
    private Provider<LogService> logServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        String oldname = request.getParameter("oldname");
        String oldsite = request.getParameter("oldsite");
        String s = request.getParameter("s");
        String newsite = request.getParameter("newsite");
        String strGameId = request.getParameter("gameId");
        int gameId = 1;
        String k = request.getParameter("k");

        try{
            if (!Strings.isNullOrEmpty(strGameId))
                gameId = Integer.parseInt(strGameId);
        } catch (Exception e){}

        String key = "68005ba4-1a7f-4bed-a220-cf375393bfc5";
        String sKey = DigestUtils.md5Hex(oldname + oldsite + s + newsite + key);
        if (!Strings.isNullOrEmpty(strGameId) && !"1".equals(strGameId)) {
            sKey = DigestUtils.md5Hex(oldname + oldsite + s + newsite + gameId + key);
        }

        log.info("oldname={},oldsite={},s={},newsite={},gameId={},k={},sKey={},s==sKey?{}",new Object[]{oldname,oldsite,s,newsite,gameId,k, sKey,k.equals(sKey) });

        String userName = request.getParameter("user").trim();
        String passWord = request.getParameter("psw").trim();
        String verifyCode = request.getParameter("verifyCode").trim();
        String timeStr= request.getParameter("timeStr").trim();
        final Timestamp createTime = new Timestamp(System.currentTimeMillis());

        Msg msg = new Msg();
        msg.setCode(204);
        try {
            final UserService userService = userServiceProvider.get();
            final FilterCharacterProvider dirtyService = dirtyProvider.get();

            if (Strings.isNullOrEmpty(oldname) || Strings.isNullOrEmpty(oldsite) || Strings.isNullOrEmpty(s)
                    || Strings.isNullOrEmpty(newsite) || Strings.isNullOrEmpty(k) || !sKey.equals(k) || oldsite.equals(newsite)) {
                msg.setCode(199);
                msg.setMsg("非法参数!");
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
            if (!userService.isLegalPassword(passWord)) {
                msg.setCode(201);
                msg.setMsg("密码含有空格或长度不够!");
                return;
            }
            String very ="captcha_"+timeStr+"_rl";
            VerifyCodeService verifyCodeService=verifyCodeServiceProvider.get();
            if (!verifyCodeService.checkVerifyCode(very,verifyCode)) {
                msg.setCode(202);
                msg.setMsg("验证码错误!");
                return;
            }

            if (null != userService.findAccountByUserName(userName)) {
                msg.setCode(199);
                msg.setMsg("已经存在的用户名!");
            } else {

                msg.setCode(204);
                msg.setMsg("注册失败!");

                String result = userService.reRegister(userName, passWord, oldname, oldsite,s, newsite);
                if ("0".equals(result)) {
                    msg.setCode(200);
                    msg.setMsg("注册成功!");
                    return;
                } else if("3".equals(result)){
                    msg.setCode(204);
                    msg.setMsg("用户已注册,请登录!");
                    return;
                } else if("5".equals(result)) {
                    msg.setCode(204);
                    msg.setMsg("该区没有游戏角色!");
                    return;
                } else {
                    msg.setCode(204);
                    msg.setMsg("更新失败!");
                    return;
                }
            }
        } catch (Exception e) {
            log.error("用户注册接口异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            DataUtil.returnJson(response, msg);
        }
    }
}
