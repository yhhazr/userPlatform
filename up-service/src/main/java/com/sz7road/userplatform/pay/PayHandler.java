package com.sz7road.userplatform.pay;

import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

/**
 * @author jeremy
 */
public interface PayHandler {

    /**
     * 处理充值。
     *
     * @param request  HTTP请求对象
     * @param response
     */
    void process(HeadlessServletRequest request, HeadlessServletResponse response);

    /**
     * 回调确认处理。
     *
     * @param request
     * @param response
     */
    void callback(HeadlessServletRequest request, HeadlessServletResponse response);
}
