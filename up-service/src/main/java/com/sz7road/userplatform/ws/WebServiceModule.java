/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws;

import com.google.inject.servlet.ServletModule;
import com.sz7road.userplatform.ws.avatar.UploadBySwfServlet;
import com.sz7road.userplatform.ws.filter.WebFilterModule;
import com.sz7road.userplatform.ws.getRole.GetRoleByUserNameServlet;
import com.sz7road.userplatform.ws.sign.GetGiftPackServlet;
import com.sz7road.userplatform.ws.sign.QuerySignInfoServlet;
import com.sz7road.userplatform.ws.sign.TodaySignServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeremy
 */
public class WebServiceModule extends ServletModule {

    private static final Logger log = LoggerFactory.getLogger(WebServiceModule.class.getName());

    @Override
    protected void configureServlets() {
        serve("/getRoleInfByUserName").with(GetRoleByUserNameServlet.class);
//        serve("/uploadBySwf").with(UploadBySwfServlet.class);
        // 登录服务接口
//        serve("/homePageLogin").with(com.sz7road.userplatform.ws.homepage.LoginAuthForHomePageServlet.class);
//        serve("/testMbk").with(TestMbkServlet.class);
        serve("/checkUserName").with(CheckUserNameServlet.class);
//        serve("/syncFcmInfo").with(SyncFcmInfoServlet.class);
        serve("/todaySign").with(TodaySignServlet.class);
        serve("/getGiftPack").with(GetGiftPackServlet.class);
        serve("/querySignInfo").with(QuerySignInfoServlet.class);
        serve("/GetSqMergerInfo").with(SqMergerInfoServlet.class);
        serve("/LoginAuth").with(LoginAuthHttpServlet.class);
        serve("/LoginAuth2").with(LoginAuth2HttpServlet.class);
        serve("/SignUp").with(SignUpHttpServlet.class);
        serve("/CheckAccount").with(CheckAccountHttpServlet.class);
        serve("/CheckAccount2").with(CheckAccount2HttpServlet.class);
        serve("/Purchase").with(PurchaseServlet.class);
        serve("/GetOrder").with(GetOrderServlet.class);
        serve("/ListGameServerTable").with(ListGameServerTableServlet.class);
        serve("/testCache").with(TestCacheServlet.class);
        serve("/Sync").with(SyncConfigServlet.class);
        serve("/Recharge").with(RechargeServlet.class);
        serve("/DataGame").with(DataGameServlet.class);
        serve("/DataServer").with(DataServerServlet.class);
        serveRegex("\\/Assert[0-9A-Z]{2}", "\\/AssertReturn[0-9A-Z]{2}", "\\/AssertNotify[0-9A-Z]{2}").with(AssertServlet.class);
        serve("/PayPurchase").with(PayPurchaseServlet.class);
        serveRegex("\\/AssertPay[0-9A-Z]{2}", "\\/AssertPayReturn[0-9A-Z]{2}", "\\/AssertPayNotify[0-9A-Z]{2}").with(AssertPayServlet.class);
        install(new WebFilterModule());
        log.info("Finished install the WebService module.");
    }
}
