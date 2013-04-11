/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.web.servlet;

import com.google.inject.Inject;
import com.sz7road.userplatform.pojos.SimpleResponseResult;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jeremy
 */
public abstract class HeadlessHttpServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(HeadlessHttpServlet.class.getName());

    @Inject
    public HeadlessHttpServlet() {
        super();
    }

    /**
     * 配置
     * @param httpServletRequest
     */
    private void setConfig(HttpServletRequest httpServletRequest) {
        httpServletRequest.setAttribute("gatewayDomain", ConfigurationUtils.get("gateway.domain"));
        httpServletRequest.setAttribute("hostDomain", ConfigurationUtils.get("host.domain"));
        httpServletRequest.setAttribute("gateWayDomain", ConfigurationUtils.get("gateway.domain"));
        httpServletRequest.setAttribute("imageDomainUrl", ConfigurationUtils.get("image.domainUrl"));
        httpServletRequest.setAttribute("staticDomainUrl", ConfigurationUtils.get("static.domainUrl"));
        httpServletRequest.setAttribute("gatewayPurchaseUrl", ConfigurationUtils.get("gateway.domain") + ConfigurationUtils.get("gateway.purchaseUri"));
        httpServletRequest.setAttribute("homePageUrl", ConfigurationUtils.get("homePage.domainUrl"));
        httpServletRequest.setAttribute("accountUrl", ConfigurationUtils.get("host.accountUri"));
        httpServletRequest.setAttribute("customerUrl", ConfigurationUtils.get("host.customerUri"));
        httpServletRequest.setAttribute("forumUrl", ConfigurationUtils.get("forum.domainUrl"));
        httpServletRequest.setAttribute("payPageUrl", ConfigurationUtils.get("host.payPageUri"));
        httpServletRequest.setAttribute("payHelpUrl", ConfigurationUtils.get("help.payUri"));
        httpServletRequest.setAttribute("payConfirmUrl", ConfigurationUtils.get("pay.confirmUri"));
        httpServletRequest.setAttribute("meagerUrl", ConfigurationUtils.get("meager.domainUrl"));
        httpServletRequest.setAttribute("gameWebsiteUrl", ConfigurationUtils.get("game.websiteUrl"));
        httpServletRequest.setAttribute("imgUrl", ConfigurationUtils.get("user.resource.url"));
        httpServletRequest.setAttribute("pageVersion", ConfigurationUtils.get("pageResource.version"));
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            setConfig(httpServletRequest);
            doServe(httpServletRequest, httpServletResponse);
        } catch (final RuntimeException e) {
            log.error("GET RuntimeException: {}", e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);

        } catch (final Exception e) {
            log.error("GET Exception: {}", e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            doServe(httpServletRequest, httpServletResponse);
        } catch (final RuntimeException e) {
            log.error("POST RuntimeException: {}", e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);

        } catch (final Exception e) {
            log.error("POST Exception: {}", e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * 处理请求方法。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * 响应输出。
     *
     * @param state    响应头状态
     * @param response 响应对象
     * @param data     响应数据
     * @throws IOException
     */
    private void throwsResponse(final SimpleResponseResult.ResponseState state, final HttpServletResponse response, final Object data) throws IOException {
        if (null != response) {
            final PrintWriter writer = response.getWriter();
            try {
                SimpleResponseResult result;
                if (null == data) {
                    result = new SimpleResponseResult(state);
                } else if (data instanceof SimpleResponseResult) {
                    result = (SimpleResponseResult) data;
                } else {
                    result = new SimpleResponseResult(state, data);
                }

                writer.write(result.toString());
            } finally {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 响应无效参数结果。
     *
     * @param response 响应对象
     */
    protected void throwsInvalidParamters(final HttpServletResponse response) throws IOException {
        throwsResponse(SimpleResponseResult.ResponseState.INVALID, response, "parameters");
    }

    /**
     * 响应失败数据。
     *
     * @param response 响应对象
     * @param data     响应数据
     * @throws IOException
     */
    protected void throwsFailure(final HttpServletResponse response, final Object data) throws IOException {
        throwsResponse(SimpleResponseResult.ResponseState.FAILURE, response, data);
    }

    /**
     * 响应失败数据。
     *
     * @param response 响应对象
     * @throws IOException
     */
    protected void throwsFailure(final HttpServletResponse response) throws IOException {
        throwsFailure(response, null);
    }

    /**
     * 响应执行成功。
     *
     * @param response 响应对象
     * @throws IOException
     */
    protected void throwsSuccess(final HttpServletResponse response) throws IOException {
        throwsSuccess(response, null);
    }

    /**
     * 响应执行成功。
     *
     * @param response 响应对象
     * @param data     响应数据
     * @throws IOException
     */
    protected void throwsSuccess(final HttpServletResponse response, final Object data) throws IOException {
        throwsResponse(SimpleResponseResult.ResponseState.SUCCESS, response, data);
    }

    /**
     * 响应执行错误。
     *
     * @param response 响应对象
     * @throws IOException
     */
    protected void throwsError(final HttpServletResponse response) throws IOException {
        throwsError(response, null);
    }

    /**
     * 响应执行错误。
     *
     * @param response 响应对象
     * @param data     响应数据
     * @throws IOException
     */
    protected void throwsError(final HttpServletResponse response, final Object data) throws IOException {
        throwsResponse(SimpleResponseResult.ResponseState.ERROR, response, data);
    }
}
