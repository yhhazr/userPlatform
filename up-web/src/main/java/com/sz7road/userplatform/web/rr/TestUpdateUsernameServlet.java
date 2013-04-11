package com.sz7road.userplatform.web.rr;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiangfan.zhou
 */

@Singleton
class TestUpdateUsernameServlet extends HeadlessHttpServlet {

    private final static Logger log = LoggerFactory.getLogger(TestUpdateUsernameServlet.class.getName());

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        String currentname = request.getParameter("currentname");
        String currentsite = request.getParameter("currentsite");
        String s = request.getParameter("s");
        String tosite = request.getParameter("tosite");
        String strGameId = request.getParameter("gameId");
        int gameId = 1;
        String k = request.getParameter("k");

        try{
            gameId = Integer.parseInt(strGameId);
        } catch (Exception e){}

        String debug = request.getParameter("_debug");
        String domainHost = request.getParameter("domainHost");

        String key = "68005ba4-1a7f-4bed-a220-cf375393bfc5";
        String sKey = DigestUtils.md5Hex(currentname + currentsite + s + tosite + key);
        if (!Strings.isNullOrEmpty(strGameId) && !"1".equals(strGameId)) {
            sKey = DigestUtils.md5Hex(currentname + currentsite + s + tosite + gameId + key);
        }

        if (currentname == null) {
            currentname = "";
        }

        if (currentsite == null) {
            currentsite = "";
        }

        if (s == null) {
            s = "";
        }

        if (tosite == null) {
            tosite = "";
        }

        if (k == null) {
            k = "";
        }

        if (domainHost == null) {
            domainHost = "";
        }

        log.info("currentname={},currentsite={},s={},tosite={},gameId={},k={},sKey={},s==sKey?{}",new Object[]{currentname,currentsite,s,tosite,gameId,k, sKey,k.equals(sKey) });

        if (!"true".equals(debug)) {
            response.getWriter().println("<script>alert('维护中.');</script>");
            return;
        }

        String domain = "http://localhost:8080/updatename";
        if (!"".equals(domainHost)) {
            domain = domainHost;
        }
        String registerUrl = domain + "?currentname=" + currentname + "&currentsite=" + currentsite + "&s=" + s + "&tosite=" + tosite + "&gameId=" + gameId + "&k=" + sKey;

        request.setAttribute("currentname", currentname);
        request.setAttribute("currentsite", currentsite);
        request.setAttribute("s", s);
        request.setAttribute("tosite", tosite);
        request.setAttribute("gameId",gameId);
        request.setAttribute("k", k);
        request.setAttribute("sKey", sKey);
        request.setAttribute("registerUrl", registerUrl);
        request.setAttribute("result", k.equals(sKey));

        log.info("registerUrl={},result={}",new Object[]{registerUrl,k.equals(sKey) });

        request.getRequestDispatcher("reregister/_test_input.jsp").forward(request, response);
    }
}
