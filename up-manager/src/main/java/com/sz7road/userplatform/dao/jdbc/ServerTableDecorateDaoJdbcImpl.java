package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.dao.ServerTableDecorateDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryNotForSqDao;
import com.sz7road.userplatform.dao.cacheimp.AbstractCacheDaoImp;
import com.sz7road.userplatform.pojo.GameIdAndServerId;
import com.sz7road.userplatform.pojo.ZTreeObject;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ListData;
import com.sz7road.web.utils.DataUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.codehaus.jackson.map.ObjectMapper;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * User: leo.liao
 * Date: 12-6-19
 * Time: 上午10:47
 */
public class ServerTableDecorateDaoJdbcImpl extends ServerTableDaoImpl implements ServerTableDecorateDao {

    private static final String TABLE = "`conf_userplatform`.`conf_server_table`";

    private static final String TABLE_NOTFORSQ = "`conf_userplatform`.`server_table`";

    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE + " WHERE 1";
    private static final String SQL_ADD = " INSERT INTO " + TABLE + "(`id`,`gameId`,`serverNo`,`serverName`,`serverStatus`,`createTime`,`openingTime`,`recommand`) VALUES(?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_FIELD_BY_ID = "UPDATE " + TABLE + " SET $field WHERE 1 AND id=?";
    private static final String SQL_UPDATE_BY_ID = "UPDATE " + TABLE + " SET `gameId`=?,`serverNo`=?,`serverName`=?,`serverStatus`=?,`createTime`=?,`openingTime`=?,`recommand`=? WHERE 1 AND id=?";

    private static final String SQL_UPDATE_STATUS_BY_ID = " UPDATE " + TABLE + " SET `serverStatus`=? WHERE 1 AND id=? ";
    private static final String SQL_UPDATE_RECOMMEND_BEFORETODAY = " UPDATE " + TABLE + " SET `recommand`=0 WHERE  `recommand`=1 AND openingTime<?  ";

    private static final String SQL_UPDATE_STATUS_BY_STATUS = "UPDATE " + TABLE + " SET `serverStatus`=? WHERE 1 AND serverStatus=?";

    private static final String SQL_GET_SERVERENTRY_BY_ID = "SELECT * FROM " + TABLE + "WHERE 1 AND id=?";
    private ObjectMapper mapper = new ObjectMapper();
    @Inject
    private Provider<CacheServerEntryDao> serverEntryDaoProvider;

    @Inject
    private Provider<CacheServerEntryNotForSqDao> CacheServerEntryNotForSqDaoProvider;
    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    @Override
    public int add(ServerTable.ServerEntry entity) throws SQLException {
        if (null == entity) {
            throw new NullPointerException("null ServerEntry");
        }
        int addDbRel = 0;
        long addCacheRel = 0l;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_ADD);

