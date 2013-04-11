/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.GameWithServerUtils;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Singleton
class PayServlet extends HeadlessHttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PayServlet.class.getName());
    //选择游戏的type
    private static final String GAME = "game";
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;
    @Inject
    private javax.inject.Provider<ServerDataService> serverDataServiceProvider;
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //type用于判断请求的类型
        String type = request.getParameter("type");
        if (Strings.isNullOrEmpty(type)) {
            // 到游戏选择页,筛选开启的游戏；
            Collection<GameTable.GameEntry> gameEntryList= Collections2.filter(
                    cacheDataItemsServiceProvider.get().getGameEntriesFromCache(),
                    new Predicate<GameTable.GameEntry>() {
                @Override
                public boolean apply(GameTable.GameEntry gameEntry) {
                    return gameEntry.getStatus()==1;
                }
            });
            request.setAttribute("gameList",gameEntryList);
            String rs = ConfigurationUtils.get("payHomePage.domainUrl");
            request.getRequestDispatcher(rs).forward(request, response);
            return;
        }
       final String strGameId = request.getParameter("gameid");
        int gameId = 1;
        try {
            gameId = Ints.tryParse(strGameId);
        } catch (Exception ex) {
            gameId=1;
        }
        request.setAttribute("gameId", gameId);

        GameTable.GameEntry gameEntry =cacheDataItemsServiceProvider.get().getGameEntryFromCacheByGameId(gameId);
        if (gameEntry != null) {
            request.setAttribute("game",gameEntry);
        }
        //是不是vip ,如果是，设置金额为10000，否则就不设置
        String money = null;
        try {
            money = request.getParameter("money");
            if (Strings.isNullOrEmpty(money))
                money = "normal";
        } catch (Exception ex) {
            money = "normal";
        }
        request.setAttribute("money", money);
      //游戏里的充值链接
       final String site = request.getParameter("site");//渠道7road_%4d
       final int serverNo = getServerNoFromSite(site);
        if (serverNo > 0 && !Strings.isNullOrEmpty(site)) {
           ServerInfo serverEntry = null;
            try {
                serverEntry = serverDataServiceProvider.get().list(gameId, serverNo);
            } catch (Exception ex) {
                logger.error("未找到游戏服务区 GameId:"+gameId+"  serverNo; " + serverNo);
                serverEntry = null;
            }
            if (serverEntry != null) {
                request.setAttribute("server", serverEntry.getServerId());
            }
        }
        String uid = request.getParameter("uid");
        String userName =null;
        if (!Strings.isNullOrEmpty(uid)) {
            int userId = 0;
            try {
                userId = Integer.parseInt(uid);
            } catch (Exception e) {
                logger.error("uid error:{}", e.getMessage());
            }
            if (userId > 0) {
                userName = setUser(userId);
            }
            request.setAttribute("userName", userName);
        }
        try {
            if (type.equals(GAME)) {
                request.getRequestDispatcher(ConfigurationUtils.get("payPage.domainUrl")).forward(request, response);
                return;
            }
        } catch (Exception e) {
            logger.error("跳转到支付页面异常!");
        }
    }

    private String setUser(int uid) {
        UserService userService = userServiceProvider.get();
        UserAccount userAccount = userService.findAccountById(uid);
        if (userAccount != null) {
            return userAccount.getUserName();
        }
        return null;
    }

    private static int getServerNoFromSite(String site) {
        final String sitePrefix = "7road_";
        String serverNoString = "";
        if (!Strings.isNullOrEmpty(site) && site.startsWith(sitePrefix)) {
            serverNoString = site.substring(sitePrefix.length(), site.length());
        }
        int serverNum = 0;
        try {
            serverNum = Ints.tryParse(serverNoString);
        } catch (Exception e) {
        }
        return serverNum;
    }

}
