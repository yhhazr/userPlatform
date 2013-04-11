package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.FilterCharacterDao;
import com.sz7road.userplatform.pojos.FilterCharacter;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.*;
import java.util.List;

/**
 * @author leo.liao
 */


class FilterCharacterDaoJdbcImpl extends JdbcDaoSupport<FilterCharacter> implements FilterCharacterDao {

    static final String TABLE = "`conf_userplatform`.`conf_filter_character`";
    static final String SQL_BASE = "SELECT * FROM " + TABLE + " WHERE 1";
    private static final String SQL_LIST_ALL = "SELECT * FROM " + TABLE + " WHERE 1";
    static final String SQL_ADD = "INSERT INTO " + TABLE + "(`content`) VALUES(?)";

    @Override
    public int add(FilterCharacter entity) throws SQLException {
        if (null == entity) {
            throw new NullPointerException("null filter character");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, entity.getContent());

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
    public List<FilterCharacter> listAll() throws SQLException {
        return getQueryRunner().query(SQL_LIST_ALL, new BeanListHandler<FilterCharacter>(FilterCharacter.class));
    }
}
