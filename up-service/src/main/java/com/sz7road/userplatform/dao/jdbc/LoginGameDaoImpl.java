/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.dao.AppealDao;
import com.sz7road.userplatform.dao.LoginGameDao;
import com.sz7road.userplatform.pojos.Appeal;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.LoginGame;
import com.sz7road.userplatform.pojos.Pager;
import com.sz7road.utils.ListData;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author jiangfan.zhou
 */
public class LoginGameDaoImpl extends JdbcDaoSupport<LoginGame> implements LoginGameDao {

    private static final String TABLE = "`db_userplatform`.`dt_login_game`";
    private static final String SQL_SELECT_BASE = "SELECT * FROM " + TABLE + " WHERE 1";
    private static final String SQL_SELECT_BY_USERID = SQL_SELECT_BASE + " AND userId = ? order by `loginTime` desc";
    private static final String SQL_SELECT_BY_USERID_GAMEID = SQL_SELECT_BASE + " AND `userId`=? AND `gameId`=? order by `loginTime` desc";
    private static final String SQL_SELECT_BY_USERID_GAMEID_SERVERID = SQL_SELECT_BASE + " AND `userId`=? AND `gameId`=? AND `serverId`=? order by `loginTime` desc";
    private static final String SQL_SELECT_BY_FIELD = SQL_SELECT_BASE + " AND %s = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE +
            "(`userId`, `gameId`, `serverId`,`loginTime`) VALUES(?,?,?,?)";

    private static final String SQL_UPDATE = "UPDATE " + TABLE +
            " SET `userId`=?,`gameId`=?,`serverId`=?,`loginTime`=? WHERE `id`=?";

    private static final String SQL_INSERT_UPDATE = "INSERT INTO " + TABLE +
            "(`userId`, `gameId`, `serverId`,`loginTime`) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE `serverId`=?,`loginTime`=?";

    private static final String SQL_DELETE = "DELETE FROM " + TABLE + " WHERE `userId` = ?";

    @Override
    public int add(LoginGame entity) throws SQLException {
        return getQueryRunner().update(SQL_INSERT, entity.getUserId(), entity.getGameId(), entity.getServerId(), entity.getLoginTime());
    }

    @Override
    public int update(LoginGame entity) throws SQLException {
        return getQueryRunner().update(SQL_UPDATE, entity.getUserId(), entity.getGameId(), entity.getServerId(), entity.getLoginTime(), entity.getId());
    }

    @Override
    public void insertOrUpdate(LoginGame entity) throws SQLException {
        getQueryRunner().update(SQL_INSERT_UPDATE, entity.getUserId(), entity.getGameId(), entity.getServerId(), entity.getLoginTime(), entity.getServerId(), entity.getLoginTime());
    }

    @Override
    public int delete(LoginGame entity) throws SQLException {
        return getQueryRunner().update(SQL_DELETE, entity.getUserId());
    }

    @Override
    public List<LoginGame> list(int userId, int size) throws SQLException {
        if (size > 0){
            return getQueryRunner().query(SQL_SELECT_BY_USERID + " limit 0,?", new BeanListHandler<LoginGame>(LoginGame.class), userId, size);
        }
        return getQueryRunner().query(SQL_SELECT_BY_USERID , new BeanListHandler<LoginGame>(LoginGame.class), userId);
    }

    @Override
    public List<LoginGame> list(int userId, int gameId, int size) throws SQLException {
        if (size > 0){
            return getQueryRunner().query(SQL_SELECT_BY_USERID_GAMEID + " limit 0,?" , new BeanListHandler<LoginGame>(LoginGame.class), userId, gameId, size);
        }
        return getQueryRunner().query(SQL_SELECT_BY_USERID_GAMEID , new BeanListHandler<LoginGame>(LoginGame.class), userId, gameId);
    }

    @Override
    public List<LoginGame> list(int userId, int gameId, int serverId, int size) throws SQLException {
        if (size > 0) {
            return getQueryRunner().query(SQL_SELECT_BY_USERID_GAMEID_SERVERID + " limit 0,?", new BeanListHandler<LoginGame>(LoginGame.class), userId, gameId, serverId, size);
        }
        return getQueryRunner().query(SQL_SELECT_BY_USERID_GAMEID_SERVERID , new BeanListHandler<LoginGame>(LoginGame.class), userId, gameId, serverId);
    }
}
