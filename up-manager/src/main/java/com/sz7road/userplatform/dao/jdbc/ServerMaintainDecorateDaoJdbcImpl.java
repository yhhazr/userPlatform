package com.sz7road.userplatform.dao.jdbc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.dao.ServerMaintainDecorateDao;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.pojos.ServerMaintain2;
import com.sz7road.userplatform.service.CacheDataItemsService;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: leo.liao
 * Date: 12-7-9
 * Time: 下午2:21
 */
public class ServerMaintainDecorateDaoJdbcImpl extends JdbcDaoSupport<ServerMaintain> implements ServerMaintainDecorateDao {

    static final String TABLE = "`conf_userplatform`.conf_server_maintain";

    static final String TABLE_MULTIPLY = "`conf_userplatform`.server_maintain";

    static final String SQL_ADD = " insert into " + TABLE + "(serverId,createTime,startTime,endTime,message) values(?,?,?,?,?)";

    static final String SQL_ADD_NOTFORSQ = " insert into " + TABLE_MULTIPLY + "(serverId,createTime,startTime,endTime,message,gameId) values(?,?,?,?,?,?)";
    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    public int add(ServerMaintain entity, int[] serverIds) throws SQLException {
        if (entity == null || serverIds == null || !(serverIds.length > 0)) {
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(SQL_ADD);
            for (int serverId : serverIds) {
                stmt.setInt(1, serverId);
                stmt.setTimestamp(2, entity.getCreateTime());
                stmt.setTimestamp(3, entity.getStartTime());
                stmt.setTimestamp(4, entity.getEndTime());
                stmt.setString(5, entity.getMessage());
                stmt.addBatch();
            }
            int len = stmt.executeBatch().length;
            return len;
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
        }
    }

    /**
     * 批量增加维护服务区信息
     *
     * @param entitys
     * @return
     * @throws SQLException
     */
    @Override
    public Msg addMaintainServer(List<ServerMaintain> entitys) throws SQLException {

        if (entitys == null && entitys.size() > 0) {
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(SQL_ADD);
            for (ServerMaintain entity : entitys) {
                stmt.setInt(1, entity.getServerId());
                stmt.setTimestamp(2, entity.getCreateTime());
                stmt.setTimestamp(3, entity.getStartTime());
                stmt.setTimestamp(4, entity.getEndTime());
                stmt.setString(5, entity.getMessage());
                stmt.addBatch();
            }
            int len = stmt.executeBatch().length;
            if(len>=1)
            {
            msg.setCode(200);
            msg.setMsg("成功增加了" + len + "条维护信息!");
            }
            else
            {
                msg.setCode(204);
                msg.setMsg("增加维护信息失败!");
            }
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
        }
        return msg;
    }

