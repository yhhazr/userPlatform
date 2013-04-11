package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.GameTable.GameEntry;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTable.ServerEntry;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author leo.liao
 */
@Singleton
class AccountCenterServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AccountCenterServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = AppHelper.getUserName(request);
        String redirectUrl = "http://sq.7road.com/";
        if (Strings.isNullOrEmpty(userName)) {
            response.sendRedirect(redirectUrl);
        } else {
            try{
            UserService userService = userServiceProvider.get();
            UserObject userObject = userService.getUserObject(userName);
            if (userObject == null) {
                response.sendRedirect(redirectUrl);
            } else {
                log.info("用户【{}】进入个人中心", userName);
                String userNameShow = userName;
                if (!Strings.isNullOrEmpty(userName) && userName.length() > 6) {
                    userNameShow = userName.substring(0, 6) + "...";
                }
                request.setAttribute("userNameShow", userNameShow);
                if (Strings.isNullOrEmpty(userObject.getHeadDir())) {
                    userObject.setHeadDir("/img/geren_right_01.jpg");
                }
                if (!Strings.isNullOrEmpty(userObject.getIcn())) {
                    userObject.setIcn(DataUtil.getHandledIcn(userObject.getIcn()));
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
                String URL ="/account/index.jsp"; //直接写死固定为这个
                request.getRequestDispatcher(URL).forward(request, response);
            }
            }catch (Exception ex)
            {
                log.error("获取用户中心信息失败!");
                request.getRequestDispatcher("/login.html").forward(request, response);
                ex.printStackTrace();
            }


        }
    }

    private GameEntry getNewlyGameEntry() {
        GameWithServerService gws = gameWithServerServiceProvider.get();
        //取最新的游戏对象
        GameTable gameTable = gws.getGameTable();
        int gameId = 0;
        GameEntry gameEntry = null;
        for (Entry<Integer, GameEntry> e : gameTable.entrySet()) {
            if (e.getKey() > gameId) {
                gameEntry = e.getValue();
                gameId = e.getKey();
            }
        }
        return gameEntry;
    }


}
