package com.sz7road.web.pojos;

import java.io.Serializable;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-29
 * Time: 上午8:06
 * Description:合服信息的单元实体
 */
public class MergerInfoBean implements Serializable {
    /**
     * 服务器id
     */
    private int serverId;
    /**
     * 服务器编号
     */
    private int serverNo;
    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 主服务器id
     */
    private int mainServerId;
    /**
     * 主区的序号
     */
    private int mainServerNo;

    public int getMainServerNo() {
        return mainServerNo;
    }

    public void setMainServerNo(int mainServerNo) {
        this.mainServerNo = mainServerNo;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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

    public int getMainServerId() {
        return mainServerId;
    }

    public void setMainServerId(int mainServerId) {
        this.mainServerId = mainServerId;
    }
}
