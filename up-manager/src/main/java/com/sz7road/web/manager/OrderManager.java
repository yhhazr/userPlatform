package com.sz7road.web.manager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.service.OrderDecorateService;
import com.sz7road.utils.ListData;

import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-13
 * Time: 下午8:17
 */
@Singleton
public class OrderManager {

    @Inject
    private Provider<OrderDecorateService> orderServiceProvider;

    public ListData<OrderViewObject> queryOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {
        OrderDecorateService orderService = orderServiceProvider.get();
        ListData<OrderViewObject> listData = orderService.queryOrderViewList(orderViewObject, payStartTime, payEndTime, assertStartTime, assertEndTime, minMoney, maxMoney, pageNumber, limit);
        return listData;
    }

    public ListData<OrderViewExtObject> queryOrderViewListReWrite(Map<String, Object> condition) {
        OrderDecorateService orderService = orderServiceProvider.get();
        ListData<OrderViewExtObject> listData = orderService.queryOrderViewListReWrite(condition);
        return listData;
    }

    public ListData<OrderViewObject> queryOrderViewById(String orderId) {
        OrderDecorateService orderService = orderServiceProvider.get();
        ListData<OrderViewObject> listData = orderService.queryOrderViewById(orderId);
        return listData;
    }

    public ListData<OrderViewObject> queryExportOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {
        OrderDecorateService orderService = orderServiceProvider.get();
        ListData<OrderViewObject> listData = orderService.queryExportOrderViewList(orderViewObject, payStartTime, payEndTime, assertStartTime, assertEndTime, minMoney, maxMoney, pageNumber, limit);
        return listData;
    }

    public JqObject getOrderByGrid(Map<String,Object> conditions, int pageIndex, int pageSize, String sortFiled, String sortOrder,boolean useSelectCount) {
        OrderDecorateService orderService = orderServiceProvider.get();
        JqObject orders = orderService.getOrderByGrid(conditions,pageIndex,pageSize,sortFiled,sortOrder,useSelectCount);
        return orders;
    }
}
