/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;


import com.google.common.collect.Sets;


import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.GetCacheDataThread;
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


    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        int gameId = request.getIntParameter("_g");

        if (gameId <= 0) {
            notFound(response);
            return;
        }

        final GameWithServerService service = getInstance(GameWithServerService.class);
        try {
            ServerTable serverTable = service.getGameTable().getServerTable(gameId);
            Set<ServerEntity> serverEntrySet = Sets.newTreeSet(new Comparator<ServerEntity>() {
                @Override
                public int compare(ServerEntity o1, ServerEntity o2) {
                    if (o1 == null || o2 == null) return 0;
                    if (o1.getServerNo() > o2.getServerNo()) {
                        return 1;
                    } else if (o1.getServerNo() < o2.getServerNo()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            Collection<ServerTable.ServerEntry> serverEntries=null;
            if(serverTable!=null&&!serverTable.isEmpty())
            {
                serverEntries= serverTable.values();
            }
            if(serverEntries!=null&&!serverEntries.isEmpty())
            {
                for(ServerTable.ServerEntry serverEntry:serverEntries)
                {
                    int serverStatus=serverEntry.getServerStatus();
                    if( serverStatus== -2||serverStatus==1){
                        serverEntrySet.add(DataUtil.transToServerEntity(serverEntry));
                    }
                }
            }
            final PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            try {
                final ObjectMapper mapper = new ObjectMapper();
                if(serverEntrySet!=null&&!serverEntrySet.isEmpty())
                {
                    mapper.writeValue(out, serverEntrySet);
                }
                else
                {
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