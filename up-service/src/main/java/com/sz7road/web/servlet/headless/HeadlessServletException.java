package com.sz7road.web.servlet.headless;

import javax.servlet.ServletException;

/**
 * @author jeremy
 */
public class HeadlessServletException extends ServletException {

    public HeadlessServletException(Exception e) {
        super(e);
    }
}
