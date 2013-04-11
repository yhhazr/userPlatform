package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.sz7road.userplatform.dao.UserManagerDao;
import com.sz7road.userplatform.pojo.UserInfoObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.RowObject;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.pojos.User;
import com.sz7road.web.utils.DataUtil;
import com.sz7road.web.utils.ServletUtil;
import com.sz7road.web.utils.TimeStampUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 下午2:37
 */
class UserManagerDaoJdbcImpl extends JdbcDaoSupport<User> implements UserManagerDao {

    static final String TABLE = "`db_usermanager`.`tm_user`";
    static final String SQL_BASE = "SELECT * FROM " + TABLE + " WHERE 1";
    static final String SQL_GETBY_USERNAME = SQL_BASE + " AND `username`=?";
    static final String SQL_GETBY_UAP = SQL_GETBY_USERNAME + " AND `password`=? LIMIT 1";


    static final String SQL_GETACCUNTBYID = " select *  from " + " `db_userplatform`.`dt_account` " + "  where id=?  ";

    static final String SQL_GETACCUNTBYNAME = " select *  from " + " `db_userplatform`.`dt_account` " + "  where userName=?  ";

    static final String SQL_ADD = "INSERT INTO " + TABLE + "(`username`,`password`,`createTime`) VALUES(?,?,?)";
    static final String SQL_UPDATE = "UPDATE " + TABLE + "SET password=?" + "WHERE username=?";

    static final String SQL_UPDATEPSW = " UPDATE  `db_userplatform`.`dt_account` SET password=? WHERE id=? ";

