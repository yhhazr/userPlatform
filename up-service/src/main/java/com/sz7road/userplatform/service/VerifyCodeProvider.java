package com.sz7road.userplatform.service;

import com.sz7road.userplatform.pojos.VerifyCode;

import java.util.List;

/**
 * @author leo.liao
 */


public interface VerifyCodeProvider {

    int add(VerifyCode code);

    boolean checkVerifyCode(String verify, String code);

    void delete(final VerifyCode entity);

    List<VerifyCode> getByVerify(String verify);

    VerifyCode getVerifyCode(String verify, String code);
}
