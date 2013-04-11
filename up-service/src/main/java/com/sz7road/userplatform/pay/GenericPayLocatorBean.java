/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay;

import javax.inject.Inject;

/**
 * @author jeremy
 */
public class GenericPayLocatorBean implements PayLocatorBean {

    private char channelId;
    private int subTypeId;
    private String subTag;

    private PayManager manager;

    @Inject
    protected GenericPayLocatorBean(PayManager manager) {
        super();
        this.manager = manager;
    }

    public GenericPayLocatorBean(PayManager manager, char channelId, int subTypeId) {
        this(manager, channelId, subTypeId, null);
    }

    GenericPayLocatorBean(PayManager manager, char channelId, int subTypeId, String subTag) {
        super();
        this.manager = manager;
        this.channelId = channelId;
        this.subTypeId = subTypeId;
        this.subTag = subTag;
    }

    @Override
    public char getChannelId() {
        return channelId;
    }

    protected void setChannelId(char channelId) {
        this.channelId = channelId;
    }

    @Override
    public int getSubTypeId() {
        return subTypeId;
    }

    @Override
    public CharSequence getChannelTag() {
        return getChannelId() + String.valueOf(getSubTypeId());
    }

    @Override
    public String getSubTag() {
        return subTag;
    }

    protected void setSubTag(String subTag) {
        this.subTag = subTag;
    }

    protected void setSubTypeId(int subTypeId) {
        this.subTypeId = subTypeId;
    }

    @Override
    public int getPayHash() {
        return PayManager.hash(getChannelId(), getSubTypeId());
    }

    protected boolean isValidChannelId() {
        char c = getChannelId();
        return (c >= '1' && c <= '9') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 获取当前充值渠道的提交处理组件。
     *
     * @return 充值提交处理组件，或无效请求下返回NULL
     */
    @Override
    public PayHandler getHandler() {
        return manager.get(getPayHash());
    }

    @Override
    public PayManager getManager() {
        return manager;
    }
}
