/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.google.common.collect.Maps;
import com.sz7road.userplatform.pojos.LoginGame;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.LoginGameService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.apache.http.protocol.HTTP;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
@Singleton
@Parameter({"value:", "type:"})
class CheckAccountHttpServlet extends HeadlessServlet {

    @Override
    protected void doService(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        response.setContentType(HTTP.PLAIN_TEXT_TYPE);
        final String value = request.getNotNullParameter("value").trim();
        final int type = request.getIntParameter("type");
        final boolean details = request.getBoolParameter("details");
        final int gameId = request.getIntParameter("gameId");

        final UserService userService = getInstance(UserService.class);
        UserAccount account;

        switch (type) {
            case 1:
                account = userService.findAccountByUserName(value);
                break;
            case 2:
                account = userService.findByEmail(value);
                break;
            default:
                response.sendInvalidParameters();
                return;
        }

        if (null == account) {
            response.sendFailure();
        } else if (details) {
            // 验证通过
            final UserObject user = userService.findByAccount(account);
            Map<String,Object> map = Maps.newHashMap();
            map.put("user",user);
            GameWithServerService gs = getInstance(GameWithServerService.class);
            LoginGameService loginGameService = getInstance(LoginGameService.class);
            try{
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
            } catch (Exception e){
                e.printStackTrace();
                map.put("gs", null);
            }
            //map.put("gs",gs.getGameServerEntity(user.getLastGameId(),user.getLastGameZoneId()));
            response.sendSuccess(map);
        } else {
            response.sendSuccess();
        }
    }
}
