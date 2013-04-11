package com.sz7road.userplatform.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.GameTableDao;
import com.sz7road.userplatform.dao.ServerMaintainDao;
import com.sz7road.userplatform.dao.ServerTableDao;
import com.sz7road.userplatform.dao.cachedao.CacheGameDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryNotForSqDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerMaintain2Dao;
import com.sz7road.userplatform.dao.cacheimp.AbstractCacheDaoImp;
import com.sz7road.userplatform.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-10-29
 * Time: 上午6:36
 * Description: 缓存数据操作的service层
 */
@Singleton
public class CacheDataItemsService extends Injection {

    private final static Logger log = LoggerFactory.getLogger(CacheDataItemsService.class);

    @Inject
    private Provider<GameTableDao> gameTableDaoProvider;

    @Inject
    private Provider<ServerTableDao> serverTableDaoProvider;

    @Inject
    private Provider<CacheGameDao> cacheGameDaoProvider;

    @Inject
    private Provider<CacheServerEntryDao> cacheServerEntryDaoProvider;


    @Inject
    private Provider<CacheServerEntryNotForSqDao> CacheServerEntryNotForSqDaoProvider;


    @Inject
    private Provider<CacheServerMaintain2Dao> cacheServerMaintain2DaoProvider;


    /**
     * 把所需的服务区数据重新加载到缓存中
     *
     * @return
     */
    public boolean reloadCacheDataOfServerEntries() {
        try {
            return cacheServerEntryDaoProvider.get().reloadCache(serverTableDaoProvider.get().listsWithStatus(), null);
        } catch (SQLException e) {
            log.error("重新加载缓存中的服务器数据异常!");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 把所需的服务区数据重新加载到缓存中
     *
     * @return
     */
    public boolean reloadCacheDataOfServerEntriesNotForSq() {
        try {
            List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryNotForSqList = serverTableDaoProvider.get().listsWithStatusNotForSq();
            if (serverEntryNotForSqList != null && !serverEntryNotForSqList.isEmpty()) {
                return CacheServerEntryNotForSqDaoProvider.get().reloadCache(serverEntryNotForSqList, null);
            } else {
                serverEntryNotForSqList = serverTableDaoProvider.get().listsWithStatusNotForSq();
                return CacheServerEntryNotForSqDaoProvider.get().reloadCache(serverEntryNotForSqList, null);
            }
        } catch (SQLException e) {
            log.error("重新加载缓存中的的非神曲服务器数据异常!");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 把所需的游戏数据重新加载到redis缓存中
     *
     * @return
     */
    public boolean reloadCacheDataOfGameEntries() {
        try {
            return cacheGameDaoProvider.get().reloadCache(gameTableDaoProvider.get().listAll(), null);
        } catch (SQLException e) {
            log.error("重新加载缓存中的游戏数据异常!");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 把所需的维护信息重新加载到redis缓存中
     *
     * @return
     */
    public boolean reloadCacheDataOfServerMaintainInfo(Timestamp current) {
        try {
            return cacheServerMaintain2DaoProvider.get().reloadCache(getInstance(ServerMaintainDao.class).getMaintainServerFromTime2(current).values(), null);
        } catch (Exception e) {
            log.error("重新加载缓存中的维护信息数据异常!");
            e.printStackTrace();
        }
        return false;
    }


    public List<GameTable.GameEntry> getGameEntriesFromCache() {
        List<GameTable.GameEntry> gameEntryList = null;
        try {
            gameEntryList = cacheGameDaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_GAME_ENTRIES_KEY);
            if (gameEntryList == null || gameEntryList.isEmpty()) {
                reloadCacheDataOfGameEntries();
                gameEntryList = cacheGameDaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_GAME_ENTRIES_KEY);
                if(gameEntryList!=null&&!gameEntryList.isEmpty())
                {
                    Collections.sort(gameEntryList,new Comparator<GameTable.GameEntry>() {
                        @Override
                        public int compare(GameTable.GameEntry o1, GameTable.GameEntry o2) {
                            return o1.getShowOrder()-o2.getShowOrder();
                        }
                    });
                }
            }
        } catch (Exception ex) {
            log.error("redis缓存中获取游戏数据异常!");
            ex.printStackTrace();
        }
        return gameEntryList;
    }

    /**
     * 根据游戏Id获得游戏信息
     * @param gameId
     * @return
     */
    public GameTable.GameEntry getGameEntryFromCacheByGameId(int gameId) {
         Preconditions.checkArgument(gameId>0,"gamId非法：{}",gameId);
        GameTable.GameEntry gameEntryWanted=null;
         List<GameTable.GameEntry> gameEntryList=getGameEntriesFromCache();
         if(gameEntryList!=null&&!gameEntryList.isEmpty())
         {
             for(GameTable.GameEntry gameEntry:gameEntryList)
             {
                 if(gameId==gameEntry.getId())
                 {
                     gameEntryWanted=gameEntry;
                 }
             }
         }
        return gameEntryWanted;
    }

    public List<ServerTable.ServerEntry> getServerEntriesFromCache() {
        List<ServerTable.ServerEntry> serverEntryList = null;
        try {
            serverEntryList = cacheServerEntryDaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_ENTRIES_KEY);
            if (serverEntryList == null || serverEntryList.isEmpty()) {
                reloadCacheDataOfServerEntries();
                serverEntryList = cacheServerEntryDaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_ENTRIES_KEY);
            }
        } catch (Exception ex) {
            log.error("redis缓存中获取服务区数据异常!");
            ex.printStackTrace();
        }
        return serverEntryList;
    }

    public List<ServerTableNotForSq.ServerEntryNotForSq> getServerEntriesFromCacheNotForSq() {
        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryList = null;
        try {
            serverEntryList = CacheServerEntryNotForSqDaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_ENTRIES_NOTSQ_KEY);
            if (serverEntryList == null || serverEntryList.isEmpty()) {
                reloadCacheDataOfServerEntriesNotForSq();
                serverEntryList = CacheServerEntryNotForSqDaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_ENTRIES_NOTSQ_KEY);
            }
        } catch (Exception ex) {
            log.error("redis缓存中获取服务区数据异常!");
            ex.printStackTrace();
        }
        return serverEntryList;
    }

