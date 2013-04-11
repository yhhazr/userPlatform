package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Author: jiangfan.zhou
 * 账户申诉
 */
public class Appeal implements Serializable {
    private int id;
    private int userId;
    private String userName;
    private String realName;
    private String idCard;
    private String oftenPlayGame;
    private String serverName;
    private String email;
    private String playerName;
    private int playerLevel;
    private String createDate;
    private String createCity;
    private String exceptionDate;
    private String lastLoginDate;
    private int pay;
    private String orderIds;
    private String idCardImgPath;
    private String otherInfo;
    private int gainPoints;
    private int status;
    private String auditor;
    private Timestamp auditorTime;
    private Timestamp appealTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOftenPlayGame() {
        return oftenPlayGame;
    }

    public void setOftenPlayGame(String oftenPlayGame) {
        this.oftenPlayGame = oftenPlayGame;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateCity() {
        return createCity;
    }

    public void setCreateCity(String createCity) {
        this.createCity = createCity;
    }

    public String getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(String exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getIdCardImgPath() {
        return idCardImgPath;
    }

    public void setIdCardImgPath(String idCardImgPath) {
        this.idCardImgPath = idCardImgPath;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public int getGainPoints() {
        return gainPoints;
    }

    public void setGainPoints(int gainPoints) {
        this.gainPoints = gainPoints;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Timestamp getAuditorTime() {
        return auditorTime;
    }

    public void setAuditorTime(Timestamp auditorTime) {
        this.auditorTime = auditorTime;
    }

    public Timestamp getAppealTime() {
        return appealTime;
    }

    public void setAppealTime(Timestamp appealTime) {
        this.appealTime = appealTime;
    }
}
