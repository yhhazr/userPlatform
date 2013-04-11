/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jiangfan.zhou
 */
public interface LoginGameDao extends Dao<LoginGame> {

    void insertOrUpdate(LoginGame entity) throws SQLException;

    List<LoginGame> list(int userId, int size) throws SQLException;

    List<LoginGame> list(int userId, int gameId, int size) throws SQLException;

    List<LoginGame> list(int userId, int gameId, int serverId, int size) throws SQLException;
}
