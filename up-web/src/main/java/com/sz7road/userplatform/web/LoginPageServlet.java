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
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class LoginPageServlet extends HeadlessHttpServlet {

    /**
     * 处理请求方法。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String url=request.getParameter("fromUrl");
        request.setAttribute("fromUrl",url);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
