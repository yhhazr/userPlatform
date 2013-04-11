package com.sz7road.userplatform.web;

import com.google.inject.Singleton;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-21
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class LogoutServlet extends HeadlessHttpServlet {
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0)
        for (Cookie cookie : cookies)
        {
           cookie.setMaxAge(0);
            cookie.setDomain(".7road.com");
           cookie.setPath("/");
           response.addCookie(cookie);
        }
    }
}
