/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeremy
 */
public abstract class HeadlessFilter extends BaseFilter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    /**
     * 过滤GET请求，返回404.
     *
     * @param servletRequest
     * @param servletResponse
     * @return true 如果成功过滤，false 不符合过滤条件
     */
    protected boolean filterGet(final ServletRequest servletRequest, final ServletResponse servletResponse) {
        return filterMethod("get", servletRequest, servletResponse);
    }

    /**
     * 过滤POST请求，返回404.
     *
     * @param servletRequest
     * @param servletResponse
     * @return true 如果成功过滤，false 不符合过滤条件
     */
    protected boolean filterPost(final ServletRequest servletRequest, final ServletResponse servletResponse) {
        return filterMethod("post", servletRequest, servletResponse);
    }

    private boolean filterMethod(final String method, final ServletRequest servletRequest, final ServletResponse servletResponse) {
        if (((HttpServletRequest) servletRequest).getMethod().equalsIgnoreCase(method)) {
            ((HttpServletResponse) servletResponse).setStatus(405);
            return true;
        }
        return false;
    }

}
