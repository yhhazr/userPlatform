package com.sz7road.userplatform.pojo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-24
 * Time: 下午12:21
 * 树形结构实体
 */
public class ZTreeObject implements Serializable {
    //id
    private int id;
    //父ID
    private int pId;
    //名称
    private String name;
    //服务器编号
    private String serverNo;

    public String getServerNo() {
        return serverNo;
    }

    public void setServerNo(String serverNo) {
        this.serverNo = serverNo;
    }

    //是不是可以被选中
    private String nocheck;

    public String getNocheck() {
        return nocheck;
    }

    public void setNocheck(String nocheck) {
        this.nocheck = nocheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
}
