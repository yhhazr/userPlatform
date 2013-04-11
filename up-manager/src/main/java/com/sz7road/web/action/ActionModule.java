package com.sz7road.web.action;

import com.google.inject.servlet.ServletModule;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 下午12:11
 */
public class ActionModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/user").with(UserServlet.class);
        serve("/login.html").with(InServlet.class);
        serve("/order").with(OrderServlet.class);
        serve("/initDropdownMenu").with(InitDropdownMenuServlet.class);
        serve("/index").with(IndexServlet.class);
        serve("/loseOrder").with(LoseOrderServlet.class);
        serve("/Recharge").with(ReChargeOrderServlet.class);
        serve("/getRoleRankUrl").with(getRoleRankURLServlet.class);
        serve("/MaintainServer").with(ServerMaintainServlet.class);
        serve("/DataStatistic").with(DataStatisticalServlet.class);
        serve("/faqKind").with(FaqKindServlet.class);
        serve("/master").with(LoginMasterDataServlet.class);
        serve("/csInfo").with(CsInfoServlet.class);
        serve("/faq").with(FaqServlet.class);
        serve("/merger").with(MergerServerServlet.class);
        serve("/appealManage").with(AppealManageServlet.class);
        serve("/showIdCardImg").with(ShowIdCardImgServlet.class);
        serve("/getEnterGameServerList").with(EnterGameServerListServlet.class);
        serve("/paySwitch").with(PaySwitchServlet.class);
        serve("/paySwitchType").with(PaySwitchTypeServlet.class);
    }
}
