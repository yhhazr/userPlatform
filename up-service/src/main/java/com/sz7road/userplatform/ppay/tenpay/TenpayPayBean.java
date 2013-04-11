/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.tenpay;

import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.ppay.GenericPayBean;
import com.sz7road.userplatform.ppay.PayManager;
import com.sz7road.userplatform.service.ProductPayService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-20
 * Time: 上午3:59
 * Description: 财付通充值对象泛型
 */
@RequestScoped
public class TenpayPayBean extends GenericPayBean {

    /**
     * Constructs with google-guice.
     *
     * @param manager the manager facede for pay
     */
    @Inject
    TenpayPayBean(PayManager manager, ProductPayService payService) {
        super(manager, payService);
    }

    @Inject
    @Override
    protected void validate(HttpServletRequest request) {
        super.validate(request);
    }
}
