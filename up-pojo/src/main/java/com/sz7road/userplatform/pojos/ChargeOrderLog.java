/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jeremy
 */
public class ChargeOrderLog implements Serializable {

    private int id;
    private int gameId;
    private int gameZoneId;
    private float amount;
    private int gold;
    private int userId;
    private int playerId;
    private CharSequence orderId;
    private String assistResult;
    private int responseState;
    private Timestamp payTime;
    private Timestamp assertTime;
    private Timestamp createTime;

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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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

    public CharSequence getOrderId() {
        return orderId;
    }

    public void setOrderId(CharSequence orderId) {
        this.orderId = orderId;
    }

    public String getAssistResult() {
        return assistResult;
    }

    public void setAssistResult(String assistResult) {
        this.assistResult = assistResult;
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

    public Timestamp getAssertTime() {
        return assertTime;
    }

    public void setAssertTime(Timestamp assertTime) {
        this.assertTime = assertTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ChargeOrderLog{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", gameZoneId=" + gameZoneId +
                ", amount=" + amount +
                ", gold=" + gold +
                ", userId=" + userId +
                ", playerId=" + playerId +
                ", orderId=" + orderId +
                ", assistResult='" + assistResult + '\'' +
                ", responseState=" + responseState +
                ", payTime=" + payTime +
                ", assertTime=" + assertTime +
                ", createTime=" + createTime +
                '}';
    }
}
