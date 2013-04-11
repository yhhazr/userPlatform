/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Provider;
import com.sz7road.configuration.ConfigurationProvider;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.core.InjectionProxy;
import com.sz7road.userplatform.dao.ConfigurationDao;
import com.sz7road.userplatform.pojos.Configuration;
import com.sz7road.web.WebApplicationNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author leo.liao
 */
@Singleton
public class ConfigurationService extends Injection implements ConfigurationProvider {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class.getName());
    private final Map<String, Object> config = new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors() * 2).makeMap();
    private volatile int INSTNACE_INDEX = 1;  //代表该变量不稳定，每次都从主存中读取。

    private final ScheduledExecutorService taskService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Config-Sync-" + INSTNACE_INDEX++);
        }
    });

    private final ReentrantLock lock = new ReentrantLock();//返回用来与此 Lock 实例一起使用的 Condition 实例。
    private final Condition finishedCondition = lock.newCondition();
    private final AtomicBoolean running = new AtomicBoolean(false);  //可以用原子方式更新的 boolean 值
    private final Set<ConfigurationUpdateListener> updateListeners = Sets.newHashSet();
    @com.google.inject.Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    private final Runnable taskRunner = new Runnable() {

        private long lastModifiedTime = 0;

        @Override
        public void run() {
            try {
                final long time = getInstance(ConfigurationDao.class).getLastModifiedTime();
                if (lastModifiedTime == 0 || time > lastModifiedTime) {
                    // process as update.
                    config.putAll(acquires());
                    lastModifiedTime = time;
                    //如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
                    running.compareAndSet(false, true);
                    log.info("系统配置已更新.");
                    notifyUpdate();
                }

            } catch (final Exception e) {
                log.error("获取配置表同步时间异常：{}", e.getMessage());
            } finally {
            }
        }
    };

    /**
     * Constructs by google-guice.
     */
    @Inject
    private ConfigurationService(WebApplicationNotifier notifier) {
        super();

        notifier.addListener(new WebApplicationNotifier.WebApplicationListener() {
            @Override
            public void onUnDeploy(InjectionProxy injection) {
                if (null != taskService) {
                    taskService.shutdown();
                    taskService.shutdownNow();
                }
            }

            @Override
            public void onDeploy(InjectionProxy injection) {
                if (!running.get()) {
                    autoStartTask();
//                    cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
                }
            }
        });
    }

    /**
     * Retrieves all configuration as list.
     *
     * @return the list of configuration
     */
    protected List<Configuration> list() {
        try {
            return getInstance(ConfigurationDao.class).listAsConfiguration();
        } catch (SQLException ex) {
            log.error("查询系统配置表出现异常{}", ex.getMessage());
        }
        return null;
    }

    protected Map<String, Object> acquires() {
        try {
            // acquires the startup env arguments as the instance id.
            final Map<String, Object> map = getInstance(ConfigurationDao.class).listAsMap();
            if (!Strings.isNullOrEmpty(System.getProperty("instance.id"))) {
                map.put("instance.id", System.getProperty("instance.id").trim().charAt(0));
            }
            log.info("The Instance ID: {}", map.get("instance.id"));
            return map;
        } catch (final Exception e) {
            log.error("获取系统配置表配置异常：{}", e.getMessage());
            return Maps.newHashMap();
        }
    }

    @Override
    public String get(String key) {
        return config.get(key) == null ? null : config.get(key).toString();
    }

    @Override
    public boolean containsKey(String key) {
        return config.containsKey(key);
    }

    private void autoStartTask() {
        taskService.scheduleAtFixedRate(acquireSyncTask(), 1, 15, TimeUnit.SECONDS);

    }

    /**
     * 获取同步任务工作组件。
     *
     * @return the sync runnable task
     */
    protected Runnable acquireSyncTask() {
        return taskRunner;
    }

    public boolean save(final Map<String, Object> configuration) {
        final Properties properties = new Properties();
        properties.putAll(configuration);
        return save(properties);
    }

    public boolean save(final Properties configuration) {
        try {
            return getInstance(ConfigurationDao.class).batchUpdate(configuration) != -1;
        } catch (final Exception e) {
            log.error("更新配置表异常：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 配置更新监听器。
     *
     * @author jeremy
     */
    public interface ConfigurationUpdateListener {

        /**
         * 在更新配置后触发。
         *
         * @param service 配置服务组件
         */
        void onUpdate(ConfigurationService service);

    }

    public void addListener(ConfigurationUpdateListener listener) {
        updateListeners.add(listener);
    }

    public void removeListener(ConfigurationUpdateListener listener) {
        updateListeners.remove(listener);
    }

    public void notifyUpdate() {
        try {
            for (ConfigurationUpdateListener l : updateListeners) {
                try {
                    l.onUpdate(this);
                    log.info("通知更新的类：" + l.toString());
                } catch (final Exception e) {
                    log.error("通知更新事件异常：{}", e.getMessage());
                }
            }
        } finally {
//            log.info("一共通知了" + updateListeners.size() + "监听类!");
        }
    }

    public void updateTableTimestamp() {
        lock.lock();
        try {
            getInstance(ConfigurationDao.class).updateTableTimestamp();
        } catch (Exception e) {
            log.error("更新表信息异常：{}", e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
