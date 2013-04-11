/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform;

import com.google.inject.*;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sz7road.userplatform.dao.DataSourceCleaner;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.web.WebModule;
import com.sz7road.web.WebApplicationNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * @author jeremy
 */
@WebListener
public final class ServletContextListener extends GuiceServletContextListener {

    private Injector injector;
    @Override
    protected Injector getInjector() {
        if (null == injector)
            injector = Guice.createInjector(Stage.DEVELOPMENT, new AppModule(), new WebModule());
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
