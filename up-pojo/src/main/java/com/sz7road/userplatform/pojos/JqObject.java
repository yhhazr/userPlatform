package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-10
 * Time: 下午4:57
 * jqgrid实体整体模型
 */
public class JqObject implements Serializable {
    /**
     * 第几页
     */
    private int page;
    /**
     * 总页数
     */
    private int total;
    /**
     * 总条数
     */
    private int records;
    /**
     * 返回页的数据
     */
    private List<RowObject> rows;
    /**
     * 附加的信息
     */
    private Object ext;

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

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

    public List<RowObject> getRows() {
        return rows;
    }

    public void setRows(List<RowObject> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
