package com.sz7road.userplatform.pay.w99bill;

import com.sz7road.userplatform.pay.GenericPayBean;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.userplatform.service.PayService;

import javax.inject.Inject;

/**
 * 只做针对于99bill所提交的订单进行验证处理。
 *
 * @author jeremy
 */
public class W99billPayBean extends GenericPayBean {

    /**
     * Constructs with google-guice.
     *
     * @param manager the manager facede for pay
     */
    @Inject
    protected W99billPayBean(PayManager manager, PayService payService) {
        super(manager, payService);
    }

    @Override
    public boolean isAvailableForSubmit() {
        boolean flag = super.isAvailableForSubmit();
        flag = flag && isValidSubTag();
        return flag;
    }

    public boolean isValidSubTag() {
        final String subTag = getSubTag();
        return true;
    }
}
