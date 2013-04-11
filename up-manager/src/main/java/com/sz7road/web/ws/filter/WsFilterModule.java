package com.sz7road.web.ws.filter;

import com.google.inject.servlet.ServletModule;
import com.sz7road.userplatform.ws.filter.EncodingFilter;

/**
 * User: leo.liao
 * Date: 12-6-15
 * Time: 下午5:46
 */
public class WsFilterModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // First to filtering by IP.
        filter("/*").through(EncodingFilter.class);
        filter("/GameWithServer", "/getRoleRankUrl", "/MaintainServer","/getEnterGameServerList","/getEnterGameServerList","/GameWithServerNotForSq","/ServerMaintainNotForSq").through(GameServerIpFilter.class);
    }
}
