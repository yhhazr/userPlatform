/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

@Deprecated
public class GameServer {
    private int id;
    private int gameId;
    private String link;
    private String selectRoleUrl;
    private String state;
    private String stateInformation;
    private String serverName;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSelectRoleUrl() {
        return selectRoleUrl;
    }

    public void setSelectRoleUrl(String selectRoleUrl) {
        this.selectRoleUrl = selectRoleUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateInformation() {
        return stateInformation;
    }

    public void setStateInformation(String stateInformation) {
        this.stateInformation = stateInformation;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPlayGameUrl() {
        return playGameUrl;
    }

    public void setPlayGameUrl(String playGameUrl) {
        this.playGameUrl = playGameUrl;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getChargeKey() {
        return chargeKey;
    }

    public void setChargeKey(String chargeKey) {
        this.chargeKey = chargeKey;
    }

    public String getChargeUrl() {
        return chargeUrl;
    }

    public void setChargeUrl(String chargeUrl) {
        this.chargeUrl = chargeUrl;
    }

    private String site;
    private String playGameUrl;
    private String loginKey;
    private String loginUrl;
    private String chargeKey;
    private String chargeUrl;
}
