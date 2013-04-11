/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay.alipay;

import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pay.GenericPayBean;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.userplatform.service.PayService;

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
    AlipayPayBean(PayManager manager, PayService payService) {
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
