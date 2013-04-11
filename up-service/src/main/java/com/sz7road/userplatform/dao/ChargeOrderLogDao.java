/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.ChargeOrderLog;

import java.sql.SQLException;

/**
 * @author jeremy
 */
public interface ChargeOrderLogDao extends Dao<ChargeOrderLog> {

    ChargeOrderLog getByOrderId(String order) throws SQLException;
}
