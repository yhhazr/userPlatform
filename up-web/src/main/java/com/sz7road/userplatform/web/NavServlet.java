package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.GameTable.GameEntry;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTable.ServerEntry;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class NavServlet extends HeadlessHttpServlet
{

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    @Inject
    private  Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    protected void doServe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String action = request.getParameter("action");
        String url = "";
        String userName = AppHelper.getUserName(request);
        UserService userService = (UserService)this.userServiceProvider.get();
        if (Strings.isNullOrEmpty(userName)) {
            response.sendRedirect(url);
        } else {
            UserObject userObject = userService.getUserObject(userName);

            if ("index".equals(action)) {
                GameTable.GameEntry gameEntry=null;
                ServerInfo serverEntry = serverDataServiceProvider.get().get(userObject.getLastGameId(), userObject.getLastGameZoneId());
                List<GameEntry> gameEntryList= cacheDataItemsServiceProvider.get().getGameEntriesFromCache();

                if (serverEntry == null)
                {//没登录过的，直接推荐蛋2的最新服
                    serverEntry =serverDataServiceProvider.get().getNewOpenedServer(3);
                    gameEntry =cacheDataItemsServiceProvider.get().getGameEntryFromCacheByGameId(3);
                    if(serverEntry==null)
                    { //如果没有，推荐神曲的最新服
                        serverEntry=serverDataServiceProvider.get().getNewOpenedServer(1);
                        gameEntry=cacheDataItemsServiceProvider.get().getGameEntryFromCacheByGameId(1);
                    }
                    request.setAttribute("newOpen", "new");

                } else
                {
                    gameEntry =cacheDataItemsServiceProvider.get().getGameEntryFromCacheByGameId(userObject.getLastGameId());
                }

                String userNameShow = null;
                if (userObject == null) {
                    response.sendRedirect(url);
                } else {
                    if ((!Strings.isNullOrEmpty(userName)) && (userName.length() > 6))
                        userNameShow = userName.substring(0, 6) + "...";
                    else {
                        userNameShow = userName;
                    }
                    String icn = userObject.getIcn();
                    if (!Strings.isNullOrEmpty(icn)) {
                        icn = DataUtil.getHandledIcn(icn);
                        userObject.setIcn(icn);
                    }
                    if (Strings.isNullOrEmpty(userObject.getHeadDir()))
                        userObject.setHeadDir("/images/geren_right_01.jpg");
                    request.setAttribute("userObject", userObject);
                    request.setAttribute("userNameShow", userNameShow);

                    if (Strings.isNullOrEmpty(url)) {
                        response.setStatus(404);
                        return;
                    }

                    Timestamp openingTime = null;
                    if (serverEntry != null) {
                        openingTime = serverEntry.getOpeningTime();
                    }
                    boolean isOpen = AppHelper.isOpen(openingTime);
                    List userIPLogs = userService.getUserIPLogs(userName);
                    Map safeInfo = userService.getWholeSafeInfo(userObject);
                    request.setAttribute("isOpen", Boolean.valueOf(isOpen));
                    request.setAttribute("gameEntry", gameEntry);
                    request.setAttribute("serverEntry", serverEntry);
                    request.setAttribute("userIPLogs", userIPLogs);
                    request.setAttribute("safeInfo", safeInfo);
                    url = "/account/userInfo/index.jsp";
                }
            }
            if ("resetPsw".equals(action)) {
                url = "/account/userInfo/resetPsw.jsp";
            }
            if ("ipException".equals(action)) {
                List userIPLogs = userService.getUserIPLogs(userName);
                request.setAttribute("userIPLogs", userIPLogs);
                url = "account/userInfo/IpException.jsp";
            }

            if ("workInfo".equals(action)) {
                if (userObject == null)
                    response.sendRedirect(url);
                else {
                    request.setAttribute("userObject", userObject);
                }
                url = "account/userInfo/workInfo.jsp";
            }

            if ("eduInfo".equals(action)) {
                if (userObject == null)
                    response.sendRedirect(url);
                else {
                    request.setAttribute("userObject", userObject);
                }
                url = "account/userInfo/eduInfo.jsp";
            }

            if ("avatar".equals(action)) {
                if (userObject == null) {
                    response.sendRedirect(url);
                } else {
                    if (Strings.isNullOrEmpty(userObject.getHeadDir())) {
                        userObject.setHeadDir("/images/geren_right_01.jpg");
                    }
                    request.setAttribute("userObject", userObject);
                }
                url = "account/userInfo/avatar.jsp";
            }

            if ("detailInfo".equals(action)) {
                if (userObject == null)
                    response.sendRedirect(url);
                else {
                    request.setAttribute("userObject", userObject);
                }
                url = "account/userInfo/detailInfo.jsp";
            }

            if ("baseInfo".equals(action)) {
                if (userObject == null) {
                    response.sendRedirect(url);
                } else {
                    String icn = userObject.getIcn();
                    if (!Strings.isNullOrEmpty(icn)) {
                        icn = DataUtil.getHandledIcn(icn);
                        userObject.setIcn(icn);
                    }
                    request.setAttribute("userObject", userObject);
                }
                url = "account/userInfo/baseInfo.jsp";
            }

            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private GameTable.GameEntry getNewlyGameEntry()
    {
        GameWithServerService gws = (GameWithServerService)this.gameWithServerServiceProvider.get();

        GameTable gameTable = gws.getGameTable();
        int gameId = 0;
        GameTable.GameEntry gameEntry = null;
        for (Map.Entry e : gameTable.entrySet()) {
            if (((Integer)e.getKey()).intValue() > gameId) {
                gameEntry = (GameTable.GameEntry)e.getValue();
                gameId = ((Integer)e.getKey()).intValue();
            }
        }
        return gameEntry;
    }

    private ServerTable.ServerEntry getNewlyServerEntry(ServerTable serverTable) {
        int serverNo = 0;
        ServerTable.ServerEntry serverEntry = null;
        if (serverTable != null) {
            for (Map.Entry entry : serverTable.entrySet()) {
                ServerTable.ServerEntry tmp = (ServerTable.ServerEntry)entry.getValue();

                if (tmp.getServerStatus() != -3)
                    if (tmp.getServerNo() >= serverNo) {
                        serverNo = tmp.getServerNo();
                        serverEntry = tmp;
                    }
            }
        }
        return serverEntry;
    }
}