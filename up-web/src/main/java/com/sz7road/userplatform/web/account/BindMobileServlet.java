package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
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
class BindMobileServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BindMobileServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    public BindMobileServlet() {
        super();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _uname = AppHelper.getUserName(request);
        int id = AppHelper.getUserId(request);
        String type = request.getParameter("type");
        //String userName = request.getParameter("userName");
        String forward = "/account/mobile/index.jsp";

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

        UserObject userObject = service.findByAccount(userAccount);
        String mobile = userObject.getMobile();
        boolean isSetMobile = false;
        if( mobile != null && !"".equals(mobile)) {
            if (mobile.length() > 10) {
                mobile = mobile.substring(0, 3) + "******" + mobile.substring(9, 11);
            }
            request.setAttribute("showMobile", mobile);
            isSetMobile = true;
        }

        if ("bind".equals(type) && !isSetMobile) {
            forward = "/account/mobile/bindmobile.jsp";
        }
        if ("unbind".equals(type) && isSetMobile) {
            forward = "/account/mobile/unbindmobile.jsp";
        }
        request.setAttribute("user", userAccount);
        request.setAttribute("userObject", userObject);
        request.setAttribute("isSetMobile", isSetMobile);
        request.getRequestDispatcher(forward).forward(request, response);
    }
}
