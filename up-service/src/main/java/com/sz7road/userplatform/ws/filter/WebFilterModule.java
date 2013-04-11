/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ws.filter;

import com.google.inject.servlet.ServletModule;

/**
 * @author jeremy
 */
public class WebFilterModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // First to filtering by IP.
        filter("/*").through(EncodingFilter.class);
        filter("/LoginAuth", "/SignUp", "/CheckAccount").through(IpFilter.class);

    }
}
