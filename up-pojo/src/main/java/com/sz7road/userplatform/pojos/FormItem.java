package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-22
 * Time: 下午5:00
 * 存放表单的项空值判断的实体
 */
public class FormItem implements Serializable {
    /**
     * 返回的消息码
     */
    private int code;
    /**
     * 返回的消息
     */
    private String msg;
    /**
     * 验证的表单项的名称
     */
    private String itemName;
    /**
     * 验证表单项接收的值
     */
    private String itemValue;


    public FormItem(String itemName, String itemValue, String msg, int code) {
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.msg = msg;
        this.code = code;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
