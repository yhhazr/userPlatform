/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.exception.EnterGameException;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.CookieUtils;
import com.sz7road.utils.Headend;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
@Singleton
public class PlayGameServlet extends HeadlessHttpServlet {

    static final Logger log = LoggerFactory.getLogger(PlayGameServlet.class.getName());

    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<GameWithServerService> gwsServiceProvider;

    /**
     * 协议：
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doServe(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String jsoncallback = request.getParameter("jsoncallback");
        String value = CookieUtils.getValueByName(request, "USERINFO");
        try {
            if (Strings.isNullOrEmpty(value)) {
                throw new EnterGameException(-1, "Cookie过期");
            }

            final GameWithServerService gwsService = gwsServiceProvider.get();

            int uid = AppHelper.getUserId(request);
            if (uid == 0) {
                throw new EnterGameException(1, "不正确Cookie格式");
            }
            int gameId = 0;
            int gameZoneId = 0;
            try {
                gameId = Integer.parseInt(request.getParameter("g"));
                gameZoneId = Integer.parseInt(request.getParameter("z"));
            } catch (Exception e) {
                throw new EnterGameException(2, "非法请求");
            }
            final UserService userService = userServiceProvider.get();
            final UserAccount userAccount = userService.findAccountById(uid);
            if (userAccount == null) {
                throw new EnterGameException(3, "未找到该用户");
            }
            String uname = userAccount.getUserName();
            long time = System.currentTimeMillis() / 1000;
            final Map<String, Object> param = Maps.newHashMap();
            param.put("uname", uname);
            param.put("uid", String.valueOf(uid));
            param.put("time", String.valueOf(time));

            List<String> list = Lists.newArrayList(param.keySet());
            Collections.sort(list);

            final StringBuilder sb = new StringBuilder();
            for (String p : list) {
                sb.append(p).append("=").append(param.get(p));
            }

            final ServerTable.ServerEntry serverEntry = getServerEntry(gameId, gameZoneId);
            //判断游戏是否开放
            Timestamp openingTime = serverEntry.getOpeningTime();
            boolean isOpen = AppHelper.isOpen(openingTime);

            if (!isOpen) {
                throw new EnterGameException(5, "游戏暂未开放");
            }

            if (!(gwsService.isAvaiableServer(gameId, gameZoneId))) {
                throw new EnterGameException(6, "游戏维护中");
            }
            if (!Strings.isNullOrEmpty(jsoncallback)) {
                writeClient(response, jsoncallback, new EnterGameException(0));
                return;
            }
            final String loginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.loginUrl");
            final String key = GameWithServerService.getServerValueByKey(serverEntry, "sq.loginKey");
            final String sign = MD5Utils.digestAsHex(sb.toString() + key);

            param.put("sign", sign);
            param.put("sid", serverEntry.getServerNo());
            log.info("uname = " + uname + ",uid = " + String.valueOf(uid) + ",time = " + String.valueOf(time) + ",sign = " + sign + ",sid=" + serverEntry.getServerNo());
            Headend.redirectForm(response, loginUrl, "POST", param);
            UserObject user = new UserObject();
            user.setId(uid);
            user.setLastGameId(gameId);
            user.setLastGameZoneId(gameZoneId);
            user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
            userService.updateGameData(user);
        } catch (EnterGameException e) {
            if (Strings.isNullOrEmpty(jsoncallback)) {
                response.setStatus(404);
            } else {
                writeClient(response, jsoncallback, e);
            }
        }
    }

    private void writeClient(HttpServletResponse response, String jsoncallback, EnterGameException e) {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = null;
        try {
            Msg msg = new Msg(e.getCode(), e.getMsg());
            writer = response.getWriter();
            String resp = mapper.writeValueAsString(msg);
            writer.write(jsoncallback + "(" + resp + ")");
        } catch (Exception ex) {
            log.error("{}", e.getMessage());
            writer.write(jsoncallback + "(" + "{\"code:\":\"9\",\"msg\":\"服务器异常\"}" + ")");
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    private ServerTable.ServerEntry getServerEntry(int gameId, int gameZoneId) {
        final GameTable gameTable = gwsServiceProvider.get().getGameTable();
        if (null != gameTable) {
            final ServerTable serverTable = gameTable.getServerTable(gameId);
            if (null != serverTable) {
                final ServerTable.ServerEntry serverEntry = serverTable.get(gameZoneId);
                if (serverEntry == null) {
                    throw new EnterGameException(4, "未找到游戏服务区");
                }
                return serverEntry;
            }
        }
        return null;
    }
}
