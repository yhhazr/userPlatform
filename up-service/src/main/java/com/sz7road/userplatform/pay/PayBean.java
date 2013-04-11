/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay;

import java.sql.Timestamp;

/**
 * @author jeremy
 */
public interface PayBean extends PayLocatorBean {

    /**
     * @return 订单号
     */
    String getOrderId();

    /**
     * @return 游戏ID
     */
    int getGameId();

    /**
     * @return 游戏大区ID
     */
    int getGameZoneId();

    /**
     * @return 充值用户ID
     */
    int getUserId();

    /**
     * @return 充值用户名
     */
    String getUserName();

    /**
     * @return 充值游戏角色ID
     */
    int getPlayerId();

    /**
     * @return 充值游戏角色名称
     */
    String getPlayerName();

    /**
     * @return 充值金额
     */
    float getPayAmount();

    /**
     * @return 充值提交时间
     */
    Timestamp getTime();

    /**
     * 检查是否为有效的充值提交。
     *
     * @return true 为有效
     */
    boolean isAvailableForSubmit();

    /**
     * @return 充值商品描述
     */
    String getDescription();

    /**
     * @return 客户端IP
     */
    String getClientIp();
}