    /**
     * 取得一个服务区，根据游戏id 和服务器id
     *
     * @param gameId
     * @param serverId
     * @return
     */
    public ServerTableNotForSq.ServerEntryNotForSq getServerEntryFromCacheNotForSqByGameIdAndServerId(int gameId, int serverId) {
        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryNotForSqList = getServerEntriesFromCacheNotForSq();

        ServerTableNotForSq.ServerEntryNotForSq serverEntryNotForSq = null;
        if (serverEntryNotForSqList != null && !serverEntryNotForSqList.isEmpty()) {
            for (ServerTableNotForSq.ServerEntryNotForSq serverEntry : serverEntryNotForSqList) {
                if (serverEntry.getGameId() == gameId && serverEntry.getServerId() == serverId) {
                    serverEntryNotForSq = serverEntry;
                    break;
                }
            }
        }
        return serverEntryNotForSq;
    }

    /**
     * 取得一种游戏的所有服务区
     *
     * @param gameId
     * @return
     */
    public List<ServerTableNotForSq.ServerEntryNotForSq> getServerEntriesFromCacheNotForSqByGameId(int gameId) {
        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryNotForSqList = getServerEntriesFromCacheNotForSq();

        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryNotForSqs = Lists.newLinkedList();
        if (serverEntryNotForSqList != null && !serverEntryNotForSqList.isEmpty()) {
            for (ServerTableNotForSq.ServerEntryNotForSq serverEntry : serverEntryNotForSqList) {
                if (serverEntry.getGameId() == gameId) {
                    serverEntryNotForSqs.add(serverEntry);
                }
            }
        }
        return serverEntryNotForSqs;
    }

    /**
     * 取得非神曲服务器的即将开服列表
     *
     * @return
     */
    public List<ServerTableNotForSq.ServerEntryNotForSq> getSoonOpenedServerEntriesFromCacheNotForSq() {
        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryNotForSqList = getServerEntriesFromCacheNotForSq();

        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryNotForSqs = Lists.newLinkedList();
        if (serverEntryNotForSqList != null && !serverEntryNotForSqList.isEmpty()) {
            for (ServerTableNotForSq.ServerEntryNotForSq serverEntry : serverEntryNotForSqList) {
                final Timestamp openTime = serverEntry.getOpenTime();
                boolean flag = false;
                if (openTime != null) {
                    flag = (serverEntry.getOpenTime().getTime() - System.currentTimeMillis() <= 1000 * 60 * 60);
                }
                if (serverEntry.getStatus() == -3 && flag) {
                    serverEntryNotForSqs.add(serverEntry);
                }
            }
        }
        return serverEntryNotForSqs;
    }


