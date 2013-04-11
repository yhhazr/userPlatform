/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeremy
 */
public abstract class GenericPayHandler extends Injection implements PayHandler {

    private static final Logger log = LoggerFactory.getLogger(GenericPayHandler.class.getName());

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    protected String getServerName(PayBean bean) {
        try {
            final String serverName = serverDataServiceProvider.get().get(bean.getGameId(),bean.getGameZoneId()).getServerName();
            if (!Strings.isNullOrEmpty(serverName)) {
                return serverName;
            }
        } catch (final Exception e) {
            log.error("获取游戏区名称异常：{}", e.getMessage());
        }
        return ConfigurationUtils.get("pay.common.description");
    }



    protected String getProductName(PayBean bean){
        try {
            final GameWithServerService gwsService = getInstance(GameWithServerService.class);
            final GameTable gameTable = gwsService.getGameTable();
            final String gameName = gameTable.get(bean.getGameId()).getGameName();
            final ServerDataService serverDataService = serverDataServiceProvider.get();
            final ServerInfo serverInfo = serverDataService.get(bean.getGameId(),bean.getGameZoneId());
            if (serverInfo != null) {
                return gameName + " " + serverInfo.getServerName();
            }
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
        return gatewayDomain + ConfigurationUtils.get("gateway.assertUri");
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
        return String.format(ConfigurationUtils.get("pay.common.result.pageUrl"), order);
    }
}
