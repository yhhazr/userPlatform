package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.LoseOrderDao;
import com.sz7road.userplatform.pojo.LoseOrderLogObject;
import com.sz7road.utils.ListData;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class LoseOrderService {
    @Inject
    private Provider<LoseOrderDao> loseOrderDaoProvider;

    private LoseOrderDao getLoseOrderDao() {
        LoseOrderDao loseOrderDao = loseOrderDaoProvider.get();

        if (loseOrderDao == null) {
            throw new NullPointerException("loseOrderDao is null!");
        } else {
            return loseOrderDao;
        }
    }

    public ListData<LoseOrderLogObject> queryLoseOrderObjectByOrderId(String orderId) {
        if (Strings.isNullOrEmpty(orderId)) {
            throw new IllegalArgumentException("orderid is null!it's illegal!");
        }
        return getLoseOrderDao().queryLoseOrderObjectByOrderId(orderId);
    }

    /**
     * 根据批量的订单号，通过批量查询，转换成带掉单标识的订单视图对象
     * @param orders   订单列表
     * @return       一个map<orderId 订单号,loseOrderObjectList 调单日志列表></>
     */
    public ListData<OrderViewExtObject> getOrderViewExtObjectListDataByOrderViewObjectList(List<OrderViewObject> orders)
    {
        return getLoseOrderDao().getOrderViewExtObjectListDataByOrderViewObjectList(orders);
    }
}