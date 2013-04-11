package com.sz7road.web.servlet.headless;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author jeremy
 */
@RequestScoped
public class HeadlessServletRequest extends HttpServletRequestWrapper {

    /**
     * Constructs by default.
     *
     * @param request serlvet requester
     */
    @Inject
    public HeadlessServletRequest(HttpServletRequest request) {
        super(request);
    }

    /**
     * 获取不为空的参数值，假如该结果为NULL，则返回空字符串(EMPTY)。
     *
     * @param name 参数名
     * @return 参数值
     */
    public String getNotNullParameter(String name) {
        return Strings.nullToEmpty(getParameter(name));
    }

    /**
     * 获取整型参数值，假如该原结果为NULL，则返回0.
     *
     * @param name 参数名
     * @return 参数值
     */
    public int getIntParameter(String name) {
        try {
            return Integer.parseInt(getNotNullParameter(name));
        } catch (final NumberFormatException e) {
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取短整型参数值，假如该原结果为NULL，则返回0.
     *
     * @param name 参数名
     * @return 参数值
     */
    public short getShortParameter(String name) {
        try {
            return Short.parseShort(getNotNullParameter(name));
        } catch (final NumberFormatException e) {
            return (short) 0;
        }
    }

    /**
     * 获取布尔参数值.
     *
     * @param name 参数名
     * @return 参数值
     */
    public boolean getBoolParameter(String name) {
        return Boolean.parseBoolean(getNotNullParameter(name));
    }

    /**
     * 获取长整型参数值，假如该原结果为NULL，则返回0.
     *
     * @param name 参数名
     * @return 参数值
     */
    public long getLongParameter(String name) {
        try {
            return Long.parseLong(getNotNullParameter(name));
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 获取字符参数值，假如该原结果为无效值，则返回0.
     *
     * @param name 参数名
     * @return 参数值
     */
    public char getCharParameter(String name) {
        final String value = getNotNullParameter(name);
        if (value.length() > 0) {
            return value.charAt(0);
        }
        return 0;
    }
}
