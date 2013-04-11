/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * @author jeremy
 */
@Singleton
@Parameter(value = {"user:", "password:"}, method = "POST")
class SignUpHttpServlet extends HeadlessServlet {

    private static final Logger log = LoggerFactory.getLogger(SignUpHttpServlet.class.getName());

    @Inject
    private Provider<FilterCharacterProvider> dirtyProvider;

    @Override
    protected void doPost(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        final String userName = request.getNotNullParameter("user").trim();
        final String passWord = request.getNotNullParameter("password").trim();
//        final String email = request.getParameter("email");
//        final String icn = request.getParameter("icn");
//        final String realName = request.getParameter("name");
        final String site = request.getParameter("site");
        final Timestamp createTime = new Timestamp(System.currentTimeMillis());

        try {
            final UserService userService = getInstance(UserService.class);
            final FilterCharacterProvider dirtyService = dirtyProvider.get();
            // 验证用户名
            if (!userService.isLegalNaming(userName)) {
                response.sendFailure("invalid name");
                return;
            }
            if (!userService.isNameCanReg(userName)) {
                response.sendFailure("name can not register");
                return;
            }
            if (dirtyService.isContainKey(userName)) {
                response.sendFailure("name contain filter character");
                return;
            }
            if (!userService.isLegalPassword(passWord)) {
                response.sendFailure("invalid password");
                return;
            }
            if (null != userService.findAccountByUserName(userName)) {
                // 已经存在该用户
                response.sendFailure("already exists");
            } else {
                final UserAccount account = userService.signUpReWrite(userName, passWord);
                if (null != account && account.getId() > 0) {
                    final UserObject userObject = new UserObject();
                    userObject.setRealName("");
                    userObject.setIcn("");
                    userObject.setSite(site);
                    userObject.setAccount(account);

                    userObject.setCreateTime(createTime);
                    userObject.setLastLoginTime(createTime);
                    userObject.setLoginSum(0);
                    userObject.setPswStrength(RuleUtil.getPswStrength(passWord));
                    if (userService.saveData(userObject)) {
                        response.sendSuccess();
                        return;
                    }
                }
                response.sendFailure();
            }
        } catch (final Exception e) {
            log.error("用户注册接口异常：{}", e.getMessage());
            response.sendError(404);
            e.printStackTrace();
        }
    }
}
