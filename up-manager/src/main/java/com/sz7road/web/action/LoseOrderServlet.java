package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.LoseOrderLogObject;
import com.sz7road.utils.ListData;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import com.sz7road.web.manager.LoseOrderManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 下午3:38
 * 调单查询的servlet
 */
@Singleton
public class LoseOrderServlet extends BaseServlet {
    @Inject
    private Provider<LoseOrderManager> providerLoseOrderManager;

    //显示调单页面
    public void showLoseOrderPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("loseOrderQuery.jsp").forward(request, response);
    }


    public void queryLoseOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String orderId = request.getParameter("orderId");
        ListData<LoseOrderLogObject> listData = null;
        String msg = null;

            if (!Strings.isNullOrEmpty(orderId)) {
                listData = providerLoseOrderManager.get().queryLoseOrderObjectByOrderId(orderId);
                ServletUtil.returnJsonData(response, listData);
            }
    }

}
