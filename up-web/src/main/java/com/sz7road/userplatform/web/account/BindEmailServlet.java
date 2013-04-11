package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiangfan.zhou
 */
@Singleton
class BindEmailServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BindEmailServlet.class.getName());

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    public BindEmailServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _uname = AppHelper.getUserName(request);
        //String userName = request.getParameter("userName");
        String type = request.getParameter("type");
        int id = AppHelper.getUserId(request);
        String forward = "/account/mail/index.jsp";

        if (Strings.isNullOrEmpty(_uname) || id == 0) {
            response.setStatus(404);
            return;
        }

        UserService service = userServiceProvider.get();
        UserAccount userAccount = service.findAccountById(id);
        if (userAccount == null) {
            response.setStatus(404);
            return;
        }

        if(type == null) {
            boolean isSetEmail = false;
            if (userAccount.getEmail() != null && !userAccount.getEmail().equals("")) {
                isSetEmail = true;
            }
            request.setAttribute("isSetEmail",isSetEmail);
        } else if ("info".equals(type)){
            if (userAccount.getEmail() == null || userAccount.getEmail().equals("")) {
                forward = "/account/mail/bindemail.jsp";
            }
            if (userAccount.getEmail() != null && !userAccount.getEmail().equals("")) {
                forward = "/account/mail/info.jsp";
            }
        } else {
            if (userAccount.getEmail() == null || userAccount.getEmail().equals("")) {
                forward = "/account/mail/bindemail.jsp";
            }
            if (userAccount.getEmail() != null && !userAccount.getEmail().equals("")) {
                forward = "/account/mail/unbindemail.jsp";
            }
        }

        String email = userAccount.getEmail();
        if (null != email && !email.equals("") && email.indexOf("@") != -1) {
            int index = email.indexOf("@");
            if( index > 2) {
                email = email.substring(0, 3) + "*****" + email.substring(index);
            } else {
                email = email.substring(0, 1) + "*****" + email.substring(index);
            }
            request.setAttribute("showEmail", email);
        }
        request.setAttribute("user", userAccount);
        request.getRequestDispatcher(forward).forward(request, response);
    }
}