    static final String SQL_UPDATEEMAIL = "UPDATE  `db_userplatform`.`dt_account` SET email=? WHERE id=? ";

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    @Override
    public User get(String userName) {
        try {
            return getQueryRunner().query(SQL_GETBY_USERNAME, new BeanHandler<User>(User.class), userName);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User get(String userName, String passWord) {
        try {
            return getQueryRunner().query(SQL_GETBY_UAP, new BeanHandler<User>(User.class), userName, passWord);
        } catch (final SQLException e) {

        }
        return null;
    }

    @Override
    public int add(User user) {
        if (null == user) {
            throw new NullPointerException("null user");
        }


        try {
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setTimestamp(3, user.getCreateTime());

            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return 0;
    }

    @Override
    public List<User> listAll() throws SQLException {
        List<User> users = new ArrayList<User>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "SELECT * FROM " + TABLE;

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRealName(rs.getString("realName"));
                user.setPassword(rs.getString("password"));
                user.setCreateTime(rs.getTimestamp("createTime"));
                user.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
                users.add(user);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return users;
    }

    @Override
    public int updateLastLoginTime(User entity) {
        int resultInt = 0;
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "update " + TABLE + " set lastLoginTime=? where id=?";

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setTimestamp(1, entity.getLastLoginTime());
            stmt.setInt(2, entity.getId());


            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    resultInt = rs.getInt(1);
                }
            }

        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return resultInt;

    }

    @Override
    public int delete(User entity) throws SQLException {

        int resultInt = 0;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            String sql = "delete  from " + TABLE + " where id=?";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, entity.getId());
            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    resultInt = rs.getInt(1);
                }
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return resultInt;


    }

    public int updatePwd(final User user) {
        if (user == null) {
            throw new NullPointerException("user null");
        }
        try {
            return getQueryRunner().update(SQL_UPDATE, user.getPassword(), user.getUsername());
        } catch (Exception e) {
            log.error("修改密码出现异常{}", e.getMessage());
        }
        return 0;
    }

    public int resetPwd(int id, String password, String userName) {

        return resetPwdOrEmail(id, password, userName, "psw");
    }


    private int resetPwdOrEmail(int id, String data, String operator, String type) {
        int rel = 0;
        String userName = null;
        UserInfoObject userInfoObject = getAccountById(id);
        if (userInfoObject != null) {
            userName = userInfoObject.getUserName();
        }
        if (Strings.isNullOrEmpty(type)) {
            throw new NullPointerException("type 参数为空!");
        } else {
            StringBuffer updateUserData = null;
            StringBuilder insertSql = null;
            Connection connection = null;
            Statement stmt = null;

            if ("psw".equals(type)) {
                //修改用户信息的sql
                updateUserData = new StringBuffer(" UPDATE  `db_userplatform`.`dt_account` SET password='").
                        append(MD5Utils.password(data)).append("' WHERE id= ").append(id);

                //修改日志的sql
                insertSql =
                        new StringBuilder(" insert into  `log_userplatform`.`").append("log_operation_").
                                append(TimeStampUtil.fromTimeStampToSimpleStringDate(new Timestamp(new Date().getTime()))).
                                append("`(logType,log_time,userName,content) values(").append(1).append(",'").
                                append(new Timestamp(new Date().getTime())).append("','").append(userName).append("','").
                                append("运营人员：" + operator + " 重置密码为：" + data).append("');");
            } else if ("email".equals(type)) {
                //修改用户信息的sql
                updateUserData = new StringBuffer(" UPDATE  `db_userplatform`.`dt_account` SET email='").
                        append(data).append("' WHERE id= ").append(id);

                //修改日志的sql
                insertSql =
                        new StringBuilder(" insert into  `log_userplatform`.`").append("log_operation_").
                                append(TimeStampUtil.fromTimeStampToSimpleStringDate(new Timestamp(new Date().getTime()))).
                                append("`(logType,log_time,userName,content) values(").append(1).append(",'").
                                append(new Timestamp(new Date().getTime())).append("','").append(userName).append("','").
                                append("运营人员：" + operator + " 重置邮箱为：" + data).append("');");
            }


            try {
                connection = getQueryRunner().getDataSource().getConnection();
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.addBatch(updateUserData.toString());
                stmt.addBatch(insertSql.toString());
                rel = stmt.executeBatch().length;
            } catch (Exception ex) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    log.error("回滚异常!");
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                log.error("修改密码或者邮箱异常：" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try {
                    connection.commit();
                    connection.setAutoCommit(true);
                    DbUtils.closeQuietly(stmt);
                    DbUtils.closeQuietly(connection);
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return rel;
    }


    public int addOperationLog(int id, String content) {
        int rel = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            StringBuilder insertSql = new StringBuilder(" insert into  `log_userplatform`.`");

            UserInfoObject userInfoObject = getAccountById(id);

            if (userInfoObject != null && userInfoObject.getUserName() != null) {

                insertSql.append("log_operation_").
                        append(TimeStampUtil.fromTimeStampToSimpleStringDate(new Timestamp(new Date().getTime()))).
                        append("`(logType,log_time,userName,content) values(?,?,?,?)");
            }
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(insertSql.toString());

            stmt.setInt(1, 1);
            stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            stmt.setString(3, userInfoObject.getUserName());
            stmt.setString(4, content);

            rel = stmt.executeUpdate();

        } catch (final SQLException e) {
            log.error("插入用户日志操作日志！");
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            DbUtils.closeQuietly(conn);
        }
        return rel;
    }

    private int addOperationLogByUserName(String userName, String content) {
        int rel = 0;
        PreparedStatement stmt = null;
        try {
            StringBuilder insertSql =
                    new StringBuilder(" insert into  `log_userplatform`.`").append("log_operation_").
                            append(TimeStampUtil.fromTimeStampToSimpleStringDate(new Timestamp(new Date().getTime()))).
                            append("`(logType,log_time,userName,content) values(?,?,?,?)");
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(insertSql.toString());

            stmt.setInt(1, 1);
            stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            stmt.setString(3, userName);
            stmt.setString(4, content);
            rel = stmt.executeUpdate();
        } catch (final SQLException e) {
            log.error("插入用户日志操作日志！");
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            DbUtils.closeQuietly(conn);
        }
        return rel;
    }
    public int resetEmail(int id, String email, String userName) {

        return resetPwdOrEmail(id, email, userName, "email");
    }

    /**
     * 根据用户Id拿到用户信息
     *
     * @param id
     * @return
     */
    @Override
    public UserInfoObject getAccountById(int id) {
        UserInfoObject userInfoObject = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //1,拿到account信息
            stmt = conn.prepareStatement(SQL_GETACCUNTBYID);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userInfoObject = new UserInfoObject();
                userInfoObject.setId(id);
                userInfoObject.setUserName(rs.getString("userName"));
                userInfoObject.setEmail(rs.getString("email"));
                userInfoObject.setStatus(rs.getInt("status") == 0 ? "正常" : "停止");
            }
            StringBuilder stringBuilder = new StringBuilder(" select * from  `db_userplatform`.`");
            stringBuilder.append(TableLocator.getUserTable(id)).append("`  where id=? ");
            //2,拿到user信息
            stmt = conn.prepareStatement(stringBuilder.toString());
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                userInfoObject.setCreateTime(rs.getTimestamp("createTime"));
                userInfoObject.setLastIp(rs.getString("lastIp"));
                userInfoObject.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
                userInfoObject.setLoginSum(rs.getInt("loginSum") + 1);
            }
        } catch (final SQLException e) {
            log.error("查询用户信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return userInfoObject;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param name
     * @return
     */
    @Override
    public List<UserInfoObject> getAccountByName(String name) {
        List<UserInfoObject> userInfoObjectList = new LinkedList<UserInfoObject>();

        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //1,拿到account信息
            stmt = conn.prepareStatement(SQL_GETACCUNTBYNAME);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserInfoObject userInfoObject = new UserInfoObject();
                int id = rs.getInt("id");
                userInfoObject.setId(id);
                userInfoObject.setUserName(rs.getString("userName"));
                userInfoObject.setEmail(rs.getString("email"));
                userInfoObject.setStatus(rs.getInt("status") == 0 ? "正常" : "停止");
                //2,拿到user信息
                StringBuilder stringBuilder = new StringBuilder(" select * from  `db_userplatform`.`");
                stringBuilder.append(TableLocator.getUserTable(id)).append("`  where id=? ");
                PreparedStatement preparedStatement = conn.prepareStatement(stringBuilder.toString());
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userInfoObject.setCreateTime(resultSet.getTimestamp("createTime"));
                    userInfoObject.setLastIp(resultSet.getString("lastIp"));
                    userInfoObject.setLastLoginTime(resultSet.getTimestamp("lastLoginTime"));
                    userInfoObject.setLoginSum(resultSet.getInt("loginSum") + 1);
                }
                userInfoObjectList.add(userInfoObject);
            }
        } catch (final SQLException e) {
            log.error("查询用户信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return userInfoObjectList;
    }

    /**
     * 按照用户名查询资料修改记录
     */
    @Override
    public JqObject getProfileHistoryByName(String userName, int start, int pageSize) {
        JqObject jqObject = new JqObject();
        List<RowObject> rows = new ArrayList<RowObject>();
        try {
            List<UserInfoObject> infoObjectList = getAccountByName(userName);
            //从数据库里拿出日志表的sql
            String getLogTablesSql = "SELECT table_name FROM information_schema.TABLES  " + "where table_schema = 'log_userplatform' and table_name like 'log_operation_%' GROUP BY table_name desc ";
            //1,拿到table信息
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(getLogTablesSql);
            ResultSet rs = stmt.executeQuery();
            List<String> tables = new ArrayList<String>();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            //2，根据用户的注册日期，查询日志表。得到日志记录集合。
            for (UserInfoObject userInfoObject : infoObjectList) {
                int createTime = Integer.parseInt(TimeStampUtil.fromTimeStampToSimpleStringDate(userInfoObject.getCreateTime()));
                for (String table : tables) {
                    int tableTime = Integer.parseInt(table.substring("log_operation_".length(), table.length()));
                    if (tableTime >= createTime) {
                        StringBuilder stringBuilder = new StringBuilder(" select * from  `log_userplatform`.`");
                        stringBuilder.append(table).append("`  where userName=? order by log_time desc ");
                        //3,拿到log信息
                        PreparedStatement preparedStatement = conn.prepareStatement(stringBuilder.toString());
                        preparedStatement.setString(1, userName);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        List<RowObject> resul = transResultToRowObjectList(resultSet);
                        if (resul != null && !resul.isEmpty()) {
                            rows.addAll(resul);
                        }
                    }
                }
                int count = rows.size();
                jqObject.setTotal(ServletUtil.getTotalPages(count, pageSize));
                jqObject.setPage(start / pageSize + 1);
                jqObject.setRecords(count);
                if (count >= pageSize) {
                    int endIndex = count - start > pageSize ? pageSize : count - start;
                    jqObject.setRows(rows.subList(start, start + endIndex));
                } else {
                    jqObject.setRows(rows);
                }
            }
        } catch (final SQLException e) {
            log.error("查询用户日志信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return jqObject;
    }

    /**
     * 按照用户ID查询资料修改记录
     *
     * @param id
     * @return
     */
    @Override
    public JqObject getProfileHistoryById(int id, int start, int pageSize) {
        JqObject jqObject = new JqObject();
        List<RowObject> rows = new ArrayList<RowObject>();
        try {
            UserInfoObject infoObject = getAccountById(id);
            //从数据库里拿出日志表的sql
            String getLogTablesSql = "SELECT table_name FROM information_schema.TABLES  " + "where table_schema = 'log_userplatform' and table_name like 'log_operation_%' GROUP BY table_name desc ";
            //1,拿到table信息
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(getLogTablesSql);
            ResultSet rs = stmt.executeQuery();
            List<String> tables = new ArrayList<String>();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            //2，根据用户的注册日期，查询日志表。得到日志记录集合。
            int createTime = 999999;
            if (infoObject != null && infoObject.getCreateTime() != null)
                createTime = Integer.parseInt(TimeStampUtil.fromTimeStampToSimpleStringDate(infoObject.getCreateTime()));
            for (String table : tables) {
                int tableTime = Integer.parseInt(table.substring("log_operation_".length(), table.length()));
                if (tableTime >= createTime) {
                    StringBuilder stringBuilder = new StringBuilder(" select * from  `log_userplatform`.`");
                    stringBuilder.append(table).append("`  where userName=? order by log_time desc ");
                    //3,拿到log信息
                    PreparedStatement preparedStatement = conn.prepareStatement(stringBuilder.toString());
                    preparedStatement.setString(1, infoObject.getUserName());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    List<RowObject> queryRes = transResultToRowObjectList(resultSet);
                    if (queryRes != null && !queryRes.isEmpty()) {
                        rows.addAll(queryRes);
                    }
                }
            }
            int count = rows == null ? 0 : rows.size();
            jqObject.setTotal(ServletUtil.getTotalPages(count, pageSize));
            jqObject.setPage(start / pageSize + 1);
            jqObject.setRecords(count);
            if (count >= pageSize) {
                int endIndex = count - start > pageSize ? pageSize : count - start;
                jqObject.setRows(rows.subList(start, start + endIndex));
            } else {
                jqObject.setRows(rows);
            }
        } catch (final SQLException e) {
            log.error("查询用户日志信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return jqObject;
    }


    private List<RowObject> transResultToRowObjectList(ResultSet resultSet) {
        List<RowObject> rowObjectList = new LinkedList<RowObject>();
        try {
            if (resultSet != null) while (resultSet.next()) {
                RowObject rowObject = new RowObject();
                List<Object> cells = new LinkedList<Object>();
                cells.add(resultSet.getInt("id"));
                cells.add(resultSet.getString("userName"));
                cells.add(resultSet.getString("content"));
                cells.add(Strings.isNullOrEmpty(resultSet.getString("ext1")) == true ? "无" : resultSet.getString("ext1"));
                cells.add(Strings.isNullOrEmpty(resultSet.getString("ext2")) == true ? "无" : resultSet.getString("ext2"));
                cells.add(Strings.isNullOrEmpty(resultSet.getString("ext3")) == true ? "无" : resultSet.getString("ext3"));
                cells.add(TimeStampUtil.fromTimeStampToStringDate(resultSet.getTimestamp("log_time")));
                cells.add(DataUtil.getLogTypeStr(resultSet.getInt("logType")));
                rowObject.setId(resultSet.getInt("id"));
                rowObject.setCell(cells);
                rowObjectList.add(rowObject);
            }
        } catch (Exception ex) {
            log.error("rs转换成实体出错！");
            ex.printStackTrace();
        }
        return rowObjectList;
    }
}
