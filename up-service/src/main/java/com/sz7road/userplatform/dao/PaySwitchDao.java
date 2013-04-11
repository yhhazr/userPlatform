package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.PaySwitch;

import java.sql.SQLException;

public interface PaySwitchDao extends Dao<PaySwitch> {

    PaySwitch get(int id) throws SQLException;

}
