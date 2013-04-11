package com.sz7road.web.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojo.OrderViewExtObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.utils.ListData;
import com.sz7road.utils.VerifyFormItem;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-28
 * Time: 下午4:33
 * 提取servlet中的一些公共方法
 */
public class ServletUtil {

    private static Logger log = LoggerFactory.getLogger(ServletUtil.class);

    /**
     * 拿到查到的数据，然后写回页面
     *
     * @param response resonse对象
     * @param listData 数据
     */
    public static void returnJsonData(HttpServletResponse response, ListData listData) {
        try {
            response.setContentType("application/x-json");
            ObjectMapper mapper = new ObjectMapper();
            String rsp = mapper.writeValueAsString(listData);
            response.getWriter().print(rsp);
//            log.info("json数据是：" + rsp);
        } catch (Exception ex) {
            log.error("写回response失败！");
        } finally {
            try {
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e) {
                log.error("关闭response异常！");
            }
        }
    }

    /**
     * 把传入的对象转换成json 然后写回去
     *
     * @param response
     * @param obj
     */
    public static void returnJson(HttpServletResponse response, Object obj) {
        try {
            response.setContentType("application/x-json");
            ObjectMapper mapper = new ObjectMapper();
            String rsp = mapper.writeValueAsString(obj);
            response.getWriter().print(rsp);
        } catch (Exception ex) {
            log.error("写回response失败！");
        } finally {
            try {
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e) {
                log.error("关闭response异常！");
            }
        }
    }

    /**
     * 把json字符串写回去
     *
     * @param response
     * @param jsonStr
     */
    public static void returnJsonString(HttpServletResponse response, String jsonStr) {
        try {
            response.setContentType("application/x-json");
//            ObjectMapper mapper = new ObjectMapper();
//            String rsp = mapper.writeValueAsString(obj);
            response.getWriter().print(jsonStr);
//            log.info("json数据是：" + rsp);
        } catch (Exception ex) {
            log.error("写回response失败！");
        } finally {
            try {
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e) {
                log.error("关闭response异常！");
            }
        }
    }


    /**
     * 返回空的结果
     *
     * @param response
     */
    public static void returnNUllJsonData(HttpServletResponse response) {
        try {
            response.setContentType("application/x-json");

            response.getWriter().print("");
            log.info("json数据是：为空");
        } catch (Exception ex) {
            log.error("写回response失败！");
        } finally {
            try {
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e) {
                log.error("关闭response异常！");
            }
        }
    }

    /**
     * 把拿到的数据传递到扩展类中去
     *
     * @param orderViewObject 原来的数据实体
     * @return 扩展后的数据实体
     */
    public static OrderViewExtObject transOrderViewObjToExt(OrderViewObject orderViewObject) {
        OrderViewExtObject order = new OrderViewExtObject();

        order.setOrderId(orderViewObject.getOrderId());
        order.setUserId(orderViewObject.getUserId());
        order.setUserName(orderViewObject.getUserName());
        order.setPlayerId(orderViewObject.getPlayerId());
        order.setGameName(orderViewObject.getGameName());
        order.setGameGoldName(orderViewObject.getGameGoldName());
        order.setServerName(orderViewObject.getServerName());
        order.setChannelName(orderViewObject.getChannelName());
        order.setSubTypeName(orderViewObject.getSubTypeName());
        order.setSubTypeTagName(orderViewObject.getSubTypeTagName());
        order.setEndOrder(orderViewObject.getEndOrder());
        order.setAmount(orderViewObject.getAmount());
        order.setGold(orderViewObject.getGold());
        order.setStatus(orderViewObject.getStatus());
        order.setPayTime(orderViewObject.getPayTime());
        order.setAssertTime(orderViewObject.getAssertTime());

        return order;
    }

    /**
     * @param orderCounts    总记录数
     * @param pageItemNumber 每页的记录树
     * @return 页数
     */
    public static int getTotalPages(long orderCounts, int pageItemNumber) {
        int $pageNumber = 0;
        if (orderCounts > 0) {
            if (orderCounts % pageItemNumber == 0) {
                $pageNumber = (int) orderCounts / pageItemNumber;
            } else {
                $pageNumber = (int) orderCounts / pageItemNumber + 1;
            }
        }
        return $pageNumber;
    }

