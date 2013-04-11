/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.GameWithServerUtils;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

@Singleton
class GetRoleServlet extends HeadlessHttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PayServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<GameWithServerService> gameWithProvider;
    @Inject
    private Provider<GameWithServerUtils> gameWithServerUtilsProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String serverId_str = request.getParameter("serverId");
        String gameId_str = request.getParameter("gameId");

        try {
            int gameId = 1;
            int serverId = 1;
            if (!Strings.isNullOrEmpty(gameId_str)) {
                gameId = Integer.parseInt(gameId_str);
            }
            if (!Strings.isNullOrEmpty(serverId_str)) {
                serverId = Integer.parseInt(serverId_str);
            }
            GameWithServerService gameWithServerService = gameWithProvider.get();
            boolean isOpen = gameWithServerService.isAvaiableServer2(gameId, serverId);
            if (!isOpen) {
                out.print("-2");
                return;
            }

            long timeStart = System.currentTimeMillis();

            Map<String, String> roleList = gameWithServerUtilsProvider.get().getRole(username, gameId, serverId);

            if(roleList != null && !roleList.isEmpty()){
                Set<String> roleSet = roleList.keySet();
                for (String role : roleSet) {
                    out.print("<option value=" + role + " selected='selected'>" + roleList.get(role) + "</option>");
                }
            }else{
                out.print("-1");//未创建角色时
            }

            long endTime = System.currentTimeMillis();
            logger.info("-------time:" + (endTime - timeStart));
        } catch (Exception e) {
            out.print("-3");
            logger.error("system error");
        } finally {
            out.flush();
            out.close();
        }
    }
}