    /**
     * 取得所有游戏的维护信息Table
     *
     * @return
     */
    public Map<String,ServerMaintain2> getAllServerMaintainInfoFromCache(Timestamp current) {

        Map<String,ServerMaintain2> serverMaintain2Map= Maps.newHashMap();
        List<ServerMaintain2>  serverMaintain2List=cacheServerMaintain2DaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY);

        try {
            if (serverMaintain2List == null || serverMaintain2List.isEmpty()) {
                reloadCacheDataOfServerMaintainInfo(current);
                serverMaintain2List = cacheServerMaintain2DaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY);
            }

            if(serverMaintain2List!=null&&!serverMaintain2List.isEmpty())
            {
                for(ServerMaintain2 serverMaintain2:serverMaintain2List)
                {
                     serverMaintain2Map.put(serverMaintain2.getGameId()+"@"+serverMaintain2.getServerId(),serverMaintain2);
                }
            }
        } catch (Exception ex) {
            log.error("redis缓存中获取维护信息数据异常!");
            ex.printStackTrace();
        }
        return serverMaintain2Map;

    }


    /**
     * 取得所有游戏的维护信息Table
     *
     * @return
     */
    public Map<String,ServerMaintain2> getAllServerMaintainInfoFromCache(int gameId,Timestamp current) {
        Preconditions.checkArgument(gameId>0,"gameId 非法：{}",gameId);
        Map<String,ServerMaintain2> serverMaintain2Map= Maps.newHashMap();
        List<ServerMaintain2>  serverMaintain2List=cacheServerMaintain2DaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY);

        try {
            if (serverMaintain2List == null || serverMaintain2List.isEmpty()) {
                reloadCacheDataOfServerMaintainInfo(current);
                serverMaintain2List = cacheServerMaintain2DaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY);
            }

            if(serverMaintain2List!=null&&!serverMaintain2List.isEmpty())
            {
                for(ServerMaintain2 serverMaintain2:serverMaintain2List)
                {
                    if(gameId==serverMaintain2.getGameId())
                    serverMaintain2Map.put(serverMaintain2.getGameId()+"@"+serverMaintain2.getServerId(),serverMaintain2);
                }
            }
        } catch (Exception ex) {
            log.error("redis缓存中获取维护信息数据异常!");
            ex.printStackTrace();
        }
        return serverMaintain2Map;

    }


    /**
     * 从缓存中取得神曲的维护信息
     *
     * @return
     */
    public Map<Integer,ServerMaintain> getAllSQServerMaintainInfoFromCache(Timestamp current) {
        Map<Integer,ServerMaintain> serverMaintain2Map= Maps.newHashMap();
        List<ServerMaintain2>  serverMaintain2List=cacheServerMaintain2DaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY);


        try {
            if (serverMaintain2List == null || serverMaintain2List.isEmpty()) {
                reloadCacheDataOfServerMaintainInfo(current);
                serverMaintain2List = cacheServerMaintain2DaoProvider.get().queryCacheItems(AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY);
            }

            if(serverMaintain2List!=null&&!serverMaintain2List.isEmpty())
            {
                for(ServerMaintain2 serverMaintain2:serverMaintain2List)
                {
                    if(serverMaintain2.getGameId()==1)
                    serverMaintain2Map.put(serverMaintain2.getServerId(),transform(serverMaintain2));
                }
            }
        } catch (Exception ex) {
            log.error("redis缓存中获取维护信息数据异常!");
            ex.printStackTrace();
        }
        return serverMaintain2Map;

    }

    private  ServerMaintain transform(ServerMaintain2 serverMaintain2)
    {
       ServerMaintain serverMaintain=new ServerMaintain();
        serverMaintain.setId(serverMaintain2.getId());
        serverMaintain.setCreateTime(serverMaintain2.getCreateTime());
        serverMaintain.setEndTime(serverMaintain2.getEndTime());
        serverMaintain.setMessage(serverMaintain2.getMessage());
        serverMaintain.setStartTime(serverMaintain2.getStartTime());
        serverMaintain.setServerId(serverMaintain2.getServerId());

        return  serverMaintain;
    }


}
