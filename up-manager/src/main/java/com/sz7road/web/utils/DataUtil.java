package com.sz7road.web.utils;

import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.VerifyFormItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-10
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */
public class DataUtil {

    private static final Logger log = LoggerFactory.getLogger(DataUtil.class);

    private static Map<String, String> allPermissions = null;

    //存放所有的权限信息
    public static Map<String, String> getAllPermissions() {

        if (allPermissions == null || allPermissions.isEmpty()) {
            allPermissions = null;
            allPermissions = new HashMap<String, String>();

            allPermissions.put("showOrderPage", "true");
            allPermissions.put("orderStatistical", "true");
            allPermissions.put("swapToUserAccountPage", "true");
            allPermissions.put("queryUserLog", "true");
            allPermissions.put("resetMailAndPsw", "true");
            allPermissions.put("keywordManage", "true");
            allPermissions.put("fullTextSearch", "true");
            allPermissions.put("faqManage", "true");
            allPermissions.put("toFaqKindPage", "true");
            allPermissions.put("csInfoManage", "true");
            allPermissions.put("mergerServer", "true");
            allPermissions.put("splitServer", "true");
            allPermissions.put("queryServer", "true");
            allPermissions.put("appealManage", "true");
            allPermissions.put("showOrderByGrid", "true");
        }
        return allPermissions;
    }

    public static String transOperateToSqlTag(String op) {
        String sqlTag = null;
        if (op.equals("eq")) sqlTag = "=";
        return sqlTag;
    }

    //拿到服务区特殊配置信息的map
    public static Map<String, String> getServerConfigurations(String serverNo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sq.chargeUrl", "http://assist$.shenquol.com/payment");
        map.put("sq.createLoginUrl", "http://s$.shenquol.com/createlogin");
        map.put("sq.game.playGameUrl", "http://s$.shenquol.com/client/game.jsp");
        map.put("sq.getRoleUrl", "http://s$.shenquol.com/loginselectlist");
        map.put("sq.loginUrl", "http://assist$.shenquol.com/login");
        map.put("sq.role.rankUrl", "http://s$.shenquol.com/xml/total_unzip.xml");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String newValue = entry.getValue().replace("$", serverNo);
            entry.setValue(newValue);
        }

        return map;
    }

    public static int getServerNoFromValueStr(String valueStr) {
        String serverStr = valueStr.substring("http://s".length(), valueStr.length() - ".shenquol.com/loginselectlist".length());
        int serverNo = 0;
        if (VerifyFormItem.isInteger(serverStr)) serverNo = Integer.parseInt(serverStr);
        return serverNo;
    }

    /**
     * 拿到权限信息并转化为map
     *
     * @return
     */
    public static Map<String, String> getPermissions(Map<String, Map<String, Object>> masterDataObject) {

        Map<String, String> permissions = new HashMap<String, String>();
        if (masterDataObject != null && masterDataObject.get("data") != null) {
            List<Map<String, Object>> configs = (List<Map<String, Object>>) masterDataObject.get("data").get("configs");
            for (Map<String, Object> configObject : configs) {
                permissions.put(configObject.get("config_item_name").toString(), configObject.get("config_item_value").toString());
            }
        }
        return permissions;
    }

    //得到日志类型名称
    public static String getLogTypeStr(int logType) {
        String logStr = "";
        switch (logType) {
            case 1:
                logStr = "账户信息更新";
                break;
            case 2:
                logStr = "邮箱绑定";
                break;
            case 3:
                logStr = "邮箱激活";
                break;
            case 4:
                logStr = "密保问题答案设置";
                break;
            case 5:
                logStr = "手机绑定";
                break;
            case 6:
                logStr = "登录IP异常";
                break;
        }
        return logStr;
    }

    /**
     * 判断是不是合法的服务器状态
     * @param st
     * @return
     */
    public static boolean  isLegalStatus(String st)
    {
        Map<String,Integer> status= new HashMap<String, Integer>();
        status.put("-1",ServerEntity.SERVER_STATUS.SERVER_STATUS_CLOSED.getServerStatusCode()); //关闭
        status.put("-2", ServerEntity.SERVER_STATUS.SERVER_STATUS_MAINTAIN.getServerStatusCode()); //维护
        status.put("-3",ServerEntity.SERVER_STATUS.SERVER_STATUS_NOTOPEN.getServerStatusCode()); //即将开服
        status.put("1",ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode());   //火爆

        return status.containsKey(st);

    }

    public static void main(String[] args) {
//        Map<String,String> map=getServerConfigurations("41");
//        for(Map.Entry<String,String> entry:map.entrySet())
//        {
//            log.info(" key: "+entry.getKey()+" value: "+entry.getValue());
//        }

//        String str="http://s21.shenquol.com/loginselectlist";
//
//       int serverNO= getServerNoFromValueStr(str);
//
//        log.info(str+" 的区服号是："+serverNO);

        System.out.println( CommonDateUtils.getTimeStamp("2013-02-19 11:48:00"));

        System.out.println( CommonDateUtils.getTimeStamp("2013-02-19 11:50:00"));

    }
}
