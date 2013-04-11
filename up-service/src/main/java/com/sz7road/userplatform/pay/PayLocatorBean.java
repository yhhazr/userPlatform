/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay;

import java.io.Serializable;

/**
 * @author jeremy
 */
public interface PayLocatorBean extends Serializable {

    /**
     * @return 充值渠道ID
     */
    char getChannelId();

    /**
     * @return 充值渠道子ID
     */
    int getSubTypeId();

    /**
     * @return 充值渠道标签
     */
    CharSequence getChannelTag();

    /**
     * 例如，银行标识。
     *
     * @return 充值渠道子标签
     */
    String getSubTag();

    /**
     * @return 充值HASH值
     */
    int getPayHash();

    /**
     * @return 充值处理器
     */
    PayHandler getHandler();

    /**
     * @return 充值管理门面组件
     */
    PayManager getManager();

}
