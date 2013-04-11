package com.sz7road.userplatform.service;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.InjectionProxy;
import com.sz7road.userplatform.dao.ServerMaintainDecorateDao;
import com.sz7road.userplatform.dao.ServerTableDecorateDao;
import com.sz7road.userplatform.pojo.GameIdAndServerId;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.serverdata.ServerDataInterface;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.web.WebApplicationNotifier;
import org.apache.poi.ss.usermodel.DateUtil;
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
 * Date: 12-7-9
 * Time: 下午3:33
 */
@Singleton
public class ServerMaintainDecorateService extends ServerMaintainService {

    private static final Logger log = LoggerFactory.getLogger(ServerMaintainDecorateService.class);

    @Inject
    private  Provider<ServerTableDecorateDao> serverTableDecorateDaoProvider;

    @Inject
    private Provider<ServerMaintainDecorateDao> serverMaintainDecorateDaoProvider;

    //拿到服务区的信息
    private Map<Integer, ServerEntity> serverEntryMap;


    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile int INSTNACE_INDEX = 1;
    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;
    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Inject
    private  Provider<ServerDataInterface> serverDataInterfaceProvider;

    private final ScheduledExecutorService taskService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ServerMaintain-Task-" + INSTNACE_INDEX++);
        }
    });

    private final Runnable taskRunner = new Runnable() {

        @Override
        public void run() {//自动维护线程
            try {
                Timestamp nowTime=new Timestamp(System.currentTimeMillis());
                    //从缓存中得到所有的服务器信息
                    final List<ServerInfo> serverInfoList = serverDataInterfaceProvider.get().list();

                    //map<gameId@serverId,ServerMaintain实体>
                    final Map<String, ServerMaintain2> maintainMap = getMaintainServerInfoAll2(nowTime);

                    if (maintainMap != null && !maintainMap.isEmpty()) {
                        List<GameIdAndServerId> serverIdListTOMaintain = Lists.newLinkedList();
                        List<GameIdAndServerId> serverIdListTOHot = Lists.newLinkedList();
                        for (ServerMaintain2 maintainEntry : maintainMap.values()) {
                            ServerInfo serverInfo = null;
                            if (serverInfoList != null && !serverInfoList.isEmpty()) {
                                serverInfo = getServerInfoFromServerInfoList(maintainEntry.getServerId(),maintainEntry.getGameId(),serverInfoList);
                            }
                            boolean okFlag = false;
                            boolean maintainFlag = false;
                            if (serverInfo != null) {
                                int status = serverInfo.getServerStatus();
                                okFlag = (1==status);
                                maintainFlag = (-2==status);
                            }
                           final Timestamp current = new Timestamp(System.currentTimeMillis());
                           final Timestamp currentAdvance26Second = new Timestamp(current.getTime() + 60000); //提前5秒开始维护
                           final Timestamp currentAdvance70Second = new Timestamp(current.getTime() + 60000); //提前5秒结束维护
                            //2,到维护开始时间，更改服务器的状态为维护状态-2，然后移除缓存中的服务器列表里缓存，使得缓存中的数据是最新的
                            if (maintainEntry.getStartTime().before(currentAdvance26Second) && okFlag && maintainEntry.getEndTime().after(currentAdvance70Second)) {
                                serverIdListTOMaintain.add(new GameIdAndServerId(maintainEntry.getGameId(),maintainEntry.getServerId()));
                            }
                            //3,到维护结束时间，更改服务器的状态为正常状态0，然后移除维护服务器列表的缓存使得缓存中的数据是最新的
                            if (maintainEntry.getEndTime().before(currentAdvance70Second) && maintainFlag) {
                                serverIdListTOHot.add(new GameIdAndServerId(maintainEntry.getGameId(),maintainEntry.getServerId()));
                            }
                        }
                    if(serverIdListTOMaintain.size() > 0||serverIdListTOHot.size() > 0)
                    {
                    GameWithServerDecorateService gameWithServerDecorateService = getInstance(GameWithServerDecorateService.class);
                    if (serverIdListTOMaintain.size() > 0) {//批量修改所有选中的服务器为维护状态
                        gameWithServerDecorateService.batchUpdateStatusForMaintain(serverIdListTOMaintain, GameWithServerDecorateService.STATUS_MAINTAIN);
                        running.compareAndSet(false, true);
                        log.info("维护开始：一共有" + serverIdListTOMaintain.size() + "个游戏服务区更新为维护状态.");
                    }
                    if (serverIdListTOHot.size() > 0) {//批量修改所有选中的服务器为火爆状态
                        gameWithServerDecorateService.batchUpdateStatusForMaintain(serverIdListTOHot, GameWithServerDecorateService.STATUS_HOT);
                        running.compareAndSet(false, true);
                        log.info("维护结束：一共有" + serverIdListTOHot.size() + "个游戏服务区更新为正常状态.");
                    }
                    }
                }
            } catch (final Exception e) {
                log.error("维护游戏区出现异常：{}" + e.getMessage(), e);
                e.printStackTrace();
            }
        }};



    private ServerInfo getServerInfoFromServerInfoList(int serverId,int gameId,List<ServerInfo> serverInfoList)
    {
        ServerInfo serverInfo=null;
       if(serverInfoList!=null&&!serverInfoList.isEmpty())
       {
           for(ServerInfo serverInfoEntry:serverInfoList)
           {
               if(serverInfoEntry.getGameId()==gameId&&serverInfoEntry.getServerId()==serverId)
               {
                   serverInfo=serverInfoEntry;
                   log.info("找到的服务器："+serverInfo.getServerName());
               }
           }
       }
        return  serverInfo;
    }


    @Inject
    private  ServerMaintainDecorateService(WebApplicationNotifier notifier) {
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

        taskService.scheduleAtFixedRate(acquireSyncTask(), 1, 30, TimeUnit.SECONDS);
    }

    protected Runnable acquireSyncTask() {
        return taskRunner;
    }


    /**
     * 增加维护列表
     *
     * @return
     */
    public Msg addServerMaintain(List<ServerMaintain> needMaintainServers) {
        ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
        Msg msg = null;
        if (needMaintainServers != null && needMaintainServers.size() > 0) {
            try {
                msg = serverMaintainDao.addMaintainServer(needMaintainServers);
                getInstance(ConfigurationService.class).updateTableTimestamp();
            } catch (SQLException e) {
                log.error("增加服务器维护信息异常！");
                e.printStackTrace();
            }
        }
        return msg;
    }

    /**
     * 增加维护列表
     *
     * @return
     */
    public Msg addServerMaintainNotForSq(List<ServerMaintain2> needMaintainServers) {
        ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
        Msg msg = null;
        if (needMaintainServers != null && needMaintainServers.size() > 0) {
            try {
                msg = serverMaintainDao.addMaintainServerNotForSq(needMaintainServers);
                getInstance(ConfigurationService.class).updateTableTimestamp();
            } catch (Exception e) {
                log.error("增加服务器维护信息异常！");
                e.printStackTrace();
            }
        }
        return msg;
    }




    /**
     * 查看缓存中是不是存在正在开始维护的服务器
     *
     * @param serverEntryMap
     * @param serverId
     * @return
     */
    private boolean containsNeedMaintainServer(Collection<ServerTable.ServerEntry> serverEntryMap, int serverId) {
        boolean flag = false;
        for (ServerTable.ServerEntry serverEntry : serverEntryMap) {
            boolean res = serverEntry.getId() == serverId;
            if (res) {
                flag = true;
                break;
            }
        }

        return flag;
    }
    /**
     * 拿到状态不为-1和-3的服务器列表
     *
     * @return
     */
    public Map<Integer, ServerEntity> getAbleMaintainServerEntities() {
        Map<Integer, ServerEntity> serverEntityMap = new HashMap<Integer, ServerEntity>();
        if (serverEntryMap == null) {
            lock.lock();
            if (serverEntryMap == null) {
                try {
                    ServerTableDecorateDao serverTableDecorateDao = getInstance(ServerTableDecorateDao.class);
                    serverEntityMap = serverTableDecorateDao.getNonCloseServerEntities();

                } catch (Exception e) {
                    log.error("获取状态是1或者-2的游戏区列表异常{}", e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        }
//        log.info("服务器列表size:"+serverEntityMap==null?"空":serverEntityMap.size()+" ");
        return serverEntityMap;
    }


    /**
     * 根据服务器ID，拿到正在维护的列表   批量的查
     *
     * @param serverIdList 要查询的的服务器ID列表
     * @return 查到的结果
     * @throws java.sql.SQLException
     */
    public Msg queryMaintainServersByIdList(int[] serverIdList) throws SQLException {

        if (serverIdList == null || serverIdList.length <= 0) {
            throw new IllegalArgumentException("参数异常！");
        }
        List<ServerMaintain> serverMaintainList = new ArrayList<ServerMaintain>();
        GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();

        Map<Integer, ServerEntity> tmpServerEntryMap = gameWithServerService.getSortedServerEntriesMap(1);
        Map<Integer, ServerMaintain> tmpServerMainMap = getMaintainServerInfo();
        //1,从缓存里拿到有效的维护信息列表
        //2,从有效信息里面查询符合条件的游戏服务区信息
        for (int id : serverIdList) {
            //拿到id对应的维护列表
            ServerMaintain serverMaintainPerId = null;
            if (tmpServerEntryMap != null && !tmpServerEntryMap.isEmpty()) {
                if (tmpServerMainMap != null && !tmpServerMainMap.isEmpty())
                    serverMaintainPerId = tmpServerMainMap.get(id);
            }
            //从id对应的维护列表中拿到最近更改的维护信息并增加到返回的维护列表中
            if (serverMaintainPerId != null) {
                serverMaintainList.add(serverMaintainPerId);
            }
        }
        //3，根据查询到的结果，封装返回信息
        Msg msg = new Msg();
        int size = serverMaintainList.size();
        if (size > 0) {
            msg.setCode(200);
            msg.setMsg("一共查到" + size + "条数据!");
            msg.setObject(serverMaintainList);
        } else {
            msg.setCode(204);
            msg.setMsg("没有查到数据!");
        }
        return msg;
    }

    /**
     * 批量修改维护信息
     *
     * @param maintainInfo
     * @param ids
     * @return
     * @throws SQLException
     */
    public Msg batchUpdateMaintainServers(ServerMaintain maintainInfo, List<Integer> ids) throws SQLException {
        Msg msg=new Msg();
        if (maintainInfo == null || ids == null || ids.size() <= 0) {
//            throw new NullPointerException("null object");
            msg.setMsg("没有需要更新的维护信息");
        } else
        {
        ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
         msg = serverMaintainDao.batchUpdateMaintainServers(maintainInfo, ids);
        getInstance(ConfigurationService.class).updateTableTimestamp();
        }
        return msg;
    }

    /**
     * 批量修改维护信息
     *
     * @param maintainInfo
     * @param ids
     * @return
     * @throws SQLException
     */
    public Msg batchUpdateMaintainServersNotForSq(ServerMaintain2 maintainInfo, List<Integer> ids) throws SQLException {
        Msg msg=new Msg();
        if (maintainInfo == null || ids == null || ids.size() <= 0) {
//            throw new NullPointerException("null object");
            msg.setMsg("没有需要更新的维护信息");
        } else
        {
            ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
            msg = serverMaintainDao.batchUpdateMaintainServersNotForSq(maintainInfo, ids);
            getInstance(ConfigurationService.class).updateTableTimestamp();
        }
        return msg;
    }


    /**
     * 批量修改服务器的状态
     *
     * @param
     * @param ids
     * @return
     * @throws SQLException
     */
    public int batchUpdateServerStatus(List<Integer> ids, int status) throws SQLException {

        return serverTableDecorateDaoProvider.get().bathUpdateStatus(ids, status);
    }

    public int batchUpdateServerStatusNotForSq(List<Integer> ids, int status,int gameId) throws Exception {

        return serverTableDecorateDaoProvider.get().bathUpdateStatusNotForSq(ids, status,gameId);
    }

    public int batchUpdateServerState(List<Integer> maintains) {
        int rel = 0;
        GameWithServerDecorateService gameWithServerDecorateService = getInstance(GameWithServerDecorateService.class);
        rel = gameWithServerDecorateService.batchUpdateStatus(maintains, 1);
        return rel;
    }

    /**
     * 批量删除还没有开始维护的维护信息
     *
     * @param serverIdArray 服务器id
     * @return
     */
    public Msg batchDeleteFutureMaintainInfo(int[] serverIdArray) {
        if (serverIdArray == null || serverIdArray.length <= 0) {
            throw new NullPointerException("serverIdArray is Null!");
        }
        ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
        Msg msg = serverMaintainDao.batchDeleteFutureMaintainInfo(serverIdArray);
        getInstance(ConfigurationService.class).updateTableTimestamp();
        return msg;
    }

    public Msg queryMaintainServersByIdListAndGameId(int[] serverIdList, int gameId) {
        if (serverIdList == null || serverIdList.length <= 0) {
            throw new IllegalArgumentException("参数异常！");
        }
        List<ServerMaintain2> serverMaintainList =Lists.newArrayList();

        //1,从缓存里拿到有效的维护信息列表
        Map<Integer, ServerMaintain2> tmpServerMainMap = getMaintainServerInfoByGameId(gameId,new Timestamp(System.currentTimeMillis()));
        //2,从有效信息里面查询符合条件的游戏服务区信息
        if (tmpServerMainMap != null && !tmpServerMainMap.isEmpty()) {
            for (int id : serverIdList) {
            //拿到id对应的维护列表
            ServerMaintain2 serverMaintainPerId = tmpServerMainMap.get(id);
            //从id对应的维护列表中拿到最近更改的维护信息并增加到返回的维护列表中
            if (serverMaintainPerId != null&&serverMaintainPerId.getGameId()==gameId) {
                serverMaintainList.add(serverMaintainPerId);
            }
            }
        }
        //3，根据查询到的结果，封装返回信息
        Msg msg = new Msg();
        int size = serverMaintainList.size();
        if (size > 0) {
            msg.setCode(200);
            msg.setMsg("一共查到" + size + "条数据!");
            msg.setObject(serverMaintainList);
        } else {
            msg.setCode(204);
            msg.setMsg("没有查到数据!");
        }
        return msg;
    }

    public Msg batchDeleteFutureMaintainInfoByGameId(int[] serverIdArray, int gameId) {
        if (serverIdArray == null || serverIdArray.length <= 0||gameId<=0) {
            throw new NullPointerException("serverIdArray is Null!");
        }
        ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
        Msg msg = serverMaintainDao.batchDeleteFutureMaintainInfoByGameId(serverIdArray, gameId);
        getInstance(ConfigurationService.class).updateTableTimestamp();
        return msg;
    }

    public int batchUpdateServerStatusByGameId(List<Integer> ids, int status, int gameId) throws  Exception{
        return serverTableDecorateDaoProvider.get().bathUpdateStatusNotForSq(ids, status,gameId);
    }

    public Msg batchUpdateMaintainServersNotForSqByIds(ServerMaintain2 maintainInfo, List<Integer> ids) throws SQLException{

        Msg msg=new Msg();
        if (maintainInfo == null || ids == null || ids.size() <= 0) {
//            throw new NullPointerException("null object");
            msg.setMsg("没有需要更新的维护信息");
        } else
        {
            ServerMaintainDecorateDao serverMaintainDao = getInstance(ServerMaintainDecorateDao.class);
            msg = serverMaintainDao.batchUpdateMaintainServersNotForSqById(maintainInfo, ids);
            getInstance(ConfigurationService.class).updateTableTimestamp();
        }
        return msg;
    }
}

