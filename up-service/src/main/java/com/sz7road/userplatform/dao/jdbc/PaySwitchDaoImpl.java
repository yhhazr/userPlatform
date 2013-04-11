/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.PaySwitchDao;
import com.sz7road.userplatform.pojos.PaySwitch;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jiangfan.zhou
 */
public class PaySwitchDaoImpl extends JdbcDaoSupport<PaySwitch> implements PaySwitchDao {

    private static final String TABLE = "`conf_userplatform`.`conf_pay_switch_table`";
    private static final String SELECT_BASE = "SELECT * FROM " + TABLE + " WHERE 1";
    private static final String SELECT_BY_ID = "SELECT * FROM " + TABLE + " WHERE 1 AND ID = ?";
    private static final String SQL_SELECT_BY_FIELD = SELECT_BASE + " AND %s = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE + "(`channelId`,`subType`,`subTag`,`scale`,`status`,`channelName`,`subTypeName`,`subTagName`,`showType`,`selectType`,`comment`,`deleteFlag`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_UPDATE = "UPDATE " + TABLE + " SET `channelId`=?, `subType`=?,`subTag`=?,`scale`=?,`status`=?,`channelName`=?,`subTypeName`=?,`subTagName`=?,`showType`=?,`selectType`=?,`comment`=?,`deleteFlag`=? WHERE `id`=?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE + " WHERE `id` = ?";

    @Override
    public int add(PaySwitch entity) throws SQLException {
        return getQueryRunner().update(SQL_INSERT);
    }

    @Override
    public List<PaySwitch> listAll() throws SQLException {
        return getQueryRunner().query(SELECT_BASE + " ORDER BY channelId", new BeanListHandler<PaySwitch>(PaySwitch.class));
    }

    @Override
    public List<PaySwitch> get(String property, Object value) throws SQLException {
        return getQueryRunner().query(String.format(SQL_SELECT_BY_FIELD, property), new BeanListHandler<PaySwitch>(PaySwitch.class), value);
    }

    @Override
    public PaySwitch get(int id) throws SQLException {
        return getQueryRunner().query(SELECT_BY_ID, new BeanHandler<PaySwitch>(PaySwitch.class), id);
    }

    public int update(PaySwitch entity) throws SQLException {
        return getQueryRunner().update(SQL_UPDATE, entity.getChannelId(), entity.getSubType(),entity.getSubTag(), entity.getScale(), entity.getStatus(),
                entity.getChannelName(), entity.getSubTypeName(),entity.getSubTagName(),entity.getShowType(),entity.getSelectType(),entity.getComment(), entity.getDeleteFlag(), entity.getId());
    }

    @Override
    public int delete(PaySwitch entity) throws SQLException {
        return getQueryRunner().update(SQL_DELETE, entity.getId());
    }
}
