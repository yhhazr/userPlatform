package com.sz7road.userplatform.pojos;

import java.io.Serializable;

public class PaySwitch implements Serializable {
    private int id;
    private CharSequence channelId;
    private int subType;
    private String subTag;
    private int scale;
    private byte status;
    private String channelName;
    private String subTypeName;
    private String subTagName;
    private String showType;
    private String selectType;
    private String comment;
    private byte deleteFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CharSequence getChannelId() {
        return channelId;
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

    public String getSubTag() {
        return subTag;
    }

    public void setSubTag(String subTag) {
        this.subTag = subTag;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
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

    public String getSubTagName() {
        return subTagName;
    }

    public void setSubTagName(String subTagName) {
        this.subTagName = subTagName;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "PaySwitch{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", subType=" + subType +
                ", subTag='" + subTag + '\'' +
                ", scale=" + scale +
                ", status=" + status +
                ", channelName='" + channelName + '\'' +
                ", subTypeName='" + subTypeName + '\'' +
                ", subTagName='" + subTagName + '\'' +
                ", showType='" + showType + '\'' +
                ", selectType='" + selectType + '\'' +
                ", comment='" + comment + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
