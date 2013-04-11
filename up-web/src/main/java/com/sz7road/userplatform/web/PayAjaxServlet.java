/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTable.ServerEntry;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpClientUtil;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;

@Singleton
class PayAjaxServlet extends HeadlessHttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PayServlet.class.getName());

    //获取服务器的type
    private static final String SERVERLIST = "serverList";
    //获取角色的type
    private static final String ROLE = "role";

    private static final String UUD = "UUID";
    @Inject
    private Provider<PayManager> payManmagerProvider;
    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;
    @Inject
    private Provider<UserService> userServiceProvider;

    /**
     * 获取角色
     *
     * @param request
     * @param out
     * @return
     */
    private Map<String, String> getRole(HttpServletRequest request, PrintWriter out) throws Exception {
        Map<String, String> roleList = new HashMap<String, String>();
        try {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            String username = request.getParameter("username");
            String serverId_str = request.getParameter("serverId");
            String gameId_str = request.getParameter("gameId");
            int serverNo = 0;
            String serverSite = "";

            int gameId = 1;
            int serverId = 1;
            if (Strings.isNullOrEmpty(gameId_str)) {
                logger.error("gameId is null");
            } else {
                gameId = Integer.parseInt(gameId_str);
            }

            if (Strings.isNullOrEmpty(serverId_str)) {
                logger.error("serverId  is null");
            } else {
                serverId = Integer.parseInt(serverId_str);
            }

            GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
            GameTable gameTable = gameWithServerService.getGameTable();

            ServerTable serverTable = gameTable.getServerTable(gameId);
            ServerEntry serverEntry = serverTable.get(serverId);

            serverNo = serverEntry.getServerNo();
            serverSite = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.site");

            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(username);
            int userId = userAccount.getId();

            pairs.add(new BasicNameValuePair("username", Integer.toString(userId)));
            pairs.add(new BasicNameValuePair("site", serverSite));

            String url = ConfigurationUtils.get("game.shenqu.domainUrl");
            String roleUrl = url.replaceAll("%d", Integer.toBinaryString(serverNo));

            String xml = HttpClientUtil.getContent(roleUrl, pairs);
            //解析xml
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            String roleName = "";
            String roleId = "";
            Iterator<?> it = root.elements().iterator();

            while (it.hasNext()) {
                Element info = (Element) it.next();
                roleName = info.attributeValue("NickName");
                roleId = info.attributeValue("ID");
                roleList.put(roleId, URLDecoder.decode(roleName, "UTF-8"));//取角色名
            }
            out.print("<option value='0'>请选择角色</option>");
            Set<String> roleSet = roleList.keySet();
            for (String role : roleSet) {
                out.print("<option value=" + role + ">" + roleList.get(role) + "</option>");
                logger.info(role);
            }
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            logger.error("Get role faile");
        }
        return roleList;
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //type用于判断请求的类型
        String type = request.getParameter("type");

        if (Strings.isNullOrEmpty(type)) {
            return;
        }

        //游戏id
        String gameId_str = request.getParameter("gameId");
        if (Strings.isNullOrEmpty(gameId_str)) {
            gameId_str = "1";
        }
        int gid = Integer.valueOf(gameId_str);
        //设置游戏id
        request.setAttribute("gameId", gid);
        //设置游戏图片
        request.setAttribute("gameSrc", "cz_cen_youxi.jpg");

        try {
            //获取游戏服务器列表
            if (type.equals(SERVERLIST)) {
                getGameServer(request, out, gid);
            }
            //角色
            if (type.equals(ROLE)) {
                getRole(request, out);
                return;
            }
            //获取UUID
            if (type.equals(UUD)) {
                String channel = request.getParameter("channel");
                if (Strings.isNullOrEmpty(channel)) {
                    logger.error("channel error");
                    return;
                }
                final PayManager payManager = payManmagerProvider.get();
                String orderId = payManager.nextOrder(channel.charAt(0));
                out.print(orderId);
                out.flush();
                out.close();
                return;
            }

        } catch (Exception e) {
            logger.error("system error");
        }
    }

    private void getGameServer(HttpServletRequest request, PrintWriter out, int gameId) throws Exception {
        GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
        GameTable gameTable = gameWithServerService.getGameTable();
        ServerTable serverTable = gameTable.getServerTable(gameId);
        Set server = serverTable.keySet();
        out.print("<option value=0>请选择服务器</option>");
        Iterator<Integer> iter = server.iterator();
        Integer key = 0;

        while (iter.hasNext()) {
            key = iter.next();
            out.print("<option value=" + key + ">" + serverTable.get(key).getServerName() + "</option>");
        }
        out.flush();
        out.close();
    }

}
