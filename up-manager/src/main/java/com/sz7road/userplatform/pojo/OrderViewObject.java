package com.sz7road.userplatform.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 下午6:05
 * 存放查询的订单数据的对象
 */
public class OrderViewObject implements Serializable {
    //订单号
    private String orderId;
    //用户ID
    private int userId;
    //用户名
    private String userName;
    //用户注册时的Email
    private String userEmail;
    //角色ID
    private int playerId;
    //角色名
    private String playerName;
    //游戏的ID
    private int gameId;
    //游戏的名字
    private String gameName;
    //游戏币名称
    private String gameGoldName;
    //游戏的主页
    private String gameHomepage;
    //游戏服务器ID
    private int zoneId;
    //服务器名称
    private String serverName;
    //充值网关
    private String channelName;
    //充值方式
    private String subTypeName;
    //充值渠道
    private String subTypeTagName;
    //金额
    private float amount;
    //游戏币
    private float gold;
    //网关订单号

    private String endOrder;
    //订单状态
    private String status;
    //支付时间
    private Timestamp payTime;
    //确认时间
    private Timestamp assertTime;
    //客户端IP
    private String clientIp;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameGoldName() {
        return gameGoldName;
    }

    public void setGameGoldName(String gameGoldName) {
        this.gameGoldName = gameGoldName;
    }

    public String getGameHomepage() {
        return gameHomepage;
    }

    public void setGameHomepage(String gameHomepage) {
        this.gameHomepage = gameHomepage;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getSubTypeTagName() {
        return subTypeTagName;
    }

    public void setSubTypeTagName(String subTypeTagName) {
        this.subTypeTagName = subTypeTagName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getGold() {
        return gold;
    }

    public void setGold(float gold) {
        this.gold = gold;
    }

    public String getEndOrder() {
        return endOrder;
    }

    public void setEndOrder(String endOrder) {
        this.endOrder = endOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }


}
