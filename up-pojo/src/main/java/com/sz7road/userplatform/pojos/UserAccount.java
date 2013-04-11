/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jeremy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount implements Serializable {

    private int id;
    private String userName;
    @JsonIgnore
    private String passWord;
    private String email;
    private byte status = 0;

    public UserAccount() {
        super();
    }

    public UserAccount(final int id, final String userName) {
        super();
        this.id = id;
        this.userName = userName;
    }

    public UserAccount(final int id, final String userName, final String email) {
        this(id, userName);
        this.email = email;
    }

    public UserAccount(final int id, final String userName, final String email, final byte status) {
        this(id, userName, email);
        this.status = status;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public boolean withPassWord(String passWord) {
        if (!Strings.isNullOrEmpty(passWord)) {
            try {
                final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                final byte[] bytes = messageDigest.digest(passWord.getBytes());
                passWord = new String(bytes);

                if (passWord.equals(getPassWord())) {
                    return true;
                }
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return false;
    }
}
