package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-17
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */
public class SiteObject implements Serializable {
    //客服热线
    private String cs_telephonenum;
    //客服邮箱
    private String cs_complainmail;
    // 客服QQ群
    private List<String> cs_playqqgroup;

    public String getCs_complainmail() {
        return cs_complainmail;
    }

    public void setCs_complainmail(String cs_complainmail) {
        this.cs_complainmail = cs_complainmail;
    }

    public List<String> getCs_playqqgroup() {
        return cs_playqqgroup;
    }

    public void setCs_playqqgroup(List<String> cs_playqqgroup) {
        this.cs_playqqgroup = cs_playqqgroup;
    }

    public String getCs_telephonenum() {
        return cs_telephonenum;
    }

    public void setCs_telephonenum(String cs_telephonenum) {
        this.cs_telephonenum = cs_telephonenum;
    }
}
