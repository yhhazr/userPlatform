package com.sz7road.userplatform.ws.filter;

import com.google.common.base.Charsets;

import javax.inject.Singleton;
import javax.servlet.*;
import java.io.IOException;

/**
 * @author jeremy
 */
@Singleton
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(Charsets.UTF_8.name());
        servletResponse.setCharacterEncoding(Charsets.UTF_8.name());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
