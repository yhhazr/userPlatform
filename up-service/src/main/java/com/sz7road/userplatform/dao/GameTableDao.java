/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.GameTable;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
public interface GameTableDao extends Dao<GameTable.GameEntry> {

    /**
     * 获取游戏列表
     *
     * @return list of game table entry
     */
    List<GameTable.GameEntry> listAll() throws SQLException;
}
