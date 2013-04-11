package com.sz7road.userplatform.web.exception;

/**
 * @author leo.liao
 */


public class EnterGameException extends RuntimeException {

    private int code;

    private String msg;

    public EnterGameException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public EnterGameException(int code) {
        this(code, null);
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

    public void build(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
