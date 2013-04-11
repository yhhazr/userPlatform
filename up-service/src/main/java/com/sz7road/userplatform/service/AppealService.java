package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.*;
import com.sz7road.userplatform.dao.jdbc.AppealDaoImpl;
import com.sz7road.userplatform.dao.jdbc.JdbcDaoSupport;
import com.sz7road.userplatform.pojos.Appeal;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Pager;
import com.sz7road.utils.ListData;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.lucene.analysis.CharArrayMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author jiangfan.zhou
 */

@Singleton
public class AppealService extends Injection{

    private final static Logger log = LoggerFactory.getLogger(AppealService.class.getName());

    @Inject
    private Provider<AppealDao> appealDaoProvider;
    @Inject
    private Provider<QuestionDao> questionDaoProvider;
    @Inject
    private Provider<AccountDao> accountDaoProvider;
    @Inject
    private Provider<UserDao> userDaoProvider;
    @Inject
    private Provider<QueryRunner> runner;

    public AppealDao getAppealDao(){
        AppealDao dao = appealDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null getAppealDao!");
        }
        return dao;
    }

    public QuestionDao getQuestionDao(){
        QuestionDao dao = questionDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null getQuestionDao!");
        }
        return dao;
    }

    public AccountDao getAccountDao(){
        AccountDao dao = accountDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null getAccountDao!");
        }
        return dao;
    }

    public UserDao getUserDao(){
        UserDao dao = userDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null getUserDao!");
        }
        return dao;
    }

    public Appeal get(int id) throws SQLException{
        return getAppealDao().get(id);
    }

    public int add(Appeal entity) throws SQLException{
        return getAppealDao().add(entity);
    }

    public int delete(Appeal entity) throws SQLException {
        return getAppealDao().delete(entity);
    }

    public Appeal updateStatus(int appealId, int status, String auditor) throws SQLException{
        Connection conn = runner.get().getDataSource().getConnection();
        try{
            Appeal entity = null;
            //conn.setAutoCommit(false);
            entity = getAppealDao().updateStatus(conn, appealId, status, auditor);
            if (status == AppealDao.STATUS_AUDIT_PASS) {
                int userId = entity.getUserId();
                int retQuestion = getQuestionDao().updateStatus(conn, userId, QuestionDao.STATUS_NOT_USE);
                int retMobile = getUserDao().updateMobile(conn, userId, "");
                int retEmail = getAccountDao().updateEmail(conn, userId, "");
                return entity;
            }
            return entity;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public Pager<Appeal> query(String userName, int pay, int status, Date fromDate, Date toDate, int page, int rows) throws SQLException {
        Pager<Appeal> list = null;

        int start = 0;
        int limit = rows;
        if (page > 0 && rows > 0) {
            start = page * rows - limit;
        }

        list = getAppealDao().query(userName, pay, status, fromDate, toDate,start, limit);

        return list;
    }

    /*Object[] params = new Object[2];
        int i = 1;
        for(Map.Entry<String, Object>  entry : map.entrySet()) {
            sql = sql + " AND " + entry.getKey() + "=? ";
            params[i++] = entry.getValue();
        }*/
}
