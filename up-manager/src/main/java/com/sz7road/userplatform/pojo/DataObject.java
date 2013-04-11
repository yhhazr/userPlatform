package com.sz7road.userplatform.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-11
 * Time: 上午10:30
 * 存放数据的实体
 */
public class DataObject {
    //游戏区ID
    private int id;
    //服务区的名称

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    private String serverName;
    //key对应的URL
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
