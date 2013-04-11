/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.VerifyCodeDao;
import com.sz7road.userplatform.pojos.VerifyCode;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.*;
import java.util.List;

/**
 * @author leo.liao
 */


class VerifyCodeDaoJdbcImpl extends JdbcDaoSupport<VerifyCode> implements VerifyCodeDao {

    static final String TABLE = "dt_verify_code";
    static final String SQL_BASE = "SELECT * FROM `" + TABLE + "` WHERE 1";
    static final String SQL_GETBY_VERIFY = SQL_BASE + " AND `verify`=?";

    static final String SQL_ADD = "INSERT INTO `" + TABLE + "`(`verify`,`code`,`expiryTime`) VALUES(?,?,?)";
    static final String SQL_DELETE = "DELETE FROM `" + TABLE + "` WHERE 1 " + " AND `id` = ?";

    static final String SQL_DELETE_BY_EXPIRYTIME = "DELETE FROM `" + TABLE + "` WHERE 1 " + " AND `expiryTime` < ?";

    @Override
    public int add(VerifyCode entity) throws SQLException {
        if (null == entity) {
            throw new NullPointerException("null verify code");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, entity.getVerify());
            stmt.setString(2, entity.getCode());
            stmt.setLong(3, entity.getExpiryTime());

            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (final SQLException e) {
            log.error("添加用户验证码异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return 0;
    }

    @Override
    public List<VerifyCode> getByVerify(String verify) {
        try {
            return getQueryRunner().query(SQL_GETBY_VERIFY, new BeanListHandler<VerifyCode>(VerifyCode.class), verify);
        } catch (final Exception e) {
            log.error("查询验证码数据异常：{}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int delete(VerifyCode entity) {
        if (null == entity) {
            throw new NullPointerException("null verify code");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);

            stmt.setInt(1, entity.getId());

            stmt.executeUpdate();
            return 1;
        } catch (final SQLException e) {
            log.error("删除验证码信息异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return 0;
    }

    public int deleteByExpiryTime(long expiryTime) {
        if (expiryTime <= 0) {
            throw new NullPointerException("null verify code");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_BY_EXPIRYTIME);

            stmt.setLong(1, expiryTime);

            int ret = stmt.executeUpdate();
            return ret;
        } catch (final SQLException e) {
            log.error("清理验证码缓存信息异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return 0;
    }
}
