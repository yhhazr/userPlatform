package com.sz7road.userplatform.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: leo.liao
 * Date: 12-7-27
 * Time: 下午3:47
 */
public class ChargeRsp implements Serializable {
    
    private String message;
    
    private int amount;

    private String orderId;

    public ChargeRsp(){

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
