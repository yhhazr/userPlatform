/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.dao;

import com.google.common.collect.ImmutableList;
import com.sz7road.userplatform.pojos.UserObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
public interface UserDao extends Dao<UserObject> {

    /**
     * 根据<code>id</code>获取{@link UserObject}的数据对象。
     *
     * @param id 唯一标识
     * @return {@link UserObject}数据对象
     */
    UserObject get(int id);

    int add(Connection conn, UserObject user) throws SQLException;

    int updateLoginData(UserObject user) throws SQLException;

    /**
     * 更新用户基本信息
     *
     * @param entity
     * @return
     */
    int updateBaseInfo(UserObject entity);

    /**
     * 更新安全级别
     *
     * @param
     * @return
     */
    int updateSafeLevel(int id, byte safeLevel);

    /**
     * 更新密码级别
     *
     * @param
     * @return
     */
    public int updatePswStrengthLevel(int id, int pswStrength);

    /**
     * 更新登陆游戏相关的数据
     *
     * @param user
     * @return
     * @throws SQLException
     */
    int updateGameData(final UserObject user) throws SQLException;

    /**
     * 根据手机号查找用户
     *
     * @param mobile
     * @return
     */
    List<UserObject> getByMobile(int id, String mobile) throws SQLException;

    /**
     * 更新用户手机
     *
     * @param user
     * @return
     * @throws SQLException
     */
    int updateMobile(UserObject user) throws SQLException;

    /**
     * 更新用户手机
     *
     * @param conn
     * @param userId
     * @param mobile
     * @return
     * @throws SQLException
     */
    int updateMobile(Connection conn, int userId, String mobile) throws SQLException;

    /**
     * 更新用户短信以发送条数和最后短信发送时间
     *
     * @param entity
     * @return
     * @throws SQLException
     */
    int updateMessageCount(UserObject entity) throws SQLException;

    /**
     * 更新用户的头像
     *
     * @param id      用户Id
     * @param headDir 用户头像的相对路径
     * @return 结果
     */
    int updateUserAvatar(int id, String userName, String headDir);

    /**
     * 更新用户的基本信息
     *
     * @param userObject
     * @return
     */
    int updateUserBasicInfo(UserObject userObject);

    /**
     * 更新用户的详细信息
     *
     * @param userObject
     * @return
     */
    int updateUserDetailInfo(UserObject userObject);

    /**
     * 更新用户的教育信息
     *
     * @param userObject
     * @return
     */
    int updateUserEduInfo(UserObject userObject);

    /**
     * 更新用户的工作信息
     *
     * @param userObject
     * @return
     */
    int updateUserWorkInfo(UserObject userObject);


    public Map<String, Integer> add_register(final UserObject entity) throws SQLException;


    /**
     * 修改密码,并设置IP的显隐性，修改密码强度。
     *
     * @param userName 用户名
     * @param psw      新密码
     * @return
     */

    int modifyPasswordCommon(int id, String userName, String psw);

    /**
     * 防沉迷注册
     *
     * @param user
     * @return
     */
    public Map<String, Integer> register_fcm(UserObject user);


    /**
     * 更新平台用户的防沉迷信息
     *
     * @param userId   用户Id
     * @param realName 真实姓名
     * @param icn      身份证号码
     * @return
     */
    public boolean updateFcmInfo(int userId, String realName, String icn);

    /**
     * 20130329 11:20
     * 通过用户id得到sites
     *
     * @return
     */
    ImmutableList<String> getRoleInfoByUserId(int userId);
}
