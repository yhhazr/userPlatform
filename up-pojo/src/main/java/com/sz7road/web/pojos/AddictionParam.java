package com.sz7road.web.pojos;

import java.io.Serializable;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-14
 * Time: 上午2:46
 * Description:防沉迷注册的参数对象
 */
public class AddictionParam implements Serializable {

    public static final int QUERY_ADDICTION=1;

    public static final int ADD_ADDICTION=2;

    /**
     * 操作类型，
     1 ：查询玩家是否已注册了防沉迷
     2 ：注册防沉迷信息
     */
    private int op;
    /**
     * 用户id
     */
    private int userid;
    /**
     * 用户名
     */
     private String userName;
    /**
     * 玩家所在游戏区站点
     */
    private String site;
    /**
     * 身份证号码
     */
    private String cardId;
    /**
     * 身份证姓名
     */
    private String cardName;

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
