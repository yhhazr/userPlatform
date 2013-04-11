/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * @author leo.liao
 */

public class Configuration implements Serializable {

    private String key;
    private String value;
    private String comment;

    public Configuration() {
        super();
    }


    public Configuration(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
