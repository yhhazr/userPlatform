package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sz7road.userplatform.dao.LoseOrderDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojo.LoseOrderLogObject;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.utils.ListData;
import com.sz7road.web.utils.ServletUtil;
import org.apache.commons.dbutils.DbUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
public class LoseOrderDaoJdbcIml extends JdbcDaoSupport<LoseOrderLogObject> implements LoseOrderDao {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    /**
     * 根据订单号查询掉单记录
     *
     * @param orderId 订单号
     * @return
     */
    @Override
    public ListData<LoseOrderLogObject> queryLoseOrderObjectByOrderId(String orderId) {

        List<LoseOrderLogObject> lostOrders = new ArrayList<LoseOrderLogObject>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "select *  from " + SqlConfig.LOG_CHARGE_ORDER + " where orderId=?";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, orderId);
            log.info(sql);
            ResultSet rs = stmt.executeQuery();
            lostOrders = transResultToLoseOrderObjectList(rs);

        } catch (final SQLException e) {
            log.error("查询掉单日志异常！");
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        ListData<LoseOrderLogObject> listData = new ListData<LoseOrderLogObject>(lostOrders, lostOrders.size());
        return listData;
    }

    /**
     * 根据批量的订单号，通过批量查询，转换成带掉单标识的订单视图对象
     *
     * @param ordersInPage 订单列表
     * @return 一个map<orderId 订单号,loseOrderObjectList 调单日志列表></>
     */
    @Override
    public ListData<OrderViewExtObject> getOrderViewExtObjectListDataByOrderViewObjectList(List<OrderViewObject> ordersInPage) {
        if (ordersInPage == null || ordersInPage.size() == 0) {
            return null;
        } else {
            //1,先做一层过滤
            List<OrderViewObject> orders = new ArrayList<OrderViewObject>();

            for (OrderViewObject or : ordersInPage) {
                if ("成功".equals(or.getStatus())) {
                    orders.add(or);
                }
            }
            List<OrderViewExtObject> orderViewExtObjectLists = new ArrayList<OrderViewExtObject>();
            Map<String, List<LoseOrderLogObject>> map = null;
            try {
                if (orders != null && !orders.isEmpty()) {
                    //2,过滤之后不为空 组合sql
                    StringBuffer sql = new StringBuffer("select *  from " + SqlConfig.LOG_CHARGE_ORDER +
                            " where orderId in (");
                    for (OrderViewObject orderViewObject : orders) //增加问号
                    {
                        sql.append(" ?, ");
                    }
                    String sqlStr = sql.toString();
                    sqlStr = sqlStr.substring(0, sqlStr.length() - 2);
                    sqlStr += ")";//3,增加最后的半边括号
//                    log.info("拼接之后的sql:" + sqlStr);

                    conn = getQueryRunner().getDataSource().getConnection();
                    stmt = conn.prepareStatement(sqlStr);
                    for (int i = 0; i < orders.size(); i++)  //设置参数
                    {

                        stmt.setString(i + 1, orders.get(i).getOrderId());
                    }
                    ResultSet rs = stmt.executeQuery();
                    map = transResultToMap(rs);
                }
            } catch (final SQLException e) {
                log.error("查询掉单日志异常！");
            } finally {
                DbUtils.closeQuietly(conn, stmt, rs); //关闭连接，释放资源
                ObjectMapper mapper = new ObjectMapper();
                for (OrderViewObject orderViewObject : ordersInPage) {
                    OrderViewExtObject orderViewExtObject = ServletUtil.transOrderViewObjToExt(orderViewObject); //跟调单无关的属性先接收赋值
                    if (!"成功".equals(orderViewObject.getStatus())) {
                        orderViewExtObject.setLoseOrNot(false);
                    } else {
                        //1，拿到这个单子对应的日志集合
                        boolean loseOrNot = false;
                        List<LoseOrderLogObject> list = map.get(orderViewObject.getOrderId());
                        //2.1 没有记录，要补单 显示补单。
                        if (list == null || list.isEmpty() == true) {
                            loseOrNot = true;
                        } else {//2.2 如果有记录 遍历记录
                            int countStatus = 0;
                            for (LoseOrderLogObject loseOrderObject : list) { //3.1 如果状态不为200 说明失败，计数器+1；如果为200，到里面进行文字判断。
                                int status = loseOrderObject.getResponseState();
                                if (status == 200) {
                                    String result = loseOrderObject.getAssistantResult();
                                    if (!Strings.isNullOrEmpty(result) && result.contains("=")) {
                                        int index = result.indexOf("=");
                                        String resultStr = result.substring(0, index);
                                        boolean flag = "success".equals(resultStr);
                                        if (flag) {
                                            loseOrNot = false;
                                            break;
                                        } else {
                                            loseOrNot = true;
                                            continue;
                                        }
                                    }
                                } else {
                                    countStatus += 1;
                                }
                            }
                            if (countStatus == list.size()) { //所有的都失败的情况
                                loseOrNot = true;
                            }
                        }
                        orderViewExtObject.setLoseOrNot(loseOrNot);
                    }
                    orderViewExtObjectLists.add(orderViewExtObject);
                }
                ListData<OrderViewExtObject> listData = new ListData<OrderViewExtObject>(orderViewExtObjectLists, orderViewExtObjectLists.size());
                return listData;
            }
        }
    }

    /**
     * 把结果集转换成map
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Map<String, List<LoseOrderLogObject>> transResultToMap(ResultSet rs) throws SQLException {
        Map<String, List<LoseOrderLogObject>> map = new HashMap<String, List<LoseOrderLogObject>>();
        while (rs.next()) {
            LoseOrderLogObject loseOrderObject = new LoseOrderLogObject();
            loseOrderObject.setId(rs.getInt("id"));
            loseOrderObject.setOrderId(rs.getString("orderId"));
            loseOrderObject.setAssistantResult(rs.getString("assistResult"));
            loseOrderObject.setResponseState(rs.getInt("responseState"));
            String key = loseOrderObject.getOrderId();
            List<LoseOrderLogObject> list = map.get(key);
            if (list != null) {
                list.add(loseOrderObject);
            } else {
                list = Lists.newArrayList();
                list.add(loseOrderObject);
            }
            map.put(key, list);
        }
        return map;
    }

    /**
     * 判断map的key中是不是已经含有orderId
     *
     * @param orderId
     * @param keys
     * @return
     */
    private boolean hasKey(String orderId, Set<String> keys) {
        boolean flag = false;
        if (keys.size() > 0) {
            for (String str : keys) {
                if (!Strings.isNullOrEmpty(str) && str.equals(orderId)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }


    /**
     * 根据某订单交易日志，判断是不是可以补单
     */
    private boolean checkLoseOrderOrNot(List<LoseOrderLogObject> lostOrders) {
        boolean flag = false;
        //如果查不到数据，要补单
        if (lostOrders == null || lostOrders.size() == 0) {
            flag = true;
            log.info("交易日志为空！");
        }
        //如果有数据，只要含有success，就不补单，其它的情况要补单。
        if (lostOrders != null && lostOrders.size() > 0) {

            for (LoseOrderLogObject orderLog : lostOrders) {
                String msg = null;
                if (!Strings.isNullOrEmpty(orderLog.getAssistantResult())) msg = orderLog.getAssistantResult().trim();

                if (!Strings.isNullOrEmpty(msg) && msg.startsWith("success")) {
                    flag = false;
                    log.info("含有success!列是：" + orderLog.getOrderId());
                    break;
                } else {
                    flag = true;
                    log.info("不含有success!");
                }
            }
        }
        log.info("最后的逻辑判断结果是：" + flag);
        return flag;
    }

    /**
     * 把结果集转换成对象列表
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<LoseOrderLogObject> transResultToLoseOrderObjectList(ResultSet rs) throws SQLException {
        List<LoseOrderLogObject> lostOrders = new ArrayList<LoseOrderLogObject>();
        while (rs.next()) {
            LoseOrderLogObject loseOrderObject = new LoseOrderLogObject();
            loseOrderObject.setId(rs.getInt("id"));
            loseOrderObject.setGameId(rs.getInt("gameId"));
            loseOrderObject.setGameZoneId(rs.getInt("gameZoneId"));
            loseOrderObject.setAmount(rs.getBigDecimal("amount"));
            loseOrderObject.setGold(rs.getInt("gold"));
            loseOrderObject.setUserId(rs.getInt("userId"));
            loseOrderObject.setPlayerId(rs.getInt("playerId"));
            loseOrderObject.setOrderId(rs.getString("orderId"));
            loseOrderObject.setAssistantResult(rs.getString("assistResult"));
            loseOrderObject.setResponseState(rs.getInt("responseState"));
            loseOrderObject.setPayTime(rs.getTimestamp("payTime"));
            loseOrderObject.setCreateTime(rs.getTimestamp("createTime"));

            lostOrders.add(loseOrderObject);
        }
        return lostOrders;
    }


}
