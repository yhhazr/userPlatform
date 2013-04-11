package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.OrderDecorateDao;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.PayTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-29
 * Time: 上午11:05
 * 补单服务层
 */
@Singleton
public class ReChargeOrderService extends Injection{

    final static Logger LOGGER = LoggerFactory.getLogger(ReChargeOrderService.class.getName());

    @Inject
    private PayManager payManager;
    @Inject
    private PayService payService;

    public com.sz7road.utils.Backend.BackendResponse rechargeGolds(OrderObject order) {
        return payService.rechargeGolds(order);
    }

    public com.sz7road.utils.Backend.BackendResponse rechargeGolds(OrderObject order, int index) {
        return payService.rechargeGolds(order, index);
    }

    public OrderObject getOrder(String order) {
        return payService.getOrder(order);
    }
    
    public com.sz7road.utils.Backend.BackendResponse rechargeTest(OrderObject orderObject) {
        final OrderDecorateDao orderDao = getInstance(OrderDecorateDao.class);
        final PayTable payTable = payService.getPayTable();
        final GameTable gameTable = getInstance(GameWithServerDecorateService.class).getGameTable();

        if (null == payTable) {
            throw new IllegalStateException("没有配置充值配置表？");
        }

        final PayTable.PayEntry payEntry = payTable.get(PayTable.getKey(String.valueOf(orderObject.getChannelId()), orderObject.getSubType(), orderObject.getSubTag()));

        if (null == payEntry || payEntry.getScale() <= 0) {
            throw new IllegalStateException("没有配置充值配置表？无效的充值比例配置？");
        }

        if (null == gameTable) {
            throw new IllegalStateException("没有配置游戏信息表？");
        }

        final GameTable.GameEntry gameEntry = gameTable.get(orderObject.getGameId());

        if (null == gameEntry) {
            throw new IllegalStateException("没有配置游戏信息表？没有找到ＩＤ为：" + orderObject.getGameId() + "的配置信息。");
        }

        final int goldScale = gameEntry.getGoldScale();

        if (goldScale <= 0) {
            throw new IllegalStateException("无效游戏兑换比例值：" + goldScale);
        }
        orderObject.setId(payManager.nextOrder(orderObject.getChannelId().charAt(0)));
        orderObject.setGold((int) orderObject.getAmount() * payEntry.getScale() * goldScale / 10);
        orderObject.setRechargeAmount(orderObject.getGold() / 10);
        orderObject.setStatus((byte) 3);
        orderObject.setCurrency((byte) 0);
        orderObject.setPayTime(new Timestamp(System.currentTimeMillis()));
        orderObject.setAssertTime(null);
        orderObject.setEndOrder(null);
        orderObject.setExt1(null);
        orderObject.setExt2(null);
        orderObject.setExt3(null);
        try {
            int result = orderDao.add(orderObject);
            if(result > 0) {
                return rechargeGolds(orderObject,3);
            }
        }catch (Exception e) {
            LOGGER.error("充值测试出现异常：{}",e.getMessage());
        }
        return null;
    }
}
