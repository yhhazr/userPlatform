package com.sz7road.web.pojos;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-12
 * Time: 上午10:43
 * 游戏历程数据
 */
public class GameHistory implements Serializable {
    //用户Id
    private int userId;
    //进游戏的tag
    private String gameTag;
    //游戏id
    private int gameId;
    //游戏名称
    private String gameName;
    //服务器id
    private int serverId;
    //服务器名
    private String serverName;

    public String getGameImg() {
        return gameImg;
    }

    public void setGameImg(String gameImg) {
        this.gameImg = gameImg;
    }

    //游戏的图片名称
    private String gameImg;

    public GameHistory(int userId, String gameTag, int gameId, String gameName, int serverId, String serverName) {
        this.userId = userId;
        this.gameTag = gameTag;
        this.gameId = gameId;
        this.gameName = gameName;
        this.serverId = serverId;
        this.serverName = serverName;
    }

    public GameHistory() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGameTag() {
        return gameTag;
    }

    public void setGameTag(String gameTag) {
        this.gameTag = gameTag;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("userId", userId).add("gameName", gameName)
                .add("serverName", serverName).add("gameImg", gameImg).toString();
    }
}
