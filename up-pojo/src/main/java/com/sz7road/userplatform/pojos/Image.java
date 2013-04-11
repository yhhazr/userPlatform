package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-11
 * Time: 下午3:45
 * 图片对象实体
 */
public class Image implements Serializable {
    //显示路径
    private String path;
    //正式服务器路径
    private String realPath;
    //高度
    private int height;
    //宽度
    private int width;
    //扩展名
    private String fileExt;

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }
}
