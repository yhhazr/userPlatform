package com.sz7road.userplatform.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-29
 * Time: 下午2:30
 * 扩展实体
 */
public class OrderViewExtObject extends OrderViewObject {
    //fail返回true，表示要补单，success返回false，标识不要补单
    private boolean loseOrNot=false;

    public boolean isLoseOrNot() {
        return loseOrNot;
    }

    public void setLoseOrNot(boolean loseOrNot) {
        this.loseOrNot = loseOrNot;
    }
}
