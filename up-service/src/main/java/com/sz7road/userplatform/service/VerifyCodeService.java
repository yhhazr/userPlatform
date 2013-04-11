package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.VerifyCodeDao;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author leo.liao
 */

@Singleton
public class VerifyCodeService extends Injection implements VerifyCodeProvider {

    private static final Logger log = LoggerFactory.getLogger(VerifyCodeService.class.getName());
    @Inject
    private ExecutorService asyncExecutor;
    @Inject
    private Provider<VerifyCodeDao> verifyCodeDaoProvider;

    private volatile int INSTNACE_INDEX = 1;

    private final ScheduledExecutorService taskService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "VerifyCode-Cleaner-" + INSTNACE_INDEX++);
        }
    });

    private final Runnable taskRunner = new Runnable() {

        @Override
        public void run() {
            try {
                log.info("start clean verify code task");
                VerifyCodeDao dao = getInstance(VerifyCodeDao.class);
                dao.deleteByExpiryTime(System.currentTimeMillis());
            } catch (final Exception e) {
                log.error("执行定时清理验证码缓存出现异常：{}", e.getMessage());
            } finally {
            }
        }
    };

    @Inject
    private VerifyCodeService() {
        super();
        autoStartTask();
    }

    /**
     * 获取同步任务工作组件。
     *
     * @return the sync runnable task
     */
    protected Runnable acquireSyncTask() {
        return taskRunner;
    }

    private void autoStartTask() {
        String et = ConfigurationUtils.get("verifyCode.cleanTime");
        long expiryTime = Strings.isNullOrEmpty(et) ? 30 : Long.parseLong(et);
        taskService.scheduleAtFixedRate(acquireSyncTask(), 1, expiryTime, TimeUnit.MINUTES);
    }

    private VerifyCodeDao getVerifyCodeDao() {
        VerifyCodeDao verifyCodeDao = verifyCodeDaoProvider.get();
        if (verifyCodeDao == null) {
            throw new NullPointerException("verifyCodeDao null");
        }
        return verifyCodeDao;
    }

    @Override
    public int add(VerifyCode code) {
        if (code == null) {
            throw new NullPointerException("VerifyCode null");
        }
        try {
            return getVerifyCodeDao().add(code);
        } catch (SQLException ex) {
            log.error("添加验证码出现异常{},{}", ex.getMessage(), ex);
        }
        return 0;
    }

    @Override
    public List<VerifyCode> getByVerify(String verify) {
        if (Strings.isNullOrEmpty(verify)) {
            throw new NullPointerException("verify");
        }
        return getVerifyCodeDao().getByVerify(verify);
    }

    @Override
    public boolean checkVerifyCode(String verify, String code) {
        List<VerifyCode> list = getByVerify(verify);
        if (list != null && list.size() > 0) {
            for (VerifyCode c : list) {
                if (code.equalsIgnoreCase(c.getCode())) {
                    delete(c);
                    if (c.getExpiryTime() >= System.currentTimeMillis()) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public VerifyCode getVerifyCode(String verify, String code) {
        List<VerifyCode> list = getByVerify(verify);
        if (list != null && list.size() > 0) {
            for (VerifyCode c : list) {
                if (code.equalsIgnoreCase(c.getCode())) {
                    if (c.getExpiryTime() >= System.currentTimeMillis()) {
                        return c;
                    }
                } else {
                    continue;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void delete(final VerifyCode entity) {
        asyncExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getVerifyCodeDao().delete(entity);
                } catch (final Exception e) {
                    log.error("删除验证码：{}", e.getMessage());
                }
            }
        });
    }
}
