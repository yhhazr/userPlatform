package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.service.UserManagerService;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.TimeStampUtil;
import com.sz7road.web.pojos.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-12
 * Time: 下午7:57
 * 登录的跳转
 */
@Singleton
public class LoginServlet extends BaseServlet {
    @Inject
    private Provider<UserManagerService> userServiceProvider;

    protected void doServe(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");

        String userName = request.getParameter("username");
//        userName = new String(userName.getBytes(), "utf-8");

        String password = request.getParameter("password");
//        password = new String(password.getBytes(), "utf-8");
        HttpSession session = request.getSession();

        String forward = "login.jsp";

        session.setAttribute("msg", "用户名或者密码错误！");
        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(password)) {
            forward = "login.jsp";
        } else {
            try {
                UserManagerService userService = userServiceProvider.get();
                User user = userService.authenticated(userName, password);
                if (user != null) {
                    forward = "index.jsp";
                    session.setAttribute("msg", "欢迎登录！" + user.getUsername());
                    user.setLastLoginTime(TimeStampUtil.getNowTimeStamp());
                    userService.updateLastLoginTime(user);
                    session.setAttribute("user", user);
                }
            } catch (Exception ex) {
                forward = "login.jsp";
                session.setAttribute("msg", ex.getMessage());
                ex.printStackTrace();
            }

        }
        try {
            request.getRequestDispatcher("/" + forward).forward(request, response);
        } catch (ServletException e) {
        } catch (IOException e) {
        }

    }
}
