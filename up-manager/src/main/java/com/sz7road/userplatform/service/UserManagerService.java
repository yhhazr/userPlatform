package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.UserManagerDao;
import com.sz7road.userplatform.pojo.UserInfoObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.pojos.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 下午2:54
 */
@Singleton
public class UserManagerService {

    @Inject
    private Provider<UserManagerDao> userDaoProvider;

    private UserManagerDao getUserDao() {
        UserManagerDao userDao = userDaoProvider.get();
        if (userDao == null) {
            throw new NullPointerException("userDao null");
        }
        return userDao;
    }

    public User getUserByUsername(final String username) {
        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Illegal userName");
        }
        return getUserDao().get(username);
    }

    public User authenticated(final String userName, final String passWord) {
        if (Strings.isNullOrEmpty(userName)) throw new IllegalArgumentException("Illegal userName");
        if (Strings.isNullOrEmpty(passWord)) throw new IllegalArgumentException("Illegal passWord");
        final User user = getUserDao().get(userName, password(passWord));
        if (null != user) {
            return user;
        }
        return null;
    }

    public User add(final String username, final String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password(password));
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        final int id = getUserDao().add(user);
        if (id > 0) {
            user.setId(id);
            return user;
        }
        return null;
    }

    static String password(final String passWord) {
        try {
            return MD5Utils.digestAsHex(passWord);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> listAll() throws SQLException {
        return getUserDao().listAll();
    }

    public int updateLastLoginTime(User entity) {
        return getUserDao().updateLastLoginTime(entity);
    }

    public int removeUser(User user) throws SQLException {
        return getUserDao().delete(user);
    }

    public int updatePwd(User user, String password) {
        if (user == null || Strings.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("argument null");
        }
        user.setPassword(password(password));
        return getUserDao().updatePwd(user);
    }

    public UserInfoObject getAccountById(int id) {
        return getUserDao().getAccountById(id);
    }

    public List<UserInfoObject> getAccountByName(String name) {
        return getUserDao().getAccountByName(name);
    }

    public JqObject getProfileHistoryByName(String userName, int start, int pageSize) {
        return getUserDao().getProfileHistoryByName(userName, start, pageSize);
    }

    /**
     * 按照用户ID查询资料修改记录
     *
     * @param id
     * @return
     */
    public JqObject getProfileHistoryById(int id, int start, int pageSize) {
        return getUserDao().getProfileHistoryById(id, start, pageSize);
    }

    /**
     * 修改用户的密码
     *
     * @param id
     * @param password
     * @return
     */
    public int resetPwd(int id, String password, String userName) {
        return getUserDao().resetPwd(id, password, userName);
    }

    /**
     * 修改用户的绑定邮箱
     *
     * @param id
     * @param email
     * @return
     */
    public int resetEmail(int id, String email, String userName) {
        return getUserDao().resetEmail(id, email, userName);
    }
}
