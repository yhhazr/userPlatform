package com.sz7road.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-13
 * Time: 下午7:43
 * To change this template use File | Settings | File Templates.
 */
public class TimeStampUtil {

    private static final Logger log = LoggerFactory.getLogger(TimeStampUtil.class);

    //日期转换成时间戳
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * @return 获得当前时间的时间戳
     */
    public static Timestamp getNowTimeStamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 把时间戳转换成日期字符串
     *
     * @param timestamp
     * @return
     */
    public static String fromTimeStampToStringDate(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());


        return simpleDateFormat.format(date);
    }

    /**
     * 转换成年月的格式字符串
     *
     * @param timestamp
     * @return
     */
    public static String fromTimeStampToSimpleStringDate(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        return dateFormat.format(date);
    }

    public static String fromLongToStringDate(long timestamp) {
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    /**
     * 把时间戳转换成日期
     *
     * @param timestamp
     * @return
     */
    public static Date fromTimeStampToDate(Timestamp timestamp) {
        String StrDate = fromTimeStampToStringDate(timestamp);
        Date date = null;
        try {
            date = simpleDateFormat.parse(StrDate);
        } catch (Exception ex) {
            log.error("日期转换错误！");
            ex.printStackTrace();
        }
        return date;
    }

    /**
     * 把字符串转换才日期对应的长正数
     *
     * @param s 24小时制
     * @return
     */
    public static Timestamp transStringToLong(String s) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long reslut = 0L;
        try {
            Date date = sf.parse(s);
            reslut = date.getTime();
            System.out.println(reslut);

        } catch (ParseException ex) {
            log.error("转换失败！");
        }
        return new Timestamp(reslut);
    }


    public static void main(String[] args) {
        System.out.println(transStringToLong("2013-01-25 17:40:00"));
    }

}
