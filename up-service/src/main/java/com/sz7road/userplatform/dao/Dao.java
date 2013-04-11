/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
public interface Dao<T extends Serializable> {

    /**
     * 获取指定的数据表字段与值的数据对象。
     *
     * @param property 指定数据表字段
     * @param value    指定值
     * @return 数据对象列表
     */
    List<T> get(String property, Object value) throws SQLException;

    /**
     * 获取指定的数据表字段与值的数据对象。
     *
     * @param properties 指定数据表字段
     * @param values     指定值
     * @return 数据对象列表
     */
    List<T> get(String[] properties, Object... values) throws SQLException;

    /**
     * 查询所有数据。<b color="red">NOTE: 请慎用。</b>
     *
     * @return 数据对象列表
     */
    List<T> listAll() throws SQLException;

    /**
     * 获取数量限制为<code>limit</code>的数据对象。
     *
     * @param limit 限制数量
     * @return 数据对象列表
     */
    List<T> listAll(int limit) throws SQLException;

    /**
     * 获取数据表第一行数据。
     *
     * @return 数据对象
     */
    T first() throws SQLException;

    /**
     * 获取数据表最后一行数据。
     *
     * @return 数据对象
     */
    T last() throws SQLException;

    /**
     * 更新数据表对象。
     *
     * @param entity 用作更新的数据对象
     * @return true or false
     */
    int update(T entity) throws SQLException;

    /**
     * 删除数据表对象。
     *
     * @param entity 用作删除的数据对象
     * @return true or false
     */
    int delete(T entity) throws SQLException;

    /**
     * 添加数据表对象。
     *
     * @param entity 待添加的数据对象
     * @return true or false
     */
    int add(T entity) throws SQLException;

    /**
     * 添加多个数据表对象。
     *
     * @param entities 待添加的数据对象或集合
     * @return true or false
     */
    int add(T... entities) throws SQLException;

    /**
     * 添加多个数据表对象。
     *
     * @param entities 待添加的数据对象集合
     * @return true or false
     */
    int add(Iterable<T> entities) throws SQLException;

}
