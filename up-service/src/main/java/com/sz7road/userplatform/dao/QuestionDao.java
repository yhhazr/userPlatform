/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.Question;
import com.sz7road.userplatform.pojos.UserAccount;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jiangfan.zhou
 */
public interface QuestionDao extends Dao<Question> {

    public final static int STATUS_USE = 1;
    public final static int STATUS_NOT_USE = 0;

    /**
     * 通过<code>id</code>获取问题答案对象。
     *
     * @param id 编号
     * @return 密保问题答案对象
     */
    Question get(int id);

    /**
     * 通过<code>userId</code>获取问题答案对象。
     *
     * @param userId 用户账ID
     * @param n 返回条数
     * @return 最新n条密保问题答案对象
     */
    List<Question> getByUserId(int userId, int status, int n) throws SQLException;

    /**
     * 添加密保问题答案。
     *
     * @param question 密保问题答案对象
     * @return 添加记录数
     */
    int add(Question question) throws SQLException;

    /**
     * 添加密保问题答案。
     *
     * @param entities 密保问题答案对象
     * @return 添加记录数
     */
    int add(Question... entities);

    /**
     * 添加密保问题答案并更新之前密保状态。
     *
     * @param userId 用户账ID
     * @param status 问题状态
     * @param entities 密保问题答案对象
     * @return 添加记录数
     */
    int addAndUpdateStatus(int userId, int status, Question... entities);

    /**
     * 更新问题状态
     * @param conn
     * @param userId
     * @param status
     * @return
     */
    int updateStatus(Connection conn, int userId, int status) throws SQLException;
}
