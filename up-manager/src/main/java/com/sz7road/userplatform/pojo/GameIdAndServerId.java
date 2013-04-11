package com.sz7road.userplatform.pojo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-25
 * Time: 下午2:44
 * 唯一标示服务器的实体
 */
public class GameIdAndServerId  implements Serializable
{
    private int gameId;

    private int serverId;


    public GameIdAndServerId(int gameId,int serverId)
    {
        this.gameId=gameId;
        this.serverId=serverId;
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
}
