/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.utils;

import org.apache.commons.mail.HtmlEmail;

/**
 * @author leo.liao 邮件发送信息包装
 */
public class MailMsg {

    private long id;
    /**
     * 发送服务器名称
     */
    private String hostName;
    /**
     * 邮件来自的emal
     */
    private String mailFrom;
    /**
     * 发送人的名称
     */
    private String nameFrom;
    /**
     * 发送的邮件的地址
     */
    private String mailTo;
    /**
     * 接收的人的名称
     */
    private String nameTo;
    /**
     * 主题
     */
    private String subJect;
    /**
     * html信息
     */
    private String htmlMsg;
    /**
     * 文本信息
     */
    private String textMsg;
    /**
     * 发送服务器的验证的名字
     */
    private String authName;
    /**
     * 发送的服务器的验证密码
     */
    private String authPwd;
    /**
     * 是否已经发送
     */
    private boolean hasSend;
    /**
     * 发送时候出错
     */
    private boolean sendErr;

    public boolean isSendErr() {
        return sendErr;
    }

    public void setSendErr(boolean sendErr) {
        this.sendErr = sendErr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isHasSend() {
        return hasSend;
    }

    public void setHasSend(boolean hasSend) {
        this.hasSend = hasSend;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    public String getSubJect() {
        return subJect;
    }

    public void setSubJect(String subJect) {
        this.subJect = subJect;
    }

    public String getHtmlMsg() {
        return htmlMsg;
    }

    public void setHtmlMsg(String htmlMsg) {
        this.htmlMsg = htmlMsg;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthPwd() {
        return authPwd;
    }

    public void setAuthPwd(String authPwd) {
        this.authPwd = authPwd;
    }

    public void sendMail(MailMsg mailMsg) throws Exception {
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setHostName(mailMsg.getHostName());
        email.setAuthentication(mailMsg.getAuthName(), mailMsg.getAuthPwd());
        email.addTo(mailMsg.getMailTo(), mailMsg.getNameTo());
        email.setFrom(mailMsg.getMailFrom(), mailMsg.getNameFrom());
        email.addBcc(mailMsg.getMailFrom(), mailMsg.getNameFrom());
        email.setCharset("UTF-8");
        email.setSubject(mailMsg.getSubJect());
        email.setHtmlMsg(mailMsg.getHtmlMsg());
        email.setTextMsg("Your email client does not support HTML messages");
        email.send();
    }

    public static void main(String[] args) throws Exception {

        MailMsg c = new MailMsg();

        c.setHostName("smtp.163.com");
        c.setAuthName("fuck@163.com");
        c.setAuthPwd("hamleo24");
        c.setNameTo("fuck");
        c.setMailTo("673529171@qq.com");
        c.setMailFrom("liaopng@163.com");
        c.setNameFrom("liaopng");
        c.setSubJect("今晚很happy");
        c.setHtmlMsg("大家好才是真的好！");

        c.sendMail(c);
    }
}
