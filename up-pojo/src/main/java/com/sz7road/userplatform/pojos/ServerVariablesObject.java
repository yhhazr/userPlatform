package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-16
 * Time: 上午2:07
 * Description: 服务器变量实体
 */
public class ServerVariablesObject implements Serializable {

    private int serverId;

    private String key;

    private String value;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
