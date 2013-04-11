/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.utils.ConfigurationUtils;

/**
 * @author jeremy
 */
public abstract class GenericPayHandler extends Injection implements PayHandler {

    private static final Logger log = LoggerFactory.getLogger(GenericPayHandler.class.getName());

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    protected String getProductName(PayBean bean){
        try {
            return bean.getProductName();
        } catch (final Exception e) {
            log.error("获取游戏区名称异常：{}", e.getMessage());
        }
        return ConfigurationUtils.get("pay.common.description");
    }

    public String getAssertUrl(String replace) {
        String gatewayDomain = ConfigurationUtils.get("gateway.domain");
        if (!Strings.isNullOrEmpty(replace)) {
            return gatewayDomain + replace;
        }
        return gatewayDomain + ConfigurationUtils.get("ppay.gateway.assertUri");
    }

    public String getAssertUrl() {
        return getAssertUrl("");
    }

    /**
     * 获取当前完整的充值确认接口地址。
     *
     * @param bean
     * @return
     */
    public String getAssertUrl(PayBean bean) {
        return getAssertUrl() + bean.getChannelTag();
    }

    public String getResultUrl(String order) {
        return String.format(ConfigurationUtils.get("ppay.common.result.pageUrl"), order);
    }
}
