package com.sz7road.web;

import com.google.inject.servlet.GuiceFilter;

import javax.servlet.annotation.WebFilter;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午9:49
 */
@WebFilter(filterName = "atomicFilter", urlPatterns = "/*")
public class AtomicFilter extends GuiceFilter {
}
