package com.sz7road.userplatform.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.ProductOrderDao;
import com.sz7road.userplatform.pojos.PayTable;
import com.sz7road.userplatform.pojos.ProductOrder;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.ppay.PayBean;
import com.sz7road.userplatform.ppay.PayException;
import com.sz7road.userplatform.ppay.PayManager;
import com.sz7road.utils.Backend;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;

/**
 * 商品支付服务
 * @author leo.liao
 *
 */
@Singleton
public class ProductPayService extends Injection
{
    /**
     * Logger
     */
    static final Logger log = LoggerFactory.getLogger(ProductPayService.class.getName());
    private final PayManager manager;
    private final UserService userService;

    @Inject
    private ProductPayService(final PayManager manager, final UserService userService) {
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

    public ProductOrder submitOrder(final PayBean bean) throws PayException {
        if (null == bean) throw new NullPointerException("为空的支付模型！");
        final UserAccount account = userService.findAccountByUserName(bean.getUserName());

        return submitOrder(account, bean);
    }

    public ProductOrder submitOrder(final UserAccount account, final PayBean bean) throws PayException {
        if (null == bean){
            throw new NullPointerException("为空的支付模型！");
        }
        if (null == account || account.getId() == 0) {
            log.error("无效的支付账号或支付的用户ID不应该为0！");
            return null;
        }
        final ProductOrderDao orderDao = getInstance(ProductOrderDao.class);
        final ProductOrder order = new ProductOrder();
        final PayTable payTable = getPayTable();

        if (null == payTable) {
            throw new IllegalStateException("没有配置充值配置表？");
        }

        final PayTable.PayEntry payEntry = payTable.get(PayTable.getKey(String.valueOf(bean.getChannelId()), bean.getSubTypeId(), bean.getSubTag()));

        if (null == payEntry || payEntry.getScale() <= 0) {
            throw new IllegalStateException("没有配置充值配置表？无效的充值比例配置？");
        }

        order.setId(bean.getOrderId());
        order.setChannelId(String.valueOf(bean.getChannelId()));
        order.setSubType(bean.getSubTypeId());
        order.setSubTag(bean.getSubTag());
        order.setAmount(bean.getPayAmount()/100);
//        order.setRechargeAmount((int)bean.getPayAmount() * payEntry.getScale() / 10);
        order.setStatus((byte) 0);
        order.setCurrency((byte) 0);
        order.setUserId(account.getId());
        order.setProductId(bean.getProductId());
        order.setProductName(bean.getProductName());
        order.setProductAmount(bean.getProductAmount());
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

    public boolean assertOrder(final ProductOrder order) {
        if (null == order) throw new IllegalArgumentException("无效订单对象！");

        try {
            // Set to update the order.
            order.setStatus((byte) 1);
            //write to log file
            log.info("订单成功:{}", order);

            final ProductOrderDao orderDao = getInstance(ProductOrderDao.class);
            return orderDao.update(order) > 0;
        } catch (final SQLException e) {
            log.error("更新商品订单确认数据SQL异常：{}", e.getMessage());
        }
        return false;
    }

    public ProductOrder getOrder(String order) {
        if (Strings.isNullOrEmpty(order)) {
            throw new IllegalArgumentException("无效订单号");
        }

        try {
            final ProductOrderDao orderDao = getInstance(ProductOrderDao.class);
            return orderDao.getByOrderId(order);
        } catch (final SQLException e) {
            log.error("获取订单对象SQL异常：{}", e.getMessage());
        } catch (final Exception e) {
            log.error("获取订单对象常规异常：{}", e.getMessage());
        } finally {
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.sz7road.userplatform.service.IPayService#isSuccess(com.sz7road.userplatform.pojos.ProductOrder)
     */
    public boolean isSuccess(ProductOrder order) {
        return order.getStatus() == ProductOrder.PAY_SUCCESS;
    }

    public boolean isWaitForClosed(ProductOrder order) {
        return order.getStatus() == ProductOrder.PAY_FAIL;
    }

    public String getOrderSign(final ProductOrder order) {
        if (null != order) {
            final StringBuilder sb = new StringBuilder();
            sb.append(order.getId());
            sb.append(order.getProductId());
            sb.append(order.getProductName());
            sb.append(order.getProductAmount());
            sb.append(order.getUserId());
            sb.append(order.getAmount());
            sb.append((long) (order.getPayTime().getTime() / 1000));
            sb.append("jeremykeep");

            return MD5Utils.digestAsHex(sb.toString()).toUpperCase();
        }
        return null;
    }

    public boolean isValidOrderSign(final ProductOrder order, final String sign) {
        return Strings.nullToEmpty(getOrderSign(order)).equals(sign.toUpperCase());
    }

    public boolean isValidPayEntryKey(PayTable.PayEntryKey key) {
        final PayTable payTable = getPayTable();
        return null != payTable && payTable.containsKey(key);
    }

    public PayTable getPayTable() {
        return getInstance(PayService.class).getPayTable();
    }

    public Backend.BackendResponse rechargeGolds(final ProductOrder order, final int index) {
        Backend.BackendResponse response = null;
        if(order != null) {
            SendFlowerService sendFlowerService = getInstance(SendFlowerService.class);
            response = sendFlowerService.buyFlower(order);
        }
        return response;
    }

    public Backend.BackendResponse rechargeGolds(final ProductOrder order) {
        return rechargeGolds(order, 3);
    }
}
