/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jeremy
 */
public class OrderObject implements Serializable {

    public static final int PAY_SUCCESS = 1;//支付成功状态
    public static final int PAY_FAIL = 0;//未支付状态
    private CharSequence id;
    private CharSequence channelId;
    private int subType;
    private String subTag;
    private float amount;
    private int gold;
    private int rechargeAmount;
    private byte status;
    private byte currency;
    private int userId;
    private int playerId;
    private int gameId;
    private int zoneId;
    private Timestamp payTime;
    private Timestamp assertTime;
    private String endOrder;

    private String clientIp;
    private String ext1;
    private String ext2;
    private String ext3;

    /**
     * Constructs by default.
     */
    public OrderObject() {
        super();
    }

    /**
     * @return 订单ID
     */
    public CharSequence getId() {
        return id;
    }

    public void setId(CharSequence id) {
        this.id = id;
    }

    public CharSequence getChannelId() {
        return this.channelId;
    }

    public void setChannelId(CharSequence channelId) {
        this.channelId = channelId;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    /**
     * @return 支付渠道标签
     */
    public CharSequence getChannelTag() {
        return getChannelId() + String.valueOf(getSubType());
    }

    /**
     * @return 支付渠道子标签
     */
    public String getSubTag() {
        return subTag;
    }

    public void setSubTag(String subTag) {
        this.subTag = subTag;
    }

    /**
     * @return 支付金额
     */
    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * @return 支付状态
     */
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    /**
     * @return 支付货币类型
     */
    public byte getCurrency() {
        return currency;
    }

    public void setCurrency(byte currency) {
        this.currency = currency;
    }

    /**
     * @return 用户账号ID
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return 用户游戏角色ID
     */
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * @return 游戏ID
     */
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * @return 游戏大区ID
     */
    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * @return 支付时间
     */
    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    /**
     * @return 支付确认时间
     */
    public Timestamp getAssertTime() {
        return assertTime;
    }

    public void setAssertTime(Timestamp assertTime) {
        this.assertTime = assertTime;
    }

    /**
     * @return 对方订单号
     */
    public String getEndOrder() {
        return endOrder;
    }

    public void setEndOrder(String endOrder) {
        this.endOrder = endOrder;
    }

    /**
     * @return 客户端IP
     */
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    /**
     * @return 扩展信息1
     */
    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    /**
     * @return 扩展信息2
     */
    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    /**
     * @return 扩展信息3
     */
    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    /**
     * @return 兑换的游戏币数量
     */
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * @return 与游戏兑换的实际金额
     */
    public int getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(int rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    @Override
    public String toString() {
        return "OrderObject{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", subType=" + subType +
                ", subTag='" + subTag + '\'' +
                ", amount=" + amount +
                ", gold=" + gold +
                ", rechargeAmount=" + rechargeAmount +
                ", status=" + status +
                ", currency=" + currency +
                ", userId=" + userId +
                ", playerId=" + playerId +
                ", gameId=" + gameId +
                ", zoneId=" + zoneId +
                ", payTime=" + payTime +
                ", assertTime=" + assertTime +
                ", endOrder='" + endOrder + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", ext3='" + ext3 + '\'' +
                '}';
    }
}
