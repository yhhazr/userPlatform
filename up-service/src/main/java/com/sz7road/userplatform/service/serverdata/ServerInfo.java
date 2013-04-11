package com.sz7road.userplatform.service.serverdata;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-21
 * Time: 下午4:57
 * 有效服务区信息实体
 */
public class ServerInfo implements Serializable {

    private int serverId;

    private int gameId;

    private int serverNo;

    private String serverName;
    // (0:自己为主区)
    private int mainId;

    private int serverStatus;

    private boolean recommand;

    private Timestamp createTime;

    private Timestamp openingTime;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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

    public int getMainId() {
        return mainId;
    }

    public void setMainId(int mainId) {
        this.mainId = mainId;
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

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public Timestamp getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Timestamp openingTime) {
        this.openingTime = openingTime;
    }
}
