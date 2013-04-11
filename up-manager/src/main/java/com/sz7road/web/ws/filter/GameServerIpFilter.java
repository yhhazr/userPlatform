package com.sz7road.web.ws.filter;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: leo.liao
 * Date: 12-6-15
 * Time: 下午4:38
 */
@Singleton
public class GameServerIpFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(GameServerIpFilter.class.getName());
    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String ip = getRemoteAddress(req);
        if (!filterIpAddress(ip)) {
            filterChain.doFilter(request, response);
        } else {
            log.info("客户端IP===>"+ip);
            ((HttpServletResponse) response).setStatus(404);
        }
    }

    private boolean filterIpAddress(String ip) {
        String value = ConfigurationUtils.get("gameWithServer.filter.ip");
        if (Strings.isNullOrEmpty(value)) {
            return false;
        }
        if (value.equals(ip)) {
            return false;
        }
        return true;
    }

    public String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
