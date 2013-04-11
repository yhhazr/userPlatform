package com.sz7road.userplatform.pojo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-28
 * Time: 下午2:15
 * 放json数据
 */
public class LoseOrderJson<T> {
    private List<T> loseOrders;
    
    private int records;
    
    private int    page;
    
    private int total;


    public List<T> getLoseOrders() {
        return loseOrders;
    }

    public void setLoseOrders(List<T> loseOrders) {
        this.loseOrders = loseOrders;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }
}
