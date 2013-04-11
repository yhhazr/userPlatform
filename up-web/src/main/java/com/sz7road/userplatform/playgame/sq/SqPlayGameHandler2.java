package com.sz7road.userplatform.playgame.sq;

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
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.web.exception.EnterGameException;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.ConfigurationUtils;
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
 * Date: 12-8-9
 * Time: 上午9:43
 */
public class SqPlayGameHandler2 extends Injection implements PlayGameHandler {

    public final static String aliasName = "sq";

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response) {
        int status = request.getIntParameter("status");    //等于0需要验证；大于0 状态判断
        String sign = request.getParameter("sign");
        String jsoncallback = request.getParameter("jsoncallback");
        PrintWriter writer = null;
        Msg msg = new Msg(0, "进入游戏初始化");
        try {
            writer = response.getWriter();
            PlayGameBean playGameBean = getInstance(SqPlayGameBean.class);
            if (!playGameBean.isAvailableForSubmit()) {
                throw new EnterGameException(401, "参数验证不通过");
            }
            boolean flag = isCheckStatus(sign, playGameBean);

            ServerTable.ServerEntry serverEntry = getServerEntry(playGameBean, flag);

            if (status != 0) {
                UserAccount userAccount = getUserAccount(playGameBean.getUserName());
                boolean result = requestStatus(userAccount, serverEntry);
                if (!result) {
                    msg.build(502, "进入游戏入口关闭");
                }
            } else {
                String user = AppHelper.getUserName(request);
                UserAccount account = getUserAccount(user);
                if (Strings.isNullOrEmpty(jsoncallback)) {
                    enterGame(account, serverEntry, response);
                } else {
                    String _rs = request.getParameter("_rs");
                    if (!Strings.isNullOrEmpty(_rs) && "registers".equals(_rs)) {
                        String enterGameUrl = getEnterGameUrl(account, serverEntry);
                        msg.setObject(enterGameUrl);
                    }
                }
                updateUser(account.getId(), playGameBean.getGameId(), playGameBean.getGameZoneId());
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

    public boolean requestStatus(UserAccount userAccount, ServerTable.ServerEntry serverEntry) throws EnterGameException {
//        String enterGameUrl = getEnterGameUrl(userAccount, serverEntry);
//        if (!Strings.isNullOrEmpty(enterGameUrl)) {
//            return true;
//        }

        return true;
    }

    private boolean isCheckStatus(String sign, PlayGameBean playGameBean) {
        boolean flag = false;
        if (!Strings.isNullOrEmpty(sign)) {
            int gid = playGameBean.getGameId();
//            int zid = playGameBean.getGameZoneId();
            StringBuilder sb = new StringBuilder();
            //gid=gid|zid=zid|key.append("zid=").append(zid).append("|")
            sb.append("gid=").append(gid).append("|");
            String enterKey = ConfigurationUtils.get("game.test.enterKey");
            sb.append(enterKey);
            String cSign = MD5Utils.digestAsHex(sb.toString());
            flag = sign.equals(cSign);
        }
        return flag;
    }

    public void enterGame(UserAccount userAccount, ServerTable.ServerEntry serverEntry, HeadlessServletResponse response) throws EnterGameException {

        final Map<String, Object> param = Maps.newHashMap();
        final String key = GameWithServerService.getServerValueByKey(serverEntry, "sq.loginKey");
        String sign = getSign(param, userAccount, key);
        param.put("sign", sign);
        param.put("sid", serverEntry.getServerNo());
        final String loginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.loginUrl");
        Headend.redirectForm(response, loginUrl, "POST", param);
    }

    private String getSign(Map<String, Object> param, UserAccount userAccount, String key) {
        if (userAccount == null) {
            return null;
        }
        //把当前时间戳转换成秒
        final long time = System.currentTimeMillis() / 1000l;
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

    public ServerTable.ServerEntry getServerEntry(PlayGameBean playGameBean, boolean flag) throws EnterGameException {
        GameWithServerUtils playGameService = getInstance(GameWithServerUtils.class);
        ServerTable.ServerEntry serverEntry = playGameService.getServerEntry(playGameBean.getGameId(), playGameBean.getGameZoneId());
        if (serverEntry == null) {
            throw new EnterGameException(404, "游戏服务区尚未开区");
        } else {
            if (flag) {
            } else {
                GameWithServerService gameWithServerService = getInstance(GameWithServerService.class);
                if (!gameWithServerService.isAvaiableServer(playGameBean.getGameId(), playGameBean.getGameZoneId())) {
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
                if (!AppHelper.isOpen(serverEntry.getOpeningTime())) {
                    throw new EnterGameException(405, "游戏服务器还未开放");
                }
            }
        }
        return serverEntry;
    }

    public String getEnterGameUrl(UserAccount userAccount, ServerTable.ServerEntry serverEntry) throws EnterGameException {
        String loginKey = GameWithServerService.getServerValueByKey(serverEntry, "sq.createLoginKey");
        String createLoginUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.createLoginUrl");
        String site = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.site");
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
        String createLoginUrl_str = createLoginUrl + "?content=" + sb.toString() + "&site=" + site;
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("content", sb.toString()));
        qparams.add(new BasicNameValuePair("site", site));
//        createLoginUrl = "http://s1.shenquol.com/createlogin";
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
                String playGameUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.game.playGameUrl");
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

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response, UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws IOException {
        String jsoncallback = request.getParameter("jsoncallback");
        String _rs = request.getParameter("_rs");

        if (Strings.isNullOrEmpty(jsoncallback)) {
            // 网页进入游戏，直接输出post表单进入游戏
            enterGame(userAccount, serverInfo, mainServerInfo, response);
        } else {
            // flash 客户端进入游戏，直接回写进入游戏URL
            if (!Strings.isNullOrEmpty(_rs) && "registers".equals(_rs)) {
                String enterGameUrl = getEnterGameUrl(userAccount, serverInfo, mainServerInfo);
                Msg msg = new Msg(0, "进入游戏初始化");
                msg.setObject(enterGameUrl);
                writeClient(response.getWriter(), msg, jsoncallback);
            }
        }

    }

    public void enterGame(UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo, HeadlessServletResponse response) {
        final Map<String, Object> param = Maps.newHashMap();
        String key = ConfigurationUtils.get(aliasName + ".loginKey");
        String sign = getSign(param, userAccount, key);
        param.put("sign", sign);
        param.put("sid", serverInfo.getServerNo());
        String loginUrl = String.format(ConfigurationUtils.get(aliasName + ".loginUrl"), mainServerInfo.getServerNo());
        Headend.redirectForm(response, loginUrl, "POST", param);
    }

    public String getEnterGameUrl(UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws EnterGameException {
        String loginKey = ConfigurationUtils.get(aliasName + ".createLoginKey");
        String createLoginUrl = String.format(ConfigurationUtils.get(aliasName + ".createLoginUrl"), mainServerInfo.getServerNo());
        String site = String.format(ConfigurationUtils.get(aliasName + ".site"), serverInfo.getServerNo());

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
                String playGameUrl = String.format(ConfigurationUtils.get(aliasName + ".game.playGameUrl"), mainServerInfo.getServerNo());
                String redirectURL = playGameUrl + sb.toString();
                return redirectURL;
            }
        }
        return null;
    }
}
