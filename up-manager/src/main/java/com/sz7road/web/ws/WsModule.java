package com.sz7road.web.ws;

import com.google.inject.servlet.ServletModule;
import com.sz7road.web.action.EnterGameServerListServlet;
import com.sz7road.web.ws.filter.WsFilterModule;

/**
 * User: leo.liao
 * Date: 12-6-19
 * Time: 下午4:52
 */
public class WsModule extends ServletModule {

    @Override
    protected void configureServlets() {
        install(new WsFilterModule());
        serve("/getEnterGameServerListNotForSq").with(EnterGameServerListNotForSqServlet.class);
        serve("/ServerMaintainNotForSq").with(ServerMaintainForMultiGameServlet.class);
        serve("/GameWithServerNotForSq").with(GameWithServerForMultiGameServlet.class);
        serve("/GameWithServer").with(GameWithServerServlet.class);
        serve("/RechargeTest").with(RechargeTestServlet.class);
    }

}
