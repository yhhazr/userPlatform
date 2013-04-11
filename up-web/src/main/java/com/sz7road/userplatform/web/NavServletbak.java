package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-20
 * Time: 下午5:39
 * 导航的servlet
 */
@Singleton
public class NavServletbak extends HeadlessHttpServlet {

    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = "";
        String userName = AppHelper.getUserName(request);
        if (Strings.isNullOrEmpty(userName)) {
//            response.sendRedirect(url);
        } else {
            UserService userService = userServiceProvider.get();
            UserObject userObject = userService.getUserObject(userName);
            if (userObject == null) {
                response.sendRedirect(url);
            } else {
                if ("index".equals(action)) {
                    url = "/account/userInfo/index.jsp";
                    String userNameShow = userName;
                    if (!Strings.isNullOrEmpty(userName) && userName.length() > 6) {
                        userNameShow = userName.substring(0, 6) + "...";
                    }
                    request.setAttribute("userNameShow", userNameShow);
                    if (Strings.isNullOrEmpty(userObject.getHeadDir())) {
                        userObject.setHeadDir("/img/geren_right_01.jpg");
                    }
                    request.setAttribute("userObject", userObject);
                    GameTable.GameEntry gameEntry = gameWithServerServiceProvider.get().getGameTable().get(userObject.getLastGameId());
                    if (null == gameEntry) {
                       gameEntry= getNewlyGameEntry();
                    }
                    request.setAttribute("gameEntry", gameEntry);
                    ServerTable.ServerEntry serverEntry = gameWithServerServiceProvider.get().getGameServer(userObject.getLastGameId(), userObject.getLastGameZoneId());
                    if (serverEntry == null) {
                        serverEntry = AppHelper.getNewlyServerEntry(gameWithServerServiceProvider.get().getGameTable().getServerTable(gameEntry));
                        request.setAttribute("newOpen", "new");
                    }
                    request.setAttribute("serverEntry", serverEntry);
                    Map<String, String> safeInfo = userService.getWholeSafeInfo(userObject);
                    request.setAttribute("safeInfo", safeInfo);
                    List<Log> userIPLogs = userService.getUserIPLogs(userName);
                    request.setAttribute("userIPLogs", userIPLogs);
                } else if ("resetPsw".equals(action)) {
                    url = "/account/userInfo/resetPsw.jsp";
                } else if ("ipException".equals(action)) {
                    url = "account/userInfo/IpException.jsp";
                    List<Log> userIPLogs = userService.getUserIPLogs(userName);
                    request.setAttribute("userIPLogs", userIPLogs);
                } else if ("baseInfo".equals(action)) {
                    url = "account/userInfo/baseInfo.jsp";
                    if (!Strings.isNullOrEmpty(userObject.getIcn())) {
                        userObject.setIcn(DataUtil.getHandledIcn(userObject.getIcn()));
                    }
                    request.setAttribute("userObject", userObject);
                }
               else if ("avatar".equals(action)) {
                    url = "account/userInfo/avatar.jsp";
                    if (Strings.isNullOrEmpty(userObject.getHeadDir())) {
                        userObject.setHeadDir("/img/geren_right_01.jpg");
                    }
                    request.setAttribute("userObject", userObject);
                } else {
                    request.setAttribute("userObject", userObject);
                    if ("workInfo".equals(action)) {
                        url = "account/userInfo/workInfo.jsp";
                    }

                    if ("eduInfo".equals(action)) {
                        url = "account/userInfo/eduInfo.jsp";
                    }
                    if ("detailInfo".equals(action)) {
                        url = "account/userInfo/detailInfo.jsp";
                    }
                }
                request.getRequestDispatcher(url).forward(request, response);

            }
        }
    }

    private GameTable.GameEntry getNewlyGameEntry() {
        GameWithServerService gws = gameWithServerServiceProvider.get();
        //取最新的游戏对象
        GameTable gameTable = gws.getGameTable();
        int gameId = 0;
        GameTable.GameEntry gameEntry = null;
        for (Map.Entry<Integer, GameTable.GameEntry> e : gameTable.entrySet()) {
            if (e.getKey() > gameId) {
                gameEntry = e.getValue();
                gameId = e.getKey();
            }
        }
        return gameEntry;
    }
}