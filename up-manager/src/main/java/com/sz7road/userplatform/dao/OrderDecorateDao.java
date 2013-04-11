package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.utils.ListData;

import java.util.Map;

/**
 * @author jeremy
 */
public interface OrderDecorateDao extends OrderDao {
    /**
     * 组合查询方法
     *
     * @param orderViewObject 查询的条件集合
     * @param payStartTime    提交开始时间
     * @param payEndTime      提交结束时间
     * @param assertStartTime 确认开始时间
     * @param assertEndTime   确认结束时间
     * @param minMoney        金额下限
     * @param maxMoney        金额上限
     * @param pageNumber      第几页
     * @param limit           每页的条数
     * @return
     */
    ListData<OrderViewObject> queryOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit);

    ListData<OrderViewObject> queryOrderViewById(String orderId);

    /**
     * 拿到导出的数据
     *
     * @param orderViewObject
     * @param payStartTime
     * @param payEndTime
     * @param assertStartTime
     * @param assertEndTime
     * @param minMoney
     * @param maxMoney
     * @param pageNumber
     * @param limit
     * @return
     */
    public ListData<OrderViewObject> queryExportOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit);

    //重写订单查询，优化速度
    public ListData<OrderViewExtObject> queryOrderViewListReWrite(Map<String, Object> condition);
    /**
     *  查询得到jqgrid所需的数据格式
     * @param conditions  查询条件
     * @param pageIndex    第几页
     * @param pageSize      每页数据条数
     * @param sortFiled      排序字段
     * @param sortOrder       排序命令
     * @param useSelectCount true，重新查询数据总条数，false,不用重新查询数据总条数
     * @return
     */
    public JqObject queryOrders(Map<String,Object> conditions, int pageIndex, int pageSize, String sortFiled, String sortOrder,boolean useSelectCount);
}
