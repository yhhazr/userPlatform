package com.sz7road.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sz7road.userplatform.dao.DataSourceCleaner;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午9:39
 */
@WebListener
public final class ServletContextListener extends GuiceServletContextListener {

    private Injector injector;

    @Override
    protected Injector getInjector() {
        if (null == injector)
            injector = Guice.createInjector(Stage.DEVELOPMENT, new AppModule());
        return injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        getInjector().getInstance(WebApplicationNotifier.class).notifyDeploy();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        // 显示去除数据源引用。
        injector.getInstance(DataSourceCleaner.class).close();
        getInjector().getInstance(WebApplicationNotifier.class).notifyUnDeploy();
    }
}
