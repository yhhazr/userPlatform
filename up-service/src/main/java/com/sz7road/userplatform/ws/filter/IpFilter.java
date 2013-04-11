/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws.filter;

import com.google.common.base.Strings;
import com.sz7road.utils.CommonDateUtils;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jeremy
 */
@Singleton
class IpFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws ServletException, IOException {
        //TODO: 同步ＩＰ策略
        final String remoteAddr = CommonDateUtils.getRemoteIPAddress((HttpServletRequest)req);
        if (filterRemoteAdd(remoteAddr)) {
            ((HttpServletResponse) resp).setStatus(404);
        }
        chain.doFilter(req, resp);
    }

    boolean filterRemoteAdd(final String remoteAddr) {
        return Strings.isNullOrEmpty(remoteAddr);
    }

    @Override
    public void init(final FilterConfig config) throws ServletException {
    }

}
