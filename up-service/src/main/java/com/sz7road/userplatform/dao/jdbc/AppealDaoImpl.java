/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.dao.AppealDao;
import com.sz7road.userplatform.pojos.Appeal;
import com.sz7road.userplatform.pojos.JqObject;
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
public class AppealDaoImpl extends JdbcDaoSupport<Appeal> implements AppealDao {

    private static final String TABLE = "`db_userplatform`.`dt_appeal`";
    private static final String SQL_SELECT_BASE = "SELECT * FROM " + TABLE + " WHERE 1";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE + " WHERE 1 AND ID = ?";
    private static final String SQL_SELECT_BY_FIELD = SQL_SELECT_BASE + " AND %s = ?";
    private static final String SQL_QUERY = "SELECT * FROM " + TABLE + " WHERE 1";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM " + TABLE + " WHERE 1";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE + "(`userId`, `userName`, `realName`, " +
            "`idCard`,`oftenPlayGame`, `serverName`, `email`, `playerName`, `playerLevel`, " +
            "`createDate`, `createCity`, `exceptionDate`, `lastLoginDate`, `pay`, `orderIds`, " +
            "`idCardImgPath`, `otherInfo`, `gainPoints`, `status`, `auditor`, `auditorTime`, `appealTime`) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_UPDATE = "UPDATE " + TABLE + " SET `userId`=?,`userName`=?, `realName`=?,`idCard`=?,`oftenPlayGame`=?, `serverName`=?, `email`=?, `playerName`=?, `playerLevel`=?,`createDate`=?, `createCity`=?, `exceptionDate`=?, `lastLoginDate`=?, `pay`=?, `orderIds`=?,`idCardImgPath`=?, `otherInfo`=?, `gainPoints`=?, `status`=?, `auditor`=?, `auditorTime`=?, `appealTime`=? WHERE `id`=?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE + " WHERE `id` = ?";

    @Override
    public int add(Appeal entity) throws SQLException {
        return getQueryRunner().update(SQL_INSERT,entity.getUserId(), entity.getUserName(), entity.getRealName(), entity.getIdCard(),
                entity.getOftenPlayGame(), entity.getServerName(), entity.getEmail(), entity.getPlayerName(), entity.getPlayerLevel(),
                entity.getCreateDate(), entity.getCreateCity(), entity.getExceptionDate(), entity.getLastLoginDate(), entity.getPay(), entity.getOrderIds(),
                entity.getIdCardImgPath(), entity.getOtherInfo(), entity.getGainPoints(), entity.getStatus(), entity.getAuditor(), entity.getAuditorTime(), entity.getAppealTime());
    }

    @Override
    public List<Appeal> get(String property, Object value) throws SQLException {
        List<Appeal> list = getQueryRunner().query(String.format(SQL_SELECT_BY_FIELD, property), new BeanListHandler<Appeal>(Appeal.class), value);
        return list;
    }

    @Override
    public Appeal get(int id) throws SQLException {
        return getQueryRunner().query(SQL_SELECT_BY_ID, new BeanHandler<Appeal>(Appeal.class), id);
    }

    @Override
    public Appeal get(Connection conn, int id) throws SQLException {
        return getQueryRunner().query(conn, SQL_SELECT_BY_ID, new BeanHandler<Appeal>(Appeal.class), id);
    }

    public int update(Connection conn, Appeal entity) throws SQLException {
        return getQueryRunner().update(conn, SQL_UPDATE, entity.getUserId(), entity.getUserName(),entity.getRealName(),entity.getIdCard(),
                entity.getOftenPlayGame(),entity.getServerName(),entity.getEmail(),
                entity.getPlayerName(),entity.getPlayerLevel(),entity.getCreateDate(),entity.getCreateCity(),
                entity.getExceptionDate(),entity.getLastLoginDate(),entity.getPay(),entity.getOrderIds(),
                entity.getIdCardImgPath(),entity.getOtherInfo(),entity.getGainPoints(),
                entity.getStatus(),entity.getAuditor(),entity.getAuditorTime(),entity.getAppealTime(), entity.getId());
    }

    @Override
    public int delete(Appeal entity) throws SQLException {
        return getQueryRunner().update(SQL_DELETE, entity.getId());
    }

    @Override
    public Appeal updateStatus(int appealId, int status, String auditor) throws SQLException {
        Connection conn = null;
        try{
            conn = getQueryRunner().getDataSource().getConnection();
            Appeal entity = get(conn, appealId);
            if (entity == null)
                throw new NullPointerException("没有找到申诉信息");

            entity.setStatus(status);
            entity.setAuditor(auditor);
            entity.setAuditorTime(new Timestamp(System.currentTimeMillis()));
            if(update(conn, entity) > 0)
                return entity;
            return null;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    @Override
    public Appeal updateStatus(Connection conn, int appealId, int status, String auditor) throws SQLException {
        Appeal entity = getQueryRunner().query(conn , SQL_SELECT_BY_ID, new BeanHandler<Appeal>(Appeal.class), appealId);
        if (entity == null)
            throw new NullPointerException("没有找到申诉信息");

        entity.setStatus(status);
        entity.setAuditor(auditor);
        entity.setAuditorTime(new Timestamp(System.currentTimeMillis()));

        if(update(conn, entity) == 0)
            throw new NullPointerException("更新申诉信息失败");
        return entity;
    }

    @Override
    public Pager<Appeal> query(String userName, int pay, int status, Date fromDate, Date toDate, int start, int limit) throws SQLException {
        Pager<Appeal> pager = new Pager<Appeal>();
        List<Appeal> list = null;
        int total = 0;
        Connection conn = null;
        try{
            List<Object> cList = new ArrayList<Object>();
            String strWhere = "";
            if (userName != null && !userName.equals("")) {
                strWhere = strWhere + " AND userName=?";
                cList.add(userName);
            }
            if (pay != -1) {
                strWhere = strWhere + " AND pay=?";
                cList.add(pay);
            }
            if (status != -1) {
                strWhere = strWhere + " AND status=?";
                cList.add(status);
            }
            if (fromDate != null) {
                strWhere = strWhere + " AND appealTime>?";
                cList.add(fromDate);
            }
            if (toDate != null) {
                strWhere = strWhere + " AND appealTime<?";
                cList.add(toDate);
            }
            conn = getQueryRunner().getDataSource().getConnection();
            total = count(conn, SQL_COUNT + strWhere, cList.toArray()).intValue();
            cList.add(start);
            cList.add(limit);
            list = getQueryRunner().query(conn, SQL_QUERY + strWhere + " ORDER BY id DESC LIMIT ?,?", new BeanListHandler<Appeal>(Appeal.class), cList.toArray());

            pager.setRows(list);
            pager.setRecords(total);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return pager;
    }

    public Pager<Appeal> _query(String sql, Object ...params) throws SQLException {
        Pager<Appeal> pager = new Pager<Appeal>();
        List<Appeal> list = null;
        int total = 0;
        Connection conn = null;
        try{
            conn = getQueryRunner().getDataSource().getConnection();
            if (params == null) {
                list = getQueryRunner().query(conn, SQL_SELECT_BASE, new BeanListHandler<Appeal>(Appeal.class));
                total = count(conn, SQL_COUNT).intValue();
            } else {
                list = getQueryRunner().query(conn, String.format(SQL_QUERY + " %s", sql), new BeanListHandler<Appeal>(Appeal.class), params);
                total = count(conn, String.format(SQL_COUNT + " %s", sql), params).intValue();
            }
            pager.setRows(list);
            pager.setRecords(total);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return pager;
    }

    private ScalarHandler scalarHandler = new ScalarHandler() {
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            Object obj = super.handle(rs);
            if (obj instanceof BigInteger)
                return ((BigInteger) obj).longValue();
            return obj;
        }
    };

    public Number count(Connection conn, String sql, Object... params) throws SQLException{
        Number total = 0;
        if (params == null) {
            total = (Number)getQueryRunner().query(conn, sql, scalarHandler);
        } else {
            total = (Number)getQueryRunner().query(conn, sql, scalarHandler, params);
        }
        return total;
    }
}