            stmt.setInt(1, entity.getId());
            stmt.setInt(2, entity.getGameId());
            stmt.setInt(3, entity.getServerNo());
            stmt.setString(4, entity.getServerName());
            stmt.setInt(5, entity.getServerStatus());
            stmt.setTimestamp(6, entity.getCreateTime());
            stmt.setTimestamp(7, entity.getOpeningTime());
            stmt.setBoolean(8, entity.isRecommand());
            addDbRel = stmt.executeUpdate();
            //在缓存中增加服务器
            String id = String.valueOf(entity.getId());
            String serverEntryStr = mapper.writeValueAsString(entity);
            CacheServerEntryDao cacheDao = serverEntryDaoProvider.get();
            addCacheRel = cacheDao.updateSingleCacheDataItem(mapper.readValue(serverEntryStr, ServerTable.ServerEntry.class),
                    AbstractCacheDaoImp.CACHE_SERVER_ENTRIES_KEY);
            if (addDbRel == 1 && addCacheRel == 1l) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                log.info("DB和缓存中 增加服务器信息： " + entity.getId() + " 名称：" + entity.getServerName()
                        + " 创建日期：" + simpleDateFormat.format(new Date().getTime()) + " 开服时间：" + simpleDateFormat.format(entity.getOpeningTime().getTime()));
            }
        } catch (final Exception e) {
            log.error("添加游戏区信息异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtils.close(rs);
            stmt.close();
            DbUtils.closeQuietly(conn);
        }
        return addDbRel;
    }

    public int updateField(Map<String, Object> fieldValueMap, int id) throws SQLException {
        int rel = 0;
        if (fieldValueMap != null && fieldValueMap.size() > 0) {
            StringBuilder FIELD = new StringBuilder();
            List<Object> paramList = Lists.newArrayList();
            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                FIELD.append(entry.getKey()).append("=?,");
                paramList.add(entry.getValue());
            }
            FIELD.deleteCharAt(FIELD.length() - 1);
            paramList.add(id);
            rel = getQueryRunner().update(SQL_UPDATE_FIELD_BY_ID.replace("$field", FIELD.toString()), paramList.toArray());

        }
        return rel;
    }

    public int batchUpdate(ServerTable.ServerEntry[] entries) throws SQLException {
        if (entries == null || entries.length == 0) {
            return 0;
        }
        int len = 0;
        Connection connection = getQueryRunner().getDataSource().getConnection();
        PreparedStatement pst = null;
        try {
            connection.setAutoCommit(false);
            pst = connection.prepareStatement(SQL_UPDATE_BY_ID);
            for (ServerTable.ServerEntry entry : entries) {
                pst.setInt(1, entry.getGameId());
                pst.setInt(2, entry.getServerNo());
                pst.setString(3, entry.getServerName());
                pst.setInt(4, entry.getServerStatus());
                pst.setTimestamp(5, entry.getCreateTime());
                pst.setTimestamp(6, entry.getOpeningTime());
                pst.setBoolean(7, entry.isRecommand());
                pst.setInt(8, entry.getId());
                pst.addBatch();
            }
            len = pst.executeBatch().length;


        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            pst.close();
            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
            return len;
        }
    }

    public ListData<ServerTable.ServerEntry> list(int gameId, String order, int sort, int start, int limit, Map<String, Object> queryMap) throws SQLException {
        Connection connection = getQueryRunner().getDataSource().getConnection();
        try {
            StringBuilder SQL_CONDITION = new StringBuilder(" WHERE 1 ");
            SQL_CONDITION.append(" AND serverStatus<>-1 AND gameId=?");
            List<Object> listParam = null;
            if (queryMap != null && queryMap.size() > 0) {
                listParam = Lists.newArrayList();
                for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
                    SQL_CONDITION.append(" AND ").append(entry.getKey()).append("=?");
                    listParam.add(entry.getValue());
                }
            }
            String sql = "SELECT COUNT(*) FROM " + TABLE + SQL_CONDITION.toString();
            PreparedStatement pst1 = connection.prepareStatement(sql);
            pst1.setInt(1, gameId);
            int i = 1;
            if (listParam != null && listParam.size() > 0) {
                for (Object obj : listParam) {
                    pst1.setString(++i, obj.toString());
                }
            }
            ResultSet resultSet1 = pst1.executeQuery();
            int total = 0;
            while (resultSet1.next()) {
                total = resultSet1.getInt(1);
            }
            List<ServerTable.ServerEntry> list = null;
            if (total > 0) {
                final BeanHandler<ServerTable.ServerEntry> handler = new BeanHandler<ServerTable.ServerEntry>(ServerTable.ServerEntry.class);
                list = Lists.newArrayList();
                if (Strings.isNullOrEmpty(order)) order = "serverNo";
                String sortStr = sort > 0 ? "asc" : "desc";
                SQL_CONDITION.append(" order by `").append(order).append("` ").append(sortStr);
                SQL_CONDITION.append(" limit ?,?");
                String sqlStr = "SELECT * FROM " + TABLE + SQL_CONDITION.toString();
                PreparedStatement pst = connection.prepareStatement(sqlStr);
                pst.setInt(1, gameId);
                i = 1;
                if (listParam != null && listParam.size() > 0) {
                    for (Object obj : listParam) {
                        pst.setString(++i, obj.toString());
                    }
                }
                pst.setInt(++i, start);
                pst.setInt(++i, limit);
                ResultSet resultSet = pst.executeQuery();
                ServerTable.ServerEntry entry = null;
                while ((entry = handler.handle(resultSet)) != null) {
                    list.add(entry);
                }
            }
            ListData<ServerTable.ServerEntry> listData = new ListData<ServerTable.ServerEntry>(list, total);
            return listData;
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    public int bathUpdateStatus(List<Integer> list, int status) throws SQLException {
        int rel = 0;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Connection connection = getQueryRunner().getDataSource().getConnection();
        connection.setAutoCommit(false);
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(SQL_UPDATE_STATUS_BY_ID);
            for (int id : list) {
                pst.setInt(1, status);
                pst.setInt(2, id);
                pst.addBatch();
                log.info("服务区id为--- " + id + "---的状态更新为 " + status);
            }
            rel = pst.executeBatch().length;


        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            pst.close();
            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
            long getMaintainTime=System.currentTimeMillis();
            if(status==1)
            {
                getMaintainTime+=60000;
            }
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(getMaintainTime));
            return rel;
        }
    }

    /**
     * 把今天以前开服的服务区的推荐字段修改为不推荐
     */

    public int bathUpdateRecommendFiledBeforeToday() throws SQLException {
        int rel = 0;
        Connection connection = getQueryRunner().getDataSource().getConnection();
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(SQL_UPDATE_RECOMMEND_BEFORETODAY);
            pst.setTimestamp(1, CommonDateUtils.getTodayStartTimeStamp());
            rel = pst.executeUpdate();
            if (rel > 0) {
                log.info(rel + "个服务器的推荐值成功更新为不推荐! ");

            }
        } finally {
            pst.close();
            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
            return rel;
        }
    }

    @Override
    public int addNotForSq(ServerTableNotForSq.ServerEntryNotForSq entity) {
        if (null == entity) {
            throw new NullPointerException("null ServerEntryNotForSq");
        }
        int addDbRel = 0;
        long addCacheRel = 0l;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(" INSERT INTO `conf_userplatform`.`server_table`(`serverId`,`gameId`,`serverNo`,`serverName`,`status`,`createTime`,`openTime`,`recommand`,`mainId`) VALUES(?,?,?,?,?,?,?,?,?); ");

            stmt.setInt(1, entity.getServerId());
            stmt.setInt(2, entity.getGameId());
            stmt.setInt(3, entity.getServerNo());
            stmt.setString(4, entity.getServerName());
            stmt.setInt(5, entity.getStatus());
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(7, entity.getOpenTime());
            stmt.setBoolean(8, entity.isRecommand());
            stmt.setInt(9,entity.getServerId());
            addDbRel = stmt.executeUpdate();


            if (addDbRel == 1 ) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                log.info("DB增加非神曲服务器信息： 游戏id:" + entity.getGameId() +" 服务器id:"+entity.getServerId()+ " 服务器名称：" + entity.getServerName()
                        + " 创建日期：" + simpleDateFormat.format(new Date().getTime()) + " 开服时间：" + simpleDateFormat.format(entity.getOpenTime().getTime()));
            }
        } catch (final Exception e) {
            log.error("添加非神曲游戏区信息异常：{}", e.getMessage());
        } finally {
            try {
                DbUtils.close(rs);
                stmt.close();
            } catch (SQLException e) {
                log.error("关闭结果集或者PreparedStatement 异常！");
                e.printStackTrace();
            }
            DbUtils.closeQuietly(conn);
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntriesNotForSq();
        }
        return addDbRel;
    }

    @Override
    public int batchUpdateNotForSq(ServerTableNotForSq.ServerEntryNotForSq[] entries) {
        if (entries == null || entries.length == 0) {
            return 0;
        }
        int len = 0;
        PreparedStatement pst = null;
        Connection connection=null;
        try {
             connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            pst = connection.prepareStatement(" UPDATE `conf_userplatform`.`server_table`  SET  `gameId`=?,`serverNo`=?,`serverName`=?,`status`=?,`createTime`=?,`openTime`=?,`recommand`=? ,`serverId`=?, `mainId`=? WHERE 1 AND id=? ;");
            for (ServerTableNotForSq.ServerEntryNotForSq entry : entries) {
                pst.setInt(1, entry.getGameId());
                pst.setInt(2, entry.getServerNo());
                pst.setString(3, entry.getServerName());
                pst.setInt(4, entry.getStatus());
                pst.setTimestamp(5, entry.getCreateTime());
                pst.setTimestamp(6, entry.getOpenTime());
                pst.setBoolean(7, entry.isRecommand());
                pst.setInt(8, entry.getServerId());
                pst.setInt(9,entry.getMainId());
                pst.setInt(10,entry.getId());
                pst.addBatch();
            }
            len = pst.executeBatch().length;
            if(len>0)
            {
                log.info("成功更新“+len+”非神曲服务器信息！");
            }
        }
        catch (Exception ex)
        {
          log.error("批量更新非神曲服务器失败！"+ex.getMessage());
          connection.rollback();
        }
        finally {
            try {
                connection.commit();
                connection.setAutoCommit(true);
                pst.close();
            } catch (Exception e) {
                log.error("提交事物失败！"+e.getMessage());
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntriesNotForSq();
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
            return len;
        }
    }

    @Override
    public ListData<ServerTableNotForSq.ServerEntryNotForSq> listNotForSq(int gameId, String order, int sort, int start, int limit, Map<String, Object> queryMap) {
       Connection connection=null;
        ListData<ServerTableNotForSq.ServerEntryNotForSq> listData=null;
        try {
             connection = getQueryRunner().getDataSource().getConnection();
            StringBuilder SQL_CONDITION = new StringBuilder(" WHERE 1 ");
            SQL_CONDITION.append(" AND status<>-1 AND gameId=?");
            List<Object> listParam = null;
            if (queryMap != null && queryMap.size() > 0) {
                listParam = Lists.newArrayList();
                for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
                    SQL_CONDITION.append(" AND ").append(entry.getKey()).append("=?");
                    listParam.add(entry.getValue());
                }
            }
            String sql = "SELECT COUNT(*) FROM " + TABLE_NOTFORSQ + SQL_CONDITION.toString();
            PreparedStatement pst1 = connection.prepareStatement(sql);
            pst1.setInt(1, gameId);
            int i = 1;
            if (listParam != null && listParam.size() > 0) {
                for (Object obj : listParam) {
                    pst1.setString(++i, obj.toString());
                }
            }
            ResultSet resultSet1 = pst1.executeQuery();
            int total = 0;
            while (resultSet1.next()) {
                total = resultSet1.getInt(1);
            }
            List<ServerTableNotForSq.ServerEntryNotForSq> list = null;
            if (total > 0) {
                final BeanHandler<ServerTableNotForSq.ServerEntryNotForSq> handler = new BeanHandler<ServerTableNotForSq.ServerEntryNotForSq>(ServerTableNotForSq.ServerEntryNotForSq.class);
                list = Lists.newArrayList();
                if (Strings.isNullOrEmpty(order)) order = "serverNo";
                String sortStr = sort > 0 ? "asc" : "desc";
                SQL_CONDITION.append(" order by `").append(order).append("` ").append(sortStr);
                SQL_CONDITION.append(" limit ?,?");
                String sqlStr = "SELECT * FROM " + TABLE_NOTFORSQ + SQL_CONDITION.toString();
                PreparedStatement pst = connection.prepareStatement(sqlStr);
                pst.setInt(1, gameId);
                i = 1;
                if (listParam != null && listParam.size() > 0) {
                    for (Object obj : listParam) {
                        pst.setString(++i, obj.toString());
                    }
                }
                pst.setInt(++i, start);
                pst.setInt(++i, limit);
                ResultSet resultSet = pst.executeQuery();
                ServerTableNotForSq.ServerEntryNotForSq entry = null;
                while ((entry = handler.handle(resultSet)) != null) {
                    list.add(entry);
                }
            }
            listData = new ListData<ServerTableNotForSq.ServerEntryNotForSq>(list, total);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection);
        }
        return listData;
    }

    @Override
    public int bathUpdateStatusNotForSq(List<Integer> list, int status,int gameId) throws Exception{
        int rel = 0;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Connection connection = getQueryRunner().getDataSource().getConnection();
        connection.setAutoCommit(false);
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement( " UPDATE " + TABLE_NOTFORSQ + " SET `status`=? WHERE 1 AND serverId=? and gameId=? ;");
            for (int id : list) {
                pst.setInt(1, status);
                pst.setInt(2, id);
                pst.setInt(3,gameId);
                pst.addBatch();
                log.info("非神曲服务区编号为--- " + id + " ---游戏编号为---"+gameId+"---的状态更新为 " + status);
            }
            rel = pst.executeBatch().length;


        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            pst.close();
            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntriesNotForSq();
            long  getMaintainTime=System.currentTimeMillis();
            if(status==1)
            {
                getMaintainTime+=60000;
            }
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(getMaintainTime));
            return rel;
        }
    }

    @Override
    public int bathUpdateStatusNotForSq(List<Integer> list, int status) throws Exception {
        int rel = 0;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Connection connection = getQueryRunner().getDataSource().getConnection();
        connection.setAutoCommit(false);
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement( " UPDATE " + TABLE_NOTFORSQ + " SET `status`=? WHERE 1 AND id=?  ;");
            for (int id : list) {
                pst.setInt(1, status);
                pst.setInt(2, id);
                pst.addBatch();
                log.info("非神曲服务区编号为--- " + id + "---的状态更新为 " + status);
            }
            rel = pst.executeBatch().length;
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntriesNotForSq();

        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            pst.close();
            DbUtils.closeQuietly(connection);
            return rel;
        }
    }

    @Override
    public int bathUpdateStatusForMaintain(List<GameIdAndServerId> list, int status) throws Exception{
        int rel = 0;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Connection connection = getQueryRunner().getDataSource().getConnection();
        connection.setAutoCommit(false);
        PreparedStatement pst = null,pstNotForSq=null;
        try {
            pst = connection.prepareStatement(" UPDATE " + TABLE + " SET `serverStatus`=? WHERE 1 AND id=? ");
            pstNotForSq = connection.prepareStatement(" UPDATE " + TABLE_NOTFORSQ + " SET `status`=? WHERE serverId=? AND gameId=? ");
            for (GameIdAndServerId entry : list) {
                if(entry.getGameId()==1)
                {
                pst.setInt(1, status);
                pst.setInt(2, entry.getServerId());
                pst.addBatch();
                log.info("神曲服务区id为--- " + entry.getServerId() + "---的状态更新为 " + status);
                }
                if(entry.getGameId()!=1)
                {
                    pstNotForSq.setInt(1,status);
                    pstNotForSq.setInt(2,entry.getServerId());
                    pstNotForSq.setInt(3,entry.getGameId());
                    pstNotForSq.addBatch();
                    log.info("非神曲服务区serverId为---"+entry.getServerId()+"---状态更新为 "+status);
                }
            }
            rel = pst.executeBatch().length+pstNotForSq.executeBatch().length;


        } finally {
            connection.commit();
            connection.setAutoCommit(true);
            pst.close();
            pstNotForSq.close();
            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntriesNotForSq();
            long getMaintainTime= System.currentTimeMillis();
            if(status==1)
            {
                getMaintainTime+=60000;
            }
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(getMaintainTime));
            return rel;
        }
    }


    /**
     * 合区功能
     *
     * @param serverNo 主区的服务区编号
     * @param ids      从区的服务区id数组
     * @return
     */
    @Override
    public boolean mergerServers(String serverNo, int[] ids) {
        boolean flag = false;
        String deleteSql = "   delete from `conf_userplatform`.`conf_server_variables` where serverId=?  ;";
        //插入的sql
        String sql = "   insert into `conf_userplatform`.`conf_server_variables` (serverId,`conf_userplatform`.`conf_server_variables`.`key`,`conf_userplatform`.`conf_server_variables`.`value`) values(?,?,?) ";
        //特殊的配置信息
        Map<String, String> configurationInfo = DataUtil.getServerConfigurations(serverNo);
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(deleteSql);
            for (int id : ids) {
                for (Map.Entry<String, String> entry : configurationInfo.entrySet()) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.addBatch();
                }
            }
            int relult = preparedStatement.executeBatch().length;
            log.info("删除了" + relult + "条已有的配置信息");
            stmt = conn.prepareStatement(sql);
            for (int id : ids) {
                for (Map.Entry<String, String> entry : configurationInfo.entrySet()) {
                    stmt.setInt(1, id);
                    stmt.setString(2, entry.getKey());
                    stmt.setString(3, entry.getValue());
                    stmt.addBatch();
                }
            }
            int rel = stmt.executeBatch().length;
            if (rel > 0) {
                flag = true;
            }
        } catch (final SQLException e) {
            log.error("添加游戏区配置信息异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                conn.setAutoCommit(true);
                preparedStatement.close();
                stmt.close();
                DbUtils.closeQuietly(conn);
            } catch (SQLException e) {
                log.error("提交批量操作异常!");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return flag;
    }

    /**
     * 分区或者取消合区功能
     *
     * @param ids
     * @return
     */
    @Override
    public boolean splitServers(int[] ids) {
        boolean flag = false;
        //删除的sql
        String sql = "   delete  from  `conf_userplatform`.`conf_server_variables` where serverId=? ";
        //特殊的配置信息
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            for (int id : ids) {
                stmt.setInt(1, id);
                stmt.addBatch();
            }
            int rel = stmt.executeBatch().length;
            if (rel > 0) {
                flag = true;
            }
        } catch (final SQLException e) {
            log.error("删除游戏区配置信息异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                conn.setAutoCommit(true);
                stmt.close();
                DbUtils.closeQuietly(conn);
            } catch (SQLException e) {
                log.error("提交批量操作异常!");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return flag;
    }

    /**
     * 从服务区表和服务区配置表中拿到合区的服务区树形结构数据。
     *
     * @return
     */
    @Override
    public List<ZTreeObject> getServerTreeDataStruct() {
        List<ZTreeObject> zTreeObjectList = new ArrayList<ZTreeObject>();
        List<ServerTable.ServerEntry> list = new ArrayList<ServerTable.ServerEntry>();
        Map<Integer, String> map = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //1,拿到account信息
            stmt = conn.prepareStatement(" select * from `conf_userplatform`.`conf_server_table`  where serverStatus<>-1  order by serverNo asc ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerTable.ServerEntry entry = new ServerTable.ServerEntry();
                entry.setId(rs.getInt("id"));
                entry.setServerNo(rs.getInt("serverNo"));
                entry.setServerName(rs.getString("serverName"));
                entry.setServerStatus(rs.getInt("serverStatus"));
                list.add(entry);
            }
            stmt = conn.prepareStatement(" select * from `conf_userplatform`.`conf_server_variables` where `conf_userplatform`.`conf_server_variables`.`key`=? ");
            stmt.setString(1, "sq.getRoleUrl");
            rs = stmt.executeQuery();
            map = new HashMap<Integer, String>();
            while (rs.next()) {
                map.put(rs.getInt("serverId"), rs.getString("value"));
            }
        } catch (final SQLException e) {
            log.error("查询用户信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
            //把有配置的server 的pid改为它的父类ID
            Map<Integer, Integer> serversMap = new HashMap<Integer, Integer>();
            if (list != null && !list.isEmpty()) {
                for (ServerTable.ServerEntry entry : list) {
                    serversMap.put(entry.getServerNo(), entry.getId());
                }
                for (ServerTable.ServerEntry entry : list) {
                    ZTreeObject zTreeObject = new ZTreeObject();
                    zTreeObject.setId(entry.getId());
                    zTreeObject.setpId(0);
//                zTreeObject.setNocheck();
                    zTreeObject.setName(entry.getServerName());
                    //通过配置表 拿到父编号
                    if (map.containsKey(entry.getId())) {
                        String valueStr = map.get(entry.getId());
                        int serverNo = DataUtil.getServerNoFromValueStr(valueStr);
                        if (serverNo > 0 && serversMap.containsKey(serverNo)) {
                            if (serversMap.containsKey(serverNo)) {
                                zTreeObject.setpId(serversMap.get(serverNo));
                                zTreeObject.setNocheck("true");
                            }
                        }
                    }
                    zTreeObjectList.add(zTreeObject);
                }
            }
        }
        return zTreeObjectList;
    }

    /**
     * 从服务区表和服务区配置表中拿到合区的主服务区树形结构数据。
     *
     * @return
     */
    @Override
    public List<ZTreeObject> getServerTreeDataStructOfMain() {
        List<ZTreeObject> zTreeObjectList = new ArrayList<ZTreeObject>();
        List<ServerTable.ServerEntry> list = new ArrayList<ServerTable.ServerEntry>();
        Map<Integer, String> map = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //1,拿到account信息
            stmt = conn.prepareStatement(" select * from `conf_userplatform`.`conf_server_table`  where serverStatus<>-1  order by serverNo asc ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerTable.ServerEntry entry = new ServerTable.ServerEntry();
                entry.setId(rs.getInt("id"));
                entry.setServerNo(rs.getInt("serverNo"));
                entry.setServerName(rs.getString("serverName"));
                entry.setServerStatus(rs.getInt("serverStatus"));
                list.add(entry);
            }
            stmt = conn.prepareStatement(" select * from `conf_userplatform`.`conf_server_variables` where `conf_userplatform`.`conf_server_variables`.`key`=? ");
            stmt.setString(1, "sq.getRoleUrl");
            rs = stmt.executeQuery();
            map = new HashMap<Integer, String>();
            while (rs.next()) {
                map.put(rs.getInt("serverId"), rs.getString("value"));
            }
        } catch (final SQLException e) {
            log.error("查询用户信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
            //把有配置的server 的pid改为它的父类ID
            Map<Integer, Integer> serversMap = new HashMap<Integer, Integer>();
            if (list != null && !list.isEmpty()) {
                for (ServerTable.ServerEntry entry : list) {   //如果没有该区的合区信息，增加到主服务区中。
                    if (!map.containsKey(entry.getId())) {
                        ZTreeObject zTreeObject = new ZTreeObject();
                        zTreeObject.setId(entry.getId());
                        zTreeObject.setpId(0);
                        zTreeObject.setName(entry.getServerName());
                        zTreeObject.setServerNo(String.valueOf(entry.getServerNo()));
                        zTreeObjectList.add(zTreeObject);
                    }
                }
            }
        }
        return zTreeObjectList;
    }

    @Override
    public List<ZTreeObject> getServerTreeDataStructOfCancell() {
        List<ZTreeObject> zTreeObjectList = new ArrayList<ZTreeObject>();
        List<ServerTable.ServerEntry> list = new ArrayList<ServerTable.ServerEntry>();
        Map<Integer, String> map = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //1,拿到account信息
            stmt = conn.prepareStatement(" select * from `conf_userplatform`.`conf_server_table`  where serverStatus<>-1  order by serverNo asc ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerTable.ServerEntry entry = new ServerTable.ServerEntry();
                entry.setId(rs.getInt("id"));
                entry.setServerNo(rs.getInt("serverNo"));
                entry.setServerName(rs.getString("serverName"));
                entry.setServerStatus(rs.getInt("serverStatus"));
                list.add(entry);
            }
            stmt = conn.prepareStatement(" select * from `conf_userplatform`.`conf_server_variables` where `conf_userplatform`.`conf_server_variables`.`key`=? ");
            stmt.setString(1, "sq.getRoleUrl");
            rs = stmt.executeQuery();
            map = new HashMap<Integer, String>();
            while (rs.next()) {
                map.put(rs.getInt("serverId"), rs.getString("value"));
            }
        } catch (final SQLException e) {
            log.error("查询用户信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
            //把有配置的server 的pid改为它的父类ID
            Map<Integer, Integer> serversMap = new HashMap<Integer, Integer>();
            Map<Integer, Integer> serverNos = new HashMap<Integer, Integer>();
            if (list != null && !list.isEmpty()) {
                for (ServerTable.ServerEntry entry : list) {
                    serversMap.put(entry.getServerNo(), entry.getId());
                }
                for (ServerTable.ServerEntry entry : list) {
                    int serverNo = 0;
                    //通过配置表 拿到父编号
                    if (map.containsKey(entry.getId())) {
                        String valueStr = map.get(entry.getId());
                        serverNo = DataUtil.getServerNoFromValueStr(valueStr);
                        if (serverNo > 0) {
                            ZTreeObject zTreeObject = new ZTreeObject();
                            zTreeObject.setId(entry.getId());
                            zTreeObject.setName(entry.getServerName());
                            zTreeObject.setpId(serversMap.get(serverNo));
                            zTreeObjectList.add(zTreeObject);
                            if (!serverNos.containsKey(serverNo)) {
                                serverNos.put(serverNo, serverNo);
                            }
                        }
                    }
                }
                for (ServerTable.ServerEntry entry : list) {
                    if (serverNos.containsKey(entry.getServerNo())) {
                        ZTreeObject zTreeObject = new ZTreeObject();
                        zTreeObject.setId(entry.getId());
                        zTreeObject.setName(entry.getServerName());
                        zTreeObject.setpId(0);
                        zTreeObjectList.add(zTreeObject);
                    }
                }
            }
        }
        return zTreeObjectList;
    }

    /**
     * 全部状态为all的修改为status
     *
     * @param all
     * @param status
     * @return
     * @throws SQLException
     */
    public int batchUpdateServerFromAllToStatus(int all, int status) throws SQLException {
        Connection connection = getQueryRunner().getDataSource().getConnection();
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(SQL_UPDATE_STATUS_BY_STATUS);
            pst.setInt(1, status);
            pst.setInt(2, all);
            int len = pst.executeUpdate();
            log.info("一共有---" + len + "个---服务器的状态由---  " + all + "  ---变为=== " + status);

            return len;
        } finally {
            pst.close();
            DbUtils.closeQuietly(connection);
            //批量的更新reload缓存数据
            cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
        }
    }

    /**
     * 拿到状态不是-1和-3 的服务器列表，就是说已经停用的或者即将开服的，不用维护。
     *
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public Map<Integer, ServerTable.ServerEntry> getNonCloseServers() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<Integer, ServerTable.ServerEntry> map = new HashMap<Integer, ServerTable.ServerEntry>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            String sql = " select * from " + TABLE + " where serverStatus = 1 or serverStatus = -2 order by serverNo asc ";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerTable.ServerEntry serverTableEntry = new ServerTable.ServerEntry();
                serverTableEntry.setId(rs.getInt("id"));
                serverTableEntry.setGameId(rs.getInt("gameId"));
                serverTableEntry.setServerNo(rs.getInt("serverNo"));
                serverTableEntry.setServerName(rs.getString("serverName"));
                serverTableEntry.setServerStatus(rs.getInt("serverStatus"));
                serverTableEntry.setCreateTime(rs.getTimestamp("createTime"));
                serverTableEntry.setOpeningTime(rs.getTimestamp("openingTime"));
                serverTableEntry.setRecommand(rs.getBoolean("recommand"));

                map.put(serverTableEntry.getId(), serverTableEntry);
            }


        } catch (final SQLException e) {
            log.error("获取游戏服务区异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return map;
    }

    @Override
    public Map<Integer, ServerEntity> getNonCloseServerEntities() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<Integer, ServerEntity> map = new HashMap<Integer, ServerEntity>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            final String sql = " select * from " + TABLE + " where serverStatus = 1 or serverStatus = -2 order by serverNo asc ";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerEntity serverTableEntry = new ServerEntity();
                final int serverId = rs.getInt("id");
                serverTableEntry.setId(serverId);
                serverTableEntry.setGameId(rs.getInt("gameId"));
                serverTableEntry.setServerNo(rs.getInt("serverNo"));
                serverTableEntry.setServerName(rs.getString("serverName"));
                serverTableEntry.setServerStatus(rs.getInt("serverStatus"));
                serverTableEntry.setCreateTime(rs.getTimestamp("createTime"));
                serverTableEntry.setOpeningTime(rs.getTimestamp("openingTime"));
                serverTableEntry.setRecommand(rs.getBoolean("recommand"));
                map.put(serverId, serverTableEntry);
            }
        } catch (final SQLException e) {
            log.error("获取游戏服务区异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return map;
    }

    @Override
    public Map<Integer, ServerEntity> getAllServers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<Integer, ServerEntity> map = new HashMap<Integer, ServerEntity>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            String sql = " select * from " + TABLE + " order by serverNo asc ";
            stmt = conn.prepareStatement(sql);
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
                map.put(serverTableEntry.getId(), serverTableEntry);
            }

        } catch (final SQLException e) {
            log.error("获取所有游戏服务区异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return map;
    }

    public ServerTable.ServerEntry getServerEntryById(int id) {
        try {
            ServerTable.ServerEntry serverEntry = getQueryRunner().query(SQL_GET_SERVERENTRY_BY_ID, new BeanHandler<ServerTable.ServerEntry>(ServerTable.ServerEntry.class), id);
            return serverEntry;
        } catch (SQLException e) {
            log.error("通过ID获取游戏服务区异常：{}", e.getMessage());
        }
        return null;
    }

}
