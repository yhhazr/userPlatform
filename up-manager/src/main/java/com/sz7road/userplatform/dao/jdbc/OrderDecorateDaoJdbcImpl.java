/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.sz7road.userplatform.dao.OrderDecorateDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojo.ChargeRsp;
import com.sz7road.userplatform.pojo.LoseOrderLogObject;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.RowObject;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.ListData;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.utils.ServletUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.poi.ss.usermodel.DateUtil;
import org.codehaus.jackson.map.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author jeremy
 */
class OrderDecorateDaoJdbcImpl extends OrderDaoJdbcImpl implements OrderDecorateDao {

    @Inject
    private Injector injector;

    private QueryRunner getRunner() {
        String dbQuery = ConfigurationUtils.get("dbQuery.name");
        if (Strings.isNullOrEmpty(dbQuery)) {
            return getQueryRunner();
        } else {
            return injector.getInstance(Key.get(QueryRunner.class, Names.named(dbQuery)));
        }
    }

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
    @Override
    public ListData<OrderViewObject> queryOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {

        final QueryRunner runner = getRunner();
        Connection conn = null;
        try {
            final int gameId = orderViewObject.getGameId();
            conn = runner.getDataSource().getConnection();

            Map<String, Object> maps = ServletUtil.getSQLAndParamList(orderViewObject, payStartTime, payEndTime, assertStartTime, assertEndTime, minMoney, maxMoney, pageNumber, limit);

            List<Object> paramList = (List<Object>) maps.get("paramList");

            String SQL_CONDITION = maps.get("sql") != null ? maps.get("sql").toString() : null;

            String SQL_COUNT = (gameId == 1 ? SqlConfig.ORDER_COUNT_SQ : SqlConfig.ORDER_COUNT_NOTFORSQ) + SQL_CONDITION.toString();
            int total = getQueryDataTotalCount(conn, paramList, SQL_COUNT);
            String sql = (gameId == 1 ? SqlConfig.ORDER_SELECT_SQ : SqlConfig.ORDER_SELECT_NOTFORSQ) + SQL_CONDITION.toString() + " ORDER BY od.payTime  desc  limit ?,? ";
            List<OrderViewObject> list = getQueryDataByPage(pageNumber, limit, conn, paramList, sql);

            ListData<OrderViewObject> listData = new ListData<OrderViewObject>(list, total);
            return listData;

        } catch (Exception ex) {

            log.error("查询订单出现异常{}", ex.getMessage());
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return null;
    }

    public ListData<OrderViewExtObject> getOrderViewExtObjectListDataByOrderViewObjectList(List<OrderViewObject> ordersInPage, Connection conn) {
        if (ordersInPage == null || ordersInPage.size() == 0) return null;
        else {
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

                    conn = getQueryRunner().getDataSource().getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sqlStr);
                    for (int i = 0; i < orders.size(); i++)  //设置参数
                    {

                        stmt.setString(i + 1, orders.get(i).getOrderId());
                    }
                    ResultSet rs = stmt.executeQuery();
//                   map = transResultToMap(rs);
                }
            } catch (final SQLException e) {
                log.error("查询掉单日志异常！");
            } finally {
                ObjectMapper mapper = new ObjectMapper();
                for (OrderViewObject orderViewObject : ordersInPage) {
                    OrderViewExtObject orderViewExtObject = ServletUtil.transOrderViewObjToExt(orderViewObject); //跟调单无关的属性先接收赋值
                    if (!"成功".equals(orderViewObject.getStatus())) {
                        orderViewExtObject.setLoseOrNot(false);
                    } else {
                        boolean loseOrNot = false;
                        List<LoseOrderLogObject> list = map.get(orderViewObject.getOrderId());
                        if (list == null || list.size() == 0) { //没有记录，要补单
                            loseOrNot = true;
                        } else {
                            int countStatus = 0;
                            for (LoseOrderLogObject loseOrderObject : list) {
                                int status = loseOrderObject.getResponseState();
                                if (status == 200) {
                                    String result = loseOrderObject.getAssistantResult();
                                    int index = result.indexOf("=");
                                    String resultStr = result.substring(0, index);
                                    if ("failure".equals(resultStr)) {
                                        loseOrNot = true;
                                        continue;
                                    }
                                    String resultMsg = result.substring(index + 1, result.length() - 1);
                                    ChargeRsp rsp = null;
                                    try {
                                        rsp = mapper.readValue(resultMsg, ChargeRsp.class);
                                    } catch (Exception e) {
                                        log.error("充值信息转换出错:{}", e.getMessage());
                                    }
                                    boolean flag = false;
                                    if (rsp != null) {
                                        flag = "充值成功".equals(rsp.getMessage()) || "订单已经存在".equals(rsp.getMessage());
                                    }
                                    if ("success".equals(resultStr) && flag) {
                                        loseOrNot = false;
                                        break;
                                    } else {
                                        loseOrNot = true;
                                    }
                                } else {
                                    countStatus++;
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
     * 分页查询
     *
     * @param pageNumber
     * @param limit
     * @param conn
     * @param paramList
     * @param sql
     * @return
     * @throws Exception
     */
    private List<OrderViewObject> getQueryDataByPage(int pageNumber, int limit, Connection conn, List<Object> paramList, String sql) throws Exception {
        PreparedStatement pstt = conn.prepareStatement(sql);
        int start = (pageNumber - 1) * limit;
        paramList.add(start);
        if (limit > 0) {
            paramList.add(limit);
        } else {
            paramList.add(20);
        }
        initPrepareStatement(paramList, pstt);
        ResultSet res = pstt.executeQuery();
        List<OrderViewObject> list = Lists.newArrayList();
        while (res.next()) {
            OrderViewObject orderView = new OrderViewObject();
            buildOrderView(orderView, res);
            list.add(orderView);
        }
        return list;
    }

    /**
     * 不分页查询
     *
     * @param conn
     * @param paramList
     * @param sql
     * @return
     * @throws Exception
     */
    private List<OrderViewObject> getQueryDataNotByPage(Connection conn, List<Object> paramList, String sql) throws Exception {
//        log.info("查询订单SQL：" + sql);
        PreparedStatement pstt = conn.prepareStatement(sql);
        initPrepareStatement(paramList, pstt);

        ResultSet res = pstt.executeQuery();
        List<OrderViewObject> list = Lists.newArrayList();
        while (res.next()) {
            OrderViewObject orderView = new OrderViewObject();
            buildOrderView(orderView, res);
            list.add(orderView);
        }
        return list;
    }


    private int getQueryDataTotalCount(Connection conn, List<Object> paramList, String SQL_COUNT) throws SQLException {
//        log.info("查询总数SQL：" + SQL_COUNT);
        PreparedStatement pst = conn.prepareStatement(SQL_COUNT);
        initPrepareStatement(paramList, pst);
        ResultSet resultSet = pst.executeQuery();
        int total = 0;
        while (resultSet.next()) {
            total = resultSet.getInt(1);
        }
        return total;
    }

    /**
     * 初始化PerpareStatement
     *
     * @param paramList 参数列表
     * @param pstt      PerpareStatement
     * @throws SQLException
     */
    private void initPrepareStatement(List<Object> paramList, PreparedStatement pstt) throws SQLException {
        int j = 1;
        for (Object obj : paramList) {
            String objStr = obj.toString();
            if (VerifyFormItem.checkNumber(objStr)) {
                pstt.setInt(j++, Integer.parseInt(objStr));
                continue;
            } else if (VerifyFormItem.isFloat(objStr)) {
                pstt.setFloat(j++, Float.parseFloat(objStr));
                continue;
            } else {
                if (objStr.startsWith("endOrder")) {
                    objStr = objStr.substring(8, objStr.length());
                }
                pstt.setString(j++, objStr);
                continue;
            }
        }
    }

    @Override
    public ListData<OrderViewObject> queryOrderViewById(String orderId) {
        final QueryRunner runner = getRunner();
        Connection conn = null;
        try {
            conn = runner.getDataSource().getConnection();
            StringBuilder SQL_CONDITION = new StringBuilder(" WHERE od.id  =? ");
            String sql = SqlConfig.ORDER_SELECT_SQ + SQL_CONDITION.toString();
//            log.info("根据订单号查询订单SQL：" + sql);
            PreparedStatement pstt = conn.prepareStatement(sql);
            if (!Strings.isNullOrEmpty(orderId)) {
                pstt.setString(1, orderId);
                ResultSet res = pstt.executeQuery();
                List<OrderViewObject> list = Lists.newArrayList();
                while (res.next()) {
                    OrderViewObject orderView = new OrderViewObject();
                    buildOrderView(orderView, res);
                    list.add(orderView);
                }
                ListData<OrderViewObject> listData = new ListData<OrderViewObject>(list, list.size());
                return listData;
            }
        } catch (Exception ex) {

            log.error("查询订单出现异常{0}", ex.getMessage());
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return null;
    }

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
    @Override
    public ListData<OrderViewObject> queryExportOrderViewList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {
        final QueryRunner runner = getRunner();
        Connection conn = null;
        try {
            final int gameId = orderViewObject.getGameId();
            conn = runner.getDataSource().getConnection();

            Map<String, Object> maps = ServletUtil.getSQLAndParamList(orderViewObject, payStartTime, payEndTime, assertStartTime, assertEndTime, minMoney, maxMoney, pageNumber, limit);

            List<Object> paramList = (List<Object>) maps.get("paramList");

            String SQL_CONDITION = maps.get("sql") != null ? maps.get("sql").toString() : null;

            String sql = (gameId == 1 ? SqlConfig.ORDER_SELECT_SQ : SqlConfig.ORDER_SELECT_NOTFORSQ) + SQL_CONDITION.toString() + " ORDER BY od.payTime  desc ";

            List<OrderViewObject> list = getQueryDataNotByPage(conn, paramList, sql);

            ListData<OrderViewObject> listData = new ListData<OrderViewObject>(list, list.size());
            return listData;

        } catch (Exception ex) {

            log.error("查询导出的订单数据出现异常{}", ex.getMessage());
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return null;
    }

    @Override
    public ListData<OrderViewExtObject> queryOrderViewListReWrite(Map<String, Object> condition) {
        //----------------------------------------获得条件-------------------------------------------------------
        int pageIndex = Integer.parseInt(condition.get("pageIndex").toString());//第几页
        int pageSize = Integer.parseInt(condition.get("pageSize").toString());//每页数据条数
        int start = pageSize * (pageIndex - 1);//开始条数
        Map<String, Object> SQLAndParamListMap = ServletUtil.getSQLAndParamListReWrite(condition);
        String orderCondition = SQLAndParamListMap.get("orderCondition").toString(); //搜索条件
        List<Object> paramList = (List<Object>) SQLAndParamListMap.get("paramList"); //搜索条件对应的值
        //-----------------------------------------申明返回对象----------------------------------------------------
        ListData<OrderViewExtObject> datas = null;  //带返回的订单集合
        List<OrderViewExtObject> orders = new LinkedList<OrderViewExtObject>(); //查询到的分页订单集合
        int orderCounts = 0;//记录条数
        Map<String, List<LoseOrderLogObject>> map = null; //存放订单号和交易日志的map
        List<String> orderIds = new LinkedList<String>();//申明一个数组，用来存放支付成功的订单ID
        //-----------------------------------------1,查询数据条数----------------------------------------------------
        final QueryRunner runner = getRunner();
        Connection conn = null;
        try {
            final int gameId = ((OrderViewObject) condition.get("orderViewObject")).getGameId();
            conn = runner.getDataSource().getConnection();
            //1,查询订单数据
            PreparedStatement pst = conn.prepareStatement((gameId == 1 ? SqlConfig.ORDER_COUNT_SQ : SqlConfig.ORDER_COUNT_NOTFORSQ) + orderCondition);
            initPrepareStatement(paramList, pst);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                orderCounts = resultSet.getInt(1);
            }
            //-----------------------------------------2,按照页码查询订单----------------------------------------------------
            final String orderSelectSql = ((gameId == 1 ? SqlConfig.ORDER_SELECT_SQ : SqlConfig.ORDER_SELECT_NOTFORSQ) + orderCondition + " order by od.payTime desc limit " + start + " , " + pageSize);
//            log.info("查询订单sql:" + orderSelectSql);
            PreparedStatement pst_order = conn.prepareStatement(orderSelectSql);
            initPrepareStatement(paramList, pst_order);
            ResultSet resultSet_order = pst_order.executeQuery();
            orders = buildOrderViewReWrite(resultSet_order);
            //-----------------------------3，根据订单数据，查询交易日志。----------------------------------------------------
            if (orders == null || orders.isEmpty()) {
                log.info("没有查到符合要求的订单!");
                return null;
            } else {

                for (OrderViewExtObject or : orders) {
                    if ("成功".equals(or.getStatus())) {
                        orderIds.add(or.getOrderId());
                    }
                }
                //--------------------------------------构造交易日子的查询语句-----------------------------------------------
                if (!orderIds.isEmpty()) {
                    StringBuffer sql = new StringBuffer(" select *  from " + SqlConfig.LOG_CHARGE_ORDER +
                            " where orderId in (");
                    for (String str : orderIds) //增加问号
                    {
                        sql.append("'" + str + "', ");
                    }
                    String sqlStr = sql.toString();
                    sqlStr = sqlStr.substring(0, sqlStr.length() - 2);
                    sqlStr += ")";//3,增加最后的半边括号

//                    log.info("交易日志sql:" + sqlStr);
                    //得到交易日志的map
                    map = getQueryRunner().query(sqlStr, new ResultSetHandler<Map<String, List<LoseOrderLogObject>>>() {
                        @Override
                        public Map<String, List<LoseOrderLogObject>> handle(ResultSet hResultSet) throws SQLException {
                            Map<String, List<LoseOrderLogObject>> hMap = Maps.newHashMap();

                            while (hResultSet != null && hResultSet.next()) {
                                LoseOrderLogObject loseOrderObject = new LoseOrderLogObject();
                                loseOrderObject.setId(hResultSet.getInt("id"));
                                loseOrderObject.setGameId(hResultSet.getInt("gameId"));
                                loseOrderObject.setOrderId(hResultSet.getString("orderId"));
                                loseOrderObject.setResponseState(hResultSet.getInt("responseState"));
                                loseOrderObject.setAssistantResult(hResultSet.getString("assistResult"));
                                String key = loseOrderObject.getOrderId();
                                List<LoseOrderLogObject> list = null;
                                if (hMap != null && !hMap.isEmpty()) {
                                    list = hMap.get(key);
                                }
                                if (list != null) {
                                    list.add(loseOrderObject);
                                } else {
                                    list = Lists.newArrayList();
                                    list.add(loseOrderObject);
                                }
                                hMap.put(key, list);
                            }
                            return hMap;
                        }
                    });
                }
            }
        } catch (Exception ex) {
            log.error("查询订单出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
            if (orders != null && !orders.isEmpty()) {
                for (OrderViewExtObject orderViewObject : orders) {
                    if ("成功".equals(orderViewObject.getStatus())) {
                        //1，拿到这个单子对应的日志集合
                        boolean loseOrNot = false;
                        List<LoseOrderLogObject> list = null;
                        if (map != null && !map.isEmpty()) {
                            list = map.get(orderViewObject.getOrderId());
                        }
                        //2.1 没有记录，要补单 显示补单。
                        if (list == null || list.isEmpty()) {
                            loseOrNot = true;
                        } else {//2.2 如果有记录 遍历记录
                            int countStatus = 0;//记录失败状态的个数
                            for (LoseOrderLogObject loseOrderObject : list) {
                                int status = loseOrderObject.getResponseState();
                                //3.1 如果状态不为200 说明失败，计数器+1；如果为200，到里面进行文字判断。
                                if (status == 200) {
                                    String result = loseOrderObject.getAssistantResult();
                                    if (!Strings.isNullOrEmpty(result) ) {
                                       final boolean flag = checkLoseOrder(loseOrderObject.getGameId(),result);
                                        if (flag) {
                                            loseOrNot = false;
                                            break;
                                        } else {
                                            loseOrNot = true;
                                            countStatus += 1;
                                            continue;
                                        }
                                    } else {
                                        countStatus += 1;
                                    }
                                } else {
                                    countStatus += 1;
                                }
                            }
                            if (countStatus == list.size()) { //所有的都失败的情况
                                loseOrNot = true;
                            }
                        }
                        orderViewObject.setLoseOrNot(loseOrNot);
                    }
                }
            }
            datas = new ListData<OrderViewExtObject>(orders, orderCounts);
            return datas;
        }
    }

    /**
     * 不同的游戏的交易日志，判断成功的方法
     *
     * @param gameId
     * @param msg
     * @return
     */
    private boolean checkLoseOrder(int gameId, String msg) {
        Preconditions.checkArgument(gameId > 0, "gameId非法！");
        boolean flag = false;
        if (!Strings.isNullOrEmpty(msg)) {
            if (1 == gameId || 2 == gameId) {
                if (msg.contains("=")) {
                    int index = msg.indexOf("=");
                    String resultStr = msg.substring(0, index);
                    flag = "success".equals(resultStr);
                }
            } else if (3 == gameId) {
                  flag="充值成功。".equals(msg);
            }
        }
        return flag;
    }


    /**
     * 查询得到jqgrid所需的数据格式
     *
     * @param conditions 查询条件
     * @param pageIndex  第几页
     * @param pageSize   每页数据条数
     * @param sortFiled  排序字段
     * @param sortOrder  排序顺序 比如说 升序或者降序
     * @return
     */
    @Override
    public JqObject queryOrders(Map<String, Object> conditions, int pageIndex, int pageSize, String sortFiled, String sortOrder, boolean useSelectCount) {
        //----------------------------------------获得条件-------------------------------------------------------
        int start = pageSize * (pageIndex - 1);//开始条数
        Map<String, Object> SQLAndParamListMap = ServletUtil.getSQLAndParamListForGrid(conditions);
        String orderCondition = "";
        List<Object> paramList = null;
        if (SQLAndParamListMap != null && !SQLAndParamListMap.isEmpty()) {
            orderCondition = SQLAndParamListMap.get("orderCondition").toString(); //搜索条件
            paramList = (List<Object>) SQLAndParamListMap.get("paramList"); //搜索条件对应的参数值
        } else {
            orderCondition = " where od.status=1 ";
        }
        //-----------------------------------------申明返回对象----------------------------------------------------
        JqObject datas = new JqObject();  //带返回的订单集合
        datas.setPage(0);
        datas.setRecords(0);
        datas.setTotal(0);
        datas.setRows(null);
        int orderCounts = 0;//总记录条数
        List<OrderViewExtObject> orders = new LinkedList<OrderViewExtObject>(); //查询到的分页订单集合
        List<String> orderIds = new LinkedList<String>();//申明一个数组，用来存放支付成功的订单ID
        Map<String, List<LoseOrderLogObject>> map = null; //存放订单号和交易日志的map
        //-----------------------------------------1,查询数据条数----------------------------------------------------
        final QueryRunner runner = getRunner();
        Connection conn = null;
        PreparedStatement count_pst = null, pst_order = null, stmt_log = null;
        ResultSet count_resultSet = null, resultSet_order = null, rs_log = null;
        try {
            final int gameId = ((OrderViewObject) conditions.get("orderViewObject")).getGameId();
            conn = runner.getDataSource().getConnection();
            if (useSelectCount) {
                //1,查询订单数据+ orderCondition  " select count(*) from "+SqlConfig.ORDER_TABLE+" od "
                count_pst = conn.prepareStatement((gameId == 1 ? SqlConfig.ORDER_COUNT_SQ : SqlConfig.ORDER_COUNT_NOTFORSQ) + orderCondition);
                if (paramList != null && !paramList.isEmpty()) {
                    initPrepareStatement(paramList, count_pst);
                }
                count_resultSet = count_pst.executeQuery();
                while (count_resultSet.next()) {
                    orderCounts = count_resultSet.getInt(1);
                }
            }


            //-----------------------------------------2,按照页码查询订单+ orderCondition----------------------------------------------------
            sortFiled = "od." + sortFiled;
            StringBuffer sortCondition = new StringBuffer(" order by " + sortFiled + " " + sortOrder + " ,");
            Map<String, String> sortMap = ServletUtil.getOrderSortMap(sortFiled);
            for (String str : sortMap.keySet()) {
                sortCondition.append(str + " ").append(sortMap.get(str)).append(" ,");
            }
            sortCondition.deleteCharAt(sortCondition.toString().lastIndexOf(","));
            sortCondition.append(" limit ").append(start).append(",").append(pageSize);
            pst_order = conn.prepareStatement((gameId == 1 ? SqlConfig.ORDER_SELECT_SQ : SqlConfig.ORDER_SELECT_NOTFORSQ) + orderCondition + sortCondition.toString());
            if (paramList != null && !paramList.isEmpty()) {
                initPrepareStatement(paramList, pst_order);
            }
            resultSet_order = pst_order.executeQuery();
            orders = buildOrderViewReWrite(resultSet_order);
            //-----------------------------3，根据订单数据，查询交易日志。----------------------------------------------------
            if (orders == null || orders.isEmpty()) {
                log.info("没有查到符合要求的订单!");
                return datas;
            } else {
                for (OrderViewExtObject or : orders) {
                    if ("成功".equals(or.getStatus())) {
                        orderIds.add(or.getOrderId());
                    }
                }
                //--------------------------------------构造交易日子的查询语句-----------------------------------------------
                if (!orderIds.isEmpty()) {
                    StringBuffer sql = new StringBuffer(" select *  from " + SqlConfig.LOG_CHARGE_ORDER +
                            " where orderId in (");
                    for (String str : orderIds) //增加问号
                    {
                        sql.append(" ?, ");
                    }
                    String sqlStr = sql.toString();
                    sqlStr = sqlStr.substring(0, sqlStr.length() - 2);
                    sqlStr += ")";//3,增加最后的半边括号

                    stmt_log = conn.prepareStatement(sqlStr);
                    for (int i = 0; i < orderIds.size(); i++)  //设置参数
                    {
                        stmt_log.setString(i + 1, orderIds.get(i));
                    }
                    rs_log = stmt_log.executeQuery();
                    map = transResultToMapReWrite(rs_log);//得到交易日志的map
                }
            }
        } catch (Exception ex) {
            log.error("查询订单出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(count_resultSet);
            DbUtils.closeQuietly(resultSet_order);
            DbUtils.closeQuietly(rs_log);
            DbUtils.closeQuietly(stmt_log);
            DbUtils.closeQuietly(pst_order);
            DbUtils.closeQuietly(count_pst);
            if (conn != null)
                try {
                    DbUtils.close(conn);
                } catch (Exception e) {
                    log.error("关闭连接出现异常{}", e.getMessage());
                }
            //--------------------------------------查询是不是掉单了-------------------------------------------------------
            if (orders != null && !orders.isEmpty())
                for (OrderViewExtObject orderViewObject : orders) {
                    if ("成功".equals(orderViewObject.getStatus())) {
                        //1，拿到这个单子对应的日志集合
                        boolean loseOrNot = false;
                        List<LoseOrderLogObject> lostOrderLogList = map.get(orderViewObject.getOrderId());
                        //2.1 没有记录，要补单 显示补单。
                        if (lostOrderLogList == null || lostOrderLogList.isEmpty()) {
                            loseOrNot = true;
                        } else {//2.2 如果有记录 遍历记录
                            int countFailStatus = 0;//记录失败状态的个数
                            for (LoseOrderLogObject loseOrderObject : lostOrderLogList) {
                                int status = loseOrderObject.getResponseState();
                                //3.1 如果状态不为200 说明失败，计数器+1；如果为200，到里面进行文字判断。
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
                                            countFailStatus += 1;
                                        }
                                    } else {
                                        countFailStatus += 1;
                                    }
                                } else {
                                    countFailStatus += 1;
                                }
                            }
                            if (countFailStatus == lostOrderLogList.size()) { //所有的都失败的情况
                                loseOrNot = true;
                            }
                        }
                        orderViewObject.setLoseOrNot(loseOrNot);
                    }
                }
            if (orders != null && !orders.isEmpty()) {
                datas.setPage(pageIndex);

                datas.setRows(transOrderExtToRowObjectList(orders));
                if (useSelectCount) {
                    datas.setRecords(orderCounts);
                    datas.setTotal(ServletUtil.getTotalPages(orderCounts, pageSize));
                }
            }
        }
        return datas;
    }

    private List<RowObject> transOrderExtToRowObjectList(List<OrderViewExtObject> orders) {
        List<RowObject> rowObjectList = new LinkedList<RowObject>();
        if (orders != null && !orders.isEmpty()) {
            for (OrderViewExtObject order : orders) {
                RowObject rowObject = new RowObject();
                rowObject.setId(order.getOrderId());
                List<Object> cells = new LinkedList<Object>();
                cells.add(order.getOrderId());
                cells.add("<a style='color:#369;' onclick='showUserAccountDiv(\"" + order.getUserId() + "\")'>" + order.getUserId() + "</a>");
                cells.add(order.getUserName());
                cells.add(order.getPlayerId());
                cells.add("￥" + order.getAmount());
                cells.add("<img src='./images/zs.png'/> " + order.getGold());
                cells.add(order.getAssertTime() == null ? "无" : CommonDateUtils.getDateString(order.getAssertTime()));
                cells.add(CommonDateUtils.getDateString(order.getPayTime()));
                if ("成功".equals(order.getStatus())) {
                    cells.add("<strong style='color: green;'>成功</strong>");
                } else {
                    cells.add("<strong style='color: red;'>未支付</strong>");
                }

                cells.add(order.getServerName());
                cells.add(order.getChannelName());
                cells.add(order.getSubTypeName());
                cells.add(order.getSubTypeTagName());
                cells.add(order.getEndOrder());
                if (order.isLoseOrNot()) {
                    cells.add("<a onclick='rechargeOrder(\"" + order.getOrderId() + "\")'><img src='./images/budan.png'/></a>");
                } else {
                    cells.add("<strong style='color: green;'>否</strong>");
                }
                rowObject.setCell(cells);
                rowObjectList.add(rowObject);
            }
        }
        return rowObjectList;
    }


    private Map<String, List<LoseOrderLogObject>> transResultToMapReWrite(ResultSet rs) throws SQLException {
        Map<String, List<LoseOrderLogObject>> map = Maps.newHashMap();
        while (rs != null && rs.next()) {
            LoseOrderLogObject loseOrderObject = new LoseOrderLogObject();
            loseOrderObject.setId(rs.getInt("id"));
            loseOrderObject.setOrderId(rs.getString("orderId"));
            loseOrderObject.setResponseState(rs.getInt("responseState"));
            loseOrderObject.setAssistantResult(rs.getString("assistResult"));
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
        log.info("map is null?" + map.isEmpty() + " map.size=" + map.size());
        return map;
    }


    /**
     * 把结果集转换成对象
     *
     * @param order
     * @param resultSet
     * @throws Exception
     */

    private void buildOrderView(OrderViewObject order, ResultSet resultSet) throws Exception {
        order.setOrderId(resultSet.getString("orderId"));
        order.setUserId(resultSet.getInt("userId"));
        order.setUserName(resultSet.getString("userName"));
        order.setPlayerId(resultSet.getInt("playerId"));
        order.setGameName(resultSet.getString("gameName"));
        order.setGameGoldName(resultSet.getString("gameGoldName"));
        order.setServerName(resultSet.getString("serverName"));
        order.setChannelName(resultSet.getString("channelName"));
        order.setSubTypeName(resultSet.getString("subTypeName"));
        order.setSubTypeTagName(resultSet.getString("subTypeTagName"));
        if (Strings.isNullOrEmpty(resultSet.getString("endOrder"))) {
            order.setEndOrder("无");
        } else {
            order.setEndOrder(resultSet.getString("endOrder"));
        }
        order.setAmount(resultSet.getFloat("amount"));
        order.setGold(resultSet.getFloat("gold"));
        if (resultSet.getInt("status") == 1) {
            order.setStatus("成功");
        } else {
            order.setStatus("未支付");
        }
        order.setPayTime(resultSet.getTimestamp("payTime"));
        if (Strings.isNullOrEmpty(resultSet.getString("assertTime"))) {
            order.setAssertTime(null);
        } else {
            order.setAssertTime(resultSet.getTimestamp("assertTime"));
        }
    }

    private List<OrderViewExtObject> buildOrderViewReWrite(ResultSet resultSet) throws Exception {
        List<OrderViewExtObject> orderViewExtObjects = new LinkedList<OrderViewExtObject>();
        while (resultSet != null && resultSet.next()) {
            OrderViewExtObject order = new OrderViewExtObject();
            order.setOrderId(resultSet.getString("orderId"));
            order.setUserId(resultSet.getInt("userId"));
            order.setUserName(resultSet.getString("userName"));
            order.setPlayerId(resultSet.getInt("playerId"));
            order.setGameId(resultSet.getInt("gameId"));
            order.setGameName(resultSet.getString("gameName"));
            order.setGameGoldName(resultSet.getString("gameGoldName"));
            order.setServerName(resultSet.getString("serverName"));
            order.setChannelName(resultSet.getString("channelName"));
            order.setSubTypeName(resultSet.getString("subTypeName"));
            order.setSubTypeTagName(resultSet.getString("subTypeTagName"));
            if (Strings.isNullOrEmpty(resultSet.getString("endOrder"))) {
                order.setEndOrder("无");
            } else {
                order.setEndOrder(resultSet.getString("endOrder"));
            }
            order.setAmount(resultSet.getFloat("amount"));
            order.setGold(resultSet.getFloat("gold"));
            if (resultSet.getInt("status") == 1) {
                order.setStatus("成功");
            } else {
                order.setStatus("未支付");
            }
            order.setPayTime(resultSet.getTimestamp("payTime"));
            if (Strings.isNullOrEmpty(resultSet.getString("assertTime"))) {
                order.setAssertTime(null);
            } else {
                order.setAssertTime(resultSet.getTimestamp("assertTime"));
            }
            order.setLoseOrNot(false);
            orderViewExtObjects.add(order);
        }
        return orderViewExtObjects;
    }


    @Override
    public OrderObject getByOrderId(String order) throws SQLException {
        return super.getByOrderId(order);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
