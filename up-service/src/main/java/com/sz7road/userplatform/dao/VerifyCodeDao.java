package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.VerifyCode;

import java.util.List;

/**
 * @author leo.liao
 */


public interface VerifyCodeDao extends Dao<VerifyCode> {

    List<VerifyCode> getByVerify(String verify);

    int deleteByExpiryTime(long expiryTime);

}
