package com.sz7road.web.pojos;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-2-25
 * Time: 下午3:01
 * 防沉迷执行结果的实体
 */
public class FcmObject implements Serializable {
    //请求结果
    private String result="fail";
    //状态
    private String status;
    //累计时间
    private String totalTime;
    //身份证号码
    private String cardId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("result", result).add("status", status)
                .add("totalTime", totalTime).add("cardId", cardId).toString();
    }
}
