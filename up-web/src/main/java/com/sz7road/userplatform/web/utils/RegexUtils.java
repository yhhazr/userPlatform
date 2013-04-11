package com.sz7road.userplatform.web.utils;

import com.sz7road.userplatform.pojos.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class RegexUtils {

    public static String USER_NAME = "\\w{6,20}";
    public static String USER_PASS = ".{6,20}";

    public static boolean matches(String regex, String input){
        if (regex == null || input == null)
            return false;
        return Pattern.compile(regex).matcher(input).matches();
    }

    public static boolean isLegalUserName(String input){
        return matches(USER_NAME, input);
    }

    public static boolean isLegalUserPass(String input){
        return matches(USER_PASS, input);
    }

    public static void main(String[] args) {
        System.out.println(matches(USER_NAME, null));
        System.out.println(matches(USER_NAME, ""));
        System.out.println(matches(USER_NAME, "01234"));
        System.out.println(matches(USER_NAME, "012345"));
        System.out.println(matches(USER_NAME, "012345678901234567Zz"));
        System.out.println(matches(USER_NAME, "012345678901234567891"));

        System.out.println("---------");
        System.out.println(matches(USER_PASS, ""));
        System.out.println(matches(USER_PASS, "12345@#$"));
        System.out.println(matches(USER_PASS, "0123456789012345"));
        System.out.println(matches(USER_PASS, "01234567890123451"));
    }
}
