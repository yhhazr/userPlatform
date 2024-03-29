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
 * Date: 12-9-17
 * Time: 下午5:03
 * 注册页面的servlet
 */
@Singleton
public class RegisterPageServlet extends HeadlessHttpServlet {
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String url=request.getParameter("fromUrl");
        request.setAttribute("fromUrl",url);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
