package com.sz7road.userplatform.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.ServerMaintainDao;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.pojos.ServerMaintain2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: leo.liao
 * Date: 12-7-9
 * Time: 下午3:33
 */
@Singleton
public class ServerMaintainService extends Injection {

    private static final Logger log = LoggerFactory.getLogger(ServerMaintainService.class);

    @Inject
    private Provider<ServerMaintainDao> serverMaintainDaoProvider;


    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;



    public String fromLongToStringDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    public Collection<ServerMaintain> getMaintainServerInfoList() {
        Collection<ServerMaintain> maintainCollection = new LinkedHashSet<ServerMaintain>();
        Map<Integer, ServerMaintain> serverMaintainMaps = getMaintainServerInfo();
        if (serverMaintainMaps != null && !serverMaintainMaps.isEmpty()) {
            maintainCollection = serverMaintainMaps.values();
        }
//        log.info("维护信息列表： "+maintainCollection.size());
        return maintainCollection;
    }

    /**
     * 拿到需要维护的服务器信息
     *
     * @return
     */
    public Map<Integer, ServerMaintain> getMaintainServerInfo() {

        return cacheDataItemsServiceProvider.get().getAllSQServerMaintainInfoFromCache(new Timestamp(System.currentTimeMillis()));
    }




    public Map<Integer, ServerMaintain2> getMaintainServerInfoByGameId(int gameId,Timestamp current)
    {
        Preconditions.checkArgument(gameId>0,"非法gameId:{}",gameId);

        Map<Integer, ServerMaintain2> serverMaintainMapForGameId=Maps.newConcurrentMap();

        for(ServerMaintain2 serverMaintain2:getMaintainServerInfoAll2(current).values())
        {
            if(gameId==serverMaintain2.getGameId())
            {
                serverMaintainMapForGameId.put(serverMaintain2.getServerId(),serverMaintain2);
            }
        }

        return  serverMaintainMapForGameId;

    }

    public Map<Integer, ServerMaintain> getMaintainServerInfoAll()
    {
        Map<Integer, ServerMaintain> serverMaintainMapFromDb = Maps.newConcurrentMap();

                try {
                    final Timestamp current = new Timestamp(new Date().getTime()-60000);
                    List<ServerMaintain> maintainsServer =
                            serverMaintainDaoProvider.get().getMaintainServerFromTimeAll(current);
                    if (maintainsServer != null && !maintainsServer.isEmpty()) {
                        for (ServerMaintain serverMaintain : maintainsServer) {
                            serverMaintainMapFromDb.put(serverMaintain.getId(), serverMaintain);
                        }
                    }
                } catch (Exception e) {
                    log.error("获取维护信息列表异常{}", e.getMessage());
                    e.printStackTrace();
                }
        return serverMaintainMapFromDb;
    }

    /**
     * 获取所有的有效维护信息
     * @return   map《id,ServerMaintain2》
     */
    public Map<String, ServerMaintain2> getMaintainServerInfoAll2(Timestamp current)
    {
        return cacheDataItemsServiceProvider.get().getAllServerMaintainInfoFromCache(current);
    }


}
