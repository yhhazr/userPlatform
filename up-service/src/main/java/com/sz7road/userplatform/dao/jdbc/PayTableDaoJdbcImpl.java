package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.PayTableDao;
import com.sz7road.userplatform.pojos.PayTable;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
class PayTableDaoJdbcImpl extends JdbcDaoSupport<PayTable.PayEntry> implements PayTableDao {

    private static final String TABLE = "`conf_userplatform`.`conf_pay_table`";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE + " WHERE 1";


    @Override
    public List<PayTable.PayEntry> listAll() throws SQLException {
        final QueryRunner runner = getQueryRunner();
        return runner.query(SQL_SELECT_ALL, new BeanListHandler<PayTable.PayEntry>(PayTable.PayEntry.class));
    }
}
