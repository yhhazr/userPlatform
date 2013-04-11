/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.utils;

import java.util.regex.Pattern;

/**
 * @author leo.liao
 */


public class PasswordUtils {

    public static byte checkPasswordLevel(String str) {
        byte num = 1;
        if (Pattern.compile("\\d{6,}").matcher(str).find()) {
            num = 1;
        }
        if (Pattern.compile("[0-9a-zA-Z]{6,}").matcher(str).find()) {
            num = 2;
        }
        if (Pattern.compile("[-.!@#$%^&*()+?><]").matcher(str).find()) {
            num = 3;
        }
        return num;
    }

    public static String safeLevel(byte safeLevel) {
        if (safeLevel == 1) {
            return "低";
        } else if (safeLevel == 2) {
            return "中";
        } else if (safeLevel == 3) {
            return "较强";
        } else if (safeLevel == 4) {
            return "强";
        }
        return "低";
    }

}
