package com.sz7road.web;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.servlet.GuiceFilter;
import com.sz7road.web.manager.SessionManager;
import com.sz7road.web.utils.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 对所有的请求进行session的验证（特殊的url除外）
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午9:50
 */
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
public class SessionFilter extends GuiceFilter {

    private static final Logger log = LoggerFactory.getLogger(SessionFilter.class);


    final static HashMap<String, String[]> EXCLUDE_URL = Maps.newHashMap();

    static {
//        EXCLUDE_URL.put("/user", new String[]{"login"});
//        EXCLUDE_URL.put("/login", null);
        EXCLUDE_URL.put("/master", null);
//        EXCLUDE_URL.put("/login.html", null);
        EXCLUDE_URL.put("/ServerMaintainNotForSq", null);
        EXCLUDE_URL.put("/GameWithServerNotForSq", null);
        EXCLUDE_URL.put("/GameWithServer", null);
        EXCLUDE_URL.put("/getRoleRankUrl", null);
        EXCLUDE_URL.put("/getEnterGameServerList", null);
        EXCLUDE_URL.put("/getEnterGameServerListNotForSq", null);
        EXCLUDE_URL.put("/MaintainServer", null);
        EXCLUDE_URL.put("/RechargeTest", null);
        EXCLUDE_URL.put("/showIdCardImg", null);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath().toLowerCase();
        if (path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".ico")) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        String sid = (String) session.getAttribute("sid");
        String action = request.getParameter("action");
        Map<String, Map<String, Object>> user = SessionManager.get(sid);
        if (user == null && !isExcludeUrl(request.getServletPath(), action)) {
            authFailed(request, response);
            return;
        } else {
            boolean flag = checkPermissions(request, action);
            if (flag) { //认证成功，或者页面跳转的action,执行下一步
                filterChain.doFilter(request, response);
            } else { //否则，弹出提示，说明无权限进入该页面。
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write("<script>alert('您没有权限使用该功能，请联系主数据管理人员!');</script>");
                out.flush();
                out.close();
            }
        }
    }

    private boolean checkPermissions(HttpServletRequest request, String action) {
        boolean flag = false;

        HttpSession session = request.getSession(true);

        Map<String, Object> permissions = (Map<String, Object>) session.getAttribute("permission");

        if (permissions != null && permissions.containsKey(action)) {
            flag = true;
        } else {
            if (DataUtil.getAllPermissions().containsKey(action)) {//所有权限里有的，说明没有权限
                flag = false;
            } else { //所有权限里没有的，说明是公共的操作。
                flag = true;
            }
        }
        return flag;
    }


    protected boolean isExcludeUrl(String servletPath, String action) {
        if (EXCLUDE_URL.containsKey(servletPath) && EXCLUDE_URL.get(servletPath) == null) {
            return true;
        }
        if (Strings.isNullOrEmpty(action)) {
            if (EXCLUDE_URL.containsKey(servletPath)) {
                return true;
            }
        }
        String[] actions = EXCLUDE_URL.get(servletPath);
        if (actions != null) {
            for (String act : actions) {
                if (act.equals(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void authFailed(HttpServletRequest request, HttpServletResponse response) {
        try {
//            response.sendRedirect("/login.html");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (Exception ex) {
            log.error("跳转到登录页异常！");
        }
    }

}
