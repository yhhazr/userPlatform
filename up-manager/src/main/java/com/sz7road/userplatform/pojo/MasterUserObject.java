package com.sz7road.userplatform.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-8
 * Time: 下午6:30
 * 从主数据系统中拿到的用户信息
 */
public class MasterUserObject implements Serializable {

    private int user_id;

    private String user_name;

    private String user_password;

    private String user_comment;

    private String user_email;

    private String user_mobile;

    private int user_status;

    private Timestamp user_created_date;

    private Timestamp pwd_lastmodify_date;

    private String flag;

    private int system_type;

    private Object allow_ips;

    private Object configs;

    private Object __pConfigTable;

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public int getUser_status() {
        return user_status;
    }

    public Timestamp getUser_created_date() {
        return user_created_date;
    }

    public Timestamp getPwd_lastmodify_date() {
        return pwd_lastmodify_date;
    }

    public String getFlag() {
        return flag;
    }

    public int getSystem_type() {
        return system_type;
    }

    public Object getAllow_ips() {
        return allow_ips;
    }

    public Object getConfigs() {
        return configs;
    }

    public Object get__pConfigTable() {
        return __pConfigTable;
    }
}