    /**
     * 返回组合的sql语句和组合的参数。
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
    public static Map<String, Object> getSQLAndParamList(OrderViewObject orderViewObject, String payStartTime, String payEndTime, String assertStartTime, String assertEndTime, float minMoney, float maxMoney, int pageNumber, int limit) {
        List<Object> paramList = Lists.newArrayList();
        StringBuilder SQL_CONDITION = new StringBuilder(" WHERE 1 ");
        buildOrderViewCondition(SQL_CONDITION, paramList, orderViewObject);
        if (!Strings.isNullOrEmpty(payStartTime)) {
            SQL_CONDITION.append(" AND od.payTime >=?");
            paramList.add(payStartTime);
        }
        if (!Strings.isNullOrEmpty(payEndTime)) {
            SQL_CONDITION.append(" AND od.payTime <=?");
            paramList.add(payEndTime);
        }
        if (!Strings.isNullOrEmpty(assertStartTime)) {
            SQL_CONDITION.append(" AND od.assertTime >=?");
            paramList.add(assertStartTime);
        }
        if (!Strings.isNullOrEmpty(assertEndTime)) {
            SQL_CONDITION.append(" AND od.assertTime  <=?");
            paramList.add(assertEndTime);
        }
        if (minMoney == maxMoney && minMoney > 0) {
            SQL_CONDITION.append(" AND od.amount =?");
            paramList.add(packMoney(String.valueOf(minMoney)));
        } else {
            if (minMoney > 0) {
                SQL_CONDITION.append(" AND od.amount >=?");
                paramList.add(packMoney(String.valueOf(minMoney)));
            }
            if (maxMoney > 0 && maxMoney > minMoney) {
                SQL_CONDITION.append(" AND od.amount <=?");
                paramList.add(packMoney(String.valueOf(maxMoney)));
            }
        }
        final int gameId=orderViewObject.getGameId();
        String SQL_COUNT =(gameId==1? SqlConfig.ORDER_COUNT_SQ:SqlConfig.ORDER_COUNT_NOTFORSQ) + SQL_CONDITION.toString();

        Map<String, Object> maps = new HashMap<String, Object>();

        maps.put("sql", SQL_CONDITION);
        maps.put("paramList", paramList);
        return maps;


    }

    public static Map<String, Object> getSQLAndParamListReWrite(Map<String, Object> conditions) {
        List<Object> paramList = Lists.newArrayList();
        StringBuilder SQL_CONDITION = new StringBuilder(" WHERE 1 ");
        OrderViewObject orderViewObject = (OrderViewObject) conditions.get("orderViewObject");

        //组合订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getOrderId())) {
            SQL_CONDITION.append(" and od.id=? ");
            paramList.add(orderViewObject.getOrderId());
        }
        //组合网关订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getEndOrder())) {
            SQL_CONDITION.append(" and od.endOrder=? ");
            orderViewObject.setEndOrder("endOrder" + orderViewObject.getEndOrder());
            paramList.add(orderViewObject.getEndOrder());
        }
        //组合用户ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getUserId())) && orderViewObject.getUserId() > 0) {
            SQL_CONDITION.append(" and od.userId=? ");
            paramList.add(orderViewObject.getUserId());
        }
        //组合用户名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getUserName())) {
            SQL_CONDITION.append(" and u.userName=? ");
            paramList.add(orderViewObject.getUserName());
        }
        //组合玩家ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getPlayerId())) && orderViewObject.getPlayerId() > 0) {
            SQL_CONDITION.append(" and  od.playerId=? ");
            paramList.add(orderViewObject.getPlayerId());
        }
        // 组合玩家名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getPlayerName())) {
            SQL_CONDITION.append(" and u.userName=? ");
            paramList.add(orderViewObject.getPlayerName());
        }

        // 组合游戏名查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getGameId())) && orderViewObject.getGameId() > 0) {
            SQL_CONDITION.append(" and od.gameId=? ");
            paramList.add(orderViewObject.getGameId());
        }
        // 组合服务器名称查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getZoneId())) && orderViewObject.getZoneId() > 0) {
            SQL_CONDITION.append(" and od.zoneId=? ");
            paramList.add(orderViewObject.getZoneId());
        }
        // 组合支付网关名称查询
        if (!Strings.isNullOrEmpty(orderViewObject.getChannelName())) {
            SQL_CONDITION.append(" and od.channelId=? ");
            paramList.add(orderViewObject.getChannelName());
        }

        // 组合支付方式查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeName())) {
            SQL_CONDITION.append(" and od.subType=? ");
            paramList.add(orderViewObject.getSubTypeName());
        }

        // 组合支付渠道查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeTagName())) {
            SQL_CONDITION.append(" and od.subTag=? ");
            paramList.add(orderViewObject.getSubTypeTagName());
        }

        // 组合订单状态查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getStatus()))) {
            if ("success".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status=? ");
                paramList.add(1);
            } else if ("failed".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status !=? ");
                paramList.add(1);
            }
        }

        String payStartTime = conditions.get("payStartTime").toString();
        String payEndTime = conditions.get("payEndTime").toString();
        String assertStartTime = conditions.get("assertStartTime").toString();
        String assertEndTime = conditions.get("assertEndTime").toString();
        float startMoney = Float.parseFloat(conditions.get("startMoney").toString());
        float endMoney = Float.parseFloat(conditions.get("endMoney").toString());


        if (!"空".equals(payStartTime)) {
            SQL_CONDITION.append(" AND od.payTime >=?");
            paramList.add(payStartTime);
        }
        if (!"空".equals(payEndTime)) {
            SQL_CONDITION.append(" AND od.payTime <=?");
            paramList.add(payEndTime);
        }
        if (!"空".equals(assertStartTime)) {
            SQL_CONDITION.append(" AND od.assertTime >=?");
            paramList.add(assertStartTime);
        }
        if (!"空".equals(assertEndTime)) {
            SQL_CONDITION.append(" AND od.assertTime  <=?");
            paramList.add(assertEndTime);
        }
        if (startMoney == endMoney && startMoney > 0) {
            SQL_CONDITION.append(" AND od.amount =?");
            paramList.add(packMoney(String.valueOf(startMoney)));
        } else {
            if (startMoney > 0) {
                SQL_CONDITION.append(" AND od.amount >=?");
                paramList.add(packMoney(String.valueOf(startMoney)));
            }
            if (endMoney > 0 && endMoney > startMoney) {
                SQL_CONDITION.append(" AND od.amount <=?");
                paramList.add(packMoney(String.valueOf(endMoney)));
            }
        }
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("orderCondition", SQL_CONDITION.toString());
        maps.put("paramList", paramList);
        return maps;
    }

    private static void buildOrderViewCondition(StringBuilder SQL_CONDITION, List<Object> paramList, OrderViewObject orderViewObject) {
        //组合订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getOrderId())) {
            SQL_CONDITION.append(" and od.id=? ");
            paramList.add(orderViewObject.getOrderId());
        }
        //组合网关订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getEndOrder())) {
            SQL_CONDITION.append(" and od.endOrder=? ");
            orderViewObject.setEndOrder("endOrder" + orderViewObject.getEndOrder());
            paramList.add(orderViewObject.getEndOrder());
        }

        //组合用户ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getUserId())) && orderViewObject.getUserId() > 0) {
            SQL_CONDITION.append(" and od.userId=? ");
            paramList.add(orderViewObject.getUserId());
        }
        //组合用户名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getUserName())) {
            SQL_CONDITION.append(" and u.userName=? ");
            paramList.add(orderViewObject.getUserName());
        }
        //组合玩家ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getPlayerId())) && orderViewObject.getPlayerId() > 0) {
            SQL_CONDITION.append(" and  od.playerId=? ");
            paramList.add(orderViewObject.getPlayerId());
        }
        // 组合玩家名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getPlayerName())) {
            SQL_CONDITION.append(" and u.userName=? ");
            paramList.add(orderViewObject.getPlayerName());
        }

        // 组合游戏名查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getGameId())) && orderViewObject.getGameId() > 0) {
            SQL_CONDITION.append(" and od.gameId=? ");
            paramList.add(orderViewObject.getGameId());
        }
        // 组合服务器名称查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getZoneId())) && orderViewObject.getZoneId() > 0) {
            SQL_CONDITION.append(" and od.zoneId=? ");
            paramList.add(orderViewObject.getZoneId());
        }
        // 组合支付网关名称查询
        if (!Strings.isNullOrEmpty(orderViewObject.getChannelName())) {
            SQL_CONDITION.append(" and od.channelId=? ");
            paramList.add(orderViewObject.getChannelName());
        }

        // 组合支付方式查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeName())) {
            SQL_CONDITION.append(" and od.subType=? ");
            paramList.add(orderViewObject.getSubTypeName());
        }

        // 组合支付渠道查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeTagName())) {
            SQL_CONDITION.append(" and od.subTag=? ");
            paramList.add(orderViewObject.getSubTypeTagName());
        }

        // 组合订单状态查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getStatus()))) {
            if ("success".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status=? ");
                paramList.add(1);
            } else if ("failed".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status !=? ");
                paramList.add(1);
            }
        }
    }

    private static void buildOrderViewConditionReWrite(StringBuilder SQL_CONDITION, List<Object> paramList, Map<String, Object> condition) {
        OrderViewObject orderViewObject = (OrderViewObject) condition.get("");
        //组合订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getOrderId())) {
            SQL_CONDITION.append(" and od.id=? ");
            paramList.add(orderViewObject.getOrderId());
        }
        //组合网关订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getEndOrder())) {
            SQL_CONDITION.append(" and od.endOrder=? ");
            orderViewObject.setEndOrder("endOrder" + orderViewObject.getEndOrder());
            paramList.add(orderViewObject.getEndOrder());
        }

        //组合用户ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getUserId())) && orderViewObject.getUserId() > 0) {
            SQL_CONDITION.append(" and od.userId=? ");
            paramList.add(orderViewObject.getUserId());
        }
        //组合用户名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getUserName())) {
            SQL_CONDITION.append(" and u.userName=? ");
            paramList.add(orderViewObject.getUserName());
        }
        //组合玩家ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getPlayerId())) && orderViewObject.getPlayerId() > 0) {
            SQL_CONDITION.append(" and  od.playerId=? ");
            paramList.add(orderViewObject.getPlayerId());
        }
        // 组合玩家名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getPlayerName())) {
            SQL_CONDITION.append(" and u.userName=? ");
            paramList.add(orderViewObject.getPlayerName());
        }

        // 组合游戏名查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getGameId())) && orderViewObject.getGameId() > 0) {
            SQL_CONDITION.append(" and od.gameId=? ");
            paramList.add(orderViewObject.getGameId());
        }
        // 组合服务器名称查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getZoneId())) && orderViewObject.getZoneId() > 0) {
            SQL_CONDITION.append(" and od.zoneId=? ");
            paramList.add(orderViewObject.getZoneId());
        }
        // 组合支付网关名称查询
        if (!Strings.isNullOrEmpty(orderViewObject.getChannelName())) {
            SQL_CONDITION.append(" and od.channelId=? ");
            paramList.add(orderViewObject.getChannelName());
        }

        // 组合支付方式查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeName())) {
            SQL_CONDITION.append(" and od.subType=? ");
            paramList.add(orderViewObject.getSubTypeName());
        }

        // 组合支付渠道查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeTagName())) {
            SQL_CONDITION.append(" and od.subTag=? ");
            paramList.add(orderViewObject.getSubTypeTagName());
        }

        // 组合订单状态查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getStatus()))) {
            if ("success".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status=? ");
                paramList.add(1);
            } else if ("failed".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status !=? ");
                paramList.add(1);
            }
        }
    }


    //包装开始时间 从0时0分0秒开始
    private static String packStartDate(String date) {
        return date + " 00:00:00";
    }

    //包装结束时间 从23时59分59秒结束
    private static String packEndDate(String date) {
        if (!Strings.isNullOrEmpty(date)) return date + " 23:59:59";
        else return null;
    }

    /**
     * 非空且是true
     *
     * @param value
     * @return
     */
    private static boolean isNotNUllAndTrue(String value) {
        if (!Strings.isNullOrEmpty(value) && "checked".equals(value)) return true;
        else return false;
    }

