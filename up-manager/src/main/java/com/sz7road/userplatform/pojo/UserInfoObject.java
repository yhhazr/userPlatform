package com.sz7road.userplatform.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-20
 * Time: 上午9:47
 * 存放用户信息的实体，
 * HashMap懒得记名称
 */
public class UserInfoObject implements Serializable {
    /**
     * 用户标识ID
     */
    private int id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户帐号的状态
     * 数据表中是int，这里在程序中转换成字符串，卸掉了客户端的鸭梨
     */
    private String status;
    /**
     * 累计充值RMB
     */
    private int aggrRecharge;
    /**
     * 创建日期
     */
    private Timestamp createTime;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 最后登录IP
     */
    private String lastIp;
    /**
     * 最后登录时间
     */
    private Timestamp lastLoginTime;
    /**
     * 最后登录的游戏名称
     * 这里做下转换，把ID转换成名称
     */
    private String lastGameId;
    /**
     * 最后登录的服务器名称
     * 这里做下转换，把ID转换成名称
     */
    private String lastGameZoneId;
    /**
     * 登录次数
     */
    private int loginSum;
    /**
     * 注册渠道标识
     */
    private String site;
    /**
     * 性别
     * 库里放的是整型，这里我打算在程序中转换 也是为了卸去js的鸭梨
     */

    private String gender;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 城市
     */
    private String city;
    /**
     * 职业
     */
    private String career;
    /**
     * 安全级别
     */
    private int safeLevel;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAggrRecharge() {
        return aggrRecharge;
    }

    public void setAggrRecharge(int aggrRecharge) {
        this.aggrRecharge = aggrRecharge;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastGameId() {
        return lastGameId;
    }

    public void setLastGameId(String lastGameId) {
        this.lastGameId = lastGameId;
    }

    public String getLastGameZoneId() {
        return lastGameZoneId;
    }

    public void setLastGameZoneId(String lastGameZoneId) {
        this.lastGameZoneId = lastGameZoneId;
    }

    public int getLoginSum() {
        return loginSum;
    }

    public void setLoginSum(int loginSum) {
        this.loginSum = loginSum;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getSafeLevel() {
        return safeLevel;
    }

    public void setSafeLevel(int safeLevel) {
        this.safeLevel = safeLevel;
    }


}
