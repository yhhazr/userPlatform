package com.sz7road.userplatform.utils;

import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.utils.VerifyFormItem;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
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
     * 得到影藏的邮箱地址
     *
     * @param email
     * @return
     */
    public static String getHandledEmail(String email) {
        int index = email.indexOf('@');
        String hideStr = email.substring(1, index - 1);
        return email.replace(hideStr, getNumberHideSymbol(index - 2));
    }

    public static String getHandledIP(String ip) {
        StringBuffer stringBuffer = new StringBuffer();
        char[] ips = ip.toCharArray();
        int count = 0;
        for (char ch : ips) {
            if (ch == '.') {
                count += 1;
                stringBuffer.append(ch);
            } else {

                if (count > 1) {
                    stringBuffer.append('*');
                } else {
                    stringBuffer.append(ch);
                }
            }
        }

        return stringBuffer.toString();

    }

    /**
     * 得到影藏的电话号码
     *
     * @param phone
     * @return
     */
    public static String getHandledPhoneNum(String phone) {
        String hideStr = phone.substring(phone.length() - 4, phone.length());
        return phone.replace(hideStr, getNumberHideSymbol(4));
    }


    /**
     * 得到影藏的电话号码
     *
     * @param icn
     * @return
     */
    public static String getHandledIcn(String icn) {
        String hideStr = icn.substring(3, icn.length()-3);
        return icn.replace(hideStr, getNumberHideSymbol(hideStr.length()));
    }

    private static String getNumberHideSymbol(int n) {
        String hideSymbol = "";
        for (int i = 1; i <= n; i++) {
            hideSymbol += "*";
        }
        return hideSymbol;
    }

    /**
     * 得到IP记录最多的IP地址
     *
     * @param logs
     * @return
     */
    public static String getMaxNumIPLogs(Map<String, List<Log>> logs) {
        String ip = null;
        int temp = 0;
        for (Map.Entry<String, List<Log>> entry : logs.entrySet()) {
            int count = entry.getValue().size();
            if (temp < count) {
                temp = count;
                ip = entry.getKey();
            }
        }
        return ip;
    }

    public  static ServerEntity transToServerEntity(ServerTable.ServerEntry entry)
    {
        ServerEntity serverEntity=new ServerEntity();
        serverEntity.setId(entry.getId());
        serverEntity.setCreateTime(entry.getCreateTime());
        serverEntity.setGameId(entry.getGameId());
        serverEntity.setOpeningTime(entry.getOpeningTime());
        serverEntity.setRecommand(entry.isRecommand());
        serverEntity.setServerName(entry.getServerName());
        serverEntity.setServerNo(entry.getServerNo());
        serverEntity.setServerStatus(entry.getServerStatus());
        return  serverEntity;
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
            log.info("返回数据："+rsp);
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

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }


    public static boolean isChinese(String strName) {
        boolean flag = false;
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c) == true) {
                flag = isChinese(c);
            } else {
                flag = false;
                break;
            }

        }
        return flag;

    }

    public static boolean isNotChinese(String strName) {
        boolean flag = false;
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c) == false) {
                flag = true;
                break;
            } else {
                flag = false;
            }

        }
        return flag;

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

//        System.out.println(getHandledEmail("cutter.li.hn@126.com"));
//        System.out.println(getHandledPhoneNum("18718480901"));
//        System.out.println(getHandledIP("10.10.4.98"));

        System.out.println(getHandledIcn("anyelangwang_2008@126.com"));

    }
}
