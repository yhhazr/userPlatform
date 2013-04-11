package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Author: jiangfan.zhou
 * 登陆游戏记录
 */
public class LoginGame implements Serializable {
    private int id;
    private int userId;
    private int gameId;
    private int serverId;
    private Timestamp loginTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}
