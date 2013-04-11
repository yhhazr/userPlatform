package com.sz7road.userplatform.ws.sign;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-18
 * Time: 上午10:57
 * 返回的签到信息实体
 */
public class Sign  implements Serializable {

    /**
     * 字段描述：处理结果
     * 200 处理成功
     * 404 处理失败，sql错误
     */
    private int code=404;

    public Sign() {
    }

    /**
     * 字段描述：签到总积分
     */
    private int totalScore=0;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getSignHistory() {
        return signHistory;
    }

    public void setSignHistory(long signHistory) {
        this.signHistory = signHistory;
    }

    /**
     * 字段描述：连续签到次数
     */
    private int continueSignCount=0;


    /**
     * 字段描述：签到的历史记录
     */
    private long signHistory;

    /**
     * 字段描述：最后签到时间
     */
    private Timestamp lastModifyDate=new Timestamp(System.currentTimeMillis());

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 处理结果简介
     */
    private String msg;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }



    public int getContinueSignCount() {
        return continueSignCount;
    }

    public void setContinueSignCount(int continueSignCount) {
        this.continueSignCount = continueSignCount;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Timestamp lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

}