package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author leo.liao
 */


public class Log implements Serializable {
    public enum LogType {
        ACCOUNT_UPDATE(1),//账户信息更新
        EMAIL_BIND(2),//邮箱绑定
        EMAIL_ACTIVATE(3),//邮箱激活
        QUESTION_BIND(4),//密保问题答案设置
        MOBILE_BIND(5),// 手机绑定
        IPEXCEPTION_RECORD(6);//登录IP异常

        private int code;

        private LogType(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static LogType valueOf(int code) {
            for (LogType type : values()) {
                if (type.code == code) return type;
            }

            throw new IllegalArgumentException("Illegal error code!");
        }
    }

    private int id;
    private LogType logType;
    private String userName;
    private Timestamp log_time;
    private String content;
    private String ext1;
    private String ext2;
    private String ext3;

    public Log() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getLog_time() {
        return log_time;
    }

    public void setLog_time(Timestamp log_time) {
        this.log_time = log_time;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
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

}
