/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.utils;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.sz7road.utils.CommonDateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author leo.liao
 */

public class DateUtils {

    /**
     * yyyy-MM-dd
     *
     * @param Datestr
     * @return
     */
    public static Date getDate(String Datestr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(Datestr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String datetoStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String nowDatetoStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssssss");
        try {
            return sdf.format(new Date());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String nowDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.format(new Date());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String nowDatetoStrToMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        try {
            return sdf.format(new Date());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String datetoYearMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean is18YearOld(String icn) {
        boolean flag = false;
        final int icn_size = icn.length();
        final int birthday_year = Integer.parseInt(icn.substring(icn_size - 12, icn_size - 8));
        final int birthday_month = Integer.parseInt(icn.substring(icn_size - 8, icn_size - 6));
        final int birthday_day = Integer.parseInt(icn.substring(icn_size - 6, icn_size - 4));

        final String todayStr = nowDateToString();
        final  int today_size= todayStr.length();
        final int today_year = Integer.parseInt(todayStr.substring(0,4));
        final int today_month = Integer.parseInt(todayStr.substring(today_size-4,today_size-2));
        final int today_day = Integer.parseInt(todayStr.substring(today_size-2,today_size));
        if (today_year - birthday_year >= 19) {
            flag = true;
        } else if (today_year - birthday_year <= 17) {
            flag = false;
        } else {
           if(today_month-birthday_month>0)
           {
               flag=true;
           }else  if(today_month-birthday_month<0)
           {
              flag=false;
           } else
           {
               if(today_day-birthday_day>=0)
               {
                   flag=true;
               }
               else
               {
                   flag=false;
               }
           }
        }
        return flag;
    }

    public static void main(String[] args) {
//       System.out.println( getTimeStamp("2012-10-26 18:00:00"));
        String icn1="430482198801026554";
        System.out.println(icn1+"满18岁了吗？"+is18YearOld(icn1));
        String icn2="430482199502271234";
        System.out.println(icn2+"满18岁了吗？"+is18YearOld(icn2));

    }
}
