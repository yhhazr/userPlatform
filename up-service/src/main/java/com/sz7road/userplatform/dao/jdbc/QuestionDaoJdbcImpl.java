package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.QuestionDao;
import com.sz7road.userplatform.pojos.Question;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Create on: 12-8-22, 下午2:08
 * Author: jiangfan.zhou
 */
public class QuestionDaoJdbcImpl extends JdbcDaoSupport<Question> implements QuestionDao{

    private static final Logger log = LoggerFactory.getLogger(QuestionDaoJdbcImpl.class.getName());

    static final String TABLE = "`db_userplatform`.`dt_question`";
    static final String SQL_BASE = "SELECT * FROM " + TABLE + " WHERE 1";

    static final String SQL_GETBY_ID = SQL_BASE + " AND id = ?";
    static final String SQL_GETBY_USER_ID = SQL_BASE + " AND status = ? AND userId = ? ORDER BY id DESC LIMIT 0,?";
    static final String SQL_GETBY_FIELD = SQL_BASE + " AND `%s` = ?";

    static final String SQL_ADD = "INSERT INTO " + TABLE + "(`userId`,`question`,`answer`,`status`,`addTime`) VALUES(?,?,?,?,?)";

    static final String SQL_UPDATE_STATUS = "UPDATE " + TABLE + " SET status = ? WHERE userId = ?";

    public Question get(int id) {
        try {
            return getQueryRunner().query(SQL_GETBY_ID, new BeanHandler<Question>(Question.class), id);
        } catch (final SQLException e) {
            log.error("查询密保问题答案信息异常：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<Question> get(String property, Object value) throws SQLException {
        List<Question> list = getQueryRunner().query(formatSql(SQL_GETBY_FIELD, property),
                new BeanListHandler<Question>(Question.class), value);
        return list;
    }

    private String formatSql(String sql, String property) {
        return String.format(sql, property);
    }

    @Override
    public List<Question> getByUserId(int userId, int status, int n) throws SQLException{
        return getQueryRunner().query(SQL_GETBY_USER_ID, new BeanListHandler<Question>(Question.class),status, userId, n);
    }

    @Override
    public int add(Question question) throws SQLException {
        return getQueryRunner().update(SQL_ADD, question.getUserId(), question.getQuestion(), question.getAnswer(), question.getStatus(), question.getAddTime());
    }

    @Override
    public int add(Question... entities) {
        if(entities == null){
            throw new NullPointerException("null object");
        }

        Connection connection = null;
        PreparedStatement stmt = null;

        try{
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(SQL_ADD);
            for(Question entity : entities){
                stmt.setInt(1, entity.getUserId());
                stmt.setString(2, entity.getQuestion());
                stmt.setString(3,entity.getAnswer());
                stmt.setInt(4, entity.getStatus());
                stmt.setTimestamp(5, entity.getAddTime());
                stmt.addBatch();
            }
            return stmt.executeBatch().length;
        } catch (SQLException e){
            log.error("批量添加密保问题答案失败：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(connection, stmt, null);
        }
        return 0;
    }

    /*@Override
    public int updateStatus(int userId, int status){
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            return queryRunner.update(connection, SQL_UPDATE_STATUS, status, userId);
        } catch (SQLException e) {
            log.error("更新密保问题答案状态失败：{}", e.getMessage());
        } finally {
            DbUtils.commitAndCloseQuietly(connection);
        }
        return 0;
    }*/

    @Override
    /**
     * InnoDB 支持事物
     */
    public int addAndUpdateStatus(int userId, int status, Question... entities){
        if(entities == null){
            throw new NullPointerException("null object");
        }

        Connection connection = null;
        PreparedStatement stmt = null;

        try{
            connection = getQueryRunner().getDataSource().getConnection();
            //connection.setAutoCommit(false);
            stmt = connection.prepareStatement(SQL_UPDATE_STATUS);
            stmt.setInt(1, status);
            stmt.setInt(2, userId);
            stmt.execute();

            stmt = connection.prepareStatement(SQL_ADD);
            log.debug(SQL_ADD);
            for(Question entity : entities){
                stmt.setInt(1, entity.getUserId());
                stmt.setString(2,entity.getQuestion());
                stmt.setString(3,entity.getAnswer());
                stmt.setInt(4,entity.getStatus());
                stmt.setTimestamp(5,entity.getAddTime());
                stmt.addBatch();
            }
            return stmt.executeBatch().length;
        } catch (SQLException e){
            log.error("批量添加密保问题答案失败：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(connection, stmt, null);
        }
        return 0;
    }

    @Override
    public int updateStatus(Connection conn, int userId, int status) throws SQLException {
        return getQueryRunner().update(conn, SQL_UPDATE_STATUS, status, userId);
    }
}
