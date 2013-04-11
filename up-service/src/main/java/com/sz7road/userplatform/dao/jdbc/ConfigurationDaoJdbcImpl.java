/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
package com.sz7road.userplatform.dao.jdbc;

import com.google.common.collect.Maps;
import com.sz7road.userplatform.dao.ConfigurationDao;
import com.sz7road.userplatform.pojos.Configuration;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author leo.liao
 */

class ConfigurationDaoJdbcImpl extends JdbcDaoSupport<Configuration> implements ConfigurationDao {

    private static final String SCHEMA = "conf_userplatform";
    private static final String TABLE = "conf_system_variables";
    private static final String SQL_LIST_ALL = " SELECT * FROM `" + SCHEMA + "`.`" + TABLE + "` WHERE 1";
    private static final String SQL_CHECK_UPDATE = " SELECT `UPDATE_TIME` FROM `information_schema`.`TABLES` WHERE 1 AND `table_schema`=? AND `table_name`=? LIMIT 1";
    private static final String SQL_UPDATE = "REPLACE INTO`" + SCHEMA + "`.`" + TABLE + "`(`key`, `value`) VALUES(?,?)";
    private static final String SQL_UPDATE_SCHEMA = "UPDATE `" + SCHEMA + "`.`" + TABLE + "` SET `value` = `value` + 1 WHERE 1 AND `key`=?";

    @Override
    public long getLastModifiedTime() throws SQLException {
        long updateTime=0L;
        try{
         updateTime= getQueryRunner().query(SQL_CHECK_UPDATE, new ResultSetHandler<Long>() {
            @Override
            public Long handle(ResultSet rs) throws SQLException {
                long time = Long.MIN_VALUE;
                if (rs.next()) {
                    time = rs.getTimestamp(1).getTime();
                }
                return time;
            }
        }, SCHEMA, TABLE);

        }catch (Exception ex)
        {
            log.info("查询数据库配置表的修改时间出现异常!");
          ex.printStackTrace();
        }
        finally {

        }
        return  updateTime;
    }

    public void updateTableTimestamp() throws SQLException {
        getQueryRunner().update(SQL_UPDATE_SCHEMA, "just4update");
    }

    @Override
    public List<Configuration> listAsConfiguration() throws SQLException {
        return getQueryRunner().query(SQL_LIST_ALL, new BeanListHandler<Configuration>(Configuration.class));
    }

    @Override
    public Map<String, Object> listAsMap() throws SQLException {
        return getQueryRunner().query(SQL_LIST_ALL, new ResultSetHandler<Map<String, Object>>() {
            @Override
            public Map<String, Object> handle(final ResultSet rs) throws SQLException {
                final Map<String, Object> result = Maps.newHashMap();
                while (rs.next()) {
                    result.put(rs.getString("key"), rs.getString("value"));
                }
                return result;
            }
        });
    }

    @Override
    public int batchUpdate(Properties configuration) throws SQLException {
        final QueryRunner runner = getQueryRunner();
        final Connection conn = runner.getDataSource().getConnection();
        try {
            conn.setAutoCommit(false);
            final PreparedStatement ps = conn.prepareStatement(SQL_UPDATE);
            for (Object key : configuration.keySet()) {
                ps.setString(1, key.toString());
                ps.setString(2, configuration.getProperty(key.toString()));
                ps.addBatch();
            }
            return ps.executeBatch().length;
        } finally {
            conn.commit();
            conn.setAutoCommit(true);
            DbUtils.closeQuietly(conn);
        }
    }

}
