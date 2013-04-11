/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Log.LogType;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.Question;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.QuestionUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangfan.zhou
 */
@Singleton
class QuestionSubmitServlet extends HeadlessHttpServlet {

    static final Logger log = LoggerFactory.getLogger(QuestionSubmitServlet.class.getName());

    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<LogService> logServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        String _uname = AppHelper.getUserName(request);
        String userName = _uname;
        int id = AppHelper.getUserId(request);
        String type = request.getParameter("type");
        String q1 = request.getParameter("q1");
        String q2 = request.getParameter("q2");
        String q3 = request.getParameter("q3");
        String a1 = request.getParameter("a1");
        String a2 = request.getParameter("a2");
        String a3 = request.getParameter("a3");
        /*System.out.println("type:" + type);
        System.out.println("q1:" + q1);
        System.out.println("q2:" + q2);
        System.out.println("q3:" + q3);
        System.out.println("a1:" + a1);
        System.out.println("a2:" + a2);
        System.out.println("a3:" + a3);
        System.out.println("userName:" + userName);
        System.out.println("id:" + id);*/
        if (Strings.isNullOrEmpty(_uname) || Strings.isNullOrEmpty(type) || id == 0 ||
                Strings.isNullOrEmpty(q1) || Strings.isNullOrEmpty(q2) || Strings.isNullOrEmpty(q3) ||
                Strings.isNullOrEmpty(a1) || Strings.isNullOrEmpty(a2) || Strings.isNullOrEmpty(a3) ||
                Strings.isNullOrEmpty(method) || !"POST".equals(method.toUpperCase())) {
            response.setStatus(404);
            return;
        }

        Msg msg = new Msg(0, "设置密保失败");
        try{
            if (q1.equals(q2) || q1.equals(q3) || q2.equals(q3)) {
                throw new IllegalArgumentException("问题不能重复");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        UserService service = userServiceProvider.get();
        List<Question> list = service.getLastQuestions(id, 3);
        boolean isSetQuestion = false;
        if (list != null && list.size() > 0) {
            isSetQuestion = true;
        }
        Timestamp addTime = new Timestamp(System.currentTimeMillis());
        Question qa1 = new Question(id, q1, a1, 1,addTime);
        Question qa2 = new Question(id, q2, a2, 1,addTime);
        Question qa3 = new Question(id, q3, a3, 1,addTime);
        if ("bind".equals(type) && !isSetQuestion) {
            int ret = service.updateQuestion(id, 0, qa1, qa2, qa3);
            if(ret > 0){
                msg.setCode(ret);
                msg.setMsg("密保问题设置成功");
            }
        } else if ("bind".equals(type) && isSetQuestion) {
            Map<String, String> mapInput = QuestionUtils.getMap(new Question(q1, a1), new Question(q2, a2), new Question(q3, a3));
            Map<String, String> mapQuery = QuestionUtils.getMap(list);

            if(!mapInput.equals(mapQuery)){
                msg.setCode(0);
                msg.setMsg("密保答案不正确");
            } else {
                msg.setCode(1);
                msg.setMsg("密保答案正确");
            }
        } else if ("rebind".equals(type) && isSetQuestion) {
            String rq1 = request.getParameter("rq1");
            String rq2 = request.getParameter("rq2");
            String rq3 = request.getParameter("rq3");
            String ra1 = request.getParameter("ra1");
            String ra2 = request.getParameter("ra2");
            String ra3 = request.getParameter("ra3");

            if (Strings.isNullOrEmpty(rq1) || Strings.isNullOrEmpty(rq2) || Strings.isNullOrEmpty(rq3) ||
                    Strings.isNullOrEmpty(ra1) || Strings.isNullOrEmpty(ra2) || Strings.isNullOrEmpty(ra3)) {
                log.info("用户{},原密保问题答案不能为空", new Object[]{userName});
                response.setStatus(404);
                return;
            }

            Map<String, String> mapInput = QuestionUtils.getMap(new Question(q1, a1), new Question(q2, a2), new Question(q3, a3));
            Map<String, String> mapQuery = QuestionUtils.getMap(list);

            if(!mapInput.equals(mapQuery)){
                log.info("用户{},原密保问题答案不正确", new Object[]{userName});
                response.setStatus(404);
                return;
            }

            Question rqa1 = new Question(id, rq1, ra1, 1, addTime);
            Question rqa2 = new Question(id, rq2, ra2, 1, addTime);
            Question rqa3 = new Question(id, rq3, ra3, 1, addTime);
            int ret = service.updateQuestion(id, 0, rqa1, rqa2, rqa3);
            msg.setCode(ret);
            if(ret > 0){
                msg.setMsg("密保问题重置设置成功");
                log.info("用户{},密保问题重置成功", new Object[]{userName});
            } else {
                msg.setMsg("密保问题重置失败");
                log.info("用户{},密保问题重置失败", new Object[]{userName});
            }
        } else {
            response.setStatus(404);
            return;
        }
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        writer.write(resp);
        writer.flush();
        writer.close();
    }
}
