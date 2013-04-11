/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.ChargeOrderLogDao;
import com.sz7road.userplatform.dao.OrderDao;
import com.sz7road.userplatform.dao.PayTableDao;
import com.sz7road.userplatform.pay.PayBean;
import com.sz7road.userplatform.pay.PayException;
import com.sz7road.userplatform.pay.PayManager;
import com.sz7road.userplatform.pay.game.GameRechargeHandler;
import com.sz7road.userplatform.pay.game.GameRechargeManager;
import com.sz7road.userplatform.pojos.*;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.Backend;
import com.sz7road.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jeremy
 */
@Singleton
public class PayService extends Injection {

    /**
     * Logger
     */
    static final Logger log = LoggerFactory.getLogger(PayService.class.getName());

    private final ScheduledExecutorService rechargeTaskExec = Executors.newScheduledThreadPool(1 << 6, new ThreadFactory() {
        final String _NAME = "RECHARGE-RUNNER";
        volatile int INDEX = 0;

        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r, _NAME + "-" + (INDEX++));
        }
    });

    private final PayManager manager;
    private final UserService userService;
    private final ReentrantLock lock = new ReentrantLock();
    private PayTable payTable;

    @Inject
    private PayService(final PayManager manager, final UserService userService) {
        super();
        this.manager = manager;
        this.userService = userService;
    }

    /**
     * Retrieves the manager.
     *
     * @return manager for pay
     */
    PayManager getManager() {
        return manager;
    }

    @Inject
    void initialized(ConfigurationService configService) {
        configService.addListener(new ConfigurationService.ConfigurationUpdateListener() {

            @Override
            public void onUpdate(ConfigurationService service) {
                lock.lock();
                try {
                    payTable = null;
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    /**
     * 提交支付打单。
     *
     * @param bean 支付模型
     * @return null提交失败，或提交成功返回order对象
     * @throws PayException
     */
    public OrderObject submitOrder(final PayBean bean) throws PayException {
        if (null == bean) throw new NullPointerException("为空的支付模型！");
        final UserAccount account = userService.findAccountByUserName(bean.getUserName());

        return submitOrder(account, bean);
    }

    /**
     * 提交支付订单。
     *
     * @param account 支付的账号
     * @param bean    支付信息
     * @return null提交失败，或提交成功返回order对象
     * @throws PayException
     */
    public OrderObject submitOrder(final UserAccount account, final PayBean bean) throws PayException {
        if (null == bean) {
            throw new NullPointerException("为空的支付模型！");
        }

        if (null == account || account.getId() == 0) {
            log.error("无效的支付账号或支付的用户ID不应该为0！");
            return null;
        }

        final OrderDao orderDao = getInstance(OrderDao.class);
        final OrderObject order = new OrderObject();
        final PayTable payTable = getPayTable();
        final GameTable gameTable = getInstance(GameWithServerService.class).getGameTable();

        if (null == payTable) {
            throw new IllegalStateException("没有配置充值配置表？");
        }

        final PayTable.PayEntry payEntry = payTable.get(PayTable.getKey(String.valueOf(bean.getChannelId()), bean.getSubTypeId(), bean.getSubTag()));

        if (null == payEntry || payEntry.getScale() <= 0) {
            throw new IllegalStateException("没有配置充值配置表？无效的充值比例配置？");
        }

        if (null == gameTable) {
            throw new IllegalStateException("没有配置游戏信息表？");
        }

        final GameTable.GameEntry gameEntry = gameTable.get(bean.getGameId());

        if (null == gameEntry) {
            throw new IllegalStateException("没有配置游戏信息表？没有找到ＩＤ为：" + bean.getGameId() + "的配置信息。");
        }

        final int goldScale = gameEntry.getGoldScale();

        if (goldScale <= 0) {
            throw new IllegalStateException("无效游戏兑换比例值：" + goldScale);
        }

        order.setId(bean.getOrderId());
        order.setChannelId(String.valueOf(bean.getChannelId()));
        order.setSubType(bean.getSubTypeId());
        order.setSubTag(bean.getSubTag());
        order.setAmount(bean.getPayAmount());
        order.setGold((int) bean.getPayAmount() * payEntry.getScale() * goldScale / 10);
        order.setRechargeAmount(order.getGold() / goldScale);
        order.setStatus((byte) 0);
        order.setCurrency((byte) 0);
        order.setUserId(account.getId());
        order.setPlayerId(bean.getPlayerId());
        order.setGameId(bean.getGameId());
        order.setZoneId(bean.getGameZoneId());
        order.setPayTime(bean.getTime());
        order.setAssertTime(null);
        order.setEndOrder(null);
        order.setClientIp(bean.getClientIp());
        order.setExt1(null);
        order.setExt2(null);
        order.setExt3(null);

        try {
            if (orderDao.add(order) > 0) {
                return order;
            }
        } catch (final Exception e) {
            log.error("提交订单模型到数据库异常：{}", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 确认订单状态。
     *
     * @param order 订单对象
     * @return true 如果该订单被确认为已成功关闭
     */
    public boolean assertOrder(final OrderObject order) {
        if (null == order) throw new IllegalArgumentException("无效订单对象！");

        try {
            // Set to update the order.
            order.setStatus((byte) 1);
            //write to log file
            log.info("订单成功:{}", order);

            final OrderDao orderDao = getInstance(OrderDao.class);
            return orderDao.update(order) > 0;
        } catch (final SQLException e) {
            log.error("更新订单确认数据SQL异常：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取订单对象。
     *
     * @param order 订单号
     * @return 订单记录
     */
    public OrderObject getOrder(String order) {
        if (Strings.isNullOrEmpty(order)) {
            throw new IllegalArgumentException("无效订单号");
        }

        try {
            final OrderDao orderDao = getInstance(OrderDao.class);
            return orderDao.getByOrderId(order);
        } catch (final SQLException e) {
            log.error("获取订单对象SQL异常：{}", e.getMessage());
        } catch (final Exception e) {
            log.error("获取订单对象常规异常：{}", e.getMessage());
        } finally {
        }
        return null;
    }

    /**
     * 检查该订单是否已经成功关闭。
     *
     * @param order 订单对象
     * @return true表示已经成功关闭
     */
    public boolean isSuccess(OrderObject order) {
        return order.getStatus() == OrderObject.PAY_SUCCESS;
    }

    /**
     * 检查该订单是否在等待关闭。
     *
     * @param order 订单对象
     * @return true表示在等待中
     */
    public boolean isWaitForClosed(OrderObject order) {
        return order.getStatus() == OrderObject.PAY_FAIL;
    }

    /**
     * 获取订单签名摘要串，用于支付认证。
     *
     * @param order 订单对象
     * @return 签名摘要串
     */
    public String getOrderSign(final OrderObject order) {
        if (null != order) {
            final StringBuilder sb = new StringBuilder();
            sb.append(order.getId());
            sb.append(order.getGameId());
            sb.append(order.getZoneId());
            sb.append(order.getUserId());
            sb.append(order.getPlayerId());
            sb.append(order.getAmount());
            sb.append((long) (order.getPayTime().getTime() / 1000));
            sb.append("jeremykeep");

            return MD5Utils.digestAsHex(sb.toString()).toUpperCase();
        }
        return null;
    }

    /**
     * 验证指定的摘要串<code>sign</code>是否是指定订单对象<code>order</code>的有效签名。
     *
     * @param order 订单对象
     * @param sign  签名
     * @return true or false
     */
    public boolean isValidOrderSign(final OrderObject order, final String sign) {
        return Strings.nullToEmpty(getOrderSign(order)).equals(sign.toUpperCase());
    }

    /**
     * 检验指定的充值渠道提交ＫＥＹ是否为有效的渠道指向。
     *
     * @param key 充值渠道提交ＫＥＹ
     * @return true or false
     */
    public boolean isValidPayEntryKey(PayTable.PayEntryKey key) {
        final PayTable payTable = getPayTable();
        return null != payTable && payTable.containsKey(key);
    }

    /**
     * 获取充值配置表。
     *
     * @return 充值配置表模型
     */
    public PayTable getPayTable() {
        if (null == payTable) {
            lock.lock();
            if (null == payTable) {
                try {
                    final PayTableDao payTableDao = getInstance(PayTableDao.class);
                    final List<PayTable.PayEntry> list = payTableDao.listAll();
                    if (null != list && !list.isEmpty()) {
                        payTable = new PayTable();

                        for (PayTable.PayEntry entry : list) {
                            PayTable.PayEntryKey key = PayTable.getKey(entry.getChannelId(), entry.getSubType(), entry.getSubTag());
                            payTable.put(key, entry);
                        }
                    }
                } catch (final Exception e) {
                    log.error("获取充值渠道配置表异常：{}", e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        }
        return payTable;
    }

    public Backend.BackendResponse rechargeGolds(final OrderObject order, final int index) {
        Backend.BackendResponse response = null;

        if (null != order) {
            final GameTable gameTable = getInstance(GameWithServerService.class).getGameTable();
            if (null != gameTable) {
                GameTable.GameEntry gameEntry = gameTable.get(order.getGameId());
                if (gameEntry == null) {
                    throw new IllegalStateException("无效的游戏");
                }

                ServerDataService serverDataService = getInstance(ServerDataService.class);
                final ServerInfo serverInfo = serverDataService.get(order.getGameId(), order.getZoneId());

                ServerInfo mainServerInfo = serverInfo;
                if (serverInfo.getMainId() != 0 && serverInfo.getMainId() != serverInfo.getServerId()) {
                    mainServerInfo = serverDataService.get(order.getGameId(), serverInfo.getMainId());
                }

                GameRechargeManager gameRechargeManager = getInstance(GameRechargeManager.class);
                final GameRechargeHandler gameRechargeHandler = gameRechargeManager.get(order.getGameId());

                response = gameRechargeHandler.charge(gameEntry, serverInfo, mainServerInfo, order);

                final int responseCode = gameRechargeHandler.getCode();
                final String responseContent = gameRechargeHandler.getContent();

                getInstance(ExecutorService.class).submit(new Runnable() {
                    @Override
                    public void run() {
                        // log the charge order.
                        try {
                            final ChargeOrderLogDao logDao = getInstance(ChargeOrderLogDao.class);
                            ChargeOrderLog log = new ChargeOrderLog();

                            log.setOrderId(order.getId());
                            log.setUserId(order.getUserId());
                            log.setPlayerId(order.getPlayerId());
                            log.setGameId(order.getGameId());
                            log.setGameZoneId(order.getZoneId());
                            log.setAmount(order.getAmount());
                            log.setGold(order.getGold());
                            log.setResponseState(responseCode);
                            log.setAssistResult(responseContent);
                            log.setPayTime(order.getPayTime());
                            log.setAssertTime(order.getAssertTime());
                            log.setCreateTime(new Timestamp(new Date().getTime()));

                            PayService.log.info("订单信息日志：{}", log);

                            logDao.add(log);
                        } catch (final SQLException e) {
                            PayService.log.error("记录游戏充值兑换日志SQL异常：{}", e.getMessage());
                        } catch (final Exception e) {
                            PayService.log.error("记录游戏充值兑换日志异常：{}", e.getMessage());
                        }
                    }
                });

                boolean rechargeNeeded = gameRechargeHandler.isRechargeNeed();
                if (rechargeNeeded && index > 0) {
                    rechargeTaskExec.schedule(new Runnable() {
                        @Override
                        public void run() {
                            rechargeGolds(order, index - 1);
                        }
                    }, 180, TimeUnit.SECONDS);

                    return response;
                }

            }
        }
        return response;
    }

    public Backend.BackendResponse rechargeGolds(final OrderObject order) {
        return rechargeGolds(order, 3);
    }
}
