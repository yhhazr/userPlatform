package com.sz7road.userplatform.pojo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-11
 * Time: 上午9:37
 * 存放json游戏排行信息实体
 */
public class RankObject {
    //规则
//    private String rule;
    //数据
    private List<DataObject> data;
    //数据总数
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataObject> getData() {
        return data;
    }

    public void setData(List<DataObject> data) {
        this.data = data;
    }

//    public String getRule() {
//        return rule;
//    }
//
//    public void setRule(String rule) {
//        this.rule = rule;
//    }


}
