/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.web;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;
import com.sz7road.configuration.ConfigurationProvider;
import com.sz7road.userplatform.dao.*;
import com.sz7road.userplatform.dao.cachedao.CacheGameDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryNotForSqDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerMaintain2Dao;
import com.sz7road.userplatform.dao.cacheimp.CacheGameDaoImp;
import com.sz7road.userplatform.dao.cacheimp.CacheServerEntryDaoImp;
import com.sz7road.userplatform.dao.cacheimp.CacheServerEntryNotForSqDaoImp;
import com.sz7road.userplatform.dao.cacheimp.CacheServerMaintain2DaoImp;
import com.sz7road.userplatform.dao.jdbc.*;
import com.sz7road.userplatform.service.*;
import com.sz7road.userplatform.service.outinterface.IGameRankService;
import com.sz7road.userplatform.service.serverdata.ServerDataInterface;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.action.ActionModule;
import com.sz7road.web.masterdata.Command;
import com.sz7road.web.masterdata.LoginCommand;
import com.sz7road.web.ws.WsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author leo.liao
 */
public class AppModule extends ServletModule {

    private static final Logger log = LoggerFactory.getLogger(AppModule.class.getName());

    @Override
    protected void configureServlets() {
        log.info("Ready to install the ManagerBack App modules.");
        install(new ManagerBackJdbcDaoModule());
        install(new ActionModule());
        install(new WsModule());

        install(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ConfigurationProvider.class).to(ConfigurationService.class);
                bind(GameWithServerDecorateService.class).asEagerSingleton();
                bind(ServerMaintainDecorateService.class).asEagerSingleton();
                requestStaticInjection(ConfigurationUtils.class);
                bind(VerifyCodeProvider.class).to(VerifyCodeService.class);
                bind(IGameRankService.class).to(GameRankServiceImpl.class);
                bind(Command.class).to(LoginCommand.class);
                bind(InfoDecorateDao.class).to(InfoDecorateDaoJdbcImpl.class);
                bind(FaqDecorateDao.class).to(FaqDecorateDaoJdbcImpl.class);
                bind(QuestionDao.class).to(QuestionDaoJdbcImpl.class);
                bind(InfoDao.class).to(InfoDaoJdbcImpl.class);
                bind(FaqDao.class).to(FaqDaoJdbcImpl.class);
                bind(FaqKindDao.class).to(FaqKindDaoJdbcImpl.class);
                bind(AppealDao.class).to(AppealDaoImpl.class);
                bind(LoginGameDao.class).to(LoginGameDaoImpl.class);
                bind(ServerTableDecorateDao.class).to(ServerTableDecorateDaoJdbcImpl.class);
                bind(CacheServerEntryDao.class).to(CacheServerEntryDaoImp.class);
                bind(CacheServerEntryNotForSqDao.class).to(CacheServerEntryNotForSqDaoImp.class);
                bind(CacheGameDao.class).to(CacheGameDaoImp.class);
                bind(CacheServerMaintain2Dao.class).to(CacheServerMaintain2DaoImp.class);
                bind(ServerDataInterface.class).to(ServerDataService.class);
                bind(PaySwitchDao.class).to(PaySwitchDaoImpl.class);
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
    }
}
