/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.GameTableDao;
import com.sz7road.userplatform.pojos.GameTable;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
class GameTableDaoImpl extends JdbcDaoSupport<GameTable.GameEntry> implements GameTableDao {

    private static final String TABLE = "`conf_userplatform`.`conf_game_table`";
    private static final String SQL_LOAD_AS_TABLE = " SELECT * FROM " + TABLE + "  order by id asc ";

    @Override
    public List<GameTable.GameEntry> listAll() throws SQLException {
        final QueryRunner runner = getQueryRunner();
        return runner.query(SQL_LOAD_AS_TABLE, new BeanListHandler<GameTable.GameEntry>(GameTable.GameEntry.class));
    }
}
