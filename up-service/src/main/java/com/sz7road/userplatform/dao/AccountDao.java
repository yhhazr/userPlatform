/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.UserAccount;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jeremy
 */
public interface AccountDao extends Dao<UserAccount> {

    /**
     * 通过<code>userName</code>获取用户账号对象。
     *
     * @param userName 用户账号名称
     * @return 用户账号对象
     */
    UserAccount get(String userName);

    /**
     * 通过<code>userName</code>和<code>passWord</code>获取用户账号数据。
     *
     * @param userName 用户账号名称
     * @param passWord 用户账号密码
     * @return 用户账号对象
     */
    UserAccount get(String userName, String passWord);

    /**
     * 添加用户账号。
     *
     * @param account 用户账号对象
     * @return 用户账号ID
     */
    int add(UserAccount account);

    /**
     * 添加用户账号。
     *
     * @param account 用户账号对象
     * @return 用户账号ID
     */
    int add(Connection conn, UserAccount account) throws SQLException;

    /**
     * 更新邮箱
     *
     * @param entity
     * @return
     */
    int updateEmail(UserAccount entity);

    /**
     * 更新邮箱
     * @param conn
     * @param userId
     * @param email
     * @return
     * @throws SQLException
     */
    int updateEmail(Connection conn, int userId, String email) throws SQLException;

    /**
     * 修改密码
     *
     * @param entity
     * @return
     */
    int modifyPassword(UserAccount entity);


    /**
     * 获取用户账号。
     *
     * @param userId
     * @return
     */
    UserAccount getById(int userId) throws SQLException;

    /**
     * @param email
     * @return
     */
    List<UserAccount> getByEmail(String email) throws SQLException;

    public int addReWrite(final UserAccount account);

}
