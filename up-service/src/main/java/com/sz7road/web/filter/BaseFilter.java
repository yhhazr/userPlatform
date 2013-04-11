/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.web.filter;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sz7road.userplatform.core.ScopeConstant;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author jeremy
 */
public abstract class BaseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    /**
     * 检查是否存在指定的请求参数。
     *
     * @param request        请求对象
     * @param parameterNames 指定的请求参数名称
     * @return true or false
     */
    protected boolean checkExists(final HttpServletRequest request, String... parameterNames) {
        return checkExists(request, ScopeConstant.PARAMETER, parameterNames);
    }

    /**
     * 检查是否存在指定的请求参数。
     *
     * @param request        请求对象
     * @param parameterNames 指定的请求参数名称
     * @return true or false
     */
    protected boolean checkExists(final HttpServletRequest request, Iterable<String> parameterNames) {
        return checkExists(request, ScopeConstant.PARAMETER, parameterNames);
    }

    /**
     * 检查在<code>scope</code>中是否存在指定的请求参数。
     *
     * @param request        请求对象
     * @param scope          域
     * @param parameterNames 指定的请求参数名称
     * @return true or false
     */
    protected boolean checkExists(final HttpServletRequest request, final ScopeConstant scope, String... parameterNames) {
        return checkExists(request, scope, Sets.newHashSet(parameterNames));
    }

    /**
     * 检查在<code>scope</code>中是否存在指定的请求参数。
     *
     * @param request        请求对象
     * @param scope          域
     * @param parameterNames 指定的请求参数名称
     * @return true or false
     */
    protected boolean checkExists(final HttpServletRequest request, final ScopeConstant scope, final Iterable<String> parameterNames) {
        return checkExists(request, scope, Sets.newHashSet(parameterNames));
    }

    /**
     * 检查在<code>scope</code>中是否存在指定的请求参数。
     *
     * @param request        请求对象
     * @param scope          域
     * @param parameterNames 指定的请求参数名称
     * @return true or false
     */
    protected boolean checkExists(final HttpServletRequest request, final ScopeConstant scope, final Set<String> parameterNames) {
        switch (scope) {
            case COOKIE:
                int i = 0;
                final Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (parameterNames.contains(cookie.getName())) {
                        i++;
                    }
                }
                return i == parameterNames.size();
            case SESSION:
                final HttpSession session = request.getSession();
                for (String name : parameterNames) {
                    if (session.getAttribute(name) == null) {
                        return false;
                    }
                }
                return true;
            case PARAMETER:
                final Map<String, String[]> parameterMap = request.getParameterMap();
                boolean flag = true, emptyCheck = false;
                for (final Iterator<String> it = parameterNames.iterator(); flag && it.hasNext(); emptyCheck = false) {
                    String name = it.next();
                    if (name.endsWith(":")) {
                        name = name.substring(0, name.length() - 1);
                        emptyCheck = true;
                    }

                    flag = parameterMap.containsKey(name);

                    if (emptyCheck) {
                        // must has parameter.
                        flag = flag && parameterMap.get(name) != null;
                        flag = flag && parameterMap.get(name).length > 0;
                        flag = flag && !Strings.isNullOrEmpty(parameterMap.get(name)[0]);
                    }
                }
                return flag;
        }
        return false;
    }

}
