/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author jeremy
 */
@JsonAutoDetect(JsonMethod.GETTER)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserObject implements Serializable {

    @JsonIgnore
    private UserAccount account;
    private String realName;
    private String icn;
    private Timestamp createTime;
    private long aggrRecharge = 0;
    private String lastIp;
    private Timestamp lastLoginTime;//最后登陆时间
    private int lastGameId;
    private int lastGameZoneId;
    private int loginSum = 0;
    private String site;
    private byte gender;
    private Date birthday;
    private String city;
    private String career;
    private byte safeLevel;//安全级别
    private String mobile;
    private byte messageCount;//短信发送次数/天
    private Timestamp lastMessageTime;//最后短信发送时间
    //--------------------------------------新增的用户资料字段-----------------------------------------
    private String headDir; //头像的路径
    private int pswStrength;                  // 密码等级
    private String workPost;                   //  职位
    private String companyName;               // 单位名称
    private String schoolName;                // 学校名称
    private String selfIntroduction;          // 自我介绍
    private String hobby;                     // 爱好
    private String linkPhone;                 // 联系电话
    private String msn;                       // MSN
    private String qq;                        // QQ号码
    private String nickName;                 //  昵称

    private int schoolType;                // 学校类型
    private int eduLevel;                  // 教育程度
    private int marryStatus;               // 婚姻状况
     private int workEndYear;                // 工作结束年份
    private int workStartYear;             // 工作开始年份
     private int startEduYear;              // 入学年份
    public String getHeadDir() {
        return headDir;
    }

    public void setHeadDir(String headDir) {
        this.headDir = headDir;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }



    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPswStrength() {
        return pswStrength;
    }

    public void setPswStrength(int pswStrength) {
        this.pswStrength = pswStrength;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }



    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public int getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(int schoolType) {
        this.schoolType = schoolType;
    }

    public int getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(int eduLevel) {
        this.eduLevel = eduLevel;
    }

    public int getMarryStatus() {
        return marryStatus;
    }

    public void setMarryStatus(int marryStatus) {
        this.marryStatus = marryStatus;
    }

    public int getWorkEndYear() {
        return workEndYear;
    }

    public void setWorkEndYear(int workEndYear) {
        this.workEndYear = workEndYear;
    }

    public int getWorkStartYear() {
        return workStartYear;
    }

    public void setWorkStartYear(int workStartYear) {
        this.workStartYear = workStartYear;
    }

    public int getStartEduYear() {
        return startEduYear;
    }

    public void setStartEduYear(int startEduYear) {
        this.startEduYear = startEduYear;
    }

    public String getWorkPost() {
        return workPost;
    }

    public void setWorkPost(String workPost) {
        this.workPost = workPost;
    }



    /**
     * Constructs by empty.
     */
    public UserObject() {
        super();
    }

    public UserAccount getAccount() {
        if (null == account) account = new UserAccount();
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public int getId() {
        return getAccount().getId();
    }

    public void setId(int id) {
        getAccount().setId(id);
    }

    public String getUserName() {
        return getAccount().getUserName();
    }

    public void setUserName(String userName) {
        getAccount().setUserName(userName);
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIcn() {
        return icn;
    }

    public void setIcn(String icn) {
        this.icn = icn;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getAggrRecharge() {
        return aggrRecharge;
    }

    public void setAggrRecharge(long aggrRecharge) {
        this.aggrRecharge = aggrRecharge;
    }

    public String getEmail() {
        return getAccount().getEmail();
    }

    public void setEmail(String email) {
        getAccount().setEmail(email);
    }

    public byte getStatus() {
        return getAccount().getStatus();
    }

    public void setStatus(byte status) {
        getAccount().setStatus(status);
    }

    public byte getSafeLevel() {
        return safeLevel;
    }

    public void setSafeLevel(byte safeLevel) {
        this.safeLevel = safeLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getLoginSum() {
        return loginSum;
    }

    public void setLoginSum(int loginSum) {
        this.loginSum = loginSum;
    }

    public int getLastGameId() {
        return lastGameId;
    }

    public void setLastGameId(int lastGameId) {
        this.lastGameId = lastGameId;
    }

    public int getLastGameZoneId() {
        return lastGameZoneId;
    }

    public void setLastGameZoneId(int lastGameZoneId) {
        this.lastGameZoneId = lastGameZoneId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public byte getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(byte messageCount) {
        this.messageCount = messageCount;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
