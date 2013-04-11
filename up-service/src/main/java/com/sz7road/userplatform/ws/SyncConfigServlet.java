package com.sz7road.userplatform.ws;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.service.ConfigurationService;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;

/**
 * @author jeremy
 */
@Singleton
public class SyncConfigServlet extends HeadlessServlet {

    final static String resp = "({\"code\":\"0\",\"msg\":\"ok\"})";

    @Inject
    private Provider<ConfigurationService> configurationServiceProvider;

    @Override
    protected void doService(final HeadlessServletRequest request, final HeadlessServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");
        String jsoncallback = request.getParameter("jsoncallback");
        if ("sync".equals(type)) {
            try{
                ConfigurationService configurationService = configurationServiceProvider.get();
                configurationService.notifyUpdate();
                if(!Strings.isNullOrEmpty(jsoncallback)){
                    response.getWriter().write(jsoncallback+resp);
                }else {
                    response.getWriter().write("success="+new Date().toString());
                }
            }finally {
                response.getWriter().flush();
                response.getWriter().close();
            }
            return;
        }

        response.sendError(404);
    }
}
