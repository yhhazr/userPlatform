/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.rr;

import com.google.inject.Provider;
import com.google.inject.servlet.ServletModule;
import com.sz7road.userplatform.service.CacheDataItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiangfan.zhou
 */
public class ReRegisterServiceModule extends ServletModule {

    private static final Logger log = LoggerFactory.getLogger(ReRegisterServiceModule.class);

    @Override
    protected void configureServlets() {
        serve("/testupdatename").with(TestUpdateUsernameServlet.class);
        serve("/updatename").with(UpdateUsernameServlet.class);
        serve("/updatenameSubmit").with(UpdateUsernameSubmitServlet.class);
        log.info("Finished install the Re Register WebService module.");

    }
}
