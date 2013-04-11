package com.sz7road.userplatform.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.InjectionProxy;
import com.sz7road.userplatform.dao.ServerTableDecorateDao;
import com.sz7road.userplatform.pojo.GameIdAndServerId;
import com.sz7road.userplatform.pojo.ZTreeObject;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import com.sz7road.userplatform.service.serverdata.ServerDataInterface;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.ListData;
import com.sz7road.web.WebApplicationNotifier;
import com.sz7road.web.utils.TimeStampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: leo.liao
 * Date: 12-6-19
 * Time: 上午11:16
 */
@Singleton
public class GameWithServerDecorateService extends GameWithServerService {
    private static final Logger log = LoggerFactory.getLogger(GameWithServerDecorateService.class.getName());
    public static final int STATUS_IMMINENT_OPEN = -3;
    public static final int STATUS_HOT = 1;
    public static final int STATUS_MAINTAIN = -2;
    private List<ServerEntity> imminentOpenServerTables;
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile int INSTNACE_INDEX = 1;
    @Inject
    private  Provider<GameWithServerService> gameWithServerServiceProvider;
   @Inject
   private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    @Inject
    private Provider<ServerDataInterface> serverDataInterfaceProvider;

    private final ScheduledExecutorService taskService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "GameServer-Task-" + INSTNACE_INDEX++);
        }
    });

    private final Runnable taskRunner = new Runnable() {

        @Override
        public void run() {
            try {
                List<ServerEntity> soonToOpenServer = getImminentOpenServerEntityList();
                ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
                if (!soonToOpenServer.isEmpty()) {
                    List<Integer> list = getNeedOpenServerIdList(soonToOpenServer);
                    if (list != null && !list.isEmpty()) {
                        serverTableDecorateDao.bathUpdateStatus(list, GameWithServerDecorateService.STATUS_HOT);
                        running.compareAndSet(false, true);
                        getInstance(ConfigurationService.class).updateTableTimestamp();
                    }
                }
                String serverStr="";
                if(soonToOpenServer!=null&&!soonToOpenServer.isEmpty())
                {
                    for(ServerEntity serverEntity:soonToOpenServer)
                    {
                        serverStr+="\n serverId: "+serverEntity.getId()+"  服务器名称： "+serverEntity.getServerName()+" 开服时间:"+CommonDateUtils.getDateString(serverEntity.getOpeningTime()) ;
                    }
                    log.info("神曲即将开的服："+serverStr);
                }
                ServerEntity todayEarliestOpenServer = getTodayEarliestOpenServerEntity();
                if (todayEarliestOpenServer != null) {
                    Timestamp current = new Timestamp(new Date().getTime()+20000);
                    long timeCount = current.getTime() - todayEarliestOpenServer.getOpeningTime().getTime();
                    if (todayEarliestOpenServer.getOpeningTime().before(current) && timeCount >-60000 && timeCount <= 30000) {
                        serverTableDecorateDao.bathUpdateRecommendFiledBeforeToday();
                    }
                }

                //---------------------------------------------------非神曲游戏的自动开服---------------------------------------------------------

                List<ServerTableNotForSq.ServerEntryNotForSq> soonToOpenServerNotForSq = cacheDataItemsServiceProvider.get().getSoonOpenedServerEntriesFromCacheNotForSq();
                if (!soonToOpenServerNotForSq.isEmpty()) {
                    List<Integer> list = getNeedOpenServerIdListNotForSq(soonToOpenServerNotForSq);
                    if (list != null && !list.isEmpty()) {
                        serverTableDecorateDao.bathUpdateStatusNotForSq(list, GameWithServerDecorateService.STATUS_HOT);
                        running.compareAndSet(false, true);
                        getInstance(ConfigurationService.class).updateTableTimestamp();
                    }
                }
                String serverStrNotForSq="";
                if(soonToOpenServerNotForSq!=null&&!soonToOpenServerNotForSq.isEmpty())
                {
                    for(ServerTableNotForSq.ServerEntryNotForSq serverEntity:soonToOpenServerNotForSq)
                    {
                        serverStrNotForSq+=" \n游戏Id:"+serverEntity.getGameId()+" serverId:"+serverEntity.getServerId()+" 服务器名称："+serverEntity.getServerName()+" 开服时间: "+CommonDateUtils.getDateString(serverEntity.getOpenTime()) ;
                    }
                    log.info("非神曲服务器即将开的服："+serverStrNotForSq);
                }



            } catch (final Exception e) {
                log.error("获取立即开服游戏区出现异常：{}", e.getMessage());
                e.printStackTrace();
            }
        }
    };

    private List<Integer> getNeedOpenServer(Map<Integer, ServerTable.ServerEntry> serverEntryMap) {
        if (serverEntryMap != null && !serverEntryMap.isEmpty()) {
            List<Integer> list = Lists.newArrayList();
            for (Map.Entry<Integer, ServerTable.ServerEntry> entry : serverEntryMap.entrySet()) {

                long time = System.currentTimeMillis() + Long.parseLong(getServerValueByKey(entry.getValue(), "game.openingTime"));
                ServerTable.ServerEntry serverEntry = entry.getValue();
                Timestamp openingTime = serverEntry.getOpeningTime();
                if (openingTime != null) {
                    if (time >= openingTime.getTime()) {
                        list.add(entry.getKey());
                    }
                }
            }
            return list;
        } else {
            log.info("暂时没有开服");
        }
        return null;
    }

    /**
     * 拿到提前开启时间的服务器信息
     *
     * @param serverEntities
     * @return
     */
    private List<Integer> getNeedOpenServerIdList(List<ServerEntity> serverEntities) {
        List<Integer> list = Lists.newArrayList();
        if (serverEntities != null && !serverEntities.isEmpty()) {
            for (ServerEntity entry : serverEntities) {
                long time = System.currentTimeMillis() + Long.parseLong(ConfigurationUtils.get("game.openingTime") == null ? "0" : ConfigurationUtils.get("game.openingTime"));
                Timestamp openingTime = entry.getOpeningTime();
                if (openingTime != null) {
                    if (time >= openingTime.getTime()) {
                        list.add(entry.getId());
                    }
                }
            }
        }
        return list;
    }

    private List<Integer> getNeedOpenServerIdListNotForSq(List<ServerTableNotForSq.ServerEntryNotForSq> serverEntities) {
        List<Integer> list = Lists.newArrayList();
        if (serverEntities != null && !serverEntities.isEmpty()) {
            for (ServerTableNotForSq.ServerEntryNotForSq entry : serverEntities) {
                long time = System.currentTimeMillis() + Long.parseLong(ConfigurationUtils.get("game.openingTime") == null ? "0" : ConfigurationUtils.get("game.openingTime"));
                Timestamp openingTime = entry.getOpenTime();
                if (openingTime != null) {
                    if (time >= openingTime.getTime()) {
                        list.add(entry.getId());
                    }
                }
            }
        }
        return list;
    }


    /**
     * Constructs by google-guice.
     */
    @Inject
    private GameWithServerDecorateService(WebApplicationNotifier notifier) {
        super();
        notifier.addListener(new WebApplicationNotifier.WebApplicationListener() {
            @Override
            public void onUnDeploy(InjectionProxy injection) {
                if (null != taskService) {
                    taskService.shutdown();
                    taskService.shutdownNow();
                }
            }

            @Override
            public void onDeploy(InjectionProxy injection) {
                if (!running.get()) {
                    autoStartTask();
                }
            }
        });
    }

    private void autoStartTask() {
        taskService.scheduleAtFixedRate(acquireSyncTask(), 1, 20, TimeUnit.SECONDS);
    }

    protected Runnable acquireSyncTask() {
        return taskRunner;
    }

    @Inject
    void initListener(ConfigurationService configService) {
        configService.addListener(new ConfigurationService.ConfigurationUpdateListener() {
            @Override
            public void onUpdate(ConfigurationService service) {
                lock.lock();
                try {
                    imminentOpenServerTables = null;
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public List<ServerEntity> getImminentOpenServerEntityList() {
        List<ServerEntity> serverEntityList = new LinkedList<ServerEntity>();
        if (imminentOpenServerTables == null) {
            lock.lock();
            if (imminentOpenServerTables == null) {
                try {
                    serverEntityList = getReadyOpenStatusServersEntityList();
                } catch (Exception e) {
                    log.error("获取即将开服游戏区列表异常{}", e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        }
        return serverEntityList;
    }

    /**
     * 得到今天最早新开的服务器列表
     *
     * @return
     */
    private ServerEntity getTodayEarliestOpenServerEntity() {
        TreeSet<ServerEntity> serverEntitySet = new TreeSet<ServerEntity>(new Comparator<ServerEntity>() {
            @Override
            public int compare(ServerEntity o1, ServerEntity o2) {
                return (int) (o1.getOpeningTime().getTime() - o2.getOpeningTime().getTime());
            }
        });
        try{
        final Collection<ServerTable.ServerEntry> allServers =gameWithServerServiceProvider.get().getGameTable().getServerTable(1).values();
//                cacheDataItemsServiceProvider.get().getServerEntriesFromCache();
        if (allServers != null && !allServers.isEmpty()) {
            for (ServerTable.ServerEntry server : allServers) {
                boolean flag = server.getOpeningTime().after(new Timestamp(CommonDateUtils.getOneDayFirstMoment(new Date()))) &&
                        server.getOpeningTime().before(new Timestamp(CommonDateUtils.getOneDayLastMoment(new Date())));
                if (flag) {
                    serverEntitySet.add(DataUtil.transToServerEntity(server));
                }
            }
        }
        }catch (Exception ex)
        {
            log.error("获取今天最早开的服务区异常!");
            ex.printStackTrace();
        }
        return serverEntitySet.isEmpty() ? null : serverEntitySet.first();
    }


    public int addGameServer(ServerTable.ServerEntry entry) {
        final ServerTableDecorateDao serverTableDao = getInstance(ServerTableDecorateDao.class);
        int addDbRel = 0;
        try {
            addDbRel = serverTableDao.add(entry); //增加服务器
        } catch (Exception e) {
            log.error("添加游戏服务区异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return addDbRel;
    }

    public int addGameServerNotForSq(ServerTableNotForSq.ServerEntryNotForSq entry) {
        final ServerTableDecorateDao serverTableDao = getInstance(ServerTableDecorateDao.class);
        int addDbRel = 0;
        try {
            addDbRel = serverTableDao.addNotForSq(entry); //增加服务器
        } catch (Exception e) {
            log.error("添加游戏服务区异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return addDbRel;
    }

    public int updateField(Map<String, Object> fieldValueMap, int id) {
        final ServerTableDecorateDao serverTableDao = getInstance(ServerTableDecorateDao.class);
        try {
            return serverTableDao.updateField(fieldValueMap, id);
        } catch (SQLException e) {
            log.error("添加游戏服务区异常：{}", e.getMessage());
        }
        return 0;
    }

    public int batchUpdate(ServerTable.ServerEntry[] entries) {
        final ServerTableDecorateDao serverTableDao = getInstance(ServerTableDecorateDao.class);
        try {
            return serverTableDao.batchUpdate(entries);
        } catch (SQLException e) {
            log.error("更新游戏服务区异常：{}", e.getMessage());
        }
        return 0;
    }

    public int batchUpdateNotForSq(ServerTableNotForSq.ServerEntryNotForSq[] entries) {
        final ServerTableDecorateDao serverTableDao = getInstance(ServerTableDecorateDao.class);
        try {
            return serverTableDao.batchUpdateNotForSq(entries);
        } catch (Exception e) {
            log.error("更新游戏服务区异常：{}", e.getMessage());
        }
        return 0;
    }

    public ListData<ServerTable.ServerEntry> list(int gameId, String order, int sort, int start, int limit, Map<String, Object> queryMap) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        try {
            return serverTableDecorateDao.list(gameId, order, sort, start, limit, queryMap);
        } catch (Exception e) {
            log.error("查询游戏服务区异常：{}", e.getMessage());
        }
        return null;
    }

    public int batchUpdateStatus(List<Integer> list, int status) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        try {
            int rel = serverTableDecorateDao.bathUpdateStatus(list, status);
            if (rel > 0) {
                getInstance(ConfigurationService.class).updateTableTimestamp();
            }
            return rel;
        } catch (Exception e) {
            log.error("更新游戏服务区状态出现异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int allStatus(int all, int status) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        try {
            return serverTableDecorateDao.batchUpdateServerFromAllToStatus(all, status);
        } catch (Exception e) {
            log.error("全量状态更新出现异常：{}", e.getMessage());
        }
        return 0;
    }

    public ServerTable.ServerEntry getServerEntryById(int id) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        return serverTableDecorateDao.getServerEntryById(id);
    }

    /**
     * 直接到redis里查找
     * @param gameId
     * @param serverId
     * @return
     */
    public ServerTableNotForSq.ServerEntryNotForSq getServerEntryByGameIdAndServerId(int gameId,int serverId) {
        return cacheDataItemsServiceProvider.get().getServerEntryFromCacheNotForSqByGameIdAndServerId(gameId,serverId);
    }

    public boolean mergerServers(String serverNo, int[] ids) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        boolean suc = serverTableDecorateDao.mergerServers(serverNo, ids);
        if (suc) {
            getInstance(ConfigurationService.class).updateTableTimestamp();
        }
        log.info("合服之后更新配置");
        return suc;
    }

    /**
     * 分区或者取消合区功能
     *
     * @param ids
     * @return
     */
    public boolean splitServers(int[] ids) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        boolean suc = serverTableDecorateDao.splitServers(ids);
        if (suc) {
            getInstance(ConfigurationService.class).updateTableTimestamp();
        }
        log.info("取消合服之后更新配置");
        return suc;
    }

    /**
     * 从服务区表和服务区配置表中拿到合区的服务区树形结构数据。
     *
     * @return
     */
    public List<ZTreeObject> getServerTreeDataStruct() {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        return serverTableDecorateDao.getServerTreeDataStruct();
    }


    /**
     * 从服务区表和服务区配置表中拿到合区的主服务区树形结构数据。
     *
     * @return
     */
    public List<ZTreeObject> getServerTreeDataStructOfMain() {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        return serverTableDecorateDao.getServerTreeDataStructOfMain();
    }

    public List<ZTreeObject> getServerTreeDataStructOfCancell() {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        return serverTableDecorateDao.getServerTreeDataStructOfCancell();
    }

    /**
     * 得到所有的火爆状态的服务器ID列表
     *
     * @return
     */
    public List<Integer> getHotStatusServersIdList() {
        List<Integer> hotStatusServersIdList = new LinkedList<Integer>();
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        Map<Integer, ServerEntity> serverEntityMap = serverTableDecorateDao.getAllServers();

        for (ServerEntity serverEntity : serverEntityMap.values()) {
            int serverStatus = serverEntity.getServerStatus();
            if (ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode() == serverStatus) {
                int serverId = serverEntity.getId();
                hotStatusServersIdList.add(serverId);
            }
        }
        return hotStatusServersIdList;
    }

    /**
     * 得到所有的服务器的Map ,神曲的
     *
     * @return
     */
    public Map<Integer, ServerEntity> getAllServerMaps() {

        Map<Integer, ServerEntity> serverEntityMap = Maps.newHashMap();
        List<ServerTable.ServerEntry> serverEntryList= cacheDataItemsServiceProvider.get().getServerEntriesFromCache();

        if(serverEntryList!=null&&!serverEntryList.isEmpty())
        {
            for(ServerTable.ServerEntry serverEntity:serverEntryList)
            {
                  if(serverEntity.getGameId()==1)
                  {
                      serverEntityMap.put(serverEntity.getId(),DataUtil.transToServerEntity(serverEntity));
                  }
            }
        }


        return serverEntityMap;
    }






    /**
     * 得到所有是维护状态的服务器ID列表
     *
     * @return
     */
    public List<Integer> getMaintainStatusServersIdList() {
        List<Integer> maintainStatusServersIdList = new LinkedList<Integer>();
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        Map<Integer, ServerEntity> serverEntityMap = serverTableDecorateDao.getAllServers();
        for (ServerEntity serverEntity : serverEntityMap.values()) {
            int serverStatus = serverEntity.getServerStatus();
            if (ServerEntity.SERVER_STATUS.SERVER_STATUS_MAINTAIN.getServerStatusCode() == serverStatus) {
                int serverId = serverEntity.getId();
                maintainStatusServersIdList.add(serverId);
            }
        }
        return maintainStatusServersIdList;
    }

    /**
     * 得到所有是即将开服的服务器ID列表
     *
     * @return
     */
    public List<ServerEntity> getReadyOpenStatusServersEntityList() {
        List<ServerEntity> readyOpenStatusServersEntityList = new LinkedList<ServerEntity>();
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        Map<Integer, ServerEntity> serverEntityMap = serverTableDecorateDao.getAllServers();
        for (ServerEntity serverEntity : serverEntityMap.values()) {
            int serverStatus = serverEntity.getServerStatus();
            if (ServerEntity.SERVER_STATUS.SERVER_STATUS_NOTOPEN.getServerStatusCode() == serverStatus) {
                readyOpenStatusServersEntityList.add(serverEntity);
            }
        }
        return readyOpenStatusServersEntityList;
    }

    public ListData<ServerTableNotForSq.ServerEntryNotForSq> listNotForSq(int gameId, String order, int sort, int start, int limit, Map<String, Object> queryMap) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        try {
            return serverTableDecorateDao.listNotForSq(gameId, order, sort, start, limit, queryMap);
        } catch (Exception e) {
            log.error("查询游戏服务区异常：{}", e.getMessage());
        }
        return null;
    }


    public int batchUpdateStatusNotForSq(List<Integer> ids, int status,int gameId) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        try {
            int rel = serverTableDecorateDao.bathUpdateStatusNotForSq(ids, status,gameId);
            if (rel > 0) {
                getInstance(ConfigurationService.class).updateTableTimestamp();
            }
            return rel;
        } catch (Exception e) {
            log.error("更新游戏服务区状态出现异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从redis里得到游戏的所有区服信息
     * @param gameId
     * @return
     */
    public List<ServerInfo> getServerListsByGameId(int gameId) {

         return  serverDataInterfaceProvider.get().list(gameId);
    }

    public int batchUpdateStatusForMaintain(List<GameIdAndServerId> serverIdListTOMaintain, int status) {
        final ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
        try {
            int rel = serverTableDecorateDao.bathUpdateStatusForMaintain(serverIdListTOMaintain, status);
            if (rel > 0) {
                getInstance(ConfigurationService.class).updateTableTimestamp();
            }
            return rel;
        } catch (Exception e) {
            log.error("更新游戏服务区状态出现异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
