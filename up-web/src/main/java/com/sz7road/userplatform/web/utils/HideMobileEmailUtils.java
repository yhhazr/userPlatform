package com.sz7road.userplatform.web.utils;

/**
 * @author jiangfan.zhou
 */

public class HideMobileEmailUtils {

    public static String getWildEmail(String email){
        int index = email.indexOf("@");
        if (index == -1)
            return email;

        if( index > 2) {
            email = email.substring(0, 3) + "*****" + email.substring(index);
        } else {
            email = email.substring(0, 1) + "*****" + email.substring(index);
        }
        return email;
    }

    public static String getWildMobile(String mobile){
        if( mobile != null) {
            if (mobile.length() > 10) {
                mobile = mobile.substring(0, 3) + "******" + mobile.substring(9, 11);
            }
        }
        return mobile ;
    }
}
