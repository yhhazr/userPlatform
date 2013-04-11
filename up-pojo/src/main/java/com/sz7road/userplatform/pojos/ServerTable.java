/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeremy
 */
public class ServerTable extends HashMap<Integer, ServerTable.ServerEntry> {

    /**
     * @author jeremy
     */
    public static class ServerEntry implements Serializable {

        private int id;
        private int gameId;
        private int serverNo;
        private String serverName;
        private int serverStatus = -3;
        private boolean recommand;
        private Timestamp createTime;
        private Timestamp openingTime;

//        @JsonIgnore
        private Map<String, String> variables;

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

        public int getServerNo() {
            return serverNo;
        }

        public void setServerNo(int serverNo) {
            this.serverNo = serverNo;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public int getServerStatus() {
            return serverStatus;
        }

        public void setServerStatus(int serverStatus) {
            this.serverStatus = serverStatus;
        }

        public boolean isRecommand() {
            return recommand;
        }

        public void setRecommand(boolean recommand) {
            this.recommand = recommand;
        }

        public Timestamp getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Timestamp createTime) {
            this.createTime = createTime;
        }

        public Timestamp getOpeningTime() {
            return openingTime;
        }

        public void setOpeningTime(Timestamp openingTime) {
            this.openingTime = openingTime;
        }

        public Map<String, String> getVariables() {
            return variables;
        }

        public void setVariables(Map<String, String> variables) {
            this.variables = variables;
        }

    }
}
