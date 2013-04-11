package com.sz7road.userplatform.service.serverdata;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.ServerTableDao;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.MergerService;
import com.sz7road.userplatform.utils.ServerInfoUtils;
import com.sz7road.web.pojos.MergerInfoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-21
 * Time: 下午4:43
 * 统一的取得服务器的服务类
 */
@Singleton
public class ServerDataService extends Injection implements ServerDataInterface {

    private static final Logger log = LoggerFactory.getLogger(ServerDataService.class.getName());
    @Inject
    private Provider<ServerTableDao> serverTableDaoProvider;

    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    @Inject
    private Provider<MergerService> mergerServiceProvider;


    @Override
    public ServerInfo get(int serverId) {
        return null;
    }

    @Override
    public ServerInfo get(int gameId, int serverId) {
        ServerInfo serverInfo = null;
        List<ServerInfo> serverInfoList = getServerTableByGameId(gameId);
        if (serverInfoList != null && !serverInfoList.isEmpty()) {
            for (ServerInfo sf : serverInfoList) {
                if (sf.getServerId() == serverId) {
                    serverInfo = sf;
                }
            }
        }
        return serverInfo;
    }

    @Override
    public List<ServerInfo> list() {

        return getServerTable();
    }

    @Override
    public List<ServerInfo> listWithStatus(int status) {
        List<ServerInfo> serverInfoList = Lists.newLinkedList();
        final List<ServerInfo> serverInfos = getServerTable();
        if (serverInfos != null && !serverInfos.isEmpty()) {
            for (ServerInfo serverInfo : serverInfos) {
                if (serverInfo.getServerStatus() == status) {
                    serverInfoList.add(serverInfo);
                }
            }
        }
        return serverInfoList;
    }

