/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.Appeal;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.Pager;
import com.sz7road.utils.ListData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author jiangfan.zhou
 */
public interface AppealDao extends Dao<Appeal> {
    public static final int STATUS_AUDIT_NOT = 0;
    public static final int STATUS_AUDIT_PASS = 1;
    public static final int STATUS_AUDIT_NOT_PASS = 2;

    /**
     * 根据ID查找申诉对象
     * @param id
     * @return
     * @throws SQLException
     */
    Appeal get(int id) throws SQLException;

    /**
     * 根据ID查找申诉对象
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */
    Appeal get(Connection conn, int id) throws SQLException;

    /**
     * 更新申诉处理状态
     * @param appealId
     * @param status
     * @param auditor
     * @return
     * @throws SQLException
     */
    Appeal updateStatus(int appealId, int status, String auditor) throws SQLException;

    Appeal updateStatus(Connection conn, int appealId, int status, String auditor) throws SQLException;

    Pager<Appeal> query(String userName, int pay, int status, Date fromDate, Date toDate, int start, int limit) throws SQLException;
}
