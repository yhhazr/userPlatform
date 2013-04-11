/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.dao.ServerTableDao;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import com.sz7road.userplatform.pojos.ServerTableNotForSqEntity;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
class ServerTableDaoImpl extends JdbcDaoSupport<ServerTable.ServerEntry> implements ServerTableDao {

    static final String TABLE = "`conf_userplatform`.`conf_server_table`";
    private static final String TABLE_VARIABLES = "`conf_userplatform`.`conf_server_variables`";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE +" where 1 ";
    private static final String SQL_SELECT_ALL_VARIABLES = "SELECT * FROM " + TABLE_VARIABLES + " WHERE 1 order by serverId asc ";
    private static final String SQL_SELECT_BY_STATUS = SQL_SELECT_ALL + " and " + TABLE + ".`serverStatus`<> -1 order by serverNo asc  ";
    private static final String SQL_UPDATE = "UPDATE " + TABLE + " SET `gameId`=?,`serverNo`=?,`serverName`=?,`serverStatus`=?,`recommand`=?,`createTime`=?,`openingTime`=? WHERE id=?";

    private static final String TABLE_NOTFORSQ = "`conf_userplatform`.`server_table`";
    private static final String SQL_SELECT_ALL_NOTFORSQ = "SELECT * FROM " + TABLE_NOTFORSQ +" where 1 ";
    private static final String SQL_SELECT_ALL_VARIABLES_NOTFORSQ = "SELECT * FROM " + TABLE_VARIABLES + " WHERE 1 order by serverId asc ";
    private static final String SQL_SELECT_BY_STATUS_NOTFORSQ = SQL_SELECT_ALL_NOTFORSQ + " and " + TABLE_NOTFORSQ + ".`status`<> -1 order by createTime desc ,serverNo asc  ";
    @Override
    public List<ServerTable.ServerEntry> listAll() throws SQLException {
        final QueryRunner runner = getQueryRunner();

        final Map<Integer, Map<String, String>> variables = runner.query(SQL_SELECT_ALL_VARIABLES, new ResultSetHandler<Map<Integer, Map<String, String>>>() {

            @Override
            public Map<Integer, Map<String, String>> handle(final ResultSet rs) throws SQLException {
                final Map<Integer, Map<String, String>> resultMap = Maps.newHashMap();
                while (rs.next()) {
                    int serverId = rs.getInt("serverId");
                    String key = rs.getString("key");
                    String value = rs.getString("value");

                    if (!resultMap.containsKey(serverId)) {
                        Map<String, String> map = Maps.newHashMap();
                        resultMap.put(serverId, map);
                    }

                    Map<String, String> map = resultMap.get(serverId);
                    map.put(key, value);
                }

                return resultMap;
            }
        });

        final List<ServerTable.ServerEntry> result = Lists.newArrayList();
        final BeanHandler<ServerTable.ServerEntry> handler = new BeanHandler<ServerTable.ServerEntry>(ServerTable.ServerEntry.class);
        return runner.query(SQL_SELECT_ALL, new ResultSetHandler<List<ServerTable.ServerEntry>>() {
            @Override
            public List<ServerTable.ServerEntry> handle(final ResultSet rs) throws SQLException {
                ServerTable.ServerEntry entry;
                while (null != (entry = handler.handle(rs))) {
                    if (null != variables && variables.containsKey(entry.getId())) {
                        entry.setVariables(variables.get(entry.getId()));
                    }
                    result.add(entry);
                }
                return result;
            }
        });
    }

    @Override
    public List<ServerTableNotForSq.ServerEntryNotForSq> listsWithStatusNotForSq() throws SQLException {
        final QueryRunner runner = getQueryRunner();

        final Map<Integer, Map<String, String>> variables = runner.query(SQL_SELECT_ALL_VARIABLES, new ResultSetHandler<Map<Integer, Map<String, String>>>() {

            @Override
            public Map<Integer, Map<String, String>> handle(final ResultSet rs) throws SQLException {
                final Map<Integer, Map<String, String>> resultMap = Maps.newHashMap();
                while (rs.next()) {
                    int serverId = rs.getInt("serverId");
                    String key = rs.getString("key");
                    String value = rs.getString("value");

                    if (!resultMap.containsKey(serverId)) {
                        Map<String, String> map = Maps.newHashMap();
                        resultMap.put(serverId, map);
                    }

                    Map<String, String> map = resultMap.get(serverId);
                    map.put(key, value);
                }

                return resultMap;
            }
        });

        final List<ServerTableNotForSq.ServerEntryNotForSq> result = Lists.newArrayList();
        final BeanHandler<ServerTableNotForSq.ServerEntryNotForSq> handler = new BeanHandler<ServerTableNotForSq.ServerEntryNotForSq>(ServerTableNotForSq.ServerEntryNotForSq.class);
        return runner.query(SQL_SELECT_BY_STATUS_NOTFORSQ, new ResultSetHandler<List<ServerTableNotForSq.ServerEntryNotForSq>>() {
            @Override
            public List<ServerTableNotForSq.ServerEntryNotForSq> handle(final ResultSet rs) throws SQLException {
                ServerTableNotForSq.ServerEntryNotForSq entry;
                while (null != (entry = handler.handle(rs))) {
                    if (null != variables && variables.containsKey(entry.getServerId())) {
                        entry.setVariables(variables.get(entry.getServerId()));
                    }
                    result.add(entry);
                }
                return result;
            }
        });
    }

