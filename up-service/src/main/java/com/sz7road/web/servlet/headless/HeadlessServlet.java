package com.sz7road.web.servlet.headless;

import com.google.common.base.Strings;
import com.google.inject.Key;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.core.InjectionProxy;
import com.sz7road.web.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 无头Servlet，用于后端接口处理。不建议运用于前端。
 *
 * @author jeremy
 */
public class HeadlessServlet extends HttpServlet implements InjectionProxy {

   public Logger log= LoggerFactory.getLogger(HeadlessServlet.class);

    @Inject
    Injection injection;

    @Override
    public <T> T getInstance(Class<T> type) {
        return injection.getInstance(type);
    }

    @Override
    public <T> T getInstance(Class<T> type, String named) {
        return injection.getInstance(type, named);
    }

    @Override
    public <T> T getInstance(Class<T> type, Annotation anno) {
        return injection.getInstance(type, anno);
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return injection.getInstance(key);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return injection.getProvider(type);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type, String named) {
        return injection.getProvider(type, named);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type, Annotation anno) {
        return injection.getProvider(type, anno);
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        return injection.getProvider(key);
    }

    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final HeadlessServletRequest req = new HeadlessServletRequest(request);
            final HeadlessServletResponse res = new HeadlessServletResponse(response);
            if (filters(req, res))
                doGet(req, res);
        } catch (final Exception e) {
            notFound(response);
            throw new HeadlessServletException(e);
        }
    }

    @Override
    protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final HeadlessServletRequest req = new HeadlessServletRequest(request);
            final HeadlessServletResponse res = new HeadlessServletResponse(response);
            if (filters(req, res))
                doPost(req, res);
        } catch (final Exception e) {
            notFound(response);
            throw new HeadlessServletException(e);
        }
    }

    protected void doGet(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    protected void doPost(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    /**
     * 接受GET请求与POST请求，重写该方法后，doGet及doPost将不应该被调用。
     *
     * @param request  requester
     * @param response responser
     * @throws ServletException
     * @throws IOException
     */
    protected void doService(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        // nothing to do. waiting for overridden.
        notFound(response);
    }

    @Override
    protected long getLastModified(final HttpServletRequest request) {
        // always force to reload.
        return 0;
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        notFound(response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        notFound(response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        notFound(response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        notFound(response);
    }

    @Override
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        notFound(response);
    }

    /**
     * Tells the request that the resources was not found.
     *
     * @param response responser
     * @throws ServletException
     * @throws IOException
     */
    protected void notFound(HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(null);
        response.setStatus(404);
        response.getWriter().close();
    }

    protected boolean filters(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {
        final Parameter parameter = this.getClass().getAnnotation(Parameter.class);

        if (null == parameter)
            return true;

        // filter methods
        if (!Strings.isNullOrEmpty(parameter.method()) && !request.getMethod().equalsIgnoreCase(parameter.method())) {
            notFound(response);
            return false;
        }

        // filter parameters
        boolean flag = true, emptyCheck = false;
        try {
            final Map<String, String[]> parameterMap = request.getParameterMap();
            for (String name : parameter.value()) {
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

                if (!flag)
                    break;

                emptyCheck = false;
            }
            return flag;
        } finally {
            if (!flag) {
                response.sendInvalidParameters();
            }
        }
    }
}