    //包装钱，转换为浮点数字
    private static Float packMoney(String money) {
        if (VerifyFormItem.isFloat(money)) {
            return Float.parseFloat(money);
        }
        if (VerifyFormItem.isInteger(money)) {
            money += ".0";
            return Float.parseFloat(money);
        }
        return null;
    }

    public static Map<String, Object> getExportCustomizeCondition(HttpServletRequest request) {

        //1，声明变量
        String $checkPayStartTime = null;
        String $checkAssertStartTime = null;
        String $checkGameName = null;
        String $checkServerName = null;
        String $checkChannelId = null;
        String $checkSubType = null;
        String $checkBank = null;
        String $checkStatus = null;
        String $checkGold = null;
        String $checkUserAmount = null;
        String $checkPlayerAmount = null;
        String $checkStatusAmount = null;
        //真实数据
        String $payStartTime = null;
        String $payEndTime = null;
        String $assertStartTime = null;
        String $assertEndTime = null;
        String $gameName = null;
        String $serverName = null;
        String $channelId = null;
        String $subType = null;
        String $bank = null;
        String $status = null;
//        String $amount = null;
//        String $gold = null;
//        String $userAmount = null;
//        String $playerAmount = null;
//        String $statusAmount = null;
        HashMap<String, Object> conditons = new HashMap<String, Object>();

        try {
            //2,参数赋值
            $checkPayStartTime = request.getParameter("checkPayStartTime");
            $checkAssertStartTime = request.getParameter("checkAssertStartTime");
            $checkGameName = request.getParameter("checkGameName");
            $checkServerName = request.getParameter("checkServerName");
            $checkChannelId = request.getParameter("checkChannelId");
            $checkSubType = request.getParameter("checkSubType");
            $checkBank = request.getParameter("checkBank");
            $checkStatus = request.getParameter("checkStatus");
            $checkGold = request.getParameter("checkGold");
            $checkUserAmount = request.getParameter("checkUserAmount");
            $checkPlayerAmount = request.getParameter("checkPlayerAmount");
            $checkStatusAmount = request.getParameter("checkStatusAmount");
            //真实数据
            $payStartTime = request.getParameter("payStartTime");
            $payEndTime = request.getParameter("payEndTime");
            $assertStartTime = request.getParameter("assertStartTime");
            $assertEndTime = request.getParameter("assertEndTime");
            $gameName = request.getParameter("gameName");
            $serverName = request.getParameter("serverName");
            $channelId = request.getParameter("channelId");
            $subType = request.getParameter("subType");
            $bank = request.getParameter("bank");
            $status = request.getParameter("status");
//              $amount = request.getParameter("");
//              $gold = request.getParameter("");
//              $userAmount = request.getParameter("");
//              $playerAmount = request.getParameter("");
//              $statusAmount = request.getParameter("");
        } catch (Exception e) {
            log.error("接收订单数据统计的参数异常！ " + e.getMessage());
        } finally {
            if (isNotNUllAndTrue($checkPayStartTime)) {
                conditons.put("$payStartTime", $payStartTime);
                conditons.put("$payEndTime", $payEndTime);
            }
            if (isNotNUllAndTrue($checkAssertStartTime)) {
                conditons.put("$assertStartTime", $assertStartTime);
                conditons.put("$assertEndTime", $assertEndTime);
            }
            try {
                if (isNotNUllAndTrue($checkGameName)) {
                    conditons.put("gameName", java.net.URLDecoder.decode($gameName, "UTF-8"));
                }
                if (isNotNUllAndTrue($checkServerName)) {
                    conditons.put("serverName", java.net.URLDecoder.decode($serverName, "UTF-8"));
                }
                if (isNotNUllAndTrue($checkChannelId)) {
                    conditons.put("channelId", java.net.URLDecoder.decode($channelId, "UTF-8"));
                }
                if (isNotNUllAndTrue($checkSubType)) {
                    conditons.put("subType", java.net.URLDecoder.decode($subType, "UTF-8"));
                }
                if (isNotNUllAndTrue($checkBank)) {
                    conditons.put("bank", java.net.URLDecoder.decode($bank, "UTF-8"));
                }
                if (isNotNUllAndTrue($checkStatus)) {
                    conditons.put("status", java.net.URLDecoder.decode($status, "UTF-8"));
                }
            } catch (Exception e) {
                log.error("转换中文编码出错！");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            if (isNotNUllAndTrue($checkGold)) {
                conditons.put("gold", "true");
            }
            if (isNotNUllAndTrue($checkUserAmount)) {
                conditons.put("userAmount", "true");
            }
            if (isNotNUllAndTrue($checkPlayerAmount)) {
                conditons.put("playerAmount", "true");
            }
            if (isNotNUllAndTrue($checkStatusAmount)) {
                conditons.put("statusAmount", "true");
            }
        }
        return conditons;
    }


    public static Map<String, Object> getCustomizeCondition(HttpServletRequest request) {

        //1，声明变量
        String $checkPayStartTime = null;
        String $checkAssertStartTime = null;
        String $checkGameName = null;
        String $checkServerName = null;
        String $checkChannelId = null;
        String $checkSubType = null;
        String $checkBank = null;
        String $checkStatus = null;
        String $checkGold = null;
        String $checkUserAmount = null;
        String $checkPlayerAmount = null;
        String $checkStatusAmount = null;
        //真实数据
        String $payStartTime = null;
        String $payEndTime = null;
        String $assertStartTime = null;
        String $assertEndTime = null;
        String $gameName = null;
        String $serverName = null;
        String $channelId = null;
        String $subType = null;
        String $bank = null;
        String $status = null;
        String $page = null;  //第几页
        String $pageCount = null;//每页多少项
//        String $userAmount = null;
//        String $playerAmount = null;
//        String $statusAmount = null;
        HashMap<String, Object> conditons = new HashMap<String, Object>();

        try {
            //2,参数赋值
            $checkPayStartTime = request.getParameter("$checkPayStartTime");
            $checkAssertStartTime = request.getParameter("$checkAssertStartTime");
            $checkGameName = request.getParameter("$checkGameName");
            $checkServerName = request.getParameter("$checkServerName");
            $checkChannelId = request.getParameter("$checkChannelId");
            $checkSubType = request.getParameter("$checkSubType");
            $checkBank = request.getParameter("$checkBank");
            $checkStatus = request.getParameter("$checkStatus");
            $checkGold = request.getParameter("$checkGold");
            $checkUserAmount = request.getParameter("$checkUserAmount");
            $checkPlayerAmount = request.getParameter("$checkPlayerAmount");
            $checkStatusAmount = request.getParameter("$checkStatusAmount");
            //真实数据
            $payStartTime = request.getParameter("$payStartTime");
            $payEndTime = request.getParameter("$payEndTime");
            $assertStartTime = request.getParameter("$assertStartTime");
            $assertEndTime = request.getParameter("$assertEndTime");
            $gameName = request.getParameter("$gameName");
            $serverName = request.getParameter("$serverName");
            $channelId = request.getParameter("$channelId");
            $subType = request.getParameter("$subType");
            $bank = request.getParameter("$bank");
            $status = request.getParameter("$status");
            $page = request.getParameter("$page");
            $pageCount = request.getParameter("$pageCount");
//              $userAmount = request.getParameter("");
//              $playerAmount = request.getParameter("");
//              $statusAmount = request.getParameter("");
        } catch (Exception e) {
            log.error("接收订单数据统计的参数异常！ " + e.getMessage());
        } finally {
            if (isNotNUllAndTrue($checkPayStartTime)) {
                conditons.put("$payStartTime", $payStartTime);
                conditons.put("$payEndTime", $payEndTime);
            }
            if (isNotNUllAndTrue($checkAssertStartTime)) {
                conditons.put("$assertStartTime", $assertStartTime);
                conditons.put("$assertEndTime", $assertEndTime);
            }
            if (isNotNUllAndTrue($checkGameName)) {
                conditons.put("gameName", $gameName);
            }
            if (isNotNUllAndTrue($checkServerName)) {
                conditons.put("serverName", $serverName);
            }
            if (isNotNUllAndTrue($checkChannelId)) {
                conditons.put("channelId", $channelId);
            }
            if (isNotNUllAndTrue($checkSubType)) {
                conditons.put("subType", $subType);
            }
            if (isNotNUllAndTrue($checkBank)) {
                conditons.put("bank", $bank);
            }
            if (isNotNUllAndTrue($checkStatus)) {
                conditons.put("status", $status);
            }
            if (isNotNUllAndTrue($checkGold)) {
                conditons.put("gold", "true");
            }
            if (isNotNUllAndTrue($checkUserAmount)) {
                conditons.put("userAmount", "true");
            }
            if (isNotNUllAndTrue($checkPlayerAmount)) {
                conditons.put("playerAmount", "true");
            }
            if (isNotNUllAndTrue($checkStatusAmount)) {
                conditons.put("statusAmount", "true");
            }
            if ($page != null && !Strings.isNullOrEmpty($page)) {
                conditons.put("page", $page);
            }
            if ($pageCount != null && !Strings.isNullOrEmpty($pageCount)) {
                conditons.put("pageCount", $pageCount);
            }
        }
        return conditons;
    }

    /**
     * true 为空，false为非空
     *
     * @param str
     * @return
     */
    public static boolean isStringNull(String str) {
        boolean flag = false;
        if (Strings.isNullOrEmpty(str)) flag = true;
        return flag;
    }

    /**
     * true 为非空数字字符串  false
     *
     * @param str
     * @return
     */
    public static boolean isIntegerAndNotNull(String str) {
        boolean flag = false;
        if (isStringNull(str) == false) if (VerifyFormItem.isInteger(str)) {
            flag = true;
        }
        return flag;
    }

    //把对象的信息打印到网页，用于测试
    public static void printToHtml(HttpServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            try {
                out = response.getWriter();
                response.setContentType("application/json");
                mapper.writeValue(out, obj);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 根据查询条件得到查询数量的sql和分页的sql
     * @param conditions
     * @return
     */
    public static Map<String, Object> getSQLAndParamListForGrid(Map<String, Object> conditions) {
        if(conditions==null||conditions.isEmpty())
        {
            return null;
        }
        List<Object> paramList = Lists.newArrayList();
        StringBuilder SQL_CONDITION = new StringBuilder(" WHERE 1 ");
        OrderViewObject orderViewObject = (OrderViewObject) conditions.get("orderViewObject");

        //组合订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getOrderId())) {
            SQL_CONDITION.append(" and od.id=? ");
            paramList.add(orderViewObject.getOrderId());
        }
        //组合网关订单ID查询条件
        if (!Strings.isNullOrEmpty(orderViewObject.getEndOrder())) {
            SQL_CONDITION.append(" and od.endOrder=? ");
            orderViewObject.setEndOrder("endOrder" + orderViewObject.getEndOrder());
            paramList.add(orderViewObject.getEndOrder());
        }
        //组合用户ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getUserId())) && orderViewObject.getUserId() > 0) {
            SQL_CONDITION.append(" and od.userId=? ");
            paramList.add(orderViewObject.getUserId());
        }
        //组合用户名查询
        if (!Strings.isNullOrEmpty(orderViewObject.getUserName())) {
            SQL_CONDITION.append(" and od.userId=? ");
            paramList.add(orderViewObject.getUserName());
        }
        //组合玩家ID查询条件
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getPlayerId())) && orderViewObject.getPlayerId() > 0) {
            SQL_CONDITION.append(" and od.playerId=? ");
            paramList.add(orderViewObject.getPlayerId());
        }
        // 组合游戏名查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getGameId())) && orderViewObject.getGameId() > 0) {
            SQL_CONDITION.append(" and od.gameId=? ");
            paramList.add(orderViewObject.getGameId());
        }
        // 组合服务器名称查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getZoneId())) && orderViewObject.getZoneId() > 0) {
            SQL_CONDITION.append(" and od.zoneId=? ");
            paramList.add(orderViewObject.getZoneId());
        }
        // 组合支付网关名称查询
        if (!Strings.isNullOrEmpty(orderViewObject.getChannelName())) {
            SQL_CONDITION.append(" and od.channelId=? ");
            paramList.add(orderViewObject.getChannelName());
        }

        // 组合支付方式查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeName())) {
            SQL_CONDITION.append(" and od.subType=? ");
            paramList.add(orderViewObject.getSubTypeName());
        }

        // 组合支付渠道查询
        if (!Strings.isNullOrEmpty(orderViewObject.getSubTypeTagName())) {
            SQL_CONDITION.append(" and od.subTag=? ");
            paramList.add(orderViewObject.getSubTypeTagName());
        }

        // 组合订单状态查询
        if (!Strings.isNullOrEmpty(String.valueOf(orderViewObject.getStatus()))) {
            if ("success".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status=? ");
                paramList.add(1);
            } else if ("failed".equals(orderViewObject.getStatus())) {
                SQL_CONDITION.append(" and od.status !=? ");
                paramList.add(1);
            }
        }

        String payStartTime = conditions.get("payStartTime").toString();
        String payEndTime = conditions.get("payEndTime").toString();
        String assertStartTime = conditions.get("assertStartTime").toString();
        String assertEndTime = conditions.get("assertEndTime").toString();
        float startMoney = Float.parseFloat(conditions.get("startMoney").toString());
        float endMoney = Float.parseFloat(conditions.get("endMoney").toString());


        if (!"空".equals(payStartTime)) {
            SQL_CONDITION.append(" and od.payTime >=?");
            paramList.add(payStartTime);
        }
        if (!"空".equals(payEndTime)) {
            SQL_CONDITION.append(" and od.payTime <=?");
            paramList.add(payEndTime);
        }
        if (!"空".equals(assertStartTime)) {
            SQL_CONDITION.append(" and od.assertTime >=?");
            paramList.add(assertStartTime);
        }
        if (!"空".equals(assertEndTime)) {
            SQL_CONDITION.append(" and od.assertTime  <=?");
            paramList.add(assertEndTime);
        }
        if (startMoney == endMoney && startMoney > 0) {
            SQL_CONDITION.append(" and od.amount =?");
            paramList.add(packMoney(String.valueOf(startMoney)));
        } else {
            if (startMoney > 0) {
                SQL_CONDITION.append(" and od.amount >=?");
                paramList.add(packMoney(String.valueOf(startMoney)));
            }
            if (endMoney > 0 && endMoney > startMoney) {
                SQL_CONDITION.append(" and od.amount <=?");
                paramList.add(packMoney(String.valueOf(endMoney)));
            }
        }

        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("orderCondition", SQL_CONDITION.toString());
        maps.put("paramList", paramList);
        return maps;
    }

    //去掉主查询条件
    public static Map<String,String> getOrderSortMap(String firstOrder)
    {
      Map<String,String> sortMap=new HashMap<String, String>();
      sortMap.put("od.payTime","desc");
        sortMap.put("od.assertTime","desc");
        sortMap.put("od.gameId","asc");
        sortMap.put("od.zoneId","asc");
        sortMap.put("od.channelId","asc");
        sortMap.put("od.subType","asc");
        sortMap.put("od.subTag","asc");
        sortMap.put("od.amount","desc");

        if(!Strings.isNullOrEmpty(firstOrder)&&sortMap.containsKey(firstOrder))
        {
             sortMap.remove(firstOrder);
        }
        return sortMap;
    }
}
