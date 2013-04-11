package com.sz7road.web.pojos;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-10
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class UserExt extends User {
    //头像的路径
    private String headDir;

    public String getHeadDir() {
        return headDir;
    }

    public void setHeadDir(String headDir) {
        this.headDir = headDir;
    }
}
