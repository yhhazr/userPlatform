/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cutter.li
 */
public class ServerTableNotForSqEntity extends HashMap<Integer, ServerTableNotForSqEntity.ServerEntryNotForSqEntity> {

    /**
     * 新表的结构，增加了两个字段
     */
    public static class ServerEntryNotForSqEntity implements Serializable {

        private int id;
        private int serverId;
        private int mainId;
        private int gameId;
        private int serverNo;
        private String serverName;
        private int status = -3;
        private boolean recommand;
        private Timestamp createTime;
        private Timestamp openTime;

////        @JsonIgnore
//        private Map<String, String> variables;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Timestamp getOpenTime() {
            return openTime;
        }

        public void setOpenTime(Timestamp openTime) {
            this.openTime = openTime;
        }

        //        public Map<String, String> getVariables() {
//            return variables;
//        }
//
//        public void setVariables(Map<String, String> variables) {
//            this.variables = variables;
//        }

        public int getServerId() {
            return serverId;
        }

        public void setServerId(int serverId) {
            this.serverId = serverId;
        }

        public int getMainId() {
            return mainId;
        }

        public void setMainId(int mainId) {
            this.mainId = mainId;
        }
    }
}
