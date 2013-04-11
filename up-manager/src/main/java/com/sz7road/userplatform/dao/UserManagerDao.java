package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojo.UserInfoObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.web.pojos.User;

import java.util.List;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 下午2:35
 */
public interface UserManagerDao extends Dao<User> {
    User get(String username);

    User get(String username, String password);

    int add(User user);

    int updateLastLoginTime(User entitiy);

    int updatePwd(final User user);

    /**
     * 根据用户Id拿到用户信息
     *
     * @param id
     * @return
     */
    UserInfoObject getAccountById(int id);

    /**
     * 根据用户名查询用户信息
     *
     * @param name
     * @return
     */
    public List<UserInfoObject> getAccountByName(String name);

    /**
     * 按照用户名查询资料修改记录
     */

    public JqObject getProfileHistoryByName(String userName, int start, int pageSize);

    /**
     * 按照用户ID查询资料修改记录
     *
     * @param id
     * @return
     */
    public JqObject getProfileHistoryById(int id, int start, int pageSize);

    /**
     * 修改用户的密码
     *
     * @param id
     * @param password
     * @return
     */
    public int resetPwd(int id, String password, String userName);

    /**
     * 修改用户的绑定邮箱
     *
     * @param id
     * @param email
     * @return
     */
    public int resetEmail(int id, String email, String userName);

}
