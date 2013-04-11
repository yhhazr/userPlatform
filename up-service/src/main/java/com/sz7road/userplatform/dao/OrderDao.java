package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.OrderObject;

import java.sql.SQLException;

/**
 * @author jeremy
 */
public interface OrderDao extends Dao<OrderObject> {


    /**
     * 通过<code>order</code>获取订单对象。
     *
     * @param order 订单号
     * @return 订单对象
     */
    OrderObject getByOrderId(String order) throws SQLException;

}
