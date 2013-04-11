/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sz7road.userplatform.dao.OrderDao;
import com.sz7road.userplatform.pojos.OrderObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
class OrderDaoJdbcImpl extends JdbcDaoSupport<OrderObject> implements OrderDao {

    static final String QUERY_TEMPLATE = "SELECT $fields FROM $table WHERE 1";
    static final String INSERT_TEMPLATE = "INSERT INTO $table($fields) VALUES($values)";
    static final String UPDATE_TEMPLATE = "UPDATE $table SET $updateFields WHERE 1";
    static final String[] FIELDS = {
            "id",
            "channelId",
            "subType",
            "subTag",
            "amount",
            "gold",
            "rechargeAmount",
            "status",
            "currency",
            "userId",
            "playerId",
            "gameId",
            "zoneId",
            "payTime",
            "assertTime",
            "endOrder",
            "clientIp",
            "ext1",
            "ext2",
            "ext3"
    };

    protected String getTableName() {
        return "`" + TableLocator.getOrderTable() + "`";
    }

    @Override
    public int add(final OrderObject order) throws SQLException {
        if (null == order) {
            throw new IllegalArgumentException("无效的订单实体对象！");
        }

        final QueryRunner runner = getQueryRunner();

        List<Object> values = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();

        for (String field : FIELDS) {
            try {
                String name = "get" + field;
                final char[] chars = name.toCharArray();
                chars[3] = field.toUpperCase().charAt(0);
                name = String.valueOf(chars);

                final Method declaredMethod = order.getClass().getDeclaredMethod(name);
                if (null != declaredMethod) {
                    final Object valueObject = declaredMethod.invoke(order);
                    if (null != valueObject) {
                        String value = valueObject.toString();
                        if (null != value) {
                            if (sb.length() > 0)
                                sb.append(',');
                            sb.append('`').append(field).append('`');
                            values.add(value);
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        }

        String SQL = INSERT_TEMPLATE.replace("$table", getTableName());
        SQL = SQL.replace("$fields", sb.toString());
        SQL = SQL.replace("$values", Strings.repeat(",?", values.size()).substring(1));

        runner.update(SQL, values.toArray());
        return 1;
    }

    @Override
    public OrderObject getByOrderId(String order) throws SQLException {
        final QueryRunner runner = getQueryRunner();
        String SQL = QUERY_TEMPLATE + " AND `id`=?";
        SQL = SQL.replace("$fields", "*");
        SQL = SQL.replace("$table", getTableName());

        return runner.query(SQL, new BeanHandler<OrderObject>(OrderObject.class), order);
    }

    @Override
    public int update(OrderObject order) throws SQLException {
        if (null == order)
            throw new IllegalArgumentException("无效订单对象！");

        final QueryRunner runner = getQueryRunner();

        String SQL = UPDATE_TEMPLATE.replace("$table", getTableName());

        List<Object> values = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();

        for (String field : FIELDS) {
            try {
                String name = "get" + field;
                final char[] chars = name.toCharArray();
                chars[3] = field.toUpperCase().charAt(0);
                name = String.valueOf(chars);

                final Method declaredMethod = order.getClass().getDeclaredMethod(name);
                if (null != declaredMethod) {
                    final Object valueObject = declaredMethod.invoke(order);
                    if (null != valueObject) {
                        String value = valueObject.toString();
                        if (null != value) {
                            if (sb.length() > 0)
                                sb.append(',');
                            sb.append('`').append(field).append('`').append("=?");
                            values.add(value);
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        }
        SQL = SQL.replace("$updateFields", sb.toString());
        SQL = SQL + " AND `id`=?";

        values.add(order.getId());

        runner.update(SQL, values.toArray());
        return 1;
    }
}
