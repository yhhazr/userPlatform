/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
class AutoForm {

    private String method = "POST";
    private String action;
    private String target;

    private Map<String, Object> parameters;
    private boolean filterNull = false;

    public AutoForm(String action, String method) {
        if (null == action) throw new NullPointerException("action");
        if (null == method) throw new NullPointerException("method");
        this.action = action;
        this.method = method;
    }

    public AutoForm(String action, String method, Map<String, Object> parameters, boolean filterNull) {
        this(action, method);
        this.parameters = parameters;
        this.filterNull = filterNull;
    }

    public String getMethod() {
        return method;
    }

    public String getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void submit(final HttpServletResponse response) {
        if (null != response) {
            try {
                response.setContentType("text/html");
                response.getWriter().write(toString());
            } catch (final IOException ingored) {
                ingored.printStackTrace();
            }
        }
    }


    public void submitForTenPay(final HttpServletResponse response) {
        if (null != response) {
            try {
                response.setContentType("text/html");
                response.getWriter().write(toStringForTenPay());
            } catch (final IOException ingored) {
                ingored.printStackTrace();
            }
        }
    }



    protected String asHiddenInput(String name, String value) {
        return String.format("<input type=\"hidden\" name=\"%s\" value=\"%s\" />", name, value);
    }

    public String toStringForTenPay() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("<form name=\"form\" action=\"%s\" method=\"%s\" target=\"%s\">", getAction(), getMethod(), Strings.nullToEmpty(getTarget())));
        final List<String> keys = Lists.newArrayList(parameters.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!Strings.isNullOrEmpty(key)&&!key.equals("key")) {
                Object value = parameters.get(key);
                if (filterNull && (value == null || Strings.isNullOrEmpty(value.toString()))) {
                    continue;
                }

                sb.append(asHiddenInput(key, value == null ? "" : Strings.nullToEmpty(value.toString())));
            }
        }
        sb.append("</form>");
        sb.append("<script>");
        sb.append("form.submit();");
        sb.append("</script>");
        return sb.toString();
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("<form name=\"form\" action=\"%s\" method=\"%s\" target=\"%s\">", getAction(), getMethod(), Strings.nullToEmpty(getTarget())));
        final List<String> keys = Lists.newArrayList(parameters.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!Strings.isNullOrEmpty(key)) {
                Object value = parameters.get(key);
                if (filterNull && (value == null || Strings.isNullOrEmpty(value.toString()))) {
                    continue;
                }

                sb.append(asHiddenInput(key, value == null ? "" : Strings.nullToEmpty(value.toString())));
            }
        }
        sb.append("</form>");

        sb.append("<script>");
        sb.append("form.submit();");
        sb.append("</script>");
        return sb.toString();
    }


}
