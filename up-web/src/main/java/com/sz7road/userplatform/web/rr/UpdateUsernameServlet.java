package com.sz7road.userplatform.web.rr;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangfan.zhou
 */

@Singleton
class UpdateUsernameServlet extends HeadlessHttpServlet {

    private final static Logger log = LoggerFactory.getLogger(UpdateUsernameServlet.class.getName());

    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        String currentname = request.getParameter("currentname");
        String currentsite = request.getParameter("currentsite");
        String s = request.getParameter("s");
        String tosite = request.getParameter("tosite");
        String k = request.getParameter("k");
        String strGameId = request.getParameter("gameId");
        int gameId = 1;
        String debug = request.getParameter("_debug");

        try{
            if (!Strings.isNullOrEmpty(strGameId))
                gameId = Integer.parseInt(strGameId);
        } catch (Exception e){}

        String key = "68005ba4-1a7f-4bed-a220-cf375393bfc5";
        String sKey = DigestUtils.md5Hex(currentname + currentsite + s + tosite + key);
        if (!Strings.isNullOrEmpty(strGameId) && !"1".equals(strGameId)) {
            sKey = DigestUtils.md5Hex(currentname + currentsite + s + tosite + gameId + key);
        }

        log.info("currentname={},currentsite={},s={},tosite={},gameId={},k={},sKey={},s==sKey?{}",new Object[]{currentname,currentsite,s,tosite,gameId,k, sKey,k.equals(sKey) });

        if (Strings.isNullOrEmpty(currentname) || Strings.isNullOrEmpty(currentsite) || Strings.isNullOrEmpty(s)
                || Strings.isNullOrEmpty(tosite) || Strings.isNullOrEmpty(k) || !sKey.equals(k) || tosite.equals(currentsite)) {
            response.getWriter().println("<script>alert('参数异常');location.href='login.html';</script>");
            return;
        }

        Matcher matcherSite = Pattern.compile("7road_(\\d+)").matcher(tosite);
        String serverNo = "";
        if (matcherSite.find() && matcherSite.groupCount() == 1){
            serverNo = matcherSite.group(1);

            GameWithServerService gameServerService = gameWithServerServiceProvider.get();
            GameTable gameTable = gameServerService.getGameTable();
            ServerTable serverTable = gameTable.getServerTable(gameId);

            int serverId = 0;
            if (serverTable != null){
                for(ServerTable.ServerEntry entry : serverTable.values()) {
                    if (Integer.parseInt(serverNo) == entry.getServerNo()) {
                        serverId = entry.getId();
                        break;
                    }
                }
            }
            request.setAttribute("game","S");
            request.setAttribute("subGame","0");
            request.setAttribute("g",gameId);
            request.setAttribute("z",serverId);
        }

        request.getRequestDispatcher("reregister/register.jsp").forward(request, response);
    }
}
