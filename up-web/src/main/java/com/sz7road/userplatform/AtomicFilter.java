/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform;

import com.google.inject.servlet.GuiceFilter;

import javax.servlet.annotation.WebFilter;

/**
 * @author jeremy
 */
@WebFilter(filterName = "atomicFilter", urlPatterns = "/*")
public class AtomicFilter extends GuiceFilter {
}
