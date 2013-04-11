package com.sz7road.userplatform.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.*;
import com.sz7road.userplatform.dao.jdbc.AppealDaoImpl;
import com.sz7road.userplatform.dao.jdbc.JdbcDaoSupport;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.ListData;
import com.sz7road.web.pojos.GameHistory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.lucene.analysis.CharArrayMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author jiangfan.zhou
 */

@Singleton
public class LoginGameService extends Injection {

    private final static Logger log = LoggerFactory.getLogger(LoginGameService.class.getName());

    @Inject
    private Provider<LoginGameDao> loginGameDaoProvider;

    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    final String XDDT_NAME = "新弹弹堂";
    final String DDT_NAME = "弹弹堂II";

    public LoginGameDao getLoginGameDao() {
        LoginGameDao dao = loginGameDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null getLoginGameDao!");
        }
        return dao;
    }

    public List<LoginGame> list(int userId) throws SQLException {
        return getLoginGameDao().list(userId, 0);
    }

    public List<LoginGame> list(int userId, int gameId) throws SQLException {
        return getLoginGameDao().list(userId, gameId, 1);
    }

    public List<LoginGame> list(int userId, int gameId, int serverId) throws SQLException {
        return getLoginGameDao().list(userId, gameId, serverId, 0);
    }

    public void add(LoginGame entity) throws SQLException {
        getLoginGameDao().add(entity);
    }

    public void delete(LoginGame entity) throws SQLException {
        getLoginGameDao().delete(entity);
    }

    public void update(LoginGame entity) throws SQLException {
        getLoginGameDao().update(entity);
    }

    public void insertOrUpdate(LoginGame entity) throws SQLException {
        //getLoginGameDao().insertOrUpdate(entity);
        List<LoginGame> list = getLoginGameDao().list(entity.getUserId(), entity.getGameId(), 1);
        if (list != null && !list.isEmpty()) {
            entity.setId(list.get(0).getId());
            entity.setLoginTime(new Timestamp(System.currentTimeMillis()));
            getLoginGameDao().update(entity);
        } else {
            getLoginGameDao().add(entity);
        }
    }

    /**
     * 根据用户的id，得到用户的游戏历程
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    public ImmutableList<GameHistory> getGameHistoryByUserId(int userId) throws Exception {
        Preconditions.checkArgument(userId > 0, "userId 非法！");
        List<LoginGame> loginGameList = list(userId);

        List<GameHistory> gameHistoryList = Lists.newLinkedList();
        CacheDataItemsService cacheDataItemsService = cacheDataItemsServiceProvider.get();
        ServerDataService serverDataService = serverDataServiceProvider.get();
        if (loginGameList == null || loginGameList.isEmpty()) {
            return null;
        } else {
            for (LoginGame loginGame : loginGameList) {
                GameHistory gameHistory = new GameHistory();
                gameHistory.setUserId(userId);
                final int gameId = loginGame.getGameId();
                final int serverId = loginGame.getServerId();
                gameHistory.setGameId(gameId);
                gameHistory.setServerId(serverId);
                GameTable.GameEntry gameEntry = cacheDataItemsService.getGameEntryFromCacheByGameId(gameId);
                ServerInfo serverInfo = serverDataService.get(gameId, serverId);
                if (gameEntry != null) {
                    gameHistory.setGameName(gameEntry.getGameName());
                    gameHistory.setGameTag(gameEntry.getAliasName().substring(0, 1).toUpperCase());
                }
                if (serverInfo != null) {
                    gameHistory.setServerName(serverInfo.getServerName());
                }
                gameHistoryList.add(gameHistory);
            }
        }
        return ImmutableList.copyOf(gameHistoryList);
    }


    /**
     * 根据用户的id，得到推荐的游戏服务器，同游戏历程的格式
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    public ImmutableList<GameHistory> getRecommendGameServerByUserId(int userId) throws Exception {
        Preconditions.checkArgument(userId > 0, "userId 非法！");
        List<GameHistory> gameHistoryList = Lists.newLinkedList();
        CacheDataItemsService cacheDataItemsService = cacheDataItemsServiceProvider.get();
        ServerDataService serverDataService = serverDataServiceProvider.get();
        //返回推荐的新服，每一种游戏返回一个最新开的服务器
        List<GameTable.GameEntry> gameEntryList = cacheDataItemsService.getGameEntriesFromCache();
        if (gameEntryList != null && !gameEntryList.isEmpty()) {
            for (GameTable.GameEntry gameEntry : gameEntryList) {
                final int gameId = gameEntry.getId();
                GameHistory gameHistory = new GameHistory();
                gameHistory.setUserId(userId);
                gameHistory.setGameId(gameId);
                gameHistory.setGameImg(gameEntry.getAliasName() + "_item.jpg");
                gameHistory.setGameName(gameEntry.getGameName());
                gameHistory.setGameTag(gameEntry.getAliasName().substring(0, 1).toUpperCase());
                final ServerInfo serverInfo = serverDataService.getNewOpenedServer(gameId);
                if (serverInfo != null) {
                    gameHistory.setServerId(serverInfo.getServerId());
                    gameHistory.setServerName(handleServerName(serverInfo.getServerName()));
                }
                gameHistoryList.add(gameHistory);
            }
        }
        return ImmutableList.copyOf(gameHistoryList);
    }

    //去掉新蛋，蛋II 服务器名称之前的游戏名称
    private String handleServerName(String serverName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName), "serverName 为空！");

        if (serverName.startsWith(DDT_NAME)) {
            serverName = serverName.replaceFirst(DDT_NAME, "").trim();
        }
        if (serverName.startsWith(XDDT_NAME)) {
            serverName = serverName.replaceFirst(XDDT_NAME, "").trim();
        }
        return serverName;
    }

}
