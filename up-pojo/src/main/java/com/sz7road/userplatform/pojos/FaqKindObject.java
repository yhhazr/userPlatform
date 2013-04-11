package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-2
 * Time: 上午10:51
 * 客服问题分类对象
 */
public class FaqKindObject implements Serializable {
    /**
     * 主键标识ID
     */
    private int id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型描述
     */
    private String ext;
    /**
     * 排序号
     */
    private int sortNo;
    /**
     * 父ID
     */
    private int parentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
