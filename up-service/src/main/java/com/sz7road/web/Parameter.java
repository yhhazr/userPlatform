/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.web;

import java.lang.annotation.*;

/**
 * @author jeremy
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
public @interface Parameter {

    public String method() default "";

    public String[] value();
}
