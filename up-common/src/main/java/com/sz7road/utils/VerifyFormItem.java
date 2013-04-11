package com.sz7road.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-14
 * Time: 上午9:35
 * 服务器端验证常用的表单的输入
 */
public class VerifyFormItem {
    private static Logger logger = LoggerFactory.getLogger(VerifyFormItem.class);

    //1,验证长度
    public static boolean checkLength(String item, int min, int max) {
        int itemCharsLength = item.trim().toCharArray().length;

        if (itemCharsLength >= min && itemCharsLength <= max) return true;
        else return false;
    }

    //2,验证是不是浮点数字
    public static boolean isFloat(String item) {
        try {
            Float getDigital = Float.parseFloat(item);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isInteger(String item) {
        try {
            Integer getDigital = Integer.parseInt(item);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //3,验证日期
    public static boolean isDate(String item, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            simpleDateFormat.parse(item);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    //4,非空验证
    public static boolean checkEmpty(String value) {
        if (value == null || value.length() == 0) return true;
        else return false;
    }

    //5,验证匹配
    public static boolean checkMatch(String value1, String value2) {
        if (value1 == null) return false;
        else return value1.equals(value2);
    }

    //6,验证数字的范围
    public static boolean checkNumberScope(String value, float min, float max) {
        if (checkEmpty(value)) {
            return false;
        } else {
            try {
                float fvalue = Float.parseFloat(value);
                if (fvalue >= min && fvalue <= max) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    //7,验证是不是邮箱
    public static boolean checkPostCoder(String value) {
        if (!checkLength(value, 6, 6)) {
            return false;
        }
        if (checkNumber(value)) {
            return true;
        } else {
            return false;
        }
    }

    //8, 验证是不是邮编
    public static boolean checkEmail(String value) {
        if (!checkLength(value, 5, 200)) {
            return false;
        }
        int temp1 = value.indexOf('@');
        int temp2 = value.indexOf('.');
        if (temp1 < 0 || temp1 > temp2) {
            return false;
        } else {
            return true;
        }

    }

    //9,验证是不是最晚日期
    public static boolean checkDateAfter(String value, String pattern, Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date valueDate = simpleDateFormat.parse(value);
            if (date.before(valueDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    //10,验证是不是最早日期
    public static boolean checkDateBefore(String value, String pattern, Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date valueDate = simpleDateFormat.parse(value);
            if (date.after(valueDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    //11,验证是不是日期

    public static boolean checkDate(String value, String pattern) {
        if (checkEmpty(value)) {
            return false;
        } else {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date date = simpleDateFormat.parse(value);
                return value.equals(simpleDateFormat.format(date));

            } catch (Exception ex) {
                return false;
            }
        }
    }

    //12,验证是不是一个数字序列
    public static boolean checkNumber(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) >= '0' && value.charAt(i) <= '9') {
                continue;
            } else return false;
        }
        return true;
    }

    //13,验证一个字符串是不是IP地址
    public static boolean checkIPAddress(String value) {
        try {
            String rex = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
            return value.matches(rex);
        } catch (Exception ex) {
            return false;
        }
    }


    public static void main(String[] args)
    {
        String ip="45 .78.96.63";
        if(checkIPAddress(ip))
        {
            System.out.println(ip+"是ip");
        }
        else
        {
            System.out.println(ip+"不是ip");
        }
    }

}
