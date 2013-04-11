package com.sz7road.userplatform.ppay;

import com.google.inject.AbstractModule;

/**
 * @author jeremy
 */
public class PayModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(PayLocatorBean.class).to(GenericPayLocatorBean.class);
        bind(PayBean.class).to(GenericPayBean.class);
    }
}
