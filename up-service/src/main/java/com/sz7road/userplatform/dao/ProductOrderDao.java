package com.sz7road.userplatform.dao;

import java.sql.SQLException;

import com.sz7road.userplatform.pojos.ProductOrder;

/**
 * 商品订单表
 * @author leo.liao
 *
 */
public interface ProductOrderDao extends Dao<ProductOrder>
{
    /**
     * 通过<code>order</code>获取订单对象。
     *
     * @param order 订单号
     * @return 订单对象
     */
    ProductOrder getByOrderId(String order) throws SQLException;
}
