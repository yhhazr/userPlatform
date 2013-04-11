package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.LogDao;
import com.sz7road.userplatform.pojos.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

/**
 * @author leo.liao
 */

@Singleton
public class LogService {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogService.class.getName());

    @Inject
    private Provider<LogDao> logDaoProvider;

    @Inject
    private ExecutorService executorService;

    private LogDao getLogDao() {
        LogDao logDao = logDaoProvider.get();
        if (logDao == null) {
            throw new NullPointerException("null logDao provided!");
        }
        return logDao;
    }

    public void addTask(final Log log) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getLogDao().add(log);
                } catch (SQLException ex) {
                    LOGGER.error("插入日志出现异常{}", ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    public void insertLog(final Log log) {
        try {
            getLogDao().addInfoLog(log);
            LOGGER.info("插入用户信息日志success!");
        } catch (Exception ex) {
            LOGGER.error("插入日志出现异常{}", ex.getMessage());
            ex.printStackTrace();
        }
    }

}
