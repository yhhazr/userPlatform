package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.playgame.GenericPlayGameBean;
import com.sz7road.userplatform.playgame.PlayGameHandler;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.web.exception.EnterGameException;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 下午2:31
 */
@Singleton
@Parameter(value = {"g:","z:", "game:","subGame:"})
class PlayGame2Servlet extends HeadlessServlet {
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<ServerDataService> serverDataServerProvider;

    @Override
    protected void doPost(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        GenericPlayGameBean playGameBean = getInstance(GenericPlayGameBean.class);
        if(playGameBean != null){
            //等于0需要验证；大于0 状态判断
            int status = request.getIntParameter("status");
            String jsoncallback = request.getParameter("jsoncallback");
            PrintWriter out = response.getWriter();
            Msg msg = new Msg(0, "进入游戏初始化");

            try {
                if (!playGameBean.isAvailableForSubmit()) {
                    throw new EnterGameException(401, "参数验证不通过");
                }

                ServerDataService serverDataService = serverDataServerProvider.get();
                ServerInfo serverInfo = serverDataService.get(playGameBean.getGameId(), playGameBean.getGameZoneId());
                if (serverInfo == null) {
                    throw new EnterGameException(401, "游戏区服不存在");
                }

                ServerInfo mainServerInfo = serverInfo;
                if (serverInfo.getMainId() != 0 && serverInfo.getMainId() != serverInfo.getServerId()) {
                    mainServerInfo = serverDataService.get(playGameBean.getGameId(), serverInfo.getMainId());
                }
                if (mainServerInfo == null) {
                    throw new EnterGameException(401, "游戏区服不存在");
                }

                boolean isOpen = serverInfo.getServerStatus() >= 0;
                if (!isOpen) {
                    throw new EnterGameException(401, "游戏区服不可用");
                }

                if (status == 0){
                    // 验证用户有效性，非法会抛出异常
                    String userName = AppHelper.getUserName(request);
                    if (Strings.isNullOrEmpty(userName)) {
                        throw new EnterGameException(401, "用户未登陆");
                    }
                    UserAccount userAccount = userServiceProvider.get().findAccountByUserName(userName);
                    if (userAccount == null) {
                        throw new EnterGameException(401, "用户未登陆");
                    }

                    PlayGameHandler handler = playGameBean.getHandler();
                    if(handler != null){
                        handler.process(request, response, userAccount, serverInfo, mainServerInfo);
                    }

                    updateUser(userAccount.getId(), playGameBean.getGameId(), playGameBean.getGameZoneId());
                } else {
                    writeClient(out, msg, jsoncallback);
                }

            } catch (EnterGameException eg) {
                msg.build(eg.getCode(), eg.getMsg());
                writeClient(out, msg, jsoncallback);
            }  catch (Exception e) {
                msg.build(501, "服务器异常");
                if (Strings.isNullOrEmpty(jsoncallback)) {
                    response.setStatus(404);
                } else {
                    writeClient(out, msg, jsoncallback);
                }
            } finally {
                out.flush();
                out.close();
            }
        }
    }

    @Override
    protected void doGet(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void writeClient(PrintWriter writer, Msg msg, String jsoncallback) {
        ObjectMapper mapper = new ObjectMapper();
        String resp = "";
        if (!Strings.isNullOrEmpty(jsoncallback)) {
            try {
                resp = jsoncallback + "(" + mapper.writeValueAsString(msg) + ")";
            } catch (Exception e) {
                resp = jsoncallback + "(" + "{\"code\":\"505\",\"msg\":\"服务器异常\"}" + ")";
            }
        } else {
            try {
                resp = mapper.writeValueAsString(msg);
            } catch (Exception e) {
                resp = "{\"code\":\"505\",\"msg\":\"服务器异常\"}";
            }
        }
        writer.write(resp);
    }

    private void updateUser(int uid, int gameId, int gameZoneId) {
        UserService userService = getInstance(UserService.class);
        UserObject user = new UserObject();
        user.setId(uid);
        user.setLastGameId(gameId);
        user.setLastGameZoneId(gameZoneId);
        user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        userService.updateGameData(user);
    }
}
