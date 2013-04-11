package com.sz7road.utils;

import java.util.List;

/**
 * User: leo.liao
 * Date: 12-6-13
 * Time: 下午2:22
 */
public class ListData<T> {

    private List<T> list;

    private int total;

    public ListData(List<T> list, int total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
