/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.role.RoleHandler;
import com.sz7road.userplatform.role.RoleManager;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

@Singleton
class GetRole2Servlet extends HeadlessHttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(PayServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<GameWithServerService> gameWithProvider;
    @Inject
    private Provider<ServerDataService> serverDataServerProvider;
    @Inject
    private Provider<RoleManager> roleManagerProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String strGameId = request.getParameter("gameId");
        String strServerId = request.getParameter("serverId");

        try {
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(username);
            if (userAccount == null) {
                throw new IllegalArgumentException("用户名不存在");
            }

            int gameId = 1;
            int serverId = 1;

            if (!Strings.isNullOrEmpty(strGameId)) {
                gameId = Integer.parseInt(strGameId);
            }
            if (!Strings.isNullOrEmpty(strServerId)) {
                serverId = Integer.parseInt(strServerId);
            }

            ServerDataService serverDataService = serverDataServerProvider.get();
            ServerInfo serverInfo = serverDataService.get(gameId, serverId);
            if (serverInfo == null) {
                throw new IllegalArgumentException("游戏区服不存在");
            }

            ServerInfo mainServerInfo = serverInfo;
            if (serverInfo.getMainId() != 0 && serverInfo.getMainId() != serverInfo.getServerId()) {
                mainServerInfo = serverDataService.get(gameId, serverInfo.getMainId());
            }
            if (mainServerInfo == null) {
                throw new IllegalArgumentException("游戏区服不存在");
            }

            boolean isOpen = mainServerInfo.getServerStatus() >= 0;
            if (!isOpen) {
                out.print("-2");
                return;
            }

            RoleHandler handler =  roleManagerProvider.get().get(gameId);
            if (handler == null) {
                throw new IllegalArgumentException("游戏不存在");
            }

            Map<String, String> roleList = handler.getRoles(userAccount, serverInfo, mainServerInfo);

            if(roleList != null && !roleList.isEmpty()){
                Set<String> roleSet = roleList.keySet();
                for (String role : roleSet) {
                    out.print("<option value=" + role + " selected='selected'>" + roleList.get(role) + "</option>");
                }
            }else{
                out.print("-1");//未创建角色时
            }
        } catch (Exception e) {
            out.print("-3");
            logger.error(e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }
}
