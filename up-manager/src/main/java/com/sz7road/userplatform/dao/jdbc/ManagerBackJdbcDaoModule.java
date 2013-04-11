package com.sz7road.userplatform.dao.jdbc;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.sz7road.configuration.Bean;
import com.sz7road.configuration.BeanManager;
import com.sz7road.configuration.SelfMakeBean;
import com.sz7road.userplatform.dao.*;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Iterator;

/**
 * 管理后台DAO服务加载
 * User: leo.liao
 * Date: 12-6-14
 * Time: 上午10:15
 */
public class ManagerBackJdbcDaoModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(ManagerBackJdbcDaoModule.class.getName());

    private final BoneCPConfig dbConfig = new BoneCPConfig();

    public ManagerBackJdbcDaoModule() {
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
        bind(VerifyCodeDao.class).to(VerifyCodeDaoJdbcImpl.class);
        bind(ConfigurationDao.class).to(ConfigurationDaoJdbcImpl.class);
        bind(GameTableDao.class).to(GameTableDaoImpl.class);
        bind(ServerTableDao.class).to(ServerTableDaoImpl.class);
        bind(ServerTableDecorateDao.class).to(ServerTableDecorateDaoJdbcImpl.class);
        bind(OrderDao.class).to(OrderDecorateDaoJdbcImpl.class);
        bind(PayTableDao.class).to(PayTableDaoJdbcImpl.class);
        bind(OrderDecorateDao.class).to(OrderDecorateDaoJdbcImpl.class);
        bind(ChargeOrderLogDao.class).to(ChargeOrderLogDaoJdbcImpl.class);
        bind(UserManagerDao.class).to(UserManagerDaoJdbcImpl.class);
        bind(ItemDao.class).to(ItemDaoJdbcImpl.class);
        bind(LoseOrderDao.class).to(LoseOrderDaoJdbcIml.class);
        bind(ServerMaintainDao.class).to(ServerMaintainDaoJdbcImpl.class);
        bind(ServerMaintainDecorateDao.class).to(ServerMaintainDecorateDaoJdbcImpl.class);
        bind(DataStatisticalDao.class).to(DataStatisticalDaoImpl.class);
        bind(FaqKindDecorateDao.class).to(FaqKindDecorateDaoJdbcImpl.class);
        bind(LogDao.class).to(LogDaoJdbcImpl.class);
//        bind(CacheDao.class).to(ServerCacheDaoImp.class);
//        bind(CacheMaintainDao.class).to(ServerMaintainCacheDaoImp.class);
        log.info("Finished to configure the ManagerBack JDBC Dao module.");
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
