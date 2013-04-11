package com.sz7road.userplatform.playgame.ddt;

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
 * Author: jiangfan.zhou
 */
public class DdtPlayGameHandler extends Injection implements PlayGameHandler {
    public final static String aliasName = "ddt";

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response) {
        throw new IllegalAccessError("禁止访问");
    }

    @Override
    public void process(HeadlessServletRequest request, HeadlessServletResponse response, UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws IOException {
        String jsoncallback = request.getParameter("jsoncallback");
        String _rs = request.getParameter("_rs");

        if (Strings.isNullOrEmpty(jsoncallback)) {
            // 网页进入游戏，直接输出post表单进入游戏
            enterGame(userAccount, serverInfo, response);
        } else {
            // flash 客户端进入游戏，直接回写进入游戏URL
            if (!Strings.isNullOrEmpty(_rs) && "registers".equals(_rs)) {
                String enterGameUrl = getEnterGameUrl(userAccount, serverInfo);
                Msg msg = new Msg(0, "进入游戏初始化");
                msg.setObject(enterGameUrl);
                writeClient(response.getWriter(), msg, jsoncallback);
            }
        }

    }

    public void enterGame(UserAccount userAccount, ServerInfo serverInfo, HeadlessServletResponse response) {
        final Map<String, Object> param = Maps.newHashMap();
        String uname = userAccount.getUserName();
        int uid = userAccount.getId();
        long time = System.currentTimeMillis();
        String key = ConfigurationUtils.get(aliasName + ".loginKey");
        int sid = serverInfo.getServerNo();

        String sign = MD5Utils.digestAsHex(time + "" + uid + uname + key);
        param.put("sign", sign);
        param.put("uname", uname);
        param.put("uid", uid);
        param.put("time", time);
        param.put("sid", sid);
        String loginUrl = String.format(ConfigurationUtils.get(aliasName + ".loginUrl"), serverInfo.getServerNo());
        Headend.redirectForm(response, loginUrl, "POST", param);
    }

    public String getEnterGameUrl(UserAccount userAccount, ServerInfo serverInfo) throws EnterGameException {
        String loginKey = ConfigurationUtils.get(aliasName + ".createLoginKey");
        String createLoginUrl = String.format(ConfigurationUtils.get(aliasName + ".createLoginUrl"), serverInfo.getServerNo());
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
                String playGameUrl = String.format(ConfigurationUtils.get(aliasName + ".game.playGameUrl"), serverInfo.getServerNo());
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
}
