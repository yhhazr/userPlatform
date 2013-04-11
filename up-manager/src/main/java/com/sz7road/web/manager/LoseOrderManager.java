package com.sz7road.web.manager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.LoseOrderLogObject;
import com.sz7road.userplatform.service.LoseOrderService;
import com.sz7road.utils.ListData;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class LoseOrderManager {
    @Inject
    private Provider<LoseOrderService> loseOrderServiceProvider;

    public ListData<LoseOrderLogObject> queryLoseOrderObjectByOrderId(String orderId) {
        LoseOrderService loseOrderService = loseOrderServiceProvider.get();

        return loseOrderService.queryLoseOrderObjectByOrderId(orderId);
    }

    /**
     * 根据批量的订单号，通过批量查询，转换成带掉单标识的订单视图对象
     * @param orders   订单列表
     * @return       一个map<orderId 订单号,loseOrderObjectList 调单日志列表></>
     */
    public ListData<OrderViewExtObject> getOrderViewExtObjectListDataByOrderViewObjectList(List<OrderViewObject> orders)
    {
        LoseOrderService loseOrderService = loseOrderServiceProvider.get();
        return loseOrderService.getOrderViewExtObjectListDataByOrderViewObjectList(orders);
    }
}
