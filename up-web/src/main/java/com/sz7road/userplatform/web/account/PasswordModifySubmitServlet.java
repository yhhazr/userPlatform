/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @author leo.liao
 */
@Singleton
class PasswordModifySubmitServlet extends HeadlessHttpServlet {

    @Inject
    private Provider<UserService> userServiceProvider;

    private final static Logger log = LoggerFactory.getLogger(PasswordModifySubmitServlet.class);

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/x-json");
        String _uname = AppHelper.getUserName(request);
        String userName = request.getParameter("userName");
        if (Strings.isNullOrEmpty(_uname) || !_uname.equals(userName)) {
            response.setStatus(404);
            return;
        }
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        String newPwdr = request.getParameter("newPwdr");

        oldPwd = HtmlUtils.html2EntityAndCheckMaxLength(oldPwd, 20);
        newPwd = HtmlUtils.html2EntityAndCheckMaxLength(newPwd, 20);
        newPwdr = HtmlUtils.html2EntityAndCheckMaxLength(newPwdr, 20);

        Msg msg = new Msg();
        msg.setMsg("修改密码成功");
        {
            if (Strings.isNullOrEmpty(userName) && Strings.isNullOrEmpty(oldPwd)
                    && Strings.isNullOrEmpty(newPwd) && Strings.isNullOrEmpty(newPwdr)) {
                throw new IllegalArgumentException("输入参数不能为空");
            }
            if (!newPwd.equals(newPwdr)) {
                throw new IllegalArgumentException("新密码前后输入不一致");
            }
            try {
                int pwdInt = Integer.parseInt(newPwd);
                if (newPwd.length() < 9) {
                    msg.setCode(203);
                    msg.setMsg("密码不能为9位以下的纯数字!");
                    PrintWriter writer = response.getWriter();
                    ObjectMapper mapper = new ObjectMapper();
                    String resp = mapper.writeValueAsString(msg);
                    writer.write(resp);
                    writer.flush();
                    writer.close();
                    return;
                } else {
                    updatePsw(response, request, userName, oldPwd, newPwd, msg);
                }
            } catch (Exception ex) {
//                log.info("密码不是纯数字!");
                 updatePsw(response, request, userName, oldPwd, newPwd, msg);
            }
        }

    }




    private Msg updatePsw(HttpServletResponse response, HttpServletRequest req, String userName, String oldPwd, String newPwd, Msg msg) throws IOException {
        UserService userService = userServiceProvider.get();
        int ret=0;
        try{
         ret = userService.modifyPwd(userName, oldPwd, newPwd);
            if (ret > 0) {
                msg.setCode(200);
                msg.setMsg("更改密码成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("更改密码失败!");
            }
        }catch (Exception ex)
        {
            msg.setCode(205);
            msg.setMsg("原密码错误!");

        }
        finally {
            PrintWriter writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String resp = mapper.writeValueAsString(msg);
            writer.write(resp);
            writer.flush();
            writer.close();
            return msg;
        }

    }

}
