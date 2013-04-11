/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.Configuration;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author leo.liao
 */
public interface ConfigurationDao {

    long getLastModifiedTime() throws SQLException;

    List<Configuration> listAsConfiguration() throws SQLException;

    Map<String, Object> listAsMap() throws SQLException;

    int batchUpdate(Properties configuration) throws SQLException;

    void updateTableTimestamp() throws SQLException;
}
