/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.google.common.collect.Maps;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.LoginGameService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
@Singleton
@Parameter(value = {"user:", "code:", "ip:"}, method = "POST")
class LoginAuthHttpServlet extends HeadlessServlet {

    static final Logger log = LoggerFactory.getLogger(LoginAuthHttpServlet.class.getName());

    private String savePath = "resource\\qqwry.dat"; //IP数据库文件的保存路径
    private ServletContext sc;  //application对象

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        sc = servletConfig.getServletContext();
    }

    @Override
    protected void doPost(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        // fetch parameters
        final String userName = request.getNotNullParameter("user");
        final String passWord = request.getNotNullParameter("code");
        final String ip = request.getParameter("ip");
        final int gameId = request.getIntParameter("gameId");

        try {

            final UserService userService = getInstance(UserService.class);
            final UserAccount account = userService.authenticated(userName, passWord);
            if (null == account) {
                // 该用户不存在或密码不正确
                response.sendFailure("invalid user or code");
            } else {
                // 验证通过
                final UserObject user = userService.findByAccount(account);
                if (null != user) {

                    Map<String, Object> map = Maps.newHashMap();
                    Map<String,Object> userMap= Maps.newHashMap();
                    userMap.put("id",user.getId());
                    userMap.put("userName",user.getUserName());
                    userMap.put("lastGameId",user.getLastGameId());
                    userMap.put("lastGameZoneId",user.getLastGameZoneId());
                    map.put("user", userMap);
                    //  最后登录时间，最后登录ip
                    GameWithServerService gs = getInstance(GameWithServerService.class);

                    LoginGameService loginGameService = getInstance(LoginGameService.class);
                    if (gameId > 0) {
                        List<LoginGame> loginGameList = loginGameService.list(user.getId(), gameId);
                        if (loginGameList  != null && !loginGameList.isEmpty()) {
                            LoginGame loginGame = loginGameList.get(0);
                            map.put("gs", gs.getGameServerEntity(loginGame.getGameId(), loginGame.getServerId()));
                        } else {
                            map.put("gs", null);
                        }
                    } else {
                        List<LoginGame> loginGameList = loginGameService.list(user.getId());
                        List<ServerEntity> serverEntryList = new ArrayList<ServerEntity>();
                        if (serverEntryList != null){
                            for (LoginGame entity : loginGameList) {
                                serverEntryList.add(gs.getGameServerEntity(entity.getGameId(), entity.getServerId()));
                            }
                        }
                        map.put("gs", serverEntryList);
                    }
                    //map.put("gs", gs.getGameServerEntity(user.getLastGameId(), user.getLastGameZoneId()));
                    response.sendSuccess(map);

                    // 异步运算用户登录后的数据逻辑及日志记录。
                    user.setLoginSum(user.getLoginSum() + 1);
                    user.setLastIp(ip);
                    user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
                    user.setPswStrength(RuleUtil.getPswStrength(passWord));
                    userService.runLoginAsyncHooks(user);
                }
            }

        } catch (final Exception e) {
            log.error("登录验证接口异常：{}", e.getMessage());
            response.sendError(404);
            e.printStackTrace();
        }
    }
}
