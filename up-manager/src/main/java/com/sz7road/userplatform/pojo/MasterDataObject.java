package com.sz7road.userplatform.pojo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-8
 * Time: 下午5:37
 * 存放从主数据系统里面拿到的信息
 */
public class MasterDataObject implements Serializable {
    // 数据信息
    private Object data;

    //错误信息
    private String err;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
