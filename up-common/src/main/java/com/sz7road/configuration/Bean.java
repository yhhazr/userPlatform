/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.configuration;

import java.io.Serializable;

/**
 * @author jeremy
 */
public interface Bean extends Serializable {

    /**
     * 根据名称获取指定的属性对象。
     *
     * @param name 属性名称
     * @return 属性对象
     */
    Object get(String name);

    /**
     * 根据属性名称获取指定的属性对象并以指定的类型包装返回。
     *
     * @param name      属性名称
     * @param classType 包装类型
     * @param <T>       type of classType
     * @return 属性对象
     */
    <T> T get(String name, Class<T> classType);

    /**
     * 获取该<code>Bean</code>的ID。
     *
     * @return value of id
     */
    String getId();

    /**
     * 获取该<code>Bean</code>的Name.
     *
     * @return value of name
     */
    String getName();

    /**
     * 获取该<code>Bean</code>的包装解析类型。
     *
     * @return 包装类型
     */
    Class<? extends SelfMakeBean> getWrapClass();
}
