package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.service.ReChargeOrderService;
import com.sz7road.utils.Backend;
import com.sz7road.web.BaseServlet;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-29
 * Time: 上午10:46
 * 补单的servlet
 */
@Singleton
public class ReChargeOrderServlet extends BaseServlet {

    @Inject
    private Provider<ReChargeOrderService> reChargeOrderService;

    public void replenishOrderByID(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String orderId = request.getParameter("order").trim();

        if (Strings.isNullOrEmpty(orderId)) {
            response.sendError(404);
            log.info("订单号为空！");
            return;
        }

        log.info("接收的订单号：" + orderId);
        response.setContentType("text/html");
        //1,通过订单号拿到订单对象
        ReChargeOrderService instance = reChargeOrderService.get();
        OrderObject order = instance.getOrder(orderId);

        //2,完成补单操作
        final Backend.BackendResponse response1 = instance.rechargeGolds(order);

        //3,返回结果
        PrintWriter out = response.getWriter();
        try{
            String result = "请求游戏服务器补单失败";
            if(response1 != null){
                result = String.format("%d => %s", response1.getResponseCode(), response1.getResponseContent());
            }
            out.println(result);
        }finally {
            out.flush();
            out.close();
        }
    }

}
