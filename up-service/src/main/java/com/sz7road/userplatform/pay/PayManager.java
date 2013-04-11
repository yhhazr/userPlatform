/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay;

import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.sz7road.userplatform.pay.alipay.AlipayHandler;
import com.sz7road.userplatform.pay.tenpay.TenpayHandler;
import com.sz7road.userplatform.pay.w99bill.W99billCCardHandler;
import com.sz7road.userplatform.pay.w99bill.W99billHandler;
import com.sz7road.userplatform.pay.yeepay.YeepayPayHandler;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jeremy
 */
@Singleton
public final class PayManager extends AbstractPayFacade {

    public static final String INSTANCE_KEY = "instance.id";
    private static final String PAY_CHANNEL_FILTER_KEY = "pay.channel.filter";

    private static final Logger log = LoggerFactory.getLogger(PayManager.class.getName());

    private final AtomicInteger id = new AtomicInteger(1);
    private final ReentrantLock lock = new ReentrantLock();

    @Inject
    private PayManager(final Injector injector) {
        super();
        //财付通
        add(hash('B', 0), injector.getProvider(TenpayHandler.class));
        add(hash('B', 1), injector.getProvider(TenpayHandler.class));
        /*----------------------------------------------------------------------*/
        add(hash('A', 0), injector.getProvider(AlipayHandler.class));
        add(hash('A', 1), injector.getProvider(AlipayHandler.class));
        add(hash('C', 1), injector.getProvider(YeepayPayHandler.class));
        add(hash('C', 3), injector.getProvider(YeepayPayHandler.class));
        add(hash('C', 4), injector.getProvider(YeepayPayHandler.class));
        add(hash('D', 1), injector.getProvider(W99billHandler.class));
        add(hash('D', 3), injector.getProvider(W99billCCardHandler.class));
        //add(hash('D', 4), injector.getProvider(W99billGCardHandler.class));
    }

    private char getInstanceIdValue() {
        final String instanceIdValue = ConfigurationUtils.get(INSTANCE_KEY);
        if (!Strings.isNullOrEmpty(instanceIdValue)) {
            return instanceIdValue.toUpperCase().charAt(0);
        }
        throw new IllegalStateException("没有配置运行实例的ID！");
    }

    @Override
    public PayHandler get(final HttpServletRequest request) {
        final PayHandler handler = super.get(request);
        if (null == handler) {
            log.warn("未能找到充值渠道的处理程序，类型编号：{}", hash(request));
        }
        return handler;
    }

    public PayHandler get(char channel, int subChannel) {
        return get(hash(channel, subChannel));
    }

    /**
     * 生成下一个ID，取值范围在1～9999。
     *
     * @return the next id
     */
    public final int nextId() {
        lock.lock();
        try {
            if (id.get() == 99999)
            {
                id.set(1);
            }
            return id.getAndIncrement();
        } finally {
            lock.unlock();
        }
    }

    public final String nextOrder(char channel) {
        String a = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return String.format("%c%s%c%05d", channel, a, getInstanceIdValue(), nextId());
    }
}
