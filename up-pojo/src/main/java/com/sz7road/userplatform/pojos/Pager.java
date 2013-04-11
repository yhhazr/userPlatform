package com.sz7road.userplatform.pojos;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Author: jiangfan.zhou
 */
public class Pager<T> implements Serializable {
    // 第几页
    private int page;
    // 总共页数
    private int total;
    // 表中所有记录数
    private int records;
    // 显示记录集合
    private List<T> rows;
    @JsonIgnore
    private int pageSize = 10;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        if (records > 0) {
            total = (records / pageSize) + (records % pageSize == 0 ? 0 : 1);
        }
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
