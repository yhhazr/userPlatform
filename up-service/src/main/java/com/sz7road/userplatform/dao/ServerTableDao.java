/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import com.sz7road.userplatform.pojos.ServerTableNotForSqEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
public interface ServerTableDao extends Dao<ServerTable.ServerEntry> {


    List<ServerTableNotForSq.ServerEntryNotForSq> listsWithStatusNotForSq() throws SQLException;

    List<ServerTable.ServerEntry> listsWithStatus() throws SQLException;

    /**
     * 拿到排好序的状态不为-1的所有服务器信息
     *
     * @param gameId
     * @return
     */
    List<ServerEntity> getSortNonClosedServersByGameId(int gameId);

    List<ServerTableNotForSqEntity.ServerEntryNotForSqEntity> getSortNonClosedServersByGameIdNotForSq(int gameId);
}
