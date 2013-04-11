/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author jeremy
 */
public class Headend {

    public static void redirectUrl(HttpServletResponse response, String spec) throws IOException {
        response.sendRedirect(spec);
    }

    public static void redirectUrl(HttpServletResponse response, URL url) throws IOException {
        redirectUrl(response, url.toString());
    }

    public static void redirectForm(HttpServletResponse response, String action, String method, Map<String, Object> parameters) {
        new AutoForm(action, method, parameters, false).submit(response);
    }

    public static void redirectForm(HttpServletResponse response, String action, String method, Map<String, Object> parameters, boolean filterNull) {
        new AutoForm(action, method, parameters, filterNull).submit(response);
    }

    public static void redirectFormForTenPay(HttpServletResponse response, String action, String method, Map<String, Object> parameters, boolean filterNull) {
        new AutoForm(action, method, parameters, filterNull).submitForTenPay(response);
    }

    public static void redirectForm(HttpServletResponse response, URL action, String method, Map<String, Object> parameters) {
        redirectForm(response, action.toString(), method, parameters);
    }
}
