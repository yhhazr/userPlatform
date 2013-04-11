/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.common.base.Strings;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.sz7road.userplatform.pojos.GameTable;

import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
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
import java.sql.Timestamp;
import java.util.*;

@Singleton
class GetServerListServlet extends HeadlessHttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PayServlet.class.getName());
    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;


    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //游戏id
        String gameId_str = request.getParameter("gameId");
        if (Strings.isNullOrEmpty(gameId_str)) {
            gameId_str = "1";
        }
        int gid = Integer.valueOf(gameId_str);
        //设置游戏id
        request.setAttribute("gameId", gid);
        //设置游戏图片
        request.setAttribute("gameSrc", "cz_cen_youxi.jpg");

        try {
            getGameServerForMultiply(request, out, gid);

        } catch (Exception e) {
            logger.error("system error");
        }
    }

    private void getGameServer(HttpServletRequest request, PrintWriter out, int gameId) throws Exception {

        GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
        GameTable gameTable = gameWithServerService.getGameTable();
        ServerTable serverTable = gameTable.getServerTable(gameId);
        if (serverTable == null) {
            out.print("nothing");
            return;
        }

        Set server = serverTable.keySet();
        out.print("<option value=0>--请选择服务器--</option>");
        Iterator<Integer> iter = server.iterator();
        Integer key = 0;
        Integer serverNo = 0;
        List<Integer> list = new ArrayList<Integer>();
        Map<Integer, Integer> serverMap = new HashMap<Integer, Integer>();
        while (iter.hasNext()) {
            key = iter.next();
            serverNo = serverTable.get(key).getServerNo();
            list.add(serverNo);
            serverMap.put(serverNo, key);
        }

        //对服务器按照serverNo(服务器的号码)进行排序
        Collections.sort(list);
        int serverId = 0;
        Timestamp current = new Timestamp(System.currentTimeMillis());
        for (Integer i : list) {
            serverId = serverMap.get(i);
            ServerTable.ServerEntry serverEntry = serverTable.get(serverId);
            if (serverEntry.getOpeningTime().before(current)) //确保真的到开服时间
            {
                if (serverEntry.getServerStatus() == -2) {
                    out.print("<option  value=" + serverId + ">" + serverEntry.getServerName() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;正在维护</option>");
                } else if (serverEntry.getServerStatus() == 1) {
                    out.print("<option value=" + serverId + ">" + serverEntry.getServerName() + "</option>");
                }
            }
        }
        out.flush();
        out.close();
    }

    private void getGameServerForMultiply(HttpServletRequest request, PrintWriter out, int gameId) throws Exception {

        List<ServerInfo> serverInfoList = serverDataServiceProvider.get().list(gameId);
        if (serverInfoList == null||serverInfoList.isEmpty()) {
            out.print("nothing");
            return;
        }
        Collections.sort(serverInfoList, new Comparator<ServerInfo>() {
            @Override
            public int compare(ServerInfo o1, ServerInfo o2) {
                return o1.getServerNo() - o2.getServerNo();
            }
        });
        out.print("<option value=0>--请选择服务器--</option>");
        Timestamp current = new Timestamp(System.currentTimeMillis());
        for (ServerInfo server : serverInfoList) {
            if (server.getOpeningTime().before(current)) //确保真的到开服时间
            {
                if (server.getServerStatus() == -2) {
                    out.print("<option  value=" + server.getServerId() + ">" + server.getServerName() + "&nbsp;&nbsp;&nbsp;&nbsp;正在维护</option>");
                } else if (server.getServerStatus() == 1) {
                    out.print("<option value=" + server.getServerId() + ">" + server.getServerName() + "</option>");
                }
            }
        }
        out.flush();
        out.close();
    }

}
