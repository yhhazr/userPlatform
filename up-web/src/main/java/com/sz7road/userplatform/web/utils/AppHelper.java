package com.sz7road.userplatform.web.utils;

import com.google.common.base.Strings;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.utils.Base64;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DesUtils;
import com.sz7road.utils.MD5Utils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;

/**
 * @author leo.liao
 */


public class AppHelper {

    private static final Logger log = LoggerFactory.getLogger(AppHelper.class.getName());

    public static boolean isOpen(Timestamp openingTime) {
        if (openingTime == null) {
            return false;
        }
        long currMillis = System.currentTimeMillis();
        Timestamp now_timestamp = new Timestamp(currMillis);
        boolean isOpen = false;
        if (openingTime != null && now_timestamp.after(openingTime)) {
            isOpen = true;
        }
        return isOpen;
    }

    public static String getUserName(HttpServletRequest request) {
        String _u = request.getParameter("_u");
        String userName = "";
        String value = getCookieValueByKey(request);
        if (!Strings.isNullOrEmpty(value)) {
            String val[] = value.split(",");
            userName = val[1];
        } else if (!Strings.isNullOrEmpty(_u)) {
            try {
                userName = DesUtils.decrypt(_u);
            } catch (Exception ex) {
                log.error("解析参数错误");
            }
        }
        return userName;
    }

    public static String getCookieValueByKey(HttpServletRequest request) {
        String value = "";
        Cookie[] cookies = request.getCookies();
        String name = "USERINFO";
        try {
            name = Base64.encode(name);
            name = URLEncoder.encode(name, "utf-8");
        } catch (Exception e) {
            log.error("USERINFO解码出错!{}", e.getMessage());
        }
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String nameStr = cookie.getName();
                if (nameStr.equals(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        if (!Strings.isNullOrEmpty(value)) {
            try {
                value = URLDecoder.decode(value, "UTF-8");
                value = Base64.decode2Str(value);
            } catch (Exception e) {
                log.error("URL解码出错!{}", e.getMessage());
            }
            String val[] = value.split(",");
            String sign = buildUserInfoSign(val[0], val[1], val[2]);
            if (!Strings.isNullOrEmpty(sign) && !Strings.isNullOrEmpty(val[3]) && sign.equals(val[3])) {
                return value;
            } else {
                return "";
            }
        }
        return "";
    }

    public static int getUserId(HttpServletRequest request) {
        int userId = 0;
        String value = getCookieValueByKey(request);
        if (!Strings.isNullOrEmpty(value)) {
            String val[] = value.split(",");
            userId = Integer.parseInt(val[0]);
        }
        return userId;
    }

    public static String RandomCode(int len) {
        java.util.Random rand = new Random(); // 设置随机种子
        // 设置备选验证码:包括"a-zA-Z"和数字"0-9"
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int size = base.length();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int start = rand.nextInt(size);
            String tmpStr = base.substring(start, start + 1);
            str.append(tmpStr);
        }
        return str.toString();
    }

    //构造cookie加密部分
    public static String buildUserInfoSign(String id, String name, String time) {
        String userInfoSrc = id + name + time;
        String key = ConfigurationUtils.get("login.cookieKey");
        String userInfo = userInfoSrc + key;
        String md5UserInfo = MD5Utils.digestAsHex(userInfo);
        return md5UserInfo;
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
     * 把传入的对象转换成json 然后写回去
     *
     * @param response
     * @param obj
     */
    public static void returnJsonAjaxForm(HttpServletResponse response, Object obj) {
        try {
            response.setContentType("text/html; charset=utf-8");
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

    public static void returnObj(HttpServletRequest request, HttpServletResponse response, Object obj) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        try {
//        validation(request, response, msg);
            ObjectMapper mapper = new ObjectMapper();
            String resp = mapper.writeValueAsString(obj);
            writer.write(resp);
        } finally {
            writer.flush();
            writer.close();
        }
    }

    public static String getPath(String path) {
        return path.replace("//", "/");
    }

    public static ServerTable.ServerEntry getNewlyServerEntry(ServerTable serverTable) {
        int serverNo = 0;
        ServerTable.ServerEntry serverEntry = null;
        if (serverTable != null) {
            Timestamp current=new Timestamp(System.currentTimeMillis());
            for (Map.Entry<Integer, ServerTable.ServerEntry> entry : serverTable.entrySet())
            {
                ServerTable.ServerEntry tmp = entry.getValue();
                if (tmp.getServerNo() > serverNo && tmp.getOpeningTime().before(current)) {
                    serverNo = tmp.getServerNo();
                    serverEntry = tmp;
                }
            }
        }
        return serverEntry;
    }


    public static void main(String[] args) {
//        String str = "146,7roadadmin,1339681140806," + "fc02cf7b-bc85-53bb-807b-a33b0544873c";
//        String cont = MD5Utils.digestAsHex(str);
//        System.out.println(cont);

        System.out.print(getPath("//head//userHead"));
    }

}
