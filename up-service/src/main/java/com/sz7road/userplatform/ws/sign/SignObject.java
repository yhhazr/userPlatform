package com.sz7road.userplatform.ws.sign;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-17
 * Time: 下午6:01
 * 签到所存储的实体对象.
 */
public class SignObject implements Serializable {
    /**
     * 标识列
     */
    private int id;
    /**
     * 用户id
     */
    private int uid;
    /**
     * 游戏id
     */
    private int gid;
    /**
     * 连续签到次数
     */
    private int signCount=1;
    /**
     * 总积分
     */
    private int integration=0;
    /**
     * 最后修改时间
     */
    private Timestamp lastModifyTime;
    /**
     * 签到历史
     */
    private long signHistory=0;
    /**
     * 保留字段
     */
    private String ext;

    public SignObject() {
    }

    public SignObject(int uid, int gid) {
        this.uid = uid;
        this.gid = gid;
    }

    public SignObject(int id, int uid, int gid, int signCount, int integration, Timestamp lastModifyTime, long signHistory, String ext) {
        this.id = id;
        this.uid = uid;
        this.gid = gid;
        this.signCount = signCount;
        this.integration = integration;
        this.lastModifyTime = lastModifyTime;
        this.signHistory = signHistory;
        this.ext = ext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getSignCount() {
        return signCount;
    }

    public void setSignCount(int signCount) {
        this.signCount = signCount;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public Timestamp getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Timestamp lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public long getSignHistory() {
        return signHistory;
    }

    public void setSignHistory(long signHistory) {
        this.signHistory = signHistory;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
