/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.web;

import com.sz7road.userplatform.pojos.Msg;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午11:42
 */
public abstract class BaseServlet extends HttpServlet {
    //基类增加日志对象
    public final static Logger log = LoggerFactory.getLogger(BaseServlet.class);

    public final static String LOGIN_LISTENER = "loginListener";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doServe(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doServe(request, response);
    }

    protected void doServe(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        try {
            Method method = this.getClass().getMethod(action, new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class});
            method.invoke(this, new Object[]{request, response});
        } catch (Exception e) {
            log.error("反射方法出现异常！");
            e.printStackTrace();
        }
    }

    protected String render(String htmlString, HttpServletResponse response) throws Exception {
        return render(htmlString, "text/html", response);
    }

    protected String render(Msg msg, HttpServletResponse response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String rsp = mapper.writeValueAsString(msg);
        return render(rsp, "application/json", response);
    }


    protected String render(String text, String contentType, HttpServletResponse response) throws Exception {
        setResponse(contentType, response);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(text);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    private void setResponse(String contentType, boolean noCache, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        if (noCache) {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
        }
    }

    private void setResponse(String contentType, HttpServletResponse response) {
        setResponse(contentType, true, response);
    }
}
