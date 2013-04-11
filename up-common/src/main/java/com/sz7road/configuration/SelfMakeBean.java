/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.configuration;

import com.google.inject.Module;

/**
 * @author jeremy
 */
public interface SelfMakeBean extends Module {

    /**
     * 自解析这个JavaBean.
     *
     * @param bean 被自解的bean
     */
    SelfMakeBean make(Bean bean);

}
