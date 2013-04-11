package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.ListData;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.manager.LoseOrderManager;
import com.sz7road.web.manager.OrderManager;
import com.sz7road.web.utils.POIUtil;
import com.sz7road.web.utils.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: leo.liao
 * Date: 12-6-13
 * Time: 上午9:47
 */
@Singleton
public class OrderServlet extends BaseServlet {

    @Inject
    private Provider<OrderManager> orderManagerProvider;

    @Inject
    private Provider<LoseOrderManager> providerLoseOrderManager;

    @Inject
    private Provider<UserService> userServiceProvider;

    private Logger log = LoggerFactory.getLogger(OrderServlet.class);

    public void showOrderPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/order.jsp").forward(request, response);
    }

    //跳转到订单统计页面
    public void orderStatistical(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("orderCount.jsp").forward(request, response);
    }


    // 查询订单数据
    public void getOrderDataByOrderId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderId = request.getParameter("orderId").trim();
        //验证，并转换
        OrderViewObject orderViewObject = new OrderViewObject();
        //取得订单号的查询条件
        if (!Strings.isNullOrEmpty(orderId)) {
            orderViewObject.setOrderId(orderId);
        }
        OrderManager orderManager = orderManagerProvider.get();
        ListData<OrderViewObject> listData = orderManager.queryOrderViewById(orderId);
        ServletUtil.returnJsonData(response, listData);
    }


    public void showServerPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("server_select.jsp").forward(request, response);
    }

    /**
     * 导出数据到xls
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void exportQueryData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1,拿到查询条件
        Map<String, Object> map = getQueryCondition(request);

        //空值判断，null没有toString方法
        String paystarttime = map.get("paystarttime") != null ? map.get("paystarttime").toString() : null;
        String payendtime = map.get("payendtime") != null ? map.get("payendtime").toString() : null;
        String assertstarttime = map.get("assertstarttime") != null ? map.get("assertstarttime").toString() : null;
        String assertendtime = map.get("assertendtime") != null ? map.get("assertendtime").toString() : null;

        //2,拿到查询的数据
        OrderManager orderManager = orderManagerProvider.get();
        ListData<OrderViewObject> queryData = orderManager.queryExportOrderViewList((OrderViewObject) map.get("orderviewobject"), paystarttime, payendtime, assertstarttime, assertendtime, Float.parseFloat(map.get("startmoney").toString()), Float.parseFloat(map.get("endmoney").toString()), Integer.parseInt(map.get("pagenumber").toString()), Integer.parseInt(map.get("limit").toString()));


        List<String> heads = new ArrayList<String>();
        heads.add("订单号");
        heads.add("用户ID");
        heads.add("用户名");
        heads.add("玩家ID");
        heads.add("金额");
        heads.add("游戏币");
        heads.add("确认时间");
        heads.add("提交时间");
        heads.add("订单状态");
        heads.add("服务器名称");
        heads.add("充值网关");
        heads.add("充值方式");
        heads.add("充值渠道");
        heads.add("网关订单号");

        //3,得到导出文件的名字
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = "订单数据" + sf.format(new Date()) + ".zip";
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        int size = queryData.getTotal();
        //小于1万条数据 直接输出excel
        if (size <= 10000) {
            fileName = "订单数据" + sf.format(new Date()) + ".xls";

            response.setHeader("content-disposition", "attachment;filename=" + response.encodeURL(new String(fileName.getBytes(), "utf-8")));

            OutputStream out = response.getOutputStream();
            POIUtil.builderHSSFWorkbook(queryData, heads, out);
        } else  //否则，分成多个excel，然后压缩输出
        {
            response.setHeader("content-disposition", "attachment;filename=" + response.encodeURL(new String(fileName.getBytes(), "utf-8")));

            OutputStream out = response.getOutputStream();
            POIUtil.builderHSSFWorkbookALot(queryData, heads, out);
        }


    }

    public void showOrderDataByAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> conditons = getQueryCondition(request);

        //空值判断，null没有toString方法
        String paystarttime = conditons.get("paystarttime") != null ? conditons.get("paystarttime").toString() : null;
        String payendtime = conditons.get("payendtime") != null ? conditons.get("payendtime").toString() : null;
        String assertstarttime = conditons.get("assertstarttime") != null ? conditons.get("assertstarttime").toString() : null;
        String assertendtime = conditons.get("assertendtime") != null ? conditons.get("assertendtime").toString() : null;

        OrderManager orderManager = orderManagerProvider.get();
        ListData<OrderViewObject> listData = orderManager.queryOrderViewList((OrderViewObject) conditons.get("orderviewobject"), paystarttime, payendtime, assertstarttime, assertendtime, Float.parseFloat(conditons.get("startmoney").toString()), Float.parseFloat(conditons.get("endmoney").toString()), Integer.parseInt(conditons.get("pagenumber").toString()), Integer.parseInt(conditons.get("limit").toString()));

        ListData<OrderViewExtObject> orderViewExtObjectLists = providerLoseOrderManager.get().getOrderViewExtObjectListDataByOrderViewObjectList(listData.getList());

        long orderCounts = 0;
        if (listData != null) {
            orderCounts = listData.getTotal();
        }
        if (orderViewExtObjectLists == null) {
            ServletUtil.returnNUllJsonData(response);
        } else {
            orderViewExtObjectLists.setTotal((int) orderCounts);

            ServletUtil.returnJsonData(response, orderViewExtObjectLists);
        }
    }

    public void showOrderDataByAjaxReWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> conditons = getQueryConditionReWrite(request);
        OrderManager orderManager = orderManagerProvider.get();
        ListData<OrderViewExtObject> listData = orderManager.queryOrderViewListReWrite(conditons);
        if (listData == null) {
            ServletUtil.returnNUllJsonData(response);
        } else {
            ServletUtil.returnJsonData(response, listData);
        }
    }

    /**
     * 获得查询的条件map
     *
     * @param request
     * @return
     */
    private Map<String, Object> getQueryConditionReWrite(HttpServletRequest request) {
        String pageSize = request.getParameter("pageSize").trim(); //每页多少条数据
        String pageIndex = request.getParameter("pageIndex").trim(); //第几页
        //获得查询条件
        String orderId = request.getParameter("orderId").trim();
        String endId = request.getParameter("endId").trim();
        String queryUserType = request.getParameter("queryUserType").trim();
        String userId = request.getParameter("userId").trim();
        try {
            userId = java.net.URLDecoder.decode(userId, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String playerId = request.getParameter("playerId").trim();
        String gameName = request.getParameter("gameName").trim();
        String serverName = request.getParameter("serverName").trim();
        String channelId = request.getParameter("channelId").trim();
        String subType = request.getParameter("subType").trim();
        String bank = request.getParameter("bank").trim();
        String status = request.getParameter("status").trim();
        String payStartTime = request.getParameter("payStartTime").trim();
        String payEndTime = request.getParameter("payEndTime").trim();
        String assertStartTime = request.getParameter("assertStartTime").trim();
        String assertEndTime = request.getParameter("assertEndTime").trim();
        String startMoney = request.getParameter("startMoney").trim();
        String endMoney = request.getParameter("endMoney").trim();
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        //验证，并转换
        OrderViewObject orderViewObject = new OrderViewObject();
        //取得订单号的查询条件
        if (!Strings.isNullOrEmpty(orderId)) {
            orderViewObject.setOrderId(orderId);
        }
        //取得网关订单号的查询条件
        if (!Strings.isNullOrEmpty(endId)) {
            orderViewObject.setEndOrder(endId);
        }
        //取得用户的查询条件
        if (!Strings.isNullOrEmpty(queryUserType) && !Strings.isNullOrEmpty(userId)) {
            if ("userId".equals(queryUserType)) {
                orderViewObject.setUserId(Integer.parseInt(userId));
            } else if ("userName".equals(queryUserType)) {
                orderViewObject.setUserName(userId);
            }
        }
        //取得玩家的查询条件
        if (!Strings.isNullOrEmpty(playerId)) {
            orderViewObject.setPlayerId(Integer.parseInt(playerId));
        }
        //取得游戏名称,转换成游戏ID
        if (!Strings.isNullOrEmpty(gameName) && VerifyFormItem.checkNumber(gameName)) {
            orderViewObject.setGameId(Integer.parseInt(gameName));
        }
        else
        {
            orderViewObject.setGameId(1);
        }
        //取得服务器名称，转换成服务器ID
        if (!Strings.isNullOrEmpty(serverName) && VerifyFormItem.checkNumber(serverName)) {
            orderViewObject.setZoneId(Integer.parseInt(serverName));
        }
        //取得支付网关
        if (!Strings.isNullOrEmpty(channelId) && !"empty".equals(channelId)) {
            orderViewObject.setChannelName(channelId);
        }
        //取得支付方式
        if (!Strings.isNullOrEmpty(subType) && !"empty".equals(subType)) {
            orderViewObject.setSubTypeName(subType);
        }
        //取得支付渠道
        if (!Strings.isNullOrEmpty(bank) && !"empty".equals(bank)) {
            orderViewObject.setSubTypeTagName(bank);
        }
        //取得支付状态
        if (!Strings.isNullOrEmpty(status) && !"empty".equals(status)) {
            orderViewObject.setStatus(status);
        }
        //取得充值金额的上限和下限 ,最早和最晚提交日期，最早和最晚验证日期
        if (!Strings.isNullOrEmpty(startMoney) && VerifyFormItem.checkNumber(startMoney)) {

        } else {
            startMoney = "0.0";
        }
        if (!Strings.isNullOrEmpty(endMoney) && VerifyFormItem.checkNumber(endMoney)) {

        } else {
            endMoney = "0.0";
        }
        if (!Strings.isNullOrEmpty(payStartTime) && VerifyFormItem.checkDate(payStartTime, datePattern)) {

        } else {
            payStartTime = "空";
        }
        if (!Strings.isNullOrEmpty(payEndTime) && VerifyFormItem.isDate(payEndTime, datePattern)) {

        } else {
            payEndTime = "空";
        }
        if (!Strings.isNullOrEmpty(assertStartTime) && VerifyFormItem.isDate(assertStartTime, datePattern)) {

        } else {
            assertStartTime = "空";
        }
        if (!Strings.isNullOrEmpty(assertEndTime) && VerifyFormItem.isDate(assertEndTime, datePattern)) {

        } else {
            assertEndTime = "空";
        }
        //如果参数是空的，设置默认值
        if (Strings.isNullOrEmpty(pageIndex)) {
            pageIndex = "1";
        }
        if (Strings.isNullOrEmpty(pageSize)) {
            pageSize = "15";
        }
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("orderViewObject", orderViewObject);
        conditions.put("payStartTime", payStartTime);
        conditions.put("payEndTime", payEndTime);
        conditions.put("assertStartTime", assertStartTime);
        conditions.put("assertEndTime", assertEndTime);
        conditions.put("startMoney", startMoney);
        conditions.put("endMoney", endMoney);
        conditions.put("pageIndex", pageIndex);
        conditions.put("pageSize", pageSize);
        return conditions;
    }

    /**
     * 获得查询的条件map
     *
     * @param request
     * @return
     */
    private Map<String, Object> getQueryCondition(HttpServletRequest request) {
        String limit = request.getParameter("pageItemNumber"); //每页多少条数据
        String pageNumber = request.getParameter("pageNumber"); //第几页
        //获得查询条件
        String orderId = request.getParameter("orderId").trim();
        String endId = request.getParameter("endId").trim();
        String queryUserType = request.getParameter("queryUserType").trim();
        String userId = request.getParameter("userId").trim();
        try {
            userId = java.net.URLDecoder.decode(userId, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String playerId = request.getParameter("playerId").trim();
        String gameName = request.getParameter("gameName").trim();
        String serverName = request.getParameter("serverName").trim();
        String startMoney = request.getParameter("startMoney").trim();
        String endMoney = request.getParameter("endMoney").trim();
        String channelId = request.getParameter("channelId").trim();
        String subType = request.getParameter("subType").trim();
        String bank = request.getParameter("bank").trim();
        String status = request.getParameter("status").trim();
        String payStartTime = request.getParameter("payStartTime").trim();
        String payEndTime = request.getParameter("payEndTime").trim();
        String assertStartTime = request.getParameter("assertStartTime").trim();
        String assertEndTime = request.getParameter("assertEndTime").trim();

        String datePattern = "yyyy-MM-dd HH:mm:ss";
        //验证，并转换
        OrderViewObject orderViewObject = new OrderViewObject();
        //取得订单号的查询条件
        if (!Strings.isNullOrEmpty(orderId)) {
            orderViewObject.setOrderId(orderId);
        }
        //取得网关订单号的查询条件
        if (!Strings.isNullOrEmpty(endId)) {
            orderViewObject.setEndOrder(endId);
        }

        //取得用户的查询条件
        if (!Strings.isNullOrEmpty(queryUserType) && !Strings.isNullOrEmpty(userId)) {
            if ("userId".equals(queryUserType)) {
                orderViewObject.setUserId(Integer.parseInt(userId));
            } else if ("userName".equals(queryUserType)) {
                orderViewObject.setUserName(userId);
            }
        }

        //取得玩家的查询条件

        if (!Strings.isNullOrEmpty(playerId)) {
            orderViewObject.setPlayerId(Integer.parseInt(playerId));
        }

        //取得游戏名称,转换成游戏ID
        if (!Strings.isNullOrEmpty(gameName) && VerifyFormItem.checkNumber(gameName)) {
            orderViewObject.setGameId(Integer.parseInt(gameName));
        }
        //取得服务器名称，转换成服务器ID
        if (!Strings.isNullOrEmpty(serverName) && VerifyFormItem.checkNumber(serverName)) {
            orderViewObject.setZoneId(Integer.parseInt(serverName));
        }
        //取得充值金额的上限和下限 ,最早和最晚提交日期，最早和最晚验证日期
        if (!Strings.isNullOrEmpty(startMoney) && VerifyFormItem.checkNumber(startMoney)) {

        } else {
            startMoney = "0.0";
        }
        if (!Strings.isNullOrEmpty(endMoney) && VerifyFormItem.checkNumber(endMoney)) {

        } else {
            endMoney = "0.0";
        }
        if (!Strings.isNullOrEmpty(payStartTime) && VerifyFormItem.checkDate(payStartTime, datePattern)) {

        } else {
            payStartTime = null;
        }
        if (!Strings.isNullOrEmpty(payEndTime) && VerifyFormItem.isDate(payEndTime, datePattern)) {

        } else {
            payEndTime = null;
        }
        if (!Strings.isNullOrEmpty(assertStartTime) && VerifyFormItem.isDate(assertStartTime, datePattern)) {

        } else {
            assertStartTime = null;
        }
        if (!Strings.isNullOrEmpty(assertEndTime) && VerifyFormItem.isDate(assertEndTime, datePattern)) {

        } else {
            assertEndTime = null;
        }
        //取得支付网关
        if (!Strings.isNullOrEmpty(channelId) && !"empty".equals(channelId)) {
            orderViewObject.setChannelName(channelId);
        }
        //取得支付方式
        if (!Strings.isNullOrEmpty(subType) && !"empty".equals(subType)) {
            orderViewObject.setSubTypeName(subType);
        }
        //取得支付渠道
        if (!Strings.isNullOrEmpty(bank) && !"empty".equals(bank)) {
            orderViewObject.setSubTypeTagName(bank);
        }
        //取得支付状态
        if (!Strings.isNullOrEmpty(status) && !"empty".equals(status)) {
            orderViewObject.setStatus(status);
        }
        //如果参数是空的，设置默认值
        if (Strings.isNullOrEmpty(pageNumber)) pageNumber = "1";

        if (Strings.isNullOrEmpty(limit)) limit = "15";

        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("orderviewobject", orderViewObject);
        conditions.put("paystarttime", payStartTime);
        conditions.put("payendtime", payEndTime);
        conditions.put("assertstarttime", assertStartTime);
        conditions.put("assertendtime", assertEndTime);
        conditions.put("startmoney", startMoney);
        conditions.put("endmoney", endMoney);
        conditions.put("pagenumber", pageNumber);
        conditions.put("limit", limit);

        return conditions;

    }


    public void showOrderByGrid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/orderGrid.jsp").forward(request, response);
    }

    /**
     * 使用jqgrid显示订单查询，保证美感和功能，还有性能。
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getOrderByGrid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*-------------------------------------1，接收基本参数-------------------------------------------------------------*/
        String pageIndex = request.getParameter("page");
        String pageSize = request.getParameter("rows");
        String orderColumn = request.getParameter("sidx");
        String orderDirect = request.getParameter("sord");
        String type = request.getParameter("type");
        String orderGridCounts = request.getParameter("f_orderGridCounts");
        OrderManager orderManager = orderManagerProvider.get();
        JqObject orders = null;
        boolean isSame = checkCondition(request);
        Map<String, Object> conditions=new HashMap<String, Object>();
        if ("init".equals(type)) { //打开订单查询，显示所有成功的订单。
            if (!Strings.isNullOrEmpty(orderGridCounts))
            {
                orders = orderManager.getOrderByGrid(null, Integer.parseInt(pageIndex), Integer.parseInt(pageSize), orderColumn, orderDirect,true);
            }
            else
            {
            orders = orderManager.getOrderByGrid(null, Integer.parseInt(pageIndex), Integer.parseInt(pageSize), orderColumn, orderDirect,true);
            }
        } else if ("query".equals(type)) {
            /*-------------------------------------接收查询参数-------------------------------------------------------------*/
            conditions = getQueryConditionForGrid(request);
            /*-------------------------------------2，验证转换-------------------------------------------------------------*/
            //空值判断，null没有toString方法
            String payStartTime = conditions.get("payStartTime") != null ? conditions.get("payStartTime").toString() : null;
            String payEndTime = conditions.get("payEndTime") != null ? conditions.get("payEndTime").toString() : null;
            String assertStartTime = conditions.get("assertStartTime") != null ? conditions.get("assertStartTime").toString() : null;
            String assertEndTime = conditions.get("assertEndTime") != null ? conditions.get("assertEndTime").toString() : null;
            if (!Strings.isNullOrEmpty(pageIndex) && VerifyFormItem.isInteger(pageIndex)
                    && !Strings.isNullOrEmpty(pageSize) && VerifyFormItem.isInteger(pageSize)
                    && !Strings.isNullOrEmpty(orderColumn) && !Strings.isNullOrEmpty(orderDirect)) {
                /*-------------------------------------3，拿到数据-------------------------------------------------------------*/
                orders = orderManager.getOrderByGrid(conditions, Integer.parseInt(pageIndex), Integer.parseInt(pageSize), orderColumn, orderDirect, true);
                /*-------------------------------------4，写回数据-------------------------------------------------------------*/
            }
        }
        if (isSame) {
            if (!Strings.isNullOrEmpty(orderGridCounts)) {
                int totalCount = Integer.parseInt(orderGridCounts);
                orders.setRecords(totalCount);
                orders.setTotal(ServletUtil.getTotalPages(totalCount, Integer.parseInt(pageSize)));
            }
        }
        conditions.put("orderGrid_totalCounts",orders.getRecords());
        orders.setExt(conditions); //设置查询条件
        ServletUtil.returnJson(response, orders);
    }

    /**
     * 获得查询的条件map
     *
     * @param request
     * @return
     */
    private Map<String, Object> getQueryConditionForGrid(HttpServletRequest request) {
        //获得查询条件
        String orderId = request.getParameter("orderId").trim();
        String endId = request.getParameter("endId").trim();
        String queryUserType = request.getParameter("queryUserType").trim();
        String userIdOrName = request.getParameter("userIdOrName").trim();
        String playerId = request.getParameter("playerId").trim();
        String gameId = request.getParameter("gameId").trim();
        String serverId = request.getParameter("serverId").trim();
        String channelId = request.getParameter("channelId").trim();
        String subType = request.getParameter("subType").trim();
        String subTypeTag = request.getParameter("subTypeTag").trim();
        String status = request.getParameter("status").trim();
        String payStartTime = request.getParameter("payStartTime").trim();
        String payEndTime = request.getParameter("payEndTime").trim();
        String assertStartTime = request.getParameter("assertStartTime").trim();
        String assertEndTime = request.getParameter("assertEndTime").trim();
        String startMoney = request.getParameter("startMoney").trim();
        String endMoney = request.getParameter("endMoney").trim();
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        try {
            userIdOrName = java.net.URLDecoder.decode(userIdOrName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //验证，并转换
        OrderViewObject orderViewObject = new OrderViewObject();
        //取得订单号的查询条件
        if (!Strings.isNullOrEmpty(orderId)) {
            orderViewObject.setOrderId(orderId);
        }
        //取得网关订单号的查询条件
        if (!Strings.isNullOrEmpty(endId)) {
            orderViewObject.setEndOrder(endId);
        }
        //取得用户的查询条件
        if (!Strings.isNullOrEmpty(queryUserType) && !Strings.isNullOrEmpty(userIdOrName)) {
            if ("userId".equals(queryUserType)) {
                orderViewObject.setUserId(Integer.parseInt(userIdOrName));
            } else if ("userName".equals(queryUserType)) {
                orderViewObject.setUserId(userServiceProvider.get().findUserIdByUserName(userIdOrName));
            }
        }
        //取得玩家的查询条件
        if (!Strings.isNullOrEmpty(playerId)) {
            orderViewObject.setPlayerId(Integer.parseInt(playerId));
        }
        //取得游戏名称,转换成游戏ID
        if (!Strings.isNullOrEmpty(gameId) && VerifyFormItem.checkNumber(gameId)) {
            orderViewObject.setGameId(Integer.parseInt(gameId));
        }
        //取得服务器名称，转换成服务器ID
        if (!Strings.isNullOrEmpty(serverId) && VerifyFormItem.checkNumber(serverId)) {
            orderViewObject.setZoneId(Integer.parseInt(serverId));
        }
        //取得支付网关
        if (!Strings.isNullOrEmpty(channelId) && !"empty".equals(channelId)) {
            orderViewObject.setChannelName(channelId);
        }
        //取得支付方式
        if (!Strings.isNullOrEmpty(subType) && !"empty".equals(subType)) {
            orderViewObject.setSubTypeName(subType);
        }
        //取得支付渠道
        if (!Strings.isNullOrEmpty(subTypeTag) && !"empty".equals(subTypeTag)) {
            orderViewObject.setSubTypeTagName(subTypeTag);
        }
        //取得支付状态
        if (!Strings.isNullOrEmpty(status) && !"empty".equals(status)) {
            orderViewObject.setStatus(status);
        }
        //取得充值金额的上限和下限 ,最早和最晚提交日期，最早和最晚验证日期
        if (!Strings.isNullOrEmpty(startMoney) && VerifyFormItem.checkNumber(startMoney)) {

        } else {
            startMoney = "0.0";
        }
        if (!Strings.isNullOrEmpty(endMoney) && VerifyFormItem.checkNumber(endMoney)) {

        } else {
            endMoney = "0.0";
        }
        if (!Strings.isNullOrEmpty(payStartTime) && VerifyFormItem.checkDate(payStartTime, datePattern)) {

        } else {
            payStartTime = "空";
        }
        if (!Strings.isNullOrEmpty(payEndTime) && VerifyFormItem.isDate(payEndTime, datePattern)) {

        } else {
            payEndTime = "空";
        }
        if (!Strings.isNullOrEmpty(assertStartTime) && VerifyFormItem.isDate(assertStartTime, datePattern)) {

        } else {
            assertStartTime = "空";
        }
        if (!Strings.isNullOrEmpty(assertEndTime) && VerifyFormItem.isDate(assertEndTime, datePattern)) {

        } else {
            assertEndTime = "空";
        }
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("orderViewObject", orderViewObject);
        conditions.put("queryUserType", queryUserType);
        conditions.put("userIdOrName", userIdOrName);
        conditions.put("payStartTime", payStartTime);
        conditions.put("payEndTime", payEndTime);
        conditions.put("assertStartTime", assertStartTime);
        conditions.put("assertEndTime", assertEndTime);
        conditions.put("startMoney", startMoney);
        conditions.put("endMoney", endMoney);
        return conditions;
    }

    /**
     * 判断基本的查询条件有没有改变
     *
     * @param request
     * @return 如果一样，返回true,如果改变，返回false
     */
    private boolean checkCondition(HttpServletRequest request) {
        //获得查询条件
        String orderId = request.getParameter("orderId");
        String f_orderId = request.getParameter("f_orderId");
        if (!Strings.nullToEmpty(orderId).equals(Strings.nullToEmpty(f_orderId)))  {
            return false;
        }

        String endId = request.getParameter("endId");
        String f_endId = request.getParameter("f_endId");
        if(!Strings.nullToEmpty(endId).equals(Strings.nullToEmpty(f_endId))){
            return false;
        }

        String queryUserType = request.getParameter("queryUserType");
        String f_queryUserType = request.getParameter("f_queryUserType");
        if (!Strings.nullToEmpty(queryUserType).equals(Strings.nullToEmpty(f_queryUserType))) {
            return false;
        }
        String userIdOrName = request.getParameter("userIdOrName");
        String f_userIdOrName = request.getParameter("f_userIdOrName");
        if (!Strings.nullToEmpty(userIdOrName).equals(Strings.nullToEmpty(f_userIdOrName))) {
            return false;
        }

        String playerId = request.getParameter("playerId");
        String f_playerId = request.getParameter("f_playerId");
        if(!Strings.nullToEmpty(playerId).equals(Strings.nullToEmpty(f_playerId)))  {
            return false;
        }
        String gameId = request.getParameter("gameId");
        String f_gameId = request.getParameter("f_gameId");
        if(!Strings.nullToEmpty(gameId).equals(Strings.nullToEmpty(f_gameId)))  {
            return false;
        }
        String serverId = request.getParameter("serverId");
        String f_serverId = request.getParameter("f_serverId");
        if(!Strings.nullToEmpty(serverId).equals(Strings.nullToEmpty(f_serverId))) {
            return false;
        }

        String channelId = request.getParameter("channelId");
        String f_channelId = request.getParameter("f_channelId");
        if (channelId != f_channelId) {
            return false;
        }

        String subType = request.getParameter("subType");
        String f_subType = request.getParameter("f_subType");
        if (subType != f_subType) {
            return false;
        }

        String subTypeTag = request.getParameter("subTypeTag");
        String f_subTypeTag = request.getParameter("f_subTypeTag");
        if (subTypeTag != f_subTypeTag) {
            return false;
        }

        String status = request.getParameter("status");
        String f_status = request.getParameter("f_status");
        if (status != f_status) {
            return false;
        }


        String payStartTime = request.getParameter("payStartTime");
        String f_payStartTime = request.getParameter("f_payStartTime");
        if (payStartTime != f_payStartTime) {
            return false;
        }
        String payEndTime = request.getParameter("payEndTime");
        String f_payEndTime = request.getParameter("f_payEndTime");
        if (payEndTime != f_payEndTime) {
            return false;
        }


        String assertStartTime = request.getParameter("assertStartTime");
        String f_assertStartTime = request.getParameter("f_assertStartTime");
        if (assertStartTime != f_assertStartTime) {
            return false;
        }

        String assertEndTime = request.getParameter("assertEndTime");
        String f_assertEndTime = request.getParameter("f_assertEndTime");
        if (assertEndTime != f_assertEndTime) {
            return false;
        }

        String startMoney = request.getParameter("startMoney");
        String f_startMoney = request.getParameter("f_startMoney");
        if (f_startMoney != f_startMoney) {
            return false;
        }

        String endMoney = request.getParameter("endMoney");
        String f_endMoney = request.getParameter("f_endMoney");
        if (endMoney != f_endMoney) {
            return false;
        }


        return true;
    }
}
