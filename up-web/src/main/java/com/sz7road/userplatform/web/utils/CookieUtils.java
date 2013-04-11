/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.utils;

import com.google.common.base.Strings;
import com.sz7road.utils.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author leo.liao
 */
public class CookieUtils {

    public static String encrypt(String str) {
        String name = "";
        if (!Strings.isNullOrEmpty(str)) {
            byte[] bytes = str.getBytes();
            try {
                name = URLEncoder.encode(new String(Base64.encode(bytes)), "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
            }
        }
        return name;
    }

    public static String decrypt(String str) {
        if (!Strings.isNullOrEmpty(str)) {
            try {
                String tmp = URLDecoder.decode(str, "UTF-8");
                byte[] bytes = Base64.decode(tmp);
                return new String(bytes);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static String getValueByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        String value = "";
        name = encrypt(name);
        for (Cookie cookie : cookies) {
            String nameStr = cookie.getName();
            if (nameStr.equals(name)) {
                value = cookie.getValue();
                break;
            }

        }
        if (!"".equals(value)) {
            value = decrypt(value);
        }
        return value;
    }
}
