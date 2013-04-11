package com.sz7road.userplatform.web.account;

import com.google.inject.Singleton;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author leo.liao
 */

@Singleton
class ForgetPwdServlet extends HeadlessHttpServlet {

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/account/forgetpwd.jsp").forward(request, response);
    }

}
