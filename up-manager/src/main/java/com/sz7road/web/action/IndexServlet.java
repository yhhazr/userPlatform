package com.sz7road.web.action;

import com.google.inject.Singleton;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.manager.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-20
 * Time: 下午5:53
 */
@Singleton
public class IndexServlet extends BaseServlet {

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            String sid = (String) session.getAttribute("sid");
            Map<String, Map<String, Object>> user = SessionManager.get(sid);
            if (user == null) {
                response.setStatus(404);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("user", user);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            response.setStatus(404);
        }

    }
}
