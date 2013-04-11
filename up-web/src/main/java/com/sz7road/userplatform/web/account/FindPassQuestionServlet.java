package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.web.utils.QuestionUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Singleton
class FindPassQuestionServlet extends HeadlessHttpServlet {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FindPassQuestionServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String method = request.getParameter("method");

        String q1 = request.getParameter("q1");
        String q2 = request.getParameter("q2");
        String q3 = request.getParameter("q3");
        String a1 = request.getParameter("a1");
        String a2 = request.getParameter("a2");
        String a3 = request.getParameter("a3");

        if (Strings.isNullOrEmpty(userName) ||
                Strings.isNullOrEmpty(q1) || Strings.isNullOrEmpty(q2) || Strings.isNullOrEmpty(q3) ||
                Strings.isNullOrEmpty(a1) || Strings.isNullOrEmpty(a2) || Strings.isNullOrEmpty(a3)) {
            response.setStatus(404);
            return;
        }

        UserService userService = userServiceProvider.get();
        UserAccount userAccount = userService.findAccountByUserName(userName);
        if (userAccount == null) {
            response.setStatus(404);
            return;
        }

        Msg msg = new Msg(0, "密保答案不正确");
        List<Question> list = userService.getLastQuestions(userAccount.getId(), 3);
        Map<String, String> mapInput = QuestionUtils.getMap(new Question(q1, a1), new Question(q2, a2), new Question(q3, a3));
        Map<String, String> mapQuery = QuestionUtils.getMap(list);

        boolean isValid = false;
        if(!mapInput.equals(mapQuery)){
            msg.setCode(0);
            msg.setMsg("密保答案不正确");
            log.info("用户[{}]密保问题回答错误", userName);
        } else {
            msg.setCode(1);
            msg.setMsg("密保答案正确");
            isValid = true;
        }

        if (isValid && "modify".equals(method)) {
            String newPass = request.getParameter("newPass");
            if (Strings.isNullOrEmpty(newPass) || newPass.length() < 6) {
                response.setStatus(404);
                return;
            }
            userService.resetPwd(userAccount, newPass);
            msg.setCode(1);
            msg.setMsg("密码重置成功");
            log.info("用户[{}]密保问题重置密码成功", userName);
        }

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        writer.write(resp);
        writer.flush();
        writer.close();
    }
}