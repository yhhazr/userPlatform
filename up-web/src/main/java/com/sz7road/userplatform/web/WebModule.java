/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.inject.servlet.ServletModule;
import com.sz7road.userplatform.web.account.AccountWebServiceModule;
import com.sz7road.userplatform.web.rr.ReRegisterServiceModule;
import com.sz7road.userplatform.ws.ListGameServerTableServlet;

/**
 * @author jeremy
 */
public class WebModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/pay/index.html").with(PayServlet.class);
        serve("/pay/getServerList").with(GetServerListServlet.class);
        serve("/pay/result.html").with(ResultServlet.class);
        serve("/pay/getRole").with(GetRoleServlet.class);
        serve("/pay/getRole2").with(GetRole2Servlet.class);
        serve("/pay/getUuid").with(GetUuidServlet.class);
        serve("/pay/CheckAccount").with(CheckAccountServlet.class);
        serve("/PlayGame2").with(PlayGameServlet.class);
        serve("/PlayGame3").with(PlayGame2Servlet.class);
        serve("/ListGameServer").with(ListGameServerTableServlet.class);
        serve("/LatestGameServer").with(LatestGameServer.class);
        serve("/LatestGameServer2").with(LatestGameServer2.class);
        serve("/appeal").with(AppealServlet.class);
        serve("/uploadImgFile").with(UploadIdCardServlet.class);
        serve("/SendFlower").with(SendFlowerServlet.class);
        install(new AccountWebServiceModule());
        install(new ReRegisterServiceModule());
        //install(new TestModule());
        //install(new DataModule());
    }
}
