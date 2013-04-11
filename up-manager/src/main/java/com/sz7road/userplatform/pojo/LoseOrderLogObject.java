package com.sz7road.userplatform.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-26
 * Time: 下午3:43
 * 调单日志实体类
 */
public class LoseOrderLogObject implements Serializable {
    //调单日志的ID
    private int id;
    //游戏编号
    private int gameId;
    //服务器编号
    private int gameZoneId;
    //充值金额
    private BigDecimal amount;
    //游戏币
    private int gold;
    //用户ID
    private int userId;
    //玩家ID
    private int playerId;
    // 订单号
    private String orderId;
    //帮助信息
    private String assistantResult;
    //返回状态码
    private int responseState;
    //支付时间
    private Timestamp payTime;
    //记录时间
    private Timestamp createTime;
    //确认时间
    private Timestamp assertTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameZoneId() {
        return gameZoneId;
    }

    public void setGameZoneId(int gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAssistantResult() {
        return assistantResult;
    }

    public void setAssistantResult(String assistantResult) {
        this.assistantResult = assistantResult;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getAssertTime() {
        return assertTime;
    }

    public void setAssertTime(Timestamp assertTime) {
        this.assertTime = assertTime;
    }


}
