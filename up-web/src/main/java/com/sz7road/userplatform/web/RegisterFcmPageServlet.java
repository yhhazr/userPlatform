package com.sz7road.userplatform.web;

import com.google.inject.Singleton;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-22
 * Time: 下午3:46
 * 防沉迷注册页面跳转
 */
   @Singleton
public class RegisterFcmPageServlet extends HeadlessHttpServlet{
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register_fcm.jsp").forward(request, response);
    }
}
