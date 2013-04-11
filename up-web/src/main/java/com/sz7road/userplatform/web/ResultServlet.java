/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.OrderService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

@Singleton
class ResultServlet extends HeadlessHttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ResultServlet.class.getName());

    @Inject
    private Provider<OrderService> orderServiceProvider;
    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<ServerDataService> serverDataServerProvider;

    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String orderId = request.getParameter("_n");

        try {
            final OrderService orderService = orderServiceProvider.get();
            OrderObject order = orderService.findByOrderId(orderId);
            if (order == null) {
                request.setAttribute("result", "fail");
                request.getRequestDispatcher(ConfigurationUtils.get("payFail.domainUrl")).forward(request, response);
            }
            String serverName = "";
            ServerInfo serverInfo = getServerInfo(order.getGameId(), order.getZoneId());
            if (serverInfo != null) {
                serverName = serverInfo.getServerName();
            }

            //设置订单显示页所需数据
            request.setAttribute("game",cacheDataItemsServiceProvider.get().getGameEntryFromCacheByGameId(order.getGameId()) );
            request.setAttribute("gameName", getGameName(order.getGameId()));
            request.setAttribute("orderId", order.getId());
            request.setAttribute("userName", getUserName(order.getUserId()));
            request.setAttribute("gameServerName", serverName);
            request.setAttribute("amount", order.getAmount());
            request.setAttribute("gold", order.getGold());
            request.setAttribute("gameId", order.getGameId());
            request.setAttribute("zoneId", order.getZoneId());
             String typeInfo= order.getChannelTag().toString() + "," + order.getSubTag().toString();
            request.setAttribute("typeInfo", typeInfo);
            byte status = order.getStatus();
            request.setAttribute("status", status);
            request.getRequestDispatcher(ConfigurationUtils.get("payFail.domainUrl")).forward(request, response);
        } catch (Exception e) {
            logger.error("system error");
        }
    }

    /*
    * 获取游戏服务器名称
    */
    private ServerInfo getServerInfo(int gameId, int serverId) {
        ServerDataService serverDataServer = serverDataServerProvider.get();
        return serverDataServer.get(gameId, serverId);
    }

    private String _getServerName(int gameId, int zoneId) {
        String gameServerName = "";
        GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
        GameTable gameTable = gameWithServerService.getGameTable();
        ServerTable serverTable = gameTable.getServerTable(gameId);
        Set server = serverTable.keySet();
        Iterator<Integer> iter = server.iterator();
        Integer key = 0;

        while (iter.hasNext()) {
            key = iter.next();
            if (key == zoneId) {
                gameServerName = serverTable.get(key).getServerName();
            }
        }
        return gameServerName;
    }

    private String getUserName(int userId) {
        UserService userService = userServiceProvider.get();
        UserAccount userAccount = userService.findAccountById(userId);
        return userAccount.getUserName();
    }

    /*
     * 获取游戏名称
     */
    private String getGameName(int gameId) {
        GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
        GameTable gameTable = gameWithServerService.getGameTable();
        GameTable.GameEntry gameEnty = gameTable.get(gameId);
        return gameEnty.getGameName();
    }
}
