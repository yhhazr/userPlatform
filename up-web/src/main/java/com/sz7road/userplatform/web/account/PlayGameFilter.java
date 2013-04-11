/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.sz7road.web.filter.HeadlessFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jeremy
 */
@Singleton
public class PlayGameFilter extends HeadlessFilter {

    private static final Logger log = LoggerFactory.getLogger(PlayGameFilter.class.getName());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (checkExists(request, "u:", "g:", "z:")) {
            filterChain.doFilter(request, servletResponse);
        } else {
            log.warn("无效的进入游戏请求：参数无效！");
            ((HttpServletResponse) servletResponse).setStatus(404);
        }
    }
}
