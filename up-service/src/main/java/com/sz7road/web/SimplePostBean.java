package com.sz7road.web;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Map;

/**
 * @author jeremy
 */
public abstract class SimplePostBean implements Serializable {

    public abstract Map<String, Object> toMap();

    public abstract String getSTR();

    public abstract void updateSign();

    /**
     * 追加请求参数。
     *
     * @param returnStr
     * @param paramId
     * @param paramValue
     * @return <code>*returnStr</code>
     */
    public static StringBuilder appendQueryString(StringBuilder returnStr, String paramId, Object paramValue) {
        if (null != returnStr && returnStr.length() > 0) {
            if (null != paramValue && null != paramValue.toString()) {
                returnStr.append('&').append(paramId).append('=').append(paramValue);
            }
        } else {
            if (null != paramValue && null != paramValue.toString()) {
                returnStr.append(paramId).append('=').append(paramValue);
            }
        }
        return returnStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : toMap().entrySet()) {
            String value = (entry.getValue() == null ? "" : Strings.nullToEmpty(entry.getValue().toString()));
            appendQueryString(sb, entry.getKey(), value);
        }
        return sb.toString();
    }
}
