package com.sz7road.utils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: leo.liao
 * Date: 12-6-13
 * Time: 上午10:21
 */
public class CommonDateUtils {

    public static Date getDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static long getTimeStamp(String dateStr) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf2.parse(dateStr).getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 拿到今天凌晨的时间戳
     * @return
     */
    public static Timestamp getTodayStartTimeStamp() {
        return new Timestamp(getOneDayFirstMoment(new Date()));
    }


    public static Date string2Date(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (Exception ex) {
        }
        return null;
    }

    public static String date2String(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static long getOneDayLastMoment(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(date);
        now = now + " 23:59:59";

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf2.parse(now).getTime();
        } catch (ParseException e) {}
        return 0l;
    }

    public static long getOneDayFirstMoment(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(date);
        now = now + " 00:00:00";

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf2.parse(now).getTime();
        } catch (ParseException e) {}
        return 0l;
    }

    /**
     * yyyy-MM-dd HH:mm
     * @param time
     * @return
     */
    public static String getDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.format(new Date(time));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * yyyy-MM-dd HH:mm
     * @param time
     * @return
     */
    public static String getDateString(Timestamp time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(new Date(time.getTime()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 得到客服端访问的IP地址
     * @param request
     * @return
     */
    public static String getRemoteIPAddress(HttpServletRequest request)
    {
        //获得登录的IP　同神曲官网
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static void main(String[] args)
    {
//       System.out.println( getTimeStamp("2012-10-26 18:00:00"));
        System.out.println(getDate(1359518694497L));
        System.out.println(getDate(1359475200000L));

    }
}
