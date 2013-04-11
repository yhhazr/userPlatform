/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.Dao;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
public abstract class JdbcDaoSupport<T extends Serializable> implements Dao<T> {

    final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private Provider<QueryRunner> queryRunnerProvider;

    protected QueryRunner getQueryRunner() {
        final QueryRunner queryRunner = queryRunnerProvider.get();
        if (null == queryRunner) {
            throw new NullPointerException("Null queryRunner provided.");
        }
        return queryRunner;
    }

    @Override
    public List<T> get(final String property, final Object value) throws SQLException {
        return null;
    }

    @Override
    public List<T> get(final String[] properties, final Object... values) throws SQLException {
        return null;
    }

    @Override
    public List<T> listAll() throws SQLException {
        return null;
    }

    @Override
    public List<T> listAll(int limit) throws SQLException {
        return null;
    }

    @Override
    public T first() throws SQLException {
        return null;
    }

    @Override
    public T last() throws SQLException {
        return null;
    }

    @Override
    public int update(T entity) throws SQLException {
        return 0;
    }

    @Override
    public int delete(T entity) throws SQLException {
        return 0;
    }

    @Override
    public int add(T entity) throws SQLException {
        return 0;
    }

    @Override
    public int add(T... entities) throws SQLException {
        return 0;
    }

    @Override
    public int add(Iterable<T> entities) throws SQLException {
        return 0;
    }
}
