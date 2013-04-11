package com.sz7road.userplatform.pojos;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.io.Serializable;

/**
 * @author leo.liao
 */

@JsonAutoDetect(JsonMethod.GETTER)
public class VerifyCode implements Serializable {
    private int id;
    private String verify;
    private String code;
    private long expiryTime;

    public VerifyCode() {

    }

    public VerifyCode(String verify, String code) {
        this.verify = verify;
        this.code = code;
    }

    public VerifyCode(String verify, String code, long expiryTime) {
        this.verify = verify;
        this.code = code;
        this.expiryTime = expiryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

}
