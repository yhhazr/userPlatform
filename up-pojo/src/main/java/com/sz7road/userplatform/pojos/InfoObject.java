package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 上午10:39
 * 存放网站信息的实体
 */
public class InfoObject implements Serializable {
    /**
     * 标识ID
     */
    private int id;
    /**
     * key
     */
    private String name;
    /**
     * value
     */
    private String text;
    /**
     * description 描述
     */
    private String ext;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{id: " + id + " name: " + name + " text: " + text + " ext: " + ext + "}";
    }
}
