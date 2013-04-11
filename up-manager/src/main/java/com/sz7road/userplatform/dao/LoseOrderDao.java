package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojo.LoseOrderLogObject;
import com.sz7road.utils.ListData;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 下午3:57
 * To change this template use File | Settings | File Templates.
 */
public interface LoseOrderDao extends Dao<LoseOrderLogObject> {
    /**
     * 根据订单号查询掉单记录
     *
     * @param orderId 订单号
     * @return
     */
    ListData<LoseOrderLogObject> queryLoseOrderObjectByOrderId(String orderId);

    /**
     * 根据批量的订单号，通过批量查询，转换成带掉单标识的订单视图对象
     * @param orders   订单列表
     * @return       一个map<orderId 订单号,loseOrderObjectList 调单日志列表></>
     */
   ListData<OrderViewExtObject> getOrderViewExtObjectListDataByOrderViewObjectList(List<OrderViewObject> orders);
}