    /**
     * 拿到到时间的维护列表
     *
     * @param current
     * @return
     */
    @Override
    public List<ServerMaintain> getMaintainClosedServerFromTime(Timestamp current) {

        String SELECT_SQL = " select * from " + TABLE + " where 1 and endTime >? order by id asc  ";

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
            log.error("获取维护信息异常！");
        } finally {
            try {
                stmt.close();
                DbUtils.close(rs);
                DbUtils.close(connection);
            } catch (Exception e) {
                log.error("connection close 异常!");
            }
        }
        return maintainsServer;
    }

    /**
     * 拿到正在维护和即将维护的维护信息
     *
     * @param current
     * @return
     */
    @Override
    public List<ServerMaintain> getMaintainingServerFromTime(Timestamp current) {
        String SELECT_SQL =  " select * from " + TABLE + " where 1 and endTime >? order by id asc  ";
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
            log.error("查询正在维护的或者即将开始维护的维护信息异常！");
        } finally {
            try {
                stmt.close();
                DbUtils.close(rs);
                DbUtils.close(connection);
            } catch (Exception e) {
                log.error("connection close 异常!");
            }
        }
        return maintainsServer;
    }


    /**
     * 批量更新维护信息
     *
     * @param maintainInfo 信息封装在这个对象里
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public Msg batchUpdateMaintainServers(ServerMaintain maintainInfo, List<Integer> ids) throws SQLException {
        if (maintainInfo == null || ids == null || ids.size() <= 0) {
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String UPDATESQL = " update " + TABLE + " set  startTime=? , endTime=? , message=?  where id=? ";
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(UPDATESQL);
            for (Integer id : ids) {
                stmt.setTimestamp(1, maintainInfo.getStartTime());
                stmt.setTimestamp(2, maintainInfo.getEndTime());
                stmt.setString(3, maintainInfo.getMessage());
                stmt.setInt(4, id);
                stmt.addBatch();
            }
            int len = stmt.executeBatch().length;
            if (len >= 1) {
                msg.setCode(200);
                msg.setMsg("成功修改" + len + "条维护信息!");

            } else {
                msg.setCode(204);
                msg.setMsg("数据库中没有这条维护信息！");
            }
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);

            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(maintainInfo.getEndTime());
        }
        return msg;
    }

    @Override
    public Msg batchUpdateMaintainServersNotForSq(ServerMaintain2 maintainInfo, List<Integer> ids) throws SQLException {
        if (maintainInfo == null || ids == null || ids.size() <= 0) {
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String UPDATESQL = " update " + TABLE_MULTIPLY + " set  startTime=? , endTime=? , message=?  where serverId=? and gameId=? ";
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(UPDATESQL);
            for (Integer id : ids) {
                stmt.setTimestamp(1, maintainInfo.getStartTime());
                stmt.setTimestamp(2, maintainInfo.getEndTime());
                stmt.setString(3, maintainInfo.getMessage());
                stmt.setInt(4, id);
                stmt.setInt(5,maintainInfo.getGameId());
                stmt.addBatch();
            }
            int len = stmt.executeBatch().length;
            if (len >= 1) {
                msg.setCode(200);
                msg.setMsg("成功修改" + len + "条维护信息!");

            } else {
                msg.setCode(204);
                msg.setMsg("数据库中没有这条维护信息！");
            }
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
        }
        return msg;
    }

    @Override
    public Msg batchDeleteFutureMaintainInfo(int[] serverIdArray) {

        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String deleteMaintainSql = " delete from " + TABLE + " where gameId=? and endTime>startTime and startTime>? and endTime>? and serverId=? ";
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(deleteMaintainSql);
            Timestamp current=new Timestamp(System.currentTimeMillis());
            for (Integer serverId : serverIdArray) {
                stmt.setInt(1,1);
                stmt.setTimestamp(2, current);
                stmt.setTimestamp(3, current);
                stmt.setInt(4, serverId);
                stmt.addBatch();
            }
            int[] rel = stmt.executeBatch();
            int len=0;
            for(int r:rel)
            {
                  len+=r;
            }
            if (len >= 1) {
                msg.setCode(200);
                msg.setMsg("成功删除" + len + "条没到时间的维护信息!");

            } else {
                msg.setCode(204);
                msg.setMsg("数据库中没有没到时间的维护信息！");
            }
        }catch (Exception ex)
        {
            msg.setCode(204);
            msg.setMsg("批量删除还没开始维护的维护信息失败!");
            log.error("批量删除还没开始维护的维护信息失败!");
            ex.printStackTrace();
        }
        finally {
            try{
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);
            }catch (Exception e)
            {
                log.error("关闭连接失败!");
               e.printStackTrace();
            }
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
            return msg;
        }
    }

    @Override
    public Msg batchDeleteFutureMaintainInfoByGameId(int[] serverIdArray, int gameId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String deleteMaintainSql = " delete from " + TABLE + " where gameId=? and endTime>startTime and startTime>? and endTime>? and serverId=? ";
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(deleteMaintainSql);
            Timestamp current=new Timestamp(System.currentTimeMillis());
            for (Integer serverId : serverIdArray) {
                stmt.setInt(1,gameId);
                stmt.setTimestamp(2, current);
                stmt.setTimestamp(3, current);
                stmt.setInt(4, serverId);
                stmt.addBatch();
            }
            int[] rel = stmt.executeBatch();
            int len=0;
            for(int r:rel)
            {
                len+=r;
            }
            if (len >= 1) {
                msg.setCode(200);
                msg.setMsg("成功删除" + len + "条没到时间的维护信息!");

            } else {
                msg.setCode(204);
                msg.setMsg("数据库中没有没到时间的维护信息！");
            }
        }catch (Exception ex)
        {
            msg.setCode(204);
            msg.setMsg("批量删除还没开始维护的维护信息失败!");
            log.error("批量删除还没开始维护的维护信息失败!");
            ex.printStackTrace();
        }
        finally {
            try{
                connection.commit();
                connection.setAutoCommit(true);
                stmt.close();
                DbUtils.close(connection);
            }catch (Exception e)
            {
                log.error("关闭连接失败!");
                e.printStackTrace();
            }
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
            return msg;
        }
    }

    @Override
    public Msg addMaintainServerNotForSq(List<ServerMaintain2> entitys) throws Exception {
        if (entitys == null && entitys.size() > 0) {
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt=null,mt = null;
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(" delete from `conf_userplatform`.server_maintain where serverId=? and gameId=? ");
            for (ServerMaintain2 entity : entitys) {
                stmt.setInt(1, entity.getServerId());
                stmt.setInt(2,entity.getGameId());
                stmt.addBatch();
            }
            stmt.executeBatch();

            mt = connection.prepareStatement(SQL_ADD_NOTFORSQ);
            for (ServerMaintain2 entity : entitys) {
                mt.setInt(1, entity.getServerId());
                mt.setTimestamp(2, entity.getCreateTime());
                mt.setTimestamp(3, entity.getStartTime());
                mt.setTimestamp(4, entity.getEndTime());
                mt.setString(5, entity.getMessage());
                mt.setInt(6,entity.getGameId());
                mt.addBatch();
            }
            int len = mt.executeBatch().length;
            if(len>=1)
            {
                msg.setCode(200);
                msg.setMsg("成功增加了" + len + "条维护信息!");
            }
            else
            {
                msg.setCode(204);
                msg.setMsg("增加维护信息失败!");
            }
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
        }
        return msg;
    }

    @Override
    public Msg batchUpdateMaintainServersNotForSqById(ServerMaintain2 maintainInfo, List<Integer> ids) throws SQLException{
        if (maintainInfo == null || ids == null || ids.size() <= 0) {
            throw new NullPointerException("null object");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String UPDATESQL = " update " + TABLE_MULTIPLY + " set  startTime=? , endTime=? , message=?  where id=? ";
        Msg msg = new Msg();
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(UPDATESQL);
            for (Integer id : ids) {
                stmt.setTimestamp(1, maintainInfo.getStartTime());
                stmt.setTimestamp(2, maintainInfo.getEndTime());
                stmt.setString(3, maintainInfo.getMessage());
                stmt.setInt(4, id);
                stmt.addBatch();
            }
            int len = stmt.executeBatch().length;
            if (len >= 1) {
                msg.setCode(200);
                msg.setMsg("成功修改" + len + "条维护信息!");

            } else {
                msg.setCode(204);
                msg.setMsg("数据库中没有这条维护信息！");
            }
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            stmt.close();
            DbUtils.close(connection);
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
        }
        return msg;
    }
}
