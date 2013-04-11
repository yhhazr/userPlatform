package com.sz7road.userplatform.playgame.sq;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.playgame.PlayGameBean;
import com.sz7road.userplatform.playgame.PlayGameHandler;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.ServerMaintainService;
import com.sz7road.userplatform.service.UserService;
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
import util.GameWithServerUtils;
import util.HttpClientUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 下午2:19
 */
@RequestScoped
public class SqPlayGameHandler extends Injection implements PlayGameHandler {
    @Inject
    private Provider<GameWithServerUtils> playGameServiceProvider;
    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response) {
        PlayGameBean playGameBean = getInstance(SqPlayGameBean.class);
        String jsoncallback = request.getParameter("jsoncallback");
        int status = request.getIntParameter("status");    //等于0>>需要验证；大于0>>状态判断
        PrintWriter writer = null;
        Msg msg = new Msg();
        try {
            writer = response.getWriter();
            if (!playGameBean.isAvailableForSubmit()) {
                throw new EnterGameException(401, "参数验证不通过");
            }
            UserService userService = getInstance(UserService.class);
            UserAccount userAccount = null;
            if (status == 0) {
                if (Strings.isNullOrEmpty(playGameBean.getUserName())) {
                    throw new EnterGameException(402, "未登陆");
                }
                userAccount = userService.findAccountByUserName(playGameBean.getUserName());
                if (userAccount == null) {
                    throw new EnterGameException(402, "用户不存在");
                }
            }
            GameWithServerUtils playGameService = getInstance(GameWithServerUtils.class);
            ServerTable.ServerEntry serverEntry = playGameService.getServerEntry(playGameBean.getGameId(), playGameBean.getGameZoneId());
            if (serverEntry == null) {
                throw new EnterGameException(404, "尚未开区");
            }
            GameWithServerService gameWithServerService = getInstance(GameWithServerService.class);
            if (!gameWithServerService.isAvaiableServer(playGameBean.getGameId(), playGameBean.getGameZoneId())) {
                ServerMaintainService serverMaintainService = getInstance(ServerMaintainService.class);
                String str = "游戏服务器正在维护";
                Map<Integer,ServerMaintain> serverMaintainMap = serverMaintainService.getMaintainServerInfo();
                ServerMaintain serverMaintain = null;
                if(serverMaintainMap != null){
                    serverMaintain = serverMaintainMap.get(playGameBean.getGameZoneId());
                }
                if (serverMaintain != null) {
                    str = serverMaintain.getMessage();
                }
                throw new EnterGameException(403, str);
            }
            if (!AppHelper.isOpen(serverEntry.getOpeningTime())) {
                throw new EnterGameException(405, "游戏服务器还未开放");
            }
            //跨域请求处理
            if (!Strings.isNullOrEmpty(jsoncallback)) {
                String _rs = request.getParameter("_rs");
                //登录器请求处理
                if (!Strings.isNullOrEmpty(_rs) && "registers".equals(_rs)) {
                    buildRegistersMsg(userAccount, serverEntry, msg);
                    if (msg.getObject() != null) {
                        updateUser(userAccount.getId(), playGameBean.getGameId(), playGameBean.getGameZoneId(), userService);
                    }
                }
                writeClient(writer, msg, jsoncallback);
                return;
            } else if (status > 0) {    //不是跨域请求直接输出
                writeClient(writer, msg, null);
            } else {
                final Map<String, Object> param = Maps.newHashMap();
                final String key = GameWithServerService.getServerValueByKey(serverEntry, "sq.loginKey");
                String sign = getSign(param, userAccount, key);
                param.put("sign", sign);
                param.put("sid", serverEntry.getServerNo());
                final String loginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.loginUrl");
                Headend.redirectForm(response, loginUrl, "POST", param);
                updateUser(userAccount.getId(), playGameBean.getGameId(), playGameBean.getGameZoneId(), userService);
            }
        } catch (EnterGameException e) {
            msg.build(e.getCode(), e.getMsg());
            writeClient(writer, msg, jsoncallback);
        } catch (IOException ex) {
            msg.build(501, "服务器异常");
            if (Strings.isNullOrEmpty(jsoncallback)) response.setStatus(404);
            else writeClient(writer, msg, jsoncallback);
        } finally {
            if (writer != null) writer.close();
        }
    }

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response, UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
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

    private void updateUser(int uid, int gameId, int gameZoneId, UserService userService) {
        UserObject user = new UserObject();
        user.setId(uid);
        user.setLastGameId(gameId);
        user.setLastGameZoneId(gameZoneId);
        user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        userService.updateGameData(user);
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

    private void buildRegistersMsg(UserAccount userAccount, ServerTable.ServerEntry serverEntry, Msg msg) {
        String loginKey = GameWithServerService.getServerValueByKey(serverEntry, "sq.createLoginKey");
        String createLoginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.createLoginUrl");
        String site = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.site");
        String playGameUrl = loginUrl(userAccount, loginKey, serverEntry, createLoginUrl, site);
        msg.setObject(playGameUrl);
    }

    private String loginUrl(UserAccount userAccount, String loginKey, ServerTable.ServerEntry serverEntry, String createLoginUrl, String site) {
        StringBuffer sb = new StringBuffer();
        String password = UUID.randomUUID().toString();
        String time = Long.toString(System.currentTimeMillis());
        int userId = userAccount.getId();
        sb.append(userId);
        sb.append('|');
        sb.append(password);
        sb.append('|');
        sb.append(time);
        sb.append('|');
        //user+password+time + key 
        //签名
        String sign = MD5Utils.digestAsHex(userId + password + time + loginKey);
        sb.append(sign);
        //拼URL
        String createLoginUrl_str = createLoginUrl + "?content=" + sb.toString() + "&site=" + site;
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("content", sb.toString()));
        qparams.add(new BasicNameValuePair("site", site));
//        createLoginUrl = "http://s1.shenquol.com/createlogin";
        String status = HttpClientUtil.getContent(createLoginUrl, qparams);

        //验证状态
        /*0	代理商登陆成功
              1	其他错误
              2	参数个数不对
              3	无效IP 
              4	Site参数不对，没有此代理
              5	Sign错误
              6	注册服务不正确*/
        //如果成功 跳转   //其他状态还没写
        if ("0".equals(status)) {
            sb = new StringBuffer();
            sb.append("?user=");
            sb.append(userId);
            sb.append("&key=");
            sb.append(password);
            sb.append("&site=");
            sb.append(site);
            String playGameUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.game.playGameUrl");
            String redirectURL = playGameUrl + sb.toString();
            return redirectURL;
        }
        return null;
    }
}
