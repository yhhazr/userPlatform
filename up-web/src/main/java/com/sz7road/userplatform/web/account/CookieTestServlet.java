/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.Base64;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author leo.liao
 */
@Singleton
public class CookieTestServlet extends HeadlessHttpServlet {

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("uname");
        int userId = Integer.parseInt(request.getParameter("uid"));

        if (!Strings.isNullOrEmpty(userName)) {
            long time = System.currentTimeMillis();
            String USERINFO = userId + "," + userName + "," + time;
            String u = Base64.encode(USERINFO + "," + AppHelper.buildUserInfoSign(String.valueOf(userId), userName, String.valueOf(time)));
            u = URLEncoder.encode(u, "utf-8");
            String key = URLEncoder.encode(Base64.encode("USERINFO"), "UTF-8");
            Cookie cookie = new Cookie(key, u);
            cookie.setMaxAge(1800);
            response.addCookie(cookie);
            request.getRequestDispatcher("/account/testCookie.jsp").forward(request, response);
        } else {
            response.setStatus(404);
        }
    }

}
