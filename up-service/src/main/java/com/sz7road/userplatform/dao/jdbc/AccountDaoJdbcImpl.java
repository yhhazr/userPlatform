/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.AccountDao;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.utils.RuleUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * 用户账户数据DAO，用于验证用户账号的存在性、登录验证及注册账号等场景，仅是作为平台入口数据。
 *
 * @author jeremy
 */
class AccountDaoJdbcImpl extends JdbcDaoSupport<UserAccount> implements AccountDao {

    private static final Logger log = LoggerFactory.getLogger(AccountDaoJdbcImpl.class.getName());

    static final String TABLE = "db_userplatform`.`dt_account";
    static final String SQL_BASE = "SELECT * FROM `" + TABLE + "` WHERE 1";
    static final String SQL_GETBY_USERNAME = SQL_BASE + " AND `userName`=?";
    static final String SQL_GETBY_UAP = SQL_GETBY_USERNAME + " AND `passWord`=? LIMIT 1";

    static final String SQL_ADD = "INSERT INTO `" + TABLE + "`(`userName`,`passWord`,`email`) VALUES(?,?,?)";

    static final String SQL_ADD_REWRITE = "INSERT INTO `" + TABLE + "`(`userName`,`passWord`) VALUES(?,?)";

    static final String SQL_UPDATE_EMAIL = "UPDATE `" + TABLE + "` SET email = ? WHERE userName = ?";
    static final String SQL_UPDATE_EMAIL_BY_ID = "UPDATE `" + TABLE + "` SET email = ? WHERE id = ?";
    static final String SQL_MODIFY_PASSWORD = "UPDATE `" + TABLE + "` SET password = ? WHERE userName = ?";

    static final String SQL_MODIFY_IPLOG = "UPDATE `" + TABLE + "` SET password = ? WHERE userName = ?";

    static final String SQL_GETBY_ID = SQL_BASE + " AND `id`=?";
    static final String SQL_GET_ACCOUNT_BY_FIELD = SQL_BASE + " AND `%s` = ?";
    static final String SQL_GET_ACCOUNT_BY_EMAIL = SQL_BASE + " AND `email`=?";

    @Override
    public UserAccount get(String userName) {
        try {
            return getQueryRunner().query(SQL_GETBY_USERNAME, new BeanHandler<UserAccount>(UserAccount.class), userName);
        } catch (final SQLException e) {
            log.error("查询用户账号信息异常：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public UserAccount get(String userName, String passWord) {
        try {
            return getQueryRunner().query(SQL_GETBY_UAP, new BeanHandler<UserAccount>(UserAccount.class), userName, passWord);
        } catch (final SQLException e) {
            log.error("查询用户账号信息异常：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public int add(final UserAccount account) {
        if (null == account) {
            throw new NullPointerException("null account");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassWord());
            stmt.setString(3, account.getEmail());

            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (final SQLException e) {
            log.error("添加用户账号信息异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return 0;
    }

    @Override
    public int add(Connection conn, final UserAccount account) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassWord());
            stmt.setString(3, account.getEmail());

            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    return rs.getInt(1);
                }
            }
        } finally {
            DbUtils.closeQuietly(null, stmt, rs);
        }
        return 0;
    }

    @Override
    public int addReWrite(final UserAccount account) {
        if (null == account) {
            throw new NullPointerException("null account");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_ADD_REWRITE, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassWord());

            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (final SQLException e) {
            log.error("添加用户账号信息异常：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return 0;
    }

    @Override
    public int updateEmail(UserAccount entity) {
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            queryRunner.update(connection, SQL_UPDATE_EMAIL, entity.getEmail(), entity.getUserName());
            return 1;
        } catch (SQLException e) {
            log.error("更新账户邮箱失败：{}", e.getMessage());
        } finally {
            DbUtils.commitAndCloseQuietly(connection);
        }
        return 0;
    }

    @Override
    public int updateEmail(Connection conn, int userId, String email) throws SQLException {
        return  getQueryRunner().update(conn, SQL_UPDATE_EMAIL_BY_ID, email, userId);
    }

    @Override
    public int modifyPassword(UserAccount entity) {
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            queryRunner.update(connection, SQL_MODIFY_PASSWORD, entity.getPassWord(), entity.getUserName());
            return 1;
        } catch (SQLException e) {
            log.error("更新账户密码失败：{}", e.getMessage());
        } finally {
            DbUtils.commitAndCloseQuietly(connection);
        }
        return 0;
    }

    @Override
    public UserAccount getById(int userId) throws SQLException {
        return getQueryRunner().query(SQL_GETBY_ID, new BeanHandler<UserAccount>(UserAccount.class), userId);
    }

    private String formatSql(String sql, String property) {
        String SQL = String.format(sql, property);
        return SQL;
    }

    @Override
    public List<UserAccount> get(String property, Object value) throws SQLException {
        List<UserAccount> list = getQueryRunner().query(formatSql(SQL_GET_ACCOUNT_BY_FIELD, property), new BeanListHandler<UserAccount>(UserAccount.class), value);
        return list;
    }

    @Override
    public List<UserAccount> getByEmail(String email) throws SQLException {
        return getQueryRunner().query(SQL_GET_ACCOUNT_BY_EMAIL, new BeanListHandler<UserAccount>(UserAccount.class), email);
    }
}
