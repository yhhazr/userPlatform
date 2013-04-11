package com.sz7road.userplatform.web.cscenter;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.SiteObject;
import com.sz7road.userplatform.service.CsInfoService;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-30
 * Time: 下午6:02
 * 显示客服信息
 */
@Singleton
public class CsInfoServlet extends HeadlessHttpServlet {

    @Inject
    private Provider<CsInfoService> csInfoServiceProvider;

    /**
     * 处理请求方法。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CsInfoService csInfoService = csInfoServiceProvider.get();

        SiteObject siteObject = csInfoService.getCsInfo();

        HttpSession session = request.getSession(true);

        session.setAttribute("siteInfo", siteObject);

    }
}
