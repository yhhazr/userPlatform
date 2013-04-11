package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServerEntity2 implements Serializable {
    private int id;
    private int gameId;
    private String gameName;
    private int serverNo;
    private String serverName;
    private int serverStatus = -3;
    private boolean recommand;
    private Timestamp createTime;
    private Timestamp openingTime;

    public enum SERVER_STATUS
    {
        SERVER_STATUS_HOT(1),//火爆
        SERVER_STATUS_MAINTAIN(-2),//维护
        SERVER_STATUS_NOTOPEN(-3),//未开启
        SERVER_STATUS_CLOSED(-1); //停止
        private int code;

        private SERVER_STATUS(int code) {
            this.code = code;
        }

        public int getServerStatusCode() {
            return this.code;
        }

        public static SERVER_STATUS valueOf(int code) {
            for (SERVER_STATUS type : values()) {
                if (type.code == code)
                {
                    return type;
                }
            }
            throw new IllegalArgumentException("Illegal error code!");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getServerNo() {
        return serverNo;
    }

    public void setServerNo(int serverNo) {
        this.serverNo = serverNo;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public boolean isRecommand() {
        return recommand;
    }

    public void setRecommand(boolean recommand) {
        this.recommand = recommand;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Timestamp openingTime) {
        this.openingTime = openingTime;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
