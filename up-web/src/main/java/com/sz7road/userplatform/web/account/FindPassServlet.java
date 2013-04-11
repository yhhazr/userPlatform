package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.pojos.Question;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.web.utils.HideMobileEmailUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Singleton
class FindPassServlet extends HeadlessHttpServlet {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FindPassServlet.class.getName());
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;
    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String _uname = AppHelper.getUserName(request);
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        String userName = request.getParameter("userName");
        String verify = request.getParameter("verify");
        String code = request.getParameter("code");
        String method = request.getMethod();

        if (Strings.isNullOrEmpty(method) || !"POST".equals(method.toUpperCase())) {
            response.setStatus(404);
            return;
        }

        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(verify) || Strings.isNullOrEmpty(code)) {
            response.getWriter().println("<script>location.href='forget.html';</script>");
            return;
        }

        VerifyCodeProvider verifyService = verifyCodeProvider.get();
        if (!verifyService.checkVerifyCode("verify_" +verify, code)) {
            //response.getWriter().println("<script>alert('验证码错误');location.href='forget.html';</script>");
            response.getWriter().println("<script>alert('验证码错误');location.href='forget.html';</script>");
            return;
        }
        // 测试使用
        /*if (verifyService.getVerifyCode(verify, code) == null ) {
            response.getWriter().println("<script>alert('验证码错误');location.href='forget.html';</script>");
            return;
        }*/

        UserService userService = userServiceProvider.get();
        UserAccount userAccount = userService.findAccountByUserName(userName);
        if (userAccount == null) {
            response.getWriter().println("<script>alert('用户名不存在');location.href='forget.html';</script>");
            return;
        }
        UserObject userObject = userService.findByAccount(userAccount);
        List<Question> list = userService.getLastQuestions(userAccount.getId(), 3);
        boolean isSetQuestion = false;
        if (list != null && list.size() > 0) {
            isSetQuestion = true;
        }
        boolean isSetEmail = false;
        if (userAccount.getEmail() != null && !userAccount.getEmail().trim().equals("")) {
            isSetEmail = true;
        }
        boolean isSetMobile = false;
        if (userObject.getMobile() != null && !userObject.getMobile().trim().equals("")) {
            isSetMobile = true;
        }

        String mobile = userObject.getMobile();
        if( mobile != null && !"".equals(mobile)) {
            mobile = HideMobileEmailUtils.getWildMobile(mobile);
            request.setAttribute("showMobile", mobile);
            isSetMobile = true;
        }
        String email = userAccount.getEmail();
        if (null != email && !email.equals("") && email.indexOf("@") != -1) {
            email = HideMobileEmailUtils.getWildEmail(email);
            request.setAttribute("showEmail", email);
        }

        request.setAttribute("userObject", userObject);
        request.setAttribute("isSetQuestion", isSetQuestion);
        request.setAttribute("isSetEmail", isSetEmail);
        request.setAttribute("isSetMobile", isSetMobile);
        request.setAttribute("showMobile", mobile);
        request.setAttribute("showEmail", email);
        request.getRequestDispatcher("/account/forget/findway.jsp").forward(request, response);
    }
}