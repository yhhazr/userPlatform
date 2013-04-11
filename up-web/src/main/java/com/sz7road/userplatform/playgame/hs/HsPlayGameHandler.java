package com.sz7road.userplatform.playgame.hs;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.playgame.PlayGameBean;
import com.sz7road.userplatform.playgame.PlayGameHandler;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.ServerMaintainService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.web.exception.EnterGameException;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.Headend;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import util.HttpClientUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * Author: jiangfan.zhou
 */
public class HsPlayGameHandler extends Injection implements PlayGameHandler {

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response) {
        // 之前已经判断游戏类型
        // 为了和之前参数保证一致
        // status = 0, 直接进入游戏, status != 0, 判断区服是否可以使用
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        int status = request.getIntParameter("status");
        String jsoncallback = request.getParameter("jsoncallback");
        String _rs = request.getParameter("_rs");
        int gameId = request.getIntParameter("g");

        PrintWriter writer = null;
        Msg msg = new Msg(0, "进入游戏初始化");
        try {
            writer = response.getWriter();

            GameWithServerService gameService = getInstance(GameWithServerService.class);
            if( gameService.getGameTable().containsKey(gameId) && gameService.getGameTable().get(gameId).getId() != 2
                    || !gameService.getGameTable().containsKey(gameId)) {
                throw new EnterGameException(402, "进入游戏非法参数");
            }

            // 验证游戏区服信息，非正常状态抛异常
            PlayGameBean playGameBean = getInstance(HsPlayGameBean.class);
            ServerTable.ServerEntry serverEntry = getServerEntry(playGameBean);

            if (status == 0){
                // 验证用户有效性，非法会抛出异常
                String user = AppHelper.getUserName(request);
                UserAccount account = getUserAccount(user);

                if (Strings.isNullOrEmpty(jsoncallback)) {
                    // 网页进入游戏，直接输出post表单进入游戏
                    enterGame(account, serverEntry, response);
                } else {
                    // flash 客户端进入游戏，直接回写进入游戏URL
                    if (!Strings.isNullOrEmpty(_rs) && "registers".equals(_rs)) {
                        String enterGameUrl = getEnterGameUrl(account, serverEntry);
                        msg.setObject(enterGameUrl);
                    }
                    updateUser(account.getId(), playGameBean.getGameId(), playGameBean.getGameZoneId());
                }
            }
            writeClient(writer, msg, jsoncallback);
        } catch (EnterGameException eg) {
            msg.build(eg.getCode(), eg.getMsg());
            writeClient(writer, msg, jsoncallback);
        } catch (Exception e) {
            msg.build(501, "服务器异常");
            if (Strings.isNullOrEmpty(jsoncallback)) {
                response.setStatus(404);
            } else {
                writeClient(writer, msg, jsoncallback);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response, UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void enterGame(UserAccount userAccount, ServerTable.ServerEntry serverEntry, HeadlessServletResponse response) throws EnterGameException {
        final Map<String, Object> param = Maps.newHashMap();
        final String key = GameWithServerService.getServerValueByKey(serverEntry, "hs.loginKey");
        String sign = getSign(param, userAccount, key);
        param.put("sign", sign);
        param.put("sid", serverEntry.getServerNo());
        final String loginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "hs.loginUrl");
        Headend.redirectForm(response, loginUrl, "POST", param);
    }

    private String getSign(Map<String, Object> param, UserAccount userAccount, String key) {
        if (userAccount == null) {
            return null;
        }
        long time = System.currentTimeMillis() / 1000;
        param.put("uname", userAccount.getUserName());
        param.put("uid", String.valueOf(userAccount.getId()));
        param.put("time", String.valueOf(time));
        List<String> list = Lists.newArrayList(param.keySet());
        Collections.sort(list);

        final StringBuilder sb = new StringBuilder();
        for (String p : list) {
            sb.append(p).append("=").append(param.get(p));
        }
        String sign = MD5Utils.digestAsHex(sb.toString() + key);
        return sign;
    }

    public UserAccount getUserAccount(String username) throws EnterGameException {
        if (Strings.isNullOrEmpty(username)) {
            throw new EnterGameException(402, "用户未登陆");
        }
        UserService userService = getInstance(UserService.class);
        UserAccount userAccount = userService.findAccountByUserName(username);
        if (userAccount == null) {
            throw new EnterGameException(402, "用户不存在");
        }
        return userAccount;
    }

    public ServerTable.ServerEntry getServerEntry(PlayGameBean playGameBean) throws EnterGameException {
        ServerInfo serverInfo = getServerInfo(playGameBean);
        ServerTable.ServerEntry serverEntry = new ServerTable.ServerEntry();
        serverEntry.setId(serverInfo.getServerId());
        serverEntry.setCreateTime(serverInfo.getCreateTime());
        serverEntry.setGameId(serverInfo.getGameId());
        serverEntry.setOpeningTime(serverInfo.getOpeningTime());
        serverEntry.setRecommand(serverInfo.isRecommand());
        serverEntry.setServerName(serverInfo.getServerName());
        serverEntry.setServerNo(serverInfo.getServerNo());
        serverEntry.setServerStatus(serverInfo.getServerStatus());
        return serverEntry;
    }

    public ServerInfo getServerInfo(PlayGameBean playGameBean) throws EnterGameException {
        //GameWithServerUtils playGameService = getInstance(GameWithServerUtils.class);
        //ServerTable.ServerEntry serverEntry = playGameService.getServerEntry(playGameBean.getGameId(), playGameBean.getGameZoneId());
        ServerDataService service = getInstance(ServerDataService.class);
        ServerInfo serverInfo = service.get(playGameBean.getGameId(), playGameBean.getGameZoneId());
        if (serverInfo == null) {
            throw new EnterGameException(404, "游戏服务区尚未开区");
        } else {
            GameWithServerService gameWithServerService = getInstance(GameWithServerService.class);
            if (!gameWithServerService.isAvaiableServer2(playGameBean.getGameId(), playGameBean.getGameZoneId())) {
                ServerMaintainService serverMaintainService = getInstance(ServerMaintainService.class);
                String str = "游戏服务器正在维护";
                Map<Integer, ServerMaintain> serverMaintainMap = serverMaintainService.getMaintainServerInfo();
                ServerMaintain serverMaintain = null;
                if (serverMaintainMap != null) {
                    serverMaintain = serverMaintainMap.get(playGameBean.getGameZoneId());
                }
                if (serverMaintain != null && !Strings.isNullOrEmpty(serverMaintain.getMessage())) {
                    str = serverMaintain.getMessage();
                }
                throw new EnterGameException(403, str);
            }
            if (!AppHelper.isOpen(serverInfo.getOpeningTime())) {
                throw new EnterGameException(405, "游戏服务器还未开放");
            }
        }
        return serverInfo;
    }

    public String getEnterGameUrl(UserAccount userAccount, ServerTable.ServerEntry serverEntry) throws EnterGameException {
        String loginKey = GameWithServerService.getServerValueByKey(serverEntry, "hs.createLoginKey");
        String createLoginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "hs.createLoginUrl");
        String site = GameWithServerService.getServerFormatValueByKey(serverEntry, "hs.site");
        String password = UUID.randomUUID().toString();

        String time = Long.toString(System.currentTimeMillis());
        int userId = userAccount.getId();
        StringBuilder sb = new StringBuilder();

        sb.append(userId);
        sb.append('|');
        sb.append(password);
        sb.append('|');
        sb.append(time);
        sb.append('|');
        //签名 user+password+time + key
        String sign = MD5Utils.digestAsHex(userId + password + time + loginKey);
        sb.append(sign);
        //拼URL
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("content", sb.toString()));
        qparams.add(new BasicNameValuePair("site", site));
        String status = HttpClientUtil.getContent(createLoginUrl, qparams);

        if (!"0".equals(status)) {
            throw new EnterGameException(502, "进入游戏入口关闭");
        } else {
            //验证状态
            /*
            0	代理商登陆成功
                 1	其他错误
                 2	参数个数不对
                 3	无效IP
                 4	Site参数不对，没有此代理
                 5	Sign错误
                 6	注册服务不正确*/
            //如果成功 跳转   //其他状态还没写
            if ("0".equals(status)) {
                sb = new StringBuilder();
                sb.append("?user=");
                sb.append(userAccount.getId());
                sb.append("&key=");
                sb.append(password);
                sb.append("&site=");
                sb.append(site);
                String playGameUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "hs.game.playGameUrl");
                String redirectURL = playGameUrl + sb.toString();
                return redirectURL;
            }
        }
        return null;
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
        writer.flush();
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
