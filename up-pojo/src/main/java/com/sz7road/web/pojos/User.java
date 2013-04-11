package com.sz7road.web.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午11:09
 */
public class User implements Serializable {

    private int id;

    private String username;

    private String realName;

    private String password;

    private Timestamp lastLoginTime;

    private Timestamp createTime;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String username) {
        this.username = username;
    }

    public User(int id, String username) {
        this(id);
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
