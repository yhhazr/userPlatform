package com.sz7road.userplatform.dao.jdbc;

import com.google.common.collect.*;
import com.sz7road.userplatform.dao.ServerMaintainDao;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.pojos.ServerMaintain2;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: leo.liao
 * Date: 12-7-9
 * Time: 下午2:21
 */
public class ServerMaintainDaoJdbcImpl extends JdbcDaoSupport<ServerMaintain> implements ServerMaintainDao {

    static final String TABLE = "`conf_userplatform`.conf_server_maintain";
    static final String TABLE_MULTIPLY = "`conf_userplatform`.server_maintain";
    static final String SELECT_SQL = " select * from " + TABLE + " where  endTime >? order by createTime desc ,serverId asc ";
    static final String SELECT_SQL_MULTIPLY = " select * from " + TABLE_MULTIPLY + " where  endTime >? order by createTime desc ,gameId asc,serverId asc ";

    /**
     * 拿到到时间的维护列表
     *
     * @param current
     * @return
     */
    @Override
    public List<ServerMaintain> getMaintainServerFromTime(Timestamp current) {

        List<ServerMaintain> maintainsServer = new ArrayList<ServerMaintain>();
        if (null == current) {
            log.error("current条件为空！");
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setTimestamp(1, current);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ServerMaintain serverMaintain = new ServerMaintain();
                serverMaintain.setId(rs.getInt("id"));
                serverMaintain.setServerId(rs.getInt("serverId"));
                serverMaintain.setCreateTime(rs.getTimestamp("createTime"));
                serverMaintain.setStartTime(rs.getTimestamp("startTime"));
                serverMaintain.setEndTime(rs.getTimestamp("endTime"));
                serverMaintain.setMessage(rs.getString("message"));
                maintainsServer.add(serverMaintain);
            }
        } catch (Exception ex) {
            log.error("查询维护信息异常！");
        } finally {
            try {
                DbUtils.close(rs);
                stmt.close();
                DbUtils.close(connection);
            } catch (Exception e) {
                log.error("connection close 异常!");
            }
        }
        return maintainsServer;
    }


    /**
     * 拿到到时间的维护列表
     *
     * @param current
     * @return
     */
    @Override
    public Table<Integer,Integer,ServerMaintain2> getMaintainServerFromTime2(Timestamp current) {

        Table<Integer,Integer,ServerMaintain2> maintainInfoTable= TreeBasedTable.create();
        if (null == current) {
            log.error("current条件为空！");
            throw new NullPointerException("null object");
        }

        try {

            maintainInfoTable.putAll(getQueryRunner().query(SELECT_SQL,new ResultSetHandler< Table<Integer,Integer,ServerMaintain2>>() {
               @Override
               public  Table<Integer,Integer,ServerMaintain2> handle(ResultSet rs) throws SQLException {
                   Table<Integer,Integer,ServerMaintain2> maintainInfoTable1= TreeBasedTable.create();
                   while (rs.next()) {
                       ServerMaintain2 serverMaintain = new ServerMaintain2();
                       serverMaintain.setId(rs.getInt("id"));
                       serverMaintain.setGameId(1);//神曲的游戏
                       serverMaintain.setServerId(rs.getInt("serverId"));
                       serverMaintain.setCreateTime(rs.getTimestamp("createTime"));
                       serverMaintain.setStartTime(rs.getTimestamp("startTime"));
                       serverMaintain.setEndTime(rs.getTimestamp("endTime"));
                       serverMaintain.setMessage(rs.getString("message"));
                       maintainInfoTable1.put(1,serverMaintain.getServerId(),serverMaintain);
                   }
                   return maintainInfoTable1;
               }
           },current));
            //得到其它游戏的维护信息
            maintainInfoTable.putAll(
                    getQueryRunner().query(SELECT_SQL_MULTIPLY, new ResultSetHandler<Table<Integer,Integer,ServerMaintain2>>() {
                        @Override
                        public Table<Integer,Integer,ServerMaintain2> handle(ResultSet rs) throws SQLException {
                            Table<Integer,Integer,ServerMaintain2> maintainInfoTable2= TreeBasedTable.create();
                            while (rs.next()) {
                                ServerMaintain2 serverMaintain = new ServerMaintain2();
                                serverMaintain.setId(rs.getInt("id"));
                                serverMaintain.setGameId(rs.getInt("gameId"));
                                serverMaintain.setServerId(rs.getInt("serverId"));
                                serverMaintain.setCreateTime(rs.getTimestamp("createTime"));
                                serverMaintain.setStartTime(rs.getTimestamp("startTime"));
                                serverMaintain.setEndTime(rs.getTimestamp("endTime"));
                                serverMaintain.setMessage(rs.getString("message"));
                                maintainInfoTable2.put(serverMaintain.getGameId(),serverMaintain.getServerId(),serverMaintain);
                            }
                            return maintainInfoTable2;
                        }
                    },current));
        } catch (Exception ex) {
            log.error("查询维护信息异常！");
        }
        return maintainInfoTable;
    }


    @Override
    public List<ServerMaintain2> getMaintainServerFromTimeAndGameId(Timestamp current, int gameId) {
        List<ServerMaintain2> maintainsServer = Lists.newArrayList();
        if (null == current || gameId <= 0) {
            log.error("current条件为空！或者gameId非法！current:" + current + " gameId:" + gameId);
            throw new IllegalArgumentException("参数非法");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setInt(1, gameId);
            stmt.setTimestamp(2, current);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ServerMaintain2 serverMaintain = new ServerMaintain2();
                serverMaintain.setId(rs.getInt("id"));
                serverMaintain.setGameId(rs.getInt("gameId"));
                serverMaintain.setServerId(rs.getInt("serverId"));
                serverMaintain.setCreateTime(rs.getTimestamp("createTime"));
                serverMaintain.setStartTime(rs.getTimestamp("startTime"));
                serverMaintain.setEndTime(rs.getTimestamp("endTime"));
                serverMaintain.setMessage(rs.getString("message"));
                maintainsServer.add(serverMaintain);
            }
        } catch (Exception ex) {
            log.error("查询维护信息异常！");
        } finally {
            try {
                DbUtils.close(rs);
                stmt.close();
                DbUtils.close(connection);
            } catch (Exception e) {
                log.error("connection close 异常!");
            }
        }
        return maintainsServer;
    }

    @Override
    public List<ServerMaintain> getMaintainServerFromTimeAll(Timestamp current) {
        List<ServerMaintain> maintainsServer = new ArrayList<ServerMaintain>();
        if (null == current) {
            log.error("current条件为空！");
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(" select * from " + TABLE + " where 1 and endTime >=? order by createTime desc ,serverId asc ");
            stmt.setTimestamp(1, current);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ServerMaintain serverMaintain = new ServerMaintain();
                serverMaintain.setId(rs.getInt("id"));
                serverMaintain.setServerId(rs.getInt("serverId"));
                serverMaintain.setCreateTime(rs.getTimestamp("createTime"));
                serverMaintain.setStartTime(rs.getTimestamp("startTime"));
                serverMaintain.setEndTime(rs.getTimestamp("endTime"));
                serverMaintain.setMessage(rs.getString("message"));
                maintainsServer.add(serverMaintain);
            }
        } catch (Exception ex) {
            log.error("查询维护信息异常！");
        } finally {
            try {
                DbUtils.close(rs);
                stmt.close();
                DbUtils.close(connection);
            } catch (Exception e) {
                log.error("connection close 异常!");
            }
        }
        return maintainsServer;
    }
}
