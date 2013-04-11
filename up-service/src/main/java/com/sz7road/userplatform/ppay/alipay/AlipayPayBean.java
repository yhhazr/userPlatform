/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.alipay;

import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.ppay.GenericPayBean;
import com.sz7road.userplatform.ppay.PayManager;
import com.sz7road.userplatform.service.ProductPayService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jeremy
 */
@RequestScoped
public class AlipayPayBean extends GenericPayBean {

    /**
     * Constructs with google-guice.
     *
     * @param manager the manager facede for pay
     */
    @Inject
    AlipayPayBean(PayManager manager, ProductPayService payService) {
        super(manager, payService);
    }

    @Inject
    @Override
    protected void validate(HttpServletRequest request) {
        super.validate(request);

        if (getSubTypeId() == 0) {
            // 余额充值。
            setSubTag("");
        }
    }
}
