/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.service;

import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.OrderDecorateDao;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.utils.ListData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * @author cutter.li
 */
@Singleton
public class OrderDecorateService extends Injection {

    static final Logger log = LoggerFactory.getLogger(OrderService.class.getName());

    @Inject
    private OrderDecorateService() {
        super();
    }

    public ListData<OrderViewObject> queryOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {
        OrderDecorateDao orderDao = getInstance(OrderDecorateDao.class);
        try {
            return orderDao.queryOrderViewList(orderViewObject, payStartTime, payEndTime, assertStartTime, assertEndTime, minMoney, maxMoney, pageNumber, limit);
        } catch (Exception e) {
            log.error("查询充值订单数据异常：{}", e.getMessage());
        }
        return null;
    }

    public ListData<OrderViewObject> queryOrderViewById(String orderId) {
        OrderDecorateDao orderDao = getInstance(OrderDecorateDao.class);
        try {
            return orderDao.queryOrderViewById(orderId);
        } catch (Exception e) {
            log.error("查询订单数据异常：{}", e.getMessage());
        }
        return null;
    }

    public ListData<OrderViewObject> queryExportOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {
        OrderDecorateDao orderDao = getInstance(OrderDecorateDao.class);
        try {
            return orderDao.queryExportOrderViewList(orderViewObject, payStartTime, payEndTime, assertStartTime, assertEndTime, minMoney, maxMoney, pageNumber, limit);
        } catch (Exception e) {
            log.error("查询充值订单数据异常：{}", e.getMessage());
        }
        return null;
    }

    public ListData<OrderViewExtObject> queryOrderViewListReWrite(Map<String, Object> condition) {
        OrderDecorateDao orderDao = getInstance(OrderDecorateDao.class);
        try {
            return orderDao.queryOrderViewListReWrite(condition);
        } catch (Exception e) {
            log.error("查询充值订单数据异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public JqObject getOrderByGrid(Map<String,Object> conditions, int pageIndex, int pageSize, String sortFiled, String sortOrder,boolean useSelectCount) {

        OrderDecorateDao orderDao = getInstance(OrderDecorateDao.class);
        try {
            return orderDao.queryOrders(conditions,pageIndex,pageSize,sortFiled,sortOrder,useSelectCount);
        } catch (Exception e) {
            log.error("查询充值订单数据异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return null;

    }
}
