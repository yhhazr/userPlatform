package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.GameTableDao;
import com.sz7road.userplatform.dao.ServerTableDao;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-19
 * Time: 上午2:18
 * Description:
 */

@Singleton
public class GameWithServerServiceBak extends Injection {

    private static final Logger log = LoggerFactory.getLogger(GameWithServerService.class.getName());
    private final ReentrantLock lock = new ReentrantLock();
    private Map<Integer, ServerTable> serverTables;
    private GameTable gameTable;

    @Inject
    private Provider<ServerTableDao> serverTableDaoProvider;
    /**
     * Constructs by google-guice.
     */
    @Inject
    public GameWithServerServiceBak() {
        super();
    }

    @Inject
    void initialized(ConfigurationService configService) {
        configService.addListener(new ConfigurationService.ConfigurationUpdateListener() {
            @Override
            public void onUpdate(ConfigurationService service) {
                lock.lock();
                try {
                    gameTable = null;
                    serverTables = Maps.newHashMap();
                } finally {
                    lock.unlock();
                }
            }
        });
        serverTables = Maps.newHashMap();
    }

    /**
     * 获取指定的游戏ID<code>gameId</code>的游戏币充值比例值。
     *
     * @param gameId 游戏ID
     * @return 游戏币充值比例值
     */
    public int getGoldScaleFor(int gameId) {
        final GameTable gameTable = getGameTable();
        if (null != gameTable) {
            final GameTable.GameEntry gameEntry = gameTable.get(gameId);
            if (null != gameEntry) {
                return gameEntry.getGoldScale();
            }
        }
        throw new RuntimeException("没有配置游戏列表或游戏列表中没有配置游戏ID为：" + gameId + "的游戏信息！");
    }

    /**
     * 检验服务器是否为有效服务器，即开通登录、充值等功能。
     *
     * @param gameId   游戏ID
     * @param serverId 游戏区服务器ID
     * @return true or false
     */
    public boolean isAvaiableServer(int gameId, int serverId) {
        final ServerTable serverTable = getGameTable().getServerTable(gameId);
        if (null != serverTable && serverTable.containsKey(serverId)) {
            final ServerTable.ServerEntry serverEntry = serverTable.get(serverId);
            return isAvaiableServer(serverEntry);
        }
        return false;
    }

    /**
     * 检验服务器是否为有效服务器，即开通登录、充值等功能。
     *
     * @param serverEntry 游戏服务器对象
     * @return true or false
     */
    public boolean isAvaiableServer(ServerTable.ServerEntry serverEntry) {
        return null != serverEntry && serverEntry.getServerStatus() == ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode();
    }

    /**
     * 获取游戏列表。
     *
     * @return gameTable.
     */
    public GameTable getGameTable() {
        // 检查缓存中是否存在已更新的游戏表。
        if (null == gameTable) {
            lock.lock();
            if (null == gameTable) {
                try {

                    // 加载游戏服务器列表。
                    final ServerTableDao serverTableDao = getInstance(ServerTableDao.class);
                    final List<ServerTable.ServerEntry> serverEntries = serverTableDao.listsWithStatus();
                    if (null != serverEntries) {
                        for (ServerTable.ServerEntry entry : serverEntries) {
                            int gameId = entry.getGameId();
                            if (!serverTables.containsKey(gameId)) {
                                serverTables.put(gameId, getInstance(ServerTable.class));
                            }
                            final ServerTable serverTable = serverTables.get(gameId);
                            serverTable.put(entry.getId(), entry);
                        }
                    }

                    final GameTableDao gameTableDao = getInstance(GameTableDao.class);
                    final List<GameTable.GameEntry> entryList = gameTableDao.listAll();
                    if (null != entryList || !entryList.isEmpty()) {
                        gameTable = new GameTable() {
                            @Override
                            public ServerTable getServerTable(int gameId) {
                                return serverTables.get(gameId);
                            }
                            @Override
                            protected void addServerTable(int gameId, ServerTable table) {

                            }
                        };
                        for (GameTable.GameEntry entry : entryList) {
                            gameTable.put(entry.getId(), entry);
                        }
                    }
                } catch (final Exception e) {
                    log.error("获取游戏列表异常：{}", e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        }

        return gameTable;
    }

    /**
     * 直接从数据库中拿到所有的没有关闭的服务器列表
     *
     * @return
     */
    public List<ServerEntity> getSortedServerEntries(int gameId) {
        return  serverTableDaoProvider.get().getSortNonClosedServersByGameId(gameId);
    }
    /**
     * 直接从数据库中拿到所有的没有关闭的服务器Map
     *
     * @return
     */
    public Map<Integer,ServerEntity> getSortedServerEntriesMap(int gameId) {
        Map<Integer,ServerEntity> serverEntitiesMap=new HashMap<Integer, ServerEntity>();
        List<ServerEntity> serverEntityList=  serverTableDaoProvider.get().getSortNonClosedServersByGameId(gameId);
        if(serverEntityList!=null&&!serverEntityList.isEmpty())
        {
            for(ServerEntity serverEntity:serverEntityList)
            {
                serverEntitiesMap.put(serverEntity.getId(),serverEntity);
            }
        }
        return serverEntitiesMap;
    }


    public ServerTable.ServerEntry getGameServer(int gameId, int gameZoneId) {
        final GameTable gameTable = getGameTable();

        if (null != gameTable) {
            final ServerTable serverTable = gameTable.getServerTable(gameId);
            if (null != serverTable) {
                final ServerTable.ServerEntry serverEntry = serverTable.get(gameZoneId);
                if (serverEntry == null) {
                    return null;
                }
                return serverEntry;
            }
        }
        return null;
    }

    public static String getServerFormatValueByKey(ServerTable.ServerEntry serverEntry, String key) {
        if (null != serverEntry) {
            final Map<String, String> variables = serverEntry.getVariables();
            String value = null;
            if (variables != null) {
                value = variables.get(key);
            }

            if (Strings.isNullOrEmpty(value)) {
                value = ConfigurationUtils.get(key);
                if (!Strings.isNullOrEmpty(value)) value = String.format(value, serverEntry.getServerNo());
            }
            return value;
        }
        return null;
    }

    public static String getServerValueByKey(ServerTable.ServerEntry serverEntry, String key) {
        if (null != serverEntry) {
            final Map<String, String> variables = serverEntry.getVariables();
            String value = null;
            if (variables != null) {
                value = variables.get(key);
            }

            if (Strings.isNullOrEmpty(value)) {
                value = ConfigurationUtils.get(key);
            }
            return value;
        }
        return null;
    }
}
