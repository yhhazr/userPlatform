/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.LoginGameService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.apache.http.protocol.HTTP;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
@Singleton
@Parameter({"value:", "type:"})
class CheckAccount2HttpServlet extends HeadlessServlet {

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

            try{
                LoginGameService loginGameService = getInstance(LoginGameService.class);
                List<LoginGame> loginGameList = new ArrayList<LoginGame>();

                if (gameId > 0) {
                    loginGameList = loginGameService.list(user.getId(), gameId);
                } else {
                    loginGameList = loginGameService.list(user.getId());
                }
                map.put("gs", getLastLoginGames(loginGameList));

            } catch (Exception e){
                e.printStackTrace();
                map.put("gs", null);
            }
            response.sendSuccess(map);
        } else {
            response.sendSuccess();
        }
    }

    public List<ServerEntity2> getLastLoginGames(List<LoginGame> loginGameList) {
        if (loginGameList == null || loginGameList.isEmpty()) {
            return null;
        }

        ServerDataService serverDataService = getInstance(ServerDataService.class);
        List<ServerEntity2> list = new ArrayList<ServerEntity2>();
        for(LoginGame loginGame : loginGameList) {
            ServerInfo serverInfo = serverDataService.get(loginGame.getGameId(), loginGame.getServerId());

            ServerEntity2 serverEntity2 = new ServerEntity2();
            serverEntity2.setId(loginGame.getServerId());
            serverEntity2.setGameId(loginGame.getGameId());
            serverEntity2.setGameName(getGameName(loginGame.getGameId()));
            serverEntity2.setServerNo(serverInfo.getServerNo());
            serverEntity2.setServerName(serverInfo.getServerName());
            serverEntity2.setServerStatus(serverInfo.getServerStatus());
            serverEntity2.setRecommand(serverInfo.isRecommand());
            serverEntity2.setCreateTime(serverInfo.getCreateTime());
            serverEntity2.setOpeningTime(serverInfo.getOpeningTime());

            list.add(serverEntity2);
        }

        return list;
    }

    private String getGameName(int gameId) {
        GameWithServerService gameWithServerService = getInstance(GameWithServerService.class);
        GameTable gameTable = gameWithServerService.getGameTable();
        GameTable.GameEntry gameEnty = gameTable.get(gameId);
        return gameEnty.getGameName();
    }
}
