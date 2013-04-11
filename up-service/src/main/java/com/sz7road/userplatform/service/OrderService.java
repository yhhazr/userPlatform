/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.OrderDao;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.userplatform.pojos.OrderObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author jeremy
 */
@Singleton
public class OrderService extends Injection {

    /**
     * Logger
     */
    static final Logger log = LoggerFactory.getLogger(OrderService.class.getName());

    /**
     * Constructs by google-guice.
     */
    @Inject
    private OrderService() {
        super();
    }

    @Inject
    private PayManager manager;

    /**
     * 生成下一个订单号。
     *
     * @param channel      充值渠道合作方ID
     * @param subChannelId 充值子渠道ID
     * @return 下一订单号
     */
    public final String nextOrder(char channel, int subChannelId) {
        return manager.nextOrder(channel);
    }

    /**
     * 校验指定的<code>channelId</code>是否为有效的充值渠道。
     *
     * @param channelId    充值渠道ID
     * @param subChannelId
     * @return true 为有效渠道，false反之
     */
    public boolean isValidChannel(char channelId, int subChannelId) {
        return manager.get(channelId, subChannelId) != null;
    }

    /**
     * 通过订单号<code>orderId</code>查找对应的充值信息。
     *
     * @param orderId 订单号
     * @return 充值订单信息
     */
    public OrderObject findByOrderId(String orderId) {
        if (!Strings.isNullOrEmpty(orderId)) {
            final OrderDao orderDao = getInstance(OrderDao.class);
            try {
                return orderDao.getByOrderId(orderId);
            } catch (final Exception e) {
                log.error("获取充值订单数据异常：{}", e.getMessage());
            }
        }
        return null;
    }
}
