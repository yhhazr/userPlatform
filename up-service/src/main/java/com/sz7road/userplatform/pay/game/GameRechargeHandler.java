package com.sz7road.userplatform.pay.game;

import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.Backend;

public interface GameRechargeHandler {

    /**
     * 游戏充值(指定游戏指定订单发放金币)
     * @param gameEntry
     * @param serverInfo
     * @param mainServerInfo
     * @param order
     */
    Backend.BackendResponse charge(GameTable.GameEntry gameEntry, ServerInfo serverInfo, ServerInfo mainServerInfo, OrderObject order);

    /**
     * 校验接口调用返回数据，对不成功的请求进行再次请求
     *
     * @return
     */
    boolean isRechargeNeed();

    /**
     * 请求返回状态码
     * @return
     */
    int getCode();

    /**
     * 请求返回内容
     * @return
     */
    String getContent();
}
