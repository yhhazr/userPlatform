package com.sz7road.web.servlet.headless;

import com.sz7road.userplatform.pojos.SimpleResponseResult;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jeremy
 */
public class HeadlessServletResponse extends HttpServletResponseWrapper {

    /**
     * Constructs by default.
     *
     * @param response
     */
    public HeadlessServletResponse(HttpServletResponse response) {
        super(response);
    }

    /**
     * 发送回客户端告知该请求参数无效。
     *
     * @throws IOException
     */
    public void sendInvalidParameters() throws IOException {
        sendResponse(SimpleResponseResult.ResponseState.INVALID, "parameters");
    }

    /**
     * 发送回客户端告知该请求参数无效。
     *
     * @throws IOException
     */
    public void sendInvalidParameters(final Object object) throws IOException {
        sendResponse(SimpleResponseResult.ResponseState.INVALID, object);
    }

    /**
     * 发送回客户端告知该请求执行失败。
     *
     * @throws IOException
     */
    public void sendFailure() throws IOException {
        sendFailure(null);
    }

    /**
     * 发送回客户端告知该请求执行失败。
     *
     * @throws IOException
     */
    public void sendFailure(final Object object) throws IOException {
        sendResponse(SimpleResponseResult.ResponseState.FAILURE, object);
    }

    /**
     * 发送回客户端告知该请求执行成功。
     *
     * @throws IOException
     */
    public void sendSuccess() throws IOException {
        sendSuccess(null);
    }

    /**
     * 发送回客户端告知该请求执行成功。
     *
     * @throws IOException
     */
    public void sendSuccess(final Object object) throws IOException {
        sendResponse(SimpleResponseResult.ResponseState.SUCCESS, object);
    }

    /**
     * 发送回客户端告知该请求执行结果。
     *
     * @throws IOException
     */
    public void sendResponse(final SimpleResponseResult.ResponseState state, final Object object) throws IOException {
        final PrintWriter writer = getWriter();
        try {
            SimpleResponseResult result;
            if (null == object) {
                result = new SimpleResponseResult(state);
            } else if (object instanceof SimpleResponseResult) {
                result = (SimpleResponseResult) object;
            } else {
                result = new SimpleResponseResult(state, object);
            }
            writer.write(result.toString());

        } finally {
            writer.flush();
            writer.close();
        }
    }
}
