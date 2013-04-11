/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;


import com.google.common.collect.Sets;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.GetCacheDataThread;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.utils.JedisFactory;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author jeremy
 */
@Singleton
@Parameter(value = "_g:")
public class ListGameServerTableServlet extends HeadlessServlet {

    private static final Logger log = LoggerFactory.getLogger(ListGameServerTableServlet.class.getName());

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        int gameId = request.getIntParameter("_g");

        if (gameId <= 0) {
            notFound(response);
            return;
        }

        List<ServerInfo> serverInfoList = serverDataServiceProvider.get().list(gameId);
        final PrintWriter out = response.getWriter();
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
        try {
            response.setContentType("application/json");
            try {
                final ObjectMapper mapper = new ObjectMapper();
                if (serverInfoList != null && !serverInfoList.isEmpty()) {
                    mapper.writeValue(out, serverInfoList);
                } else {
                    notFound(response);
                    log.info("从redis中读取数据失败!");
                }
            } finally {
                out.flush();
                out.close();
            }
        } catch (final Exception e) {
            notFound(response);
            log.error("获取游戏服务区列表网关异常：{}", e.getMessage());
            e.printStackTrace();
        }
    }
}