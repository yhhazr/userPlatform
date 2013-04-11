/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.sz7road.configuration.Bean;
import com.sz7road.configuration.BeanManager;
import com.sz7road.configuration.SelfMakeBean;
import com.sz7road.userplatform.dao.*;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryNotForSqDao;
import com.sz7road.userplatform.dao.cacheimp.CacheServerEntryNotForSqDaoImp;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.SignServiceImp;
import com.sz7road.userplatform.ws.mbk.PswSafeCardDao;
import com.sz7road.userplatform.ws.mbk.PswSafeCardDaoImp;
import com.sz7road.userplatform.ws.sign.SignServiceInterface;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Iterator;

/**
 * @author jeremy
 */
public class JdbcDaoModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(JdbcDaoModule.class.getName());
    private final BoneCPConfig dbConfig = new BoneCPConfig();

    public JdbcDaoModule() {
        super();
        initDbConfig();
    }

    private void initDbConfig() {
        try {
            BeanManager.load();
            Class.forName("com.mysql.jdbc.Driver");
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getCause());
        }
    }

    @Override
    protected void configure() {
        // install the data-sources.
        installDataSources();
        bind(UserDao.class).to(UserDaoJdbcImpl.class);
        bind(AccountDao.class).to(AccountDaoJdbcImpl.class);
        bind(OrderDao.class).to(OrderDaoJdbcImpl.class);
        bind(GameTableDao.class).to(GameTableDaoImpl.class);
        bind(ServerTableDao.class).to(ServerTableDaoImpl.class);
        bind(ConfigurationDao.class).to(ConfigurationDaoJdbcImpl.class);
        bind(PayTableDao.class).to(PayTableDaoJdbcImpl.class);
        bind(LogDao.class).to(LogDaoJdbcImpl.class);
        bind(ChargeOrderLogDao.class).to(ChargeOrderLogDaoJdbcImpl.class);
        bind(FilterCharacterDao.class).to(FilterCharacterDaoJdbcImpl.class);
        bind(VerifyCodeDao.class).to(VerifyCodeDaoJdbcImpl.class);
        bind(ServerMaintainDao.class).to(ServerMaintainDaoJdbcImpl.class);
        bind(QuestionDao.class).to(QuestionDaoJdbcImpl.class);
        bind(InfoDao.class).to(InfoDaoJdbcImpl.class);
        bind(FaqDao.class).to(FaqDaoJdbcImpl.class);
        bind(FaqKindDao.class).to(FaqKindDaoJdbcImpl.class);
        bind(AppealDao.class).to(AppealDaoImpl.class);
        bind(LoginGameDao.class).to(LoginGameDaoImpl.class);
        bind(LogDao.class).to(LogDaoJdbcImpl.class);
        bind(SignServiceInterface.class).to(SignServiceImp.class);
        bind(SignDao.class).to(SignDaoJdbcImp.class);
        bind(PswSafeCardDao.class).to(PswSafeCardDaoImp.class);
        bind(CacheServerEntryNotForSqDao.class).to(CacheServerEntryNotForSqDaoImp.class);
		bind(PaySwitchDao.class).to(PaySwitchDaoImpl.class);
        bind(ProductOrderDao.class).to(ProductOrderDaoJdbcImpl.class);
        log.info("完成配置jdbc模块.");
    }

    void installDataSources() {
        for (final Iterator<Bean> it = BeanManager.beanIterator(); it.hasNext(); ) {
            Bean bean = it.next();
            if (null != bean) {
                final Class<? extends SelfMakeBean> beanClass = bean.getWrapClass();
                if (null != beanClass) {
                    try {
                        final SelfMakeBean makeBean = beanClass.newInstance();
                        install(makeBean.make(bean));
                        bind(QueryRunner.class).annotatedWith(Names.named(bean.getId())).toProvider(getQueryRunnerProvider(bean));
                    } catch (InstantiationException e) {
                        log.error(e.getMessage(), e);
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                installExtras(bean);
            }
        }
    }

    @Provides
    DataSourceCleaner provideDataSourceCleaner(final DataSource dataSource) {
        return new DataSourceCleaner() {
            @Override
            public void close() {
                if (dataSource instanceof BoneCPDataSource) {
                    ((BoneCPDataSource) dataSource).close();
                }
            }
        };
    }

    void installExtras(final Bean bean) {
        if (null != bean && bean.getId().equals("default")) {
            // binding the default.
            if (null != binder()) {
                bind(QueryRunner.class).toProvider(getQueryRunnerProvider(bean));
            }
        }
    }

    Provider<QueryRunner> getQueryRunnerProvider(final Bean bean) {
        return new Provider<QueryRunner>() {

            @Inject
            Injector injector;

            @Override
            public QueryRunner get() {
                if (null != injector) {
                    Key<DataSource> key = Key.get(DataSource.class, Names.named(bean.getId()));
                    return new QueryRunner(injector.getInstance(key));
                } else {
                    return new QueryRunner();
                }
            }
        };
    }
}
