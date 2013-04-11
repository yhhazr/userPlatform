package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.service.outinterface.IGameRankService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-11
 * Time: 上午11:36
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class getRoleRankURLServlet extends BaseServlet {
    @Inject
    private Provider<IGameRankService> iGameRankProvider;

    public void GameRankInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String gameId = request.getParameter("gameId");
        //验证转换
        if (!Strings.isNullOrEmpty(gameId) && VerifyFormItem.isInteger(gameId)) {
            int gid = Integer.parseInt(gameId.trim());
            IGameRankService iGameRankService = iGameRankProvider.get();
            String gameRankInfo=iGameRankService.getGameRankInfo(gid);
            ServletUtil.returnJsonString(response, gameRankInfo);
        }
    }

    public void toIndexPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("test.jsp").forward(request, response);
    }


}
