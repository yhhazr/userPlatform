package util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class HttpUtil {

    public static String getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userInfo = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("USERINFO")) {
                userInfo = cookies[i].getValue();
            }
        }
        return userInfo;
    }
}
