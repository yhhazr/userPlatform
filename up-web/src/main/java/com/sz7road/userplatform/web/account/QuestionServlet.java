package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.Question;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * 验证获取密保问题
 * @author jiangfan.zhou
 */

@Singleton
class QuestionServlet extends HeadlessHttpServlet {
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<LogService> logServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _uname = AppHelper.getUserName(request);
        int id = AppHelper.getUserId(request);
        String type = request.getParameter("type");
        String forward = "/account/question/index.jsp";
        if (Strings.isNullOrEmpty(_uname) || id == 0) {
            response.setStatus(404);
            return;
        }

        boolean isSetQuestion = false;
        UserService service = userServiceProvider.get();
        List<Question> list = service.getLastQuestions(id, 3);
        if (list != null && list.size() > 0) {
            isSetQuestion = true;
        }

        if ("bind".equals(type)) {
            if (isSetQuestion == false) {
                forward = "/account/question/bindquestion.jsp";
            } else {
                forward = "/account/question/rebindquestion.jsp";
                Collections.shuffle(list);
                request.setAttribute("list", list);
            }
        }

        request.setAttribute("isSetQuestion",isSetQuestion);
        request.getRequestDispatcher(forward).forward(request, response);
    }
}
