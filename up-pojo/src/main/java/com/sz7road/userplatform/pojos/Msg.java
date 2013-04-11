/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.io.Serializable;

/**
 * @author leo.liao
 */

@JsonAutoDetect(JsonMethod.GETTER)
public class Msg implements Serializable {

    private int code = 0;
    private String msg;
    private Object object;

    public Msg() {

    }

    public Msg(int code) {
        this.code = code;
    }

    public Msg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void build(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void build(int code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.object = obj;
    }
}