    @Override
    public List<ServerTable.ServerEntry> listsWithStatus() throws SQLException {
        final QueryRunner runner = getQueryRunner();

        final Map<Integer, Map<String, String>> variables = runner.query(SQL_SELECT_ALL_VARIABLES, new ResultSetHandler<Map<Integer, Map<String, String>>>() {

            @Override
            public Map<Integer, Map<String, String>> handle(final ResultSet rs) throws SQLException {
                final Map<Integer, Map<String, String>> resultMap = Maps.newHashMap();
                while (rs.next()) {
                    int serverId = rs.getInt("serverId");
                    String key = rs.getString("key");
                    String value = rs.getString("value");

                    if (!resultMap.containsKey(serverId)) {
                        Map<String, String> map = Maps.newHashMap();
                        resultMap.put(serverId, map);
                    }

                    Map<String, String> map = resultMap.get(serverId);
                    map.put(key, value);
                }

                return resultMap;
            }
        });

        final List<ServerTable.ServerEntry> result = Lists.newArrayList();
        final BeanHandler<ServerTable.ServerEntry> handler = new BeanHandler<ServerTable.ServerEntry>(ServerTable.ServerEntry.class);
        return runner.query(SQL_SELECT_BY_STATUS, new ResultSetHandler<List<ServerTable.ServerEntry>>() {
            @Override
            public List<ServerTable.ServerEntry> handle(final ResultSet rs) throws SQLException {
                ServerTable.ServerEntry entry;
                while (null != (entry = handler.handle(rs))) {
                    if (null != variables && variables.containsKey(entry.getId())) {
                        entry.setVariables(variables.get(entry.getId()));
                    }
                    result.add(entry);
                }
                return result;
            }
        });
    }

    /**
     * 拿到所有需要的服务区数据
     *
     * @param gameId
     * @return
     */
    @Override
    public List<ServerEntity> getSortNonClosedServersByGameId(int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ServerEntity> serverEntities = new LinkedList<ServerEntity>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            String sql = " select * from " + TABLE + " where serverStatus <> -1 and gameId=? order by serverNo asc ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,gameId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerEntity serverTableEntry = new ServerEntity();
                serverTableEntry.setId(rs.getInt("id"));
                serverTableEntry.setGameId(rs.getInt("gameId"));
                serverTableEntry.setServerNo(rs.getInt("serverNo"));
                serverTableEntry.setServerName(rs.getString("serverName"));
                serverTableEntry.setServerStatus(rs.getInt("serverStatus"));
                serverTableEntry.setCreateTime(rs.getTimestamp("createTime"));
                serverTableEntry.setOpeningTime(rs.getTimestamp("openingTime"));
                serverTableEntry.setRecommand(rs.getBoolean("recommand"));
                serverEntities.add(serverTableEntry);
            }
        } catch (final SQLException e) {
            log.error("获取缓存游戏服务区异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return serverEntities;
    }

    @Override
    public List<ServerTableNotForSqEntity.ServerEntryNotForSqEntity> getSortNonClosedServersByGameIdNotForSq(int gameId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ServerTableNotForSqEntity.ServerEntryNotForSqEntity> serverEntities = Lists.newLinkedList();
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            String sql = " select * from " + TABLE_NOTFORSQ + " where status <> -1 and gameId=? order by serverNo asc ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,gameId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerTableNotForSqEntity.ServerEntryNotForSqEntity serverTableEntry = new ServerTableNotForSqEntity.ServerEntryNotForSqEntity();
                serverTableEntry.setId(rs.getInt("id"));
                serverTableEntry.setGameId(rs.getInt("gameId"));
                serverTableEntry.setServerId(rs.getInt("serverId"));
                serverTableEntry.setMainId(rs.getInt("mainId"));
                serverTableEntry.setServerNo(rs.getInt("serverNo"));
                serverTableEntry.setServerName(rs.getString("serverName"));
                serverTableEntry.setStatus(rs.getInt("status"));
                serverTableEntry.setCreateTime(rs.getTimestamp("createTime"));
                serverTableEntry.setOpenTime(rs.getTimestamp("openTime"));
                serverTableEntry.setRecommand(rs.getBoolean("recommand"));
                serverEntities.add(serverTableEntry);
            }
        } catch (final SQLException e) {
            log.error("获取缓存游戏服务区异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return serverEntities;
    }

    @Override
    public int update(ServerTable.ServerEntry entity) throws SQLException {
        return getQueryRunner().update(SQL_UPDATE, entity.getGameId(), entity.getServerNo(), entity.getServerName(),
                entity.getServerStatus(), entity.isRecommand(), entity.getCreateTime(), entity.getOpeningTime(), entity.getId());
    }
}
