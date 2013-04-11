package com.sz7road.userplatform.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-23
 * Time: 上午10:03
 * 用户资料修改日志实体
 */
public class OperationLog implements Serializable {
    //标识ID
    private int id;
    //日志类型
    private int logType;
    //记录时间
    private Timestamp log_time;
    //用户名
    private String userName;
    //内容
    private String content;
    //扩展1
    private String ext1;
    //扩展2
    private String ext2;
    //扩展3
    private String ext3;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getLog_time() {
        return log_time;
    }

    public void setLog_time(Timestamp log_time) {
        this.log_time = log_time;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
