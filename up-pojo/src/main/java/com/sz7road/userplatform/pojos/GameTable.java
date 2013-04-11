/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author jeremy
 */
public abstract class GameTable extends HashMap<Integer, GameTable.GameEntry> implements Serializable {

    /**
     * @author jeremy
     */
    public static class GameEntry implements Serializable {

        private int id;
        private String gameName;
        private String goldName;
        private int goldScale;
        private String homePage;
        private String gameLogoSelectPage;
        private String gameLogoChargePage;
        private String aliasName;
        private int status;
        private int showOrder;
        private int gameType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGoldName() {
            return goldName;
        }

        public void setGoldName(String goldName) {
            this.goldName = goldName;
        }

        public int getGoldScale() {
            return goldScale;
        }

        public void setGoldScale(int goldScale) {
            this.goldScale = goldScale;
        }

        public String getHomePage() {
            return homePage;
        }

        public void setHomePage(String homePage) {
            this.homePage = homePage;
        }

        public String getGameLogoSelectPage() {
            return gameLogoSelectPage;
        }

        public void setGameLogoSelectPage(String gameLogoSelectPage) {
            this.gameLogoSelectPage = gameLogoSelectPage;
        }

        public String getGameLogoChargePage() {
            return gameLogoChargePage;
        }

        public void setGameLogoChargePage(String gameLogoChargePage) {
            this.gameLogoChargePage = gameLogoChargePage;
        }

        public String getAliasName() {
            return aliasName;
        }

        public void setAliasName(String aliasName) {
            this.aliasName = aliasName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getShowOrder() {
            return showOrder;
        }

        public void setShowOrder(int showOrder) {
            this.showOrder = showOrder;
        }

        public int getGameType() {
            return gameType;
        }

        public void setGameType(int gameType) {
            this.gameType = gameType;
        }
    }

    /**
     * 获取游戏服务器列表。
     *
     * @param gameId 游戏ID
     * @return 游戏服务器列表或NULL
     */
    public abstract ServerTable getServerTable(int gameId);

    /**
     * 添加游戏服务器列表。
     *
     * @param gameId 游戏ID
     * @param table  游戏服务器列表
     */
    protected abstract void addServerTable(int gameId, ServerTable table);

    /**
     * 获取游戏服务器列表。
     *
     * @param entry 游戏表实体
     * @return 游戏服务器列表或NULL
     */
    public ServerTable getServerTable(GameEntry entry) {
        if (null != entry)
            return getServerTable(entry.getId());
        return null;
    }

}
