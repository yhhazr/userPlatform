package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 上午9:31
 * 下拉菜单的选择项实体
 */
public class Item implements Serializable {

    //显示的文本
    private String label;
    //选择项的值
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
