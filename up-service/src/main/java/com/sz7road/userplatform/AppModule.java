/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform;

import com.google.inject.AbstractModule;
import com.sz7road.configuration.ConfigurationProvider;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.dao.cachedao.CacheGameDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerMaintain2Dao;
import com.sz7road.userplatform.dao.cacheimp.CacheGameDaoImp;
import com.sz7road.userplatform.dao.cacheimp.CacheServerEntryDaoImp;
import com.sz7road.userplatform.dao.cacheimp.CacheServerMaintain2DaoImp;
import com.sz7road.userplatform.dao.jdbc.JdbcDaoModule;
import com.sz7road.userplatform.pay.PayModule;
import com.sz7road.userplatform.service.ConfigurationService;
import com.sz7road.userplatform.service.FilterCharacterService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.ws.WebServiceModule;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author jeremy
 */
public class AppModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(AppModule.class.getName());

    @Override
    protected void configure() {
        log.info("Ready to install the Application modules.");
        // 加载Dao接口模块配置。
        install(new JdbcDaoModule());
        // 加载文件系统配置
        install(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ConfigurationProvider.class).to(ConfigurationService.class);
                bind(FilterCharacterProvider.class).to(FilterCharacterService.class);
                bind(VerifyCodeProvider.class).to(VerifyCodeService.class);
                bind(CacheGameDao.class).to(CacheGameDaoImp.class);
                bind(CacheServerMaintain2Dao.class).to(CacheServerMaintain2DaoImp.class);
                bind(CacheServerEntryDao.class).to(CacheServerEntryDaoImp.class);
                requestStaticInjection(ConfigurationUtils.class);
                bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool(new ThreadFactory() {
                    final String _NAME = "ASYNC-RUNNER";
                    volatile int INDEX = 0;

                    @Override
                    public Thread newThread(final Runnable r) {
                        return new Thread(r, _NAME + "-" + (INDEX++));
                    }
                }));
            }

        });
        // 加载充值模块。
        install(new PayModule());
        install(new com.sz7road.userplatform.ppay.PayModule());
        // 加载网关WebService模块。
        install(new WebServiceModule());
    }
}
