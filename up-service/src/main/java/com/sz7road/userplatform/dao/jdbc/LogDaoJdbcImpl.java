package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sz7road.userplatform.dao.LogDao;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Log.LogType;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author leo.liao
 */


class LogDaoJdbcImpl extends JdbcDaoSupport<Log> implements LogDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDaoJdbcImpl.class.getName());

    private static final String TABLE = "`log_userplatform`.`log_operation_%s`";
    static final String INSERT_TEMPLATE = "INSERT INTO $table($fields) VALUES($values)";

    static final String ADD_EXCEPTIONLOG = " INSERT INTO $table(userName,content,ext1,ext2,logType,log_time) VALUES(?,?,?,?,?,?)";

    static final String ADD_INFOLOG = " INSERT INTO $table(userName,content,ext1,ext2,logType,log_time) VALUES(?,?,?,?,?,?)";

    static final String QUERY_EXCEPTIONLOG = " select * from $table where userName=? and logType=? and content=? ";

    static final String UPDATE_EXCEPTIONLOG = " update ";

    static final String[] FIELDS = {"logType", "userName", "log_time", "content", "ext1", "ext2", "ext3"};

    @Override
    public int add(final Log log) throws SQLException {
        if (null == log) {
            throw new IllegalArgumentException("无效的实体对象！");
        }

        final QueryRunner runner = getQueryRunner();

        List<Object> values = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();

        for (String field : FIELDS) {
            try {
                String name = "get" + field;
                final char[] chars = name.toCharArray();
                chars[3] = field.toUpperCase().charAt(0);
                name = String.valueOf(chars);

                final Method declaredMethod = log.getClass().getDeclaredMethod(name);
                if (null != declaredMethod) {
                    final Object valueObject = declaredMethod.invoke(log);
                    if (null != valueObject) {
                        if (valueObject instanceof LogType) {
                            LogType type = (LogType) valueObject;
                            int value = type.getCode();
                            if (value > 0) {
                                if (sb.length() > 0) sb.append(',');
                                sb.append('`').append(field).append('`');
                                values.add(value);
                            }
                        } else {
                            String value = valueObject.toString();
                            if (null != value) {
                                if (sb.length() > 0) sb.append(',');
                                sb.append('`').append(field).append('`');
                                values.add(value);
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        }

        String SQL = INSERT_TEMPLATE.replace("$table", getTable());
        SQL = SQL.replace("$fields", sb.toString());
        SQL = SQL.replace("$values", Strings.repeat(",?", values.size()).substring(1));

        int rel = runner.update(SQL, values.toArray());
        return 1;
    }

    private String getTable() {
        String yearMonth = DateUtils.formatDate(new Date(), "yyyyMM");
        return String.format(TABLE, yearMonth);
    }

    /**
     * 拿到登录异常的日志
     *
     * @param userName 用户名
     * @return
     */
    @Override
    public List<Log> getExceptionLog(String userName) {
        List<Log> exceptionLogs = new ArrayList<Log>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String nowDate = DateUtils.formatDate(new Date(), "yyyyMM");
            List<String> lastThreeMonth = new LinkedList<String>();
            lastThreeMonth.add(nowDate);
            int nowDateInt = Integer.parseInt(nowDate);
            lastThreeMonth.add(String.valueOf(nowDateInt - 1));
            lastThreeMonth.add(String.valueOf(nowDateInt - 2));
            StringBuffer lastThreeMonthSqlStr = new StringBuffer("(");
            ;
            for (String str : lastThreeMonth) {
                lastThreeMonthSqlStr.append("'").append("log_operation_").append(str).append("'").append(",");
            }
            //去掉最后的逗号，然后加上半边括号
            lastThreeMonthSqlStr.delete(lastThreeMonthSqlStr.length() - 1, lastThreeMonthSqlStr.length()).append(")");

            //从数据库里拿出日志表的sql
            String getLogTablesSql = " SELECT table_name FROM information_schema.TABLES  where table_schema = 'log_userplatform' and table_name in " + lastThreeMonthSqlStr.toString() + "  GROUP BY table_name desc ";
            //1,拿到table信息
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(getLogTablesSql);
            rs = stmt.executeQuery();
            List<String> tables = new ArrayList<String>();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            //2，查询日志表。得到日志记录集合。
            for (String table : tables) {
                StringBuilder stringBuilder = new StringBuilder(" select * from  `log_userplatform`.`").append(table).
                        append("` where userName=? and logType=? and ext2='show' order by log_time desc ");
                //3,拿到log信息
                PreparedStatement preparedStatement = conn.prepareStatement(stringBuilder.toString());
                preparedStatement.setString(1, userName);
                preparedStatement.setInt(2, LogType.IPEXCEPTION_RECORD.getCode());
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Log> queryRes = transResultToLogList(resultSet);
                if (queryRes != null && !queryRes.isEmpty()) {
                    exceptionLogs.addAll(queryRes);
                }
            }

        } catch (final SQLException e) {
            log.error("查询IP登录信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return exceptionLogs;


    }

    private List<Log> transResultToLogList(ResultSet resultSet) {
        List<Log> logs = new LinkedList<Log>();
        try {
            while (resultSet != null && resultSet.next()) {
                Log log = new Log();
                log.setId(resultSet.getInt("id"));
                log.setContent(resultSet.getString("content"));
                log.setExt1(resultSet.getString("ext1"));
                log.setLogType(LogType.IPEXCEPTION_RECORD);
                log.setLog_time(resultSet.getTimestamp("log_time"));
                log.setUserName(resultSet.getString("userName"));
                logs.add(log);
            }
        } catch (SQLException e) {
            LOGGER.error("转换成log对象失败!");
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * 增加异常登入的日志信息
     *
     * @param log
     * @return
     */
    @Override
    public int addExceptionLog(Log log) {
        if (null == log) {
            throw new NullPointerException("null account");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        int rel = 0;
        try {
            String sql = ADD_EXCEPTIONLOG.replace("$table", getTable());
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, log.getUserName()); //记录用户名
            stmt.setString(2, log.getContent());//记录IP
            stmt.setString(3, log.getExt1());//记录IP对象的地区
            stmt.setString(4, "show");
            stmt.setInt(5, LogType.IPEXCEPTION_RECORD.getCode());//日志类型确定
            stmt.setTimestamp(6, new Timestamp(new Date().getTime()));//日志时间当前时间
            rel = stmt.executeUpdate();
        } catch (final SQLException e) {
            LOGGER.error("添加用户登录IP信息异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return rel;
    }

    @Override
    public int addInfoLog(Log log) {
        if (null == log) {
            throw new NullPointerException("null account");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        int rel = 0;
        try {
            String sql = ADD_INFOLOG.replace("$table", getTable());
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, log.getUserName()); //记录用户名
            stmt.setString(2, log.getContent());//记录IP
            stmt.setString(3, log.getExt1());//记录IP对象的地区
            stmt.setString(4, log.getExt2());//记录IP对象的地区
            stmt.setInt(5, LogType.ACCOUNT_UPDATE.getCode());//日志类型确定
            stmt.setTimestamp(6, new Timestamp(new Date().getTime()));//日志时间当前时间
            rel = stmt.executeUpdate();
        } catch (final SQLException e) {
            LOGGER.error("添加用户信息日志异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return rel;
    }

    /**
     * 影藏该用户名的IP登录日志，当前时间以前的。
     *
     * @param userName
     * @return
     */
    @Override
    public int hideExceptionLog(String userName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int rel = 0;
        try {
            //从数据库里拿出日志表的sql
            String getLogTablesSql = " SELECT table_name FROM information_schema.TABLES  where table_schema = 'log_userplatform' and table_name like 'log_operation_%' GROUP BY table_name desc ";
            //1,拿到table信息
            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(getLogTablesSql);
            rs = stmt.executeQuery();
            List<String> tables = new ArrayList<String>();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            //2，更改IP的日志信息。
            for (String table : tables) {
                StringBuilder stringBuilder = new StringBuilder(" update  `log_userplatform`.`").append(table).
                        append("` set ext2='hide' where userName=? and logType=? and log_time<=? ");
                //3,拿到log信息
                PreparedStatement preparedStatement = conn.prepareStatement(stringBuilder.toString());
                preparedStatement.setString(1, userName);
                preparedStatement.setInt(2, LogType.IPEXCEPTION_RECORD.getCode());
                preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
                rel = preparedStatement.executeUpdate();
            }

        } catch (final SQLException e) {
            log.error("更新IP登录信息异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return rel;
    }
}