    private List<ServerInfo> getServerTable() {
        List<ServerInfo> serverInfoList = Lists.newLinkedList();

        List<ServerTable.ServerEntry> sqServers = cacheDataItemsServiceProvider.get().getServerEntriesFromCache();
        if (sqServers != null && !sqServers.isEmpty()) {
            final Map<Integer, Integer> idAndNoServerMap = ServerInfoUtils.getIdAndNoServerMap(sqServers);
            for (ServerTable.ServerEntry serverEntry : sqServers) {
                ServerInfo serverInfo = new ServerInfo();

                serverInfo.setGameId(serverEntry.getGameId());
                serverInfo.setServerId(serverEntry.getId());
                serverInfo.setServerName(serverEntry.getServerName());
                serverInfo.setRecommand(serverEntry.isRecommand());
                serverInfo.setOpeningTime(serverEntry.getOpeningTime());
                serverInfo.setCreateTime(serverEntry.getCreateTime());
                serverInfo.setServerStatus(serverEntry.getServerStatus());
                serverInfo.setServerNo(serverEntry.getServerNo());

                int mainServerNo = ServerInfoUtils.getMainServerNo(serverEntry);
                int mainServerId = 0;
                if (mainServerNo != 0) {
                    if (idAndNoServerMap != null && idAndNoServerMap.containsKey(mainServerNo)) {
                        mainServerId = idAndNoServerMap.get(mainServerNo);
                    } else {
                        mainServerId = 0;
                    }
                } else {
                    mainServerId = serverEntry.getId();
                }
                serverInfo.setMainId(mainServerId);
                serverInfoList.add(serverInfo);
            }
        }
        List<ServerTableNotForSq.ServerEntryNotForSq> notForSqList = cacheDataItemsServiceProvider.get().getServerEntriesFromCacheNotForSq();
        if (notForSqList != null && !notForSqList.isEmpty()) {
            for (ServerTableNotForSq.ServerEntryNotForSq serverEntryNotForSq : notForSqList) {
                ServerInfo serverInfo = new ServerInfo();
                serverInfo.setGameId(serverEntryNotForSq.getGameId());
                serverInfo.setServerId(serverEntryNotForSq.getServerId());
                serverInfo.setServerName(serverEntryNotForSq.getServerName());
                serverInfo.setMainId(serverEntryNotForSq.getMainId());
                serverInfo.setServerStatus(serverEntryNotForSq.getStatus());
                serverInfo.setRecommand(serverEntryNotForSq.isRecommand());
                serverInfo.setOpeningTime(serverEntryNotForSq.getOpenTime());
                serverInfo.setCreateTime(serverEntryNotForSq.getCreateTime());
                serverInfo.setServerNo(serverEntryNotForSq.getServerNo());
                serverInfoList.add(serverInfo);
            }
        }
        if (serverInfoList != null && !serverInfoList.isEmpty()) {
            Collections.sort(serverInfoList, new Comparator<ServerInfo>() {

                @Override
                public int compare(ServerInfo o1, ServerInfo o2) {
                    if (o1 == null || o2 == null) return 0;
                    if (o1.getOpeningTime().after(o2.getOpeningTime())) {
                        return -1;
                    } else if (o1.getOpeningTime().before(o2.getOpeningTime())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return serverInfoList;
    }


    private List<ServerInfo> getServerTableByGameId(int gameId) {

        List<ServerInfo> serverInfoList = Lists.newLinkedList();

        if (gameId == 1) {
            List<ServerTable.ServerEntry> sqServers = cacheDataItemsServiceProvider.get().getServerEntriesFromCache();
            if (sqServers != null && !sqServers.isEmpty()) {
                final Map<Integer, Integer> idAndNoServerMap = ServerInfoUtils.getIdAndNoServerMap(sqServers);
                for (ServerTable.ServerEntry serverEntry : sqServers) {
                    if (serverEntry.getGameId() == 1) {
                        ServerInfo serverInfo = new ServerInfo();
                        serverInfo.setGameId(serverEntry.getGameId());
                        serverInfo.setServerId(serverEntry.getId());
                        serverInfo.setServerName(serverEntry.getServerName());
                        serverInfo.setRecommand(serverEntry.isRecommand());
                        serverInfo.setOpeningTime(serverEntry.getOpeningTime());
                        serverInfo.setCreateTime(serverEntry.getCreateTime());
                        serverInfo.setServerStatus(serverEntry.getServerStatus());
                        serverInfo.setServerNo(serverEntry.getServerNo());

                        int mainServerNo = ServerInfoUtils.getMainServerNo(serverEntry);
                        int mainServerId = 0;
                        if (mainServerNo != 0) {
                            if (idAndNoServerMap != null && idAndNoServerMap.containsKey(mainServerNo)) {
                                mainServerId = idAndNoServerMap.get(mainServerNo);
                            } else {
                                mainServerId = 0;
                            }
                        } else {
                            mainServerId = serverEntry.getId();
                        }
                        serverInfo.setMainId(mainServerId);
                        serverInfoList.add(serverInfo);
                    }
                }
            }
        } else {
            List<ServerTableNotForSq.ServerEntryNotForSq> notForSqList = cacheDataItemsServiceProvider.get().getServerEntriesFromCacheNotForSq();
            if (notForSqList != null && !notForSqList.isEmpty()) {
                for (ServerTableNotForSq.ServerEntryNotForSq serverEntryNotForSq : notForSqList) {
                    if (serverEntryNotForSq.getGameId() == gameId) {
                        ServerInfo serverInfo = new ServerInfo();
                        serverInfo.setGameId(serverEntryNotForSq.getGameId());
                        serverInfo.setServerId(serverEntryNotForSq.getServerId());
                        serverInfo.setServerName(serverEntryNotForSq.getServerName());
                        serverInfo.setMainId(serverEntryNotForSq.getMainId());
                        serverInfo.setServerStatus(serverEntryNotForSq.getStatus());
                        serverInfo.setRecommand(serverEntryNotForSq.isRecommand());
                        serverInfo.setOpeningTime(serverEntryNotForSq.getOpenTime());
                        serverInfo.setCreateTime(serverEntryNotForSq.getCreateTime());
                        serverInfo.setServerNo(serverEntryNotForSq.getServerNo());
                        serverInfoList.add(serverInfo);
                    }
                }
            }
        }
        Collections.sort(serverInfoList, new Comparator<ServerInfo>() {

            @Override
            public int compare(ServerInfo o1, ServerInfo o2) {
                if (o1 == null || o2 == null) return 0;
                if (o1.getOpeningTime().after(o2.getOpeningTime())) {
                    return -1;
                } else if (o1.getOpeningTime().before(o2.getOpeningTime())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return serverInfoList;
    }


    @Override
    public List<ServerInfo> list(int gameId) {

        if (gameId <= 0) {
            log.error("参数非法：gameid:" + gameId);
            return null;
        }
        return getServerTableByGameId(gameId);
    }

    /**
     * 获取所有游戏的num条数据
     *
     * @param num
     * @return
     */
    @Override
    public List<ServerInfo> listNum(int num) {
        return listNumOfGame(0, num);
    }

    /**
     * 获得某个游戏的num条数据
     *
     * @param gameId
     * @param num
     * @return
     */
    @Override
    public List<ServerInfo> listNumOfGame(int gameId, int num) {
        Preconditions.checkArgument(num > 0, "num非法！");
        Preconditions.checkArgument(gameId >= 0, "gameId非法！");
        List<ServerInfo> serverInfoList = null;
        if (gameId > 0) {
            serverInfoList = getServerTableByGameId(gameId);
        } else if (gameId == 0) {
            serverInfoList = getServerTable();
        }
        if (serverInfoList == null || serverInfoList.isEmpty()) {
            return null;
        } else {
            int size = serverInfoList.size();
            if (size <= num) {
                return serverInfoList;
            } else {
                return serverInfoList.subList(0, num);
            }
        }
    }

    @Override
    public ServerInfo list(int gameId, int serverNo) {

        if (gameId <= 0 || serverNo <= 0) {
            log.error("参数非法：gameid:" + gameId + " serverNo:" + serverNo);
            return null;
        }
        ServerInfo serverInfo = null;
        List<ServerInfo> serverInfoList = getServerTableByGameId(gameId);
        if (serverInfoList != null && !serverInfoList.isEmpty()) {
            for (ServerInfo sf : serverInfoList) {
                if (sf.getServerNo() == serverNo) {
                    serverInfo = sf;
                }
            }
        }
        return serverInfo;
    }

    @Override
    public ServerInfo getNewOpenedServer(int gameId) {
        List<ServerInfo> serverInfoList = list(gameId);
        List<ServerInfo> openedServerInfoList = Lists.newLinkedList();
        if (serverInfoList != null && !serverInfoList.isEmpty())
            for (ServerInfo serverInfo : serverInfoList) {
                if (serverInfo.getServerStatus() == 1) {
                    openedServerInfoList.add(serverInfo);
                }
            }
        if (openedServerInfoList != null && !openedServerInfoList.isEmpty()) {
            ServerInfo serverInfo = openedServerInfoList.get(0);
            for (int i = 1; i < openedServerInfoList.size(); i++) {
                if (openedServerInfoList.get(i).getOpeningTime().getTime() > serverInfo.getOpeningTime().getTime()) {
                    serverInfo = openedServerInfoList.get(i);
                }
            }
            return serverInfo;
        }
        return null;
    }
}
