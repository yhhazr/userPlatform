package com.sz7road.userplatform.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-18
 * <p/>
 * Time: 下午3:41
 * 数据统计返回的定制json数据格式
 */
public class DataStatisticalObject implements Serializable {
    //返回标识
    private String message;
    //整体信息
    private String overAll;
    //放表头信息
    private Object head;
    //放表的内容信息
    private Object dataContent;
    //存放记录的条数
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getHead() {
        return head;
    }

    public void setHead(Object head) {
        this.head = head;
    }

    public Object getDataContent() {
        return dataContent;
    }

    public void setDataContent(Object dataContent) {
        this.dataContent = dataContent;
    }

    public String getOverAll() {
        return overAll;
    }

    public void setOverAll(String overAll) {
        this.overAll = overAll;
    }
}
