package com.sz7road.userplatform.pojos;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-10
 * Time: 下午5:00
 * jqgrid的数据实体
 */
public class RowObject {
    //实体的ID
    private Object id;
    //实体的类型
    private Object cell;

    public Object getCell() {
        return cell;
    }

    public void setCell(Object cell) {
        this.cell = cell;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
