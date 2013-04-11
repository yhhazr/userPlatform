/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.dao.UserDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.utils.IPUtil.IPSeeker;
import com.sz7road.userplatform.utils.IPUtil.Utils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.utils.RuleUtil;
import com.sz7road.utils.VerifyFormItem;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author jeremy
 */
class UserDaoJdbcImpl extends JdbcDaoSupport<UserObject> implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoJdbcImpl.class.getName());

    private static final String TABLE = "$TABLE";
    private static final String SQL_QUERY = "SELECT * FROM `" + TABLE + "` WHERE 1";
    private static final String SQL_GETBY_ID = SQL_QUERY + " AND `id`=?";
    private static final String SQL_GETBY_USERNAME = SQL_QUERY + " AND `userName`=?";
    private static final String SQL_GETBY_MOBILE = SQL_QUERY + " AND `mobile`=?";
    private static final String SQL_ADD = "INSERT INTO `" + TABLE + "`(`id`, `userName`,`createTime`,`icn`,`realName`,`site`,`pswStrength`,`lastLoginTime`) VALUES(?,?,?,?,?,?,?,?)";
    private static final String SQL_ADD_REGISTER = "INSERT INTO `" + TABLE + "`(`id`, `userName`,`createTime`) VALUES(?,?,?)";

    private static final String SQL_UPDATE_LOGIN_DATA = " UPDATE `" + TABLE + "` SET `loginSum`=?, `lastIp`=?, `lastLoginTime`=? , `pswStrength`=? WHERE 1 AND `id`=? ";
    private static final String SQL_UPDATE_BASEINFO = "UPDATE " + TABLE + " SET gender = ? ,birthday = ? ,city = ? ,career = ? WHERE 1 AND id = ? ";
    private static final String SQL_UPDATE_SAFE_LEVEL = "UPDATE " + TABLE + " SET safeLevel = ? WHERE 1 AND id = ? ";

    private static final String SQL_UPDATE_PSW_LEVEL = "UPDATE " + TABLE + " SET pswStrength = ? WHERE 1 AND id = ? ";
    private static final String SQL_GAME_DATA = "UPDATE `" + TABLE + "` SET `lastGameId`=?, `lastGameZoneId`=?, `lastLoginTime`=? WHERE 1 AND `id`=?";
    private static final String SQL_UPDATE_MOBILE = "UPDATE " + TABLE + " SET mobile = ? WHERE 1 AND id = ? ";
    private static final String SQL_UPDATE_MESSAGECOUNT = "UPDATE " + TABLE + " SET `messageCount`= ?,`lastMessageTime` = ? WHERE 1 AND id = ? ";

    private static final String SQL_UPDATE_USERAVATER = " UPDATE " + TABLE + " SET `headDir` = ?  WHERE  id = ? ";

    private static final String SQL_UPDATE_USERINFO = "UPDATE " + TABLE + " SET $Filed  WHERE 1 AND id = ? ";

    private static final String SQL_GETROLESITEBYUSERID = " select * from `test`.`t_u_useridbycountday`  WHERE  UserID = ? order by Site asc ; ";

    private String resolveSql(String sql, int userId) {
        return sql.replace(TABLE, TableLocator.getUserTable(userId));
    }

    @Override
    public UserObject get(final int id) {
        try {

            return getQueryRunner().query(resolveSql(SQL_GETBY_ID, id), new BeanHandler<UserObject>(UserObject.class), id);

        } catch (final Exception e) {
            log.error("查询用户数据异常：{}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int add(final UserObject entity) throws SQLException {
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            queryRunner.update(connection, resolveSql(SQL_ADD, entity.getId()), entity.getId(), entity.getUserName(), entity.getCreateTime(),
                    entity.getIcn(), entity.getRealName(), entity.getSite(), entity.getPswStrength(), entity.getLastLoginTime());
            return 1;
        } catch (SQLException e) {
            log.error("添加用户失败：{}", e.getMessage());
        } finally {
            DbUtils.commitAndCloseQuietly(connection);
        }
        return 0;
    }

    @Override
    public int add(Connection conn, UserObject entity) throws SQLException {
        return getQueryRunner().update(conn, resolveSql(SQL_ADD, entity.getId()), entity.getId(), entity.getUserName(), entity.getCreateTime(), entity.getIcn(), entity.getRealName(), entity.getSite(), entity.getPswStrength(), entity.getLastLoginTime());
    }

    @Override
    public Map<String, Integer> add_register(final UserObject user) throws SQLException {

        Map<String, Integer> map = new HashMap<String, Integer>();
        //插入用户表
        StringBuffer addAccountSB = new StringBuffer(" INSERT INTO `db_userplatform`.`dt_account`(`userName`,`passWord`,`email`) VALUES(?,?,?);");
        //添加用户信息
        StringBuffer addUserSB = new StringBuffer(" INSERT INTO `db_userplatform`.`" + TABLE + "`(`id`, `userName`,`createTime`,`loginSum`,`lastLoginTime`,`pswStrength`) VALUES(?,?,?,?,?,?);");
        String addUser = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmtExe = null;
        int rel = 0;
        int userId = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(addAccountSB.toString(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, MD5Utils.password(user.getAccount().getPassWord()));
            stmt.setString(3, "");
            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    userId = rs.getInt(1);
                }
                if (userId > 0) {
                    addUser = resolveSql(addUserSB.toString(), userId);
                    map.put("id", userId);
                    stmtExe = connection.prepareStatement(addUser);
                    stmtExe.setInt(1, userId);
                    stmtExe.setString(2, user.getUserName());
                    stmtExe.setTimestamp(3, user.getCreateTime());
                    stmtExe.setInt(4, user.getLoginSum());
                    stmtExe.setTimestamp(5, user.getLastLoginTime());
                    stmtExe.setInt(6, user.getPswStrength());
                    rel = stmtExe.executeUpdate();
                    map.put("rel", rel);
                } else {

                    log.info("增加用户信息失败! id: " + userId);
                }
            } else {
                log.info("增加账户信息失败!" + user.getUserName());
            }
        } catch (Exception ex) {
            log.error("修改登录信息和增加IP日志异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(stmtExe);
            DbUtils.closeQuietly(connection);
        }
        return map;
    }

    @Override
    public int updateBaseInfo(UserObject entity) {
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            queryRunner.update(connection, resolveSql(SQL_UPDATE_BASEINFO, entity.getId()), entity.getGender(), entity.getBirthday(), entity.getCity(), entity.getCareer(), entity.getId());
            return 1;
        } catch (SQLException e) {
            log.error("更新用户基本信息失败：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }
        return 0;
    }

    @Override
    public int updateSafeLevel(int id, byte safeLevel) {
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            queryRunner.update(connection, resolveSql(SQL_UPDATE_SAFE_LEVEL, id), safeLevel, id);
            return 1;
        } catch (SQLException e) {
            log.error("更新安全级别失败：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }
        return 0;
    }

    /**
     * 更新密码级别
     *
     * @param
     * @return
     */
    @Override
    public int updatePswStrengthLevel(int id, int pswStrength) {
        final QueryRunner queryRunner = getQueryRunner();
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            queryRunner.update(connection, resolveSql(SQL_UPDATE_PSW_LEVEL, id), pswStrength, id);
            return 1;
        } catch (SQLException e) {
            log.error("更新密码等级失败：{}", e.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }
        return 0;
    }

    private String getTable() {
        String yearMonth = DateUtils.formatDate(new java.util.Date(), "yyyyMM");
        return String.format("`log_userplatform`.`log_operation_%s`", yearMonth);
    }

    @Override
    public int updateLoginData(final UserObject user) throws SQLException {

        //修改用户信息的sql
        String updateUserData = " UPDATE `" + TABLE + "` SET `loginSum`=" + user.getLoginSum() + " , `lastIp`='" + user.getLastIp() + "', `lastLoginTime`='" + user.getLastLoginTime() + "' , `pswStrength`=" + user.getPswStrength() + " WHERE 1 AND `id`= " + user.getId();
        updateUserData = resolveSql(updateUserData, user.getId());

        //修改日志的sql
        IPSeeker ipSeeker = IPSeeker.getInstance();
        String ip = Utils.clearIP(user.getLastIp());
        if (Strings.isNullOrEmpty(ip) == true) {
            ip = "";
        }
        String address = "";
        if (!Strings.isNullOrEmpty(ip)) {
            address = ipSeeker.getAddress(ip);
        }
        StringBuffer addIPLogSqlSB = new StringBuffer(" INSERT INTO $table(userName,content,ext1,ext2,logType,log_time) VALUES('").
                append(user.getUserName()).append("','").append(ip).append("','").append(address).
                append("','").append("show").append("',").append(6).append(",'").append(new Timestamp(new java.util.Date().getTime())).append("')");

        String addIPLogSql = addIPLogSqlSB.toString().replace("$table", getTable());
        Connection connection = null;
        int rel = 0;
        PreparedStatement preparedStatement = null;
        PreparedStatement ipPrepareStatument = null;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(updateUserData);
            ipPrepareStatument = connection.prepareStatement(addIPLogSql);
            rel = preparedStatement.executeUpdate() + ipPrepareStatument.executeUpdate();
            log.info(user.getUserName() + " 登录的IP是：" + user.getLastIp() + " 地区：" + address);
        } catch (Exception ex) {
            log.error("修改登录信息和增加IP日志异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbUtils.close(preparedStatement);
            DbUtils.close(ipPrepareStatument);
            DbUtils.closeQuietly(connection);
        }
        return rel;
    }

    @Override
    public int updateGameData(final UserObject user) throws SQLException {
        final QueryRunner runner = getQueryRunner();
        return runner.update(resolveSql(SQL_GAME_DATA, user.getId()), user.getLastGameId(), user.getLastGameZoneId(), user.getLastLoginTime(), user.getId());
    }

    @Override
    public List<UserObject> getByMobile(int id, String mobile) throws SQLException {
        return getQueryRunner().query(resolveSql(SQL_GETBY_MOBILE, id), new BeanListHandler<UserObject>(UserObject.class), mobile);
    }

    @Override
    public int updateMobile(UserObject user) throws SQLException {
        return getQueryRunner().update(resolveSql(SQL_UPDATE_MOBILE, user.getId()), user.getMobile(), user.getId());
    }

    @Override
    public int updateMobile(Connection conn, int userId, String mobile) throws SQLException {
        return getQueryRunner().update(conn, resolveSql(SQL_UPDATE_MOBILE, userId), mobile, userId);
    }

    @Override
    public int updateMessageCount(UserObject entity) throws SQLException {
        return getQueryRunner().update(resolveSql(SQL_UPDATE_MESSAGECOUNT, entity.getId()), entity.getMessageCount(), entity.getLastMessageTime(), entity.getId());
    }

    /**
     * 先删除原来的头像，然后在修改路径
     *
     * @param id      用户Id
     * @param headDir 用户头像的相对路径
     * @return 结果
     */
    @Override
    public int updateUserAvatar(int id, String userName, String headDir) {
        String savePath = ConfigurationUtils.get("user.resource.dir"); //上传的图片保存路径
        String showPath = ConfigurationUtils.get("user.resource.url"); //显示图片的路径
        int rel = 0;
        //修改用户信息的sql
        StringBuffer updateUserHead = new StringBuffer(" UPDATE " + TABLE + " SET headDir='").append(headDir).append("' WHERE  id=").append(id).append(" ;");
        String updateUserHeadSql = resolveSql(updateUserHead.toString(), id);
        //修改日志的sql
        StringBuffer addHeadLogSqlSB = new StringBuffer(" INSERT INTO $table(userName,content,logType,log_time) VALUES('").
                append(userName).append("','").append("头像修改为：" + headDir).append("',").append(1).append(",'").append(new Timestamp(new java.util.Date().getTime())).append("');");
        String addHeadLogSql = addHeadLogSqlSB.toString().replace("$table", getTable());
        Connection connection = null;
        Statement stmt = null;
        try {
            UserObject userObject = get(id);
            String headStr = userObject.getHeadDir();
            if (!Strings.isNullOrEmpty(headStr)) {
                String fileRealpath = headStr.replace(showPath, savePath);
                File file = new File(fileRealpath);
                file.delete();
            }
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.createStatement();
            stmt.addBatch(updateUserHeadSql);
            stmt.addBatch(addHeadLogSql);
            rel = stmt.executeBatch().length;
        } catch (Exception ex) {
            log.error("修改登录信息和增加头像日志异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
        return rel;
    }

    /**
     * 更新用户的基本信息
     *
     * @param userObject
     * @return
     */
    @Override
    public int updateUserBasicInfo(UserObject userObject) {
        //根据条件，组合不同的sql
        Map<String, Object> sqlParamsMap = new HashMap<String, Object>();
        sqlParamsMap.put("nickName", userObject.getNickName());//昵称
        sqlParamsMap.put("realName", userObject.getRealName());//真实姓名
        sqlParamsMap.put("city", userObject.getCity());//所在城市
        sqlParamsMap.put("gender", userObject.getGender());//性别
        sqlParamsMap.put("birthday", userObject.getBirthday());//生日
        //如果已经填写了身份证号码，忽略这个字段；如果没有，添加这个字段。
        UserObject userObjectOrign = get(userObject.getId());
        if (Strings.isNullOrEmpty(userObjectOrign.getIcn())) { //身份证号码
            sqlParamsMap.put("icn", userObject.getIcn());
        }
        sqlParamsMap.put("qq", userObject.getQq()); //QQ
        sqlParamsMap.put("msn", userObject.getMsn());//msn
        sqlParamsMap.put("linkPhone", userObject.getLinkPhone());//联系电话
        sqlParamsMap.put("selfIntroduction", userObject.getSelfIntroduction());//自我介绍
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : sqlParamsMap.keySet()) {
            stringBuffer.append(str).append("=? , ");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            String sql = resolveSql(SQL_UPDATE_USERINFO.replace("$Filed",
                    stringBuffer.toString().substring(0, stringBuffer.toString().length() - 2)), userObject.getId());
            stmt = connection.prepareStatement(sql);
            int i = 1;
            for (Map.Entry fieldEntry : sqlParamsMap.entrySet()) {
                if (fieldEntry.getKey().toString().equals("gender")) {
                    stmt.setInt(i, userObject.getGender());
                } else if (fieldEntry.getKey().toString().equals("birthday")) {
                    stmt.setDate(i, new Date(userObject.getBirthday().getTime()));
                } else {
                    stmt.setString(i, fieldEntry.getValue().toString());
                }
                i++;
            }
            stmt.setInt(sqlParamsMap.size() + 1, userObject.getId());
            rel = stmt.executeUpdate();
        } catch (Exception ex) {
            log.info("更新用户基本信息异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                stmt.close();
                DbUtils.closeQuietly(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rel;
    }

    /**
     * 更新用户的详细信息
     *
     * @param userObject
     * @return
     */
    @Override
    public int updateUserDetailInfo(UserObject userObject) {
        //根据条件，组合不同的sql
        Map<String, Object> sqlParamsMap = new HashMap<String, Object>();
//        if (!Strings.isNullOrEmpty(userObject.getMarryStatus()))
        {//婚姻状况
            sqlParamsMap.put("marryStatus", userObject.getMarryStatus());
        }
//        if (!Strings.isNullOrEmpty(userObject.getHobby()))
        {//兴趣爱好
            sqlParamsMap.put("hobby", userObject.getHobby());
        }
//        if (!Strings.isNullOrEmpty(userObject.getCareer()))
        { //当前职业
            sqlParamsMap.put("career", userObject.getCareer());
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (String str : sqlParamsMap.keySet()) {
            stringBuffer.append(str).append("=? , ");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(resolveSql(SQL_UPDATE_USERINFO.replace("$Filed", stringBuffer.toString().substring(0, stringBuffer.toString().length() - 2)), userObject.getId()));
            int i = 1;
            for (Map.Entry fieldEntry : sqlParamsMap.entrySet()) {
                stmt.setString(i, fieldEntry.getValue().toString());
                i++;
            }
            stmt.setInt(sqlParamsMap.size() + 1, userObject.getId());
            rel = stmt.executeUpdate();
        } catch (Exception ex) {
            log.info("更新用户详细信息异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                stmt.close();
                DbUtils.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rel;
    }

    /**
     * 更新用户的教育信息
     *
     * @param userObject
     * @return
     */
    @Override
    public int updateUserEduInfo(UserObject userObject) {
        //根据条件，组合不同的sql
        Map<String, Object> sqlParamsMap = new HashMap<String, Object>();
        if (userObject.getEduLevel() >= 0) {//教育背景
            sqlParamsMap.put("eduLevel", userObject.getEduLevel());
        }
        if (userObject.getSchoolType() >= 0) {//学校类型
            sqlParamsMap.put("schoolType", userObject.getSchoolType());
        }
        if (!Strings.isNullOrEmpty(userObject.getSchoolName())) { //学校名称
            sqlParamsMap.put("schoolName", userObject.getSchoolName());
        }
        if (userObject.getStartEduYear() >= 0) {//入学年份
            sqlParamsMap.put("startEduYear", userObject.getStartEduYear());
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (String str : sqlParamsMap.keySet()) {
            stringBuffer.append(str).append("=? , ");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(resolveSql(SQL_UPDATE_USERINFO.replace("$Filed", stringBuffer.toString().substring(0, stringBuffer.toString().length() - 2)), userObject.getId()));
            int i = 1;
            for (Map.Entry fieldEntry : sqlParamsMap.entrySet()) {
                stmt.setString(i, fieldEntry.getValue().toString());
                i++;
            }
            stmt.setInt(sqlParamsMap.size() + 1, userObject.getId());
            rel = stmt.executeUpdate();
        } catch (Exception ex) {
            log.info("更新用户教育信息异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                stmt.close();
                DbUtils.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return rel;
    }

    /**
     * 更新用户的工作信息
     *
     * @param userObject
     * @return
     */
    @Override
    public int updateUserWorkInfo(UserObject userObject) {
        //根据条件，组合不同的sql
        Map<String, Object> sqlParamsMap = new HashMap<String, Object>();
//        if (!Strings.isNullOrEmpty(userObject.getCompanyName()))
        {//公司名称
            sqlParamsMap.put("companyName", userObject.getCompanyName());
        }
//        if (!Strings.isNullOrEmpty(userObject.getWorkStartYear()))
        {//开始工作年份
            sqlParamsMap.put("workStartYear", userObject.getWorkStartYear());
        }
//        if (!Strings.isNullOrEmpty(userObject.getWorkEndYear()))
        { //结束工作年份
            sqlParamsMap.put("workEndYear", userObject.getWorkEndYear());
        }
//        if (!Strings.isNullOrEmpty(userObject.getWorkPost()))
        {//职位
            sqlParamsMap.put("workPost", userObject.getWorkPost());
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (String str : sqlParamsMap.keySet()) {
            stringBuffer.append(str).append("=? , ");
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            String sql = resolveSql(SQL_UPDATE_USERINFO.replace("$Filed", stringBuffer.toString().substring(0, stringBuffer.toString().length() - 2)), userObject.getId());
            stmt = connection.prepareStatement(sql);
            int i = 1;
            for (Map.Entry fieldEntry : sqlParamsMap.entrySet()) {
                stmt.setString(i, fieldEntry.getValue().toString());
                i++;
            }
            stmt.setInt(sqlParamsMap.size() + 1, userObject.getId());
            rel = stmt.executeUpdate();
        } catch (Exception ex) {
            log.info("更新用户工作信息异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                stmt.close();
                DbUtils.closeQuietly(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rel;
    }

    /**
     * 修改密码,并设置IP的显隐性，修改密码强度。
     *
     * @param userName 用户名
     * @param psw      新密码
     * @return
     */
    @Override
    public int modifyPasswordCommon(int id, String userName, String psw) {
        String pswStrength = String.valueOf(RuleUtil.getPswStrength(psw));
        psw = MD5Utils.password(psw);
        //修改密码的sql
        String updatePswSql = " UPDATE " + SqlConfig.ACCOUNT_TABLE + " SET password ='" + psw + "' WHERE userName ='" + userName + "' ;";
        //修改密码强度的sql
        String updatePswStrengthSql = "UPDATE db_userplatform." + TABLE + " SET pswStrength =" + pswStrength + " WHERE 1 AND id =" + id + " ;";
        updatePswStrengthSql = resolveSql(updatePswStrengthSql, id);
        //修改日志的sql
        List<String> updateLogSqls = new LinkedList<String>();

        Connection connection = null;
        Statement stmt = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            //从数据库里拿出日志表的sql
            String getLogTablesSql = " SELECT table_name FROM information_schema.TABLES  where table_schema = 'log_userplatform' and table_name like 'log_operation_%' GROUP BY table_name desc ";
            //1,拿到table信息
            preparedStatement = connection.prepareStatement(getLogTablesSql);
            resultSet = preparedStatement.executeQuery();
            List<String> tables = new ArrayList<String>();
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }
            //2，得到更改IP的日志信息sql集合
            for (String table : tables) {
                StringBuilder stringBuilder = new StringBuilder(" update  `log_userplatform`.`").append(table).
                        append("` set ext2='hide' where userName='").append(userName).append("' and ext2='show' and logType=6 and log_time<='").
                        append(new Timestamp(new java.util.Date().getTime())).append("' ;");
                updateLogSqls.add(stringBuilder.toString());
            }

            stmt = connection.createStatement();
            stmt.addBatch(updatePswStrengthSql);
            if (!updateLogSqls.isEmpty()) {
                for (String str : updateLogSqls) {
                    stmt.addBatch(str);
                }
            }
            stmt.addBatch(updatePswSql);

            rel = stmt.executeBatch().length;
        } catch (Exception ex) {
            log.error("修改密码信息异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
        return rel;
    }

    /**
     * 防沉迷注册
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Integer> register_fcm(UserObject user) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        //插入用户表
        StringBuffer addAccountSB = new StringBuffer(" INSERT INTO `db_userplatform`.`dt_account`(`userName`,`passWord`,`email`) VALUES(?,?,?);");
        //添加用户信息
        StringBuffer addUserSB = new StringBuffer(" INSERT INTO `db_userplatform`.`" + TABLE + "`(`id`, `userName`,`createTime`,`loginSum`,`lastLoginTime`,`pswStrength`,`realName`,`icn`) VALUES(?,?,?,?,?,?,?,?);");
        String addUser = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmtExe = null;
        int rel = 0;
        int userId = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(addAccountSB.toString(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, MD5Utils.password(user.getAccount().getPassWord()));
            stmt.setString(3, user.getEmail());
            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();
                if (null != rs && rs.next()) {
                    userId = rs.getInt(1);
                }
                if (userId > 0) {
                    addUser = resolveSql(addUserSB.toString(), userId);
                    map.put("id", userId);
                    stmtExe = connection.prepareStatement(addUser);
                    stmtExe.setInt(1, userId);
                    stmtExe.setString(2, user.getUserName());
                    stmtExe.setTimestamp(3, user.getCreateTime());
                    stmtExe.setInt(4, user.getLoginSum());
                    stmtExe.setTimestamp(5, user.getLastLoginTime());
                    stmtExe.setInt(6, user.getPswStrength());
                    stmtExe.setString(7, user.getRealName());
                    stmtExe.setString(8, user.getIcn());
                    rel = stmtExe.executeUpdate();
                    map.put("rel", rel);
                } else {

                    log.info("增加用户信息失败! id: " + userId);
                }
            } else {
                log.info("增加账户信息失败!" + user.getUserName());
            }
        } catch (Exception ex) {
            log.error("防沉迷注册异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(stmtExe);
            DbUtils.closeQuietly(connection);
        }
        return map;
    }

    /**
     * 更新平台用户的防沉迷信息
     *
     * @param userId   用户Id
     * @param realName 真实姓名
     * @param icn      身份证号码
     * @return
     */
    @Override
    public boolean updateFcmInfo(int userId, String realName, String icn) {

        Preconditions.checkArgument(userId > 0, "userId 非法：{}", userId);

        StringBuffer updateSql = new StringBuffer(" update " + TABLE + " set ");

        if (!Strings.isNullOrEmpty(realName)) {
            updateSql.append(" realName ='").append(realName).append("' , ");
        }

        if (!Strings.isNullOrEmpty(icn)) {
            updateSql.append(" icn ='").append(icn).append("'");
        }

        updateSql.append(" where id= ").append(userId);

        try {
            return getQueryRunner().update(resolveSql(updateSql.toString(), userId)) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ImmutableList<String> getRoleInfoByUserId(int userId) {

        try {
            return ImmutableList.copyOf(getQueryRunner().query(SQL_GETROLESITEBYUSERID,
                    new ResultSetHandler<List<String>>() {

                        @Override
                        public List<String> handle(ResultSet resultSet) throws SQLException {
                            List<String> sites = Lists.newArrayList();
                            while (resultSet != null && resultSet.next()) {
                                sites.add(resultSet.getString("Site"));
                            }
                            return sites;
                        }
                    }, userId));
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("获取用户的角色信息异常!" + e.getMessage());
        }
        return null;
    }

}
