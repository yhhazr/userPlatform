package com.sz7road.userplatform.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.ProductOrder;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.utils.Backend;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;

/**
 * 发送花通知服务类
 * @author leo.liao
 *
 */
@Singleton
public class SendFlowerService extends Injection
{
    final static Logger LOG = LoggerFactory.getLogger(SendFlowerService.class.getName());
    private final ScheduledExecutorService buyFlowerTaskExec = Executors.newScheduledThreadPool(1 << 6, new ThreadFactory() {
        final String _NAME = "NOTIFY-SENDFLOWER-RUNNER";
        volatile int INDEX = 0;

        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r, _NAME + "-" + (INDEX++));
        }
    });
    
    public Backend.BackendResponse buyFlower(final ProductOrder order, final int index) 
    {
        Backend.BackendResponse response = null;
        if(order != null) {
            int userId = order.getUserId();
            UserService userService = getInstance(UserService.class);
            UserAccount account = userService.findAccountById(userId);
            int status = 408;
            String content = "fail";
            if(account != null) 
            {
                Map<String,Object> param = Maps.newHashMap();
                param.put("order", order.getId());
                param.put("user", account.getUserName());
                param.put("_pid", order.getProductId());
                param.put("_pn", order.getProductName());
                param.put("_pa", order.getProductAmount());
                param.put("amount", order.getAmount());
                List<String> list = Lists.newArrayList(param.keySet());
                Collections.sort(list);
                StringBuilder sb = new StringBuilder();
                for(String key : list) {
                    sb.append(key).append("=").append(param.get(key));
                }
                String signKey = ConfigurationUtils.get("ppay.param.signKey");
                sb.append(signKey);
                String sign = MD5Utils.digestAsHex(sb.toString());
                //通知买花接口
                String buyFlowerNotifyUrl = ConfigurationUtils.get("ppay.buyFlower.NotifyUrl");
                param.put("sign", sign);
                LOG.info("buyFlower notify sendFlower,request params[order={},user={},_pid={},_pn={},_pa={},amount={}]"
                        ,new Object[]{order.getId(),account.getUserName(),order.getProductId(),order.getProductName(),order.getProductAmount(),order.getAmount()});
                response = Backend.post(buyFlowerNotifyUrl, param);
                if(response != null)
                {
                    status = response.getResponseCode();
                    content = response.getResponseContent();
                }
                LOG.info("buyFlower notify sendFlower response[status={},content={},order={}]",new Object[]{order.getId(),status,content});
            }
          //调用通知返回的结果判断是否需要重新发送通知
            if((response == null || response.getResponseCode() != 200 
                    || !"success".equalsIgnoreCase(response.getResponseContent()))
                    && index > 0) {
                buyFlowerTaskExec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        buyFlower(order, index - 1);
                    }
                }, 180, TimeUnit.SECONDS);
            }
        }
        return response;
    }
    
    public Backend.BackendResponse buyFlower(final ProductOrder order) 
    {
        return buyFlower(order, 3);
    }
    
    public Backend.BackendResponse chargeSendFlower(final OrderObject order, final int index)
    {
        Backend.BackendResponse response = null;
        int status = 408;
        String content = "fail";
        if(order != null) {
            int userId = order.getUserId();
            UserService userService = getInstance(UserService.class);
            UserAccount account = userService.findAccountById(userId);
            if(account != null) 
            {
                Map<String,Object> param = Maps.newHashMap();
                param.put("order", order.getId());
                param.put("user", account.getUserName());
                param.put("gid", order.getGameId());
                param.put("zid", order.getZoneId());
                param.put("gold", order.getGold());
                param.put("amount", order.getAmount());
                List<String> list = Lists.newArrayList(param.keySet());
                Collections.sort(list);
                StringBuilder sb = new StringBuilder();
                for(String key : list) {
                    sb.append(key).append("=").append(param.get(key));
                }
                String signKey = ConfigurationUtils.get("ppay.param.signKey");
                sb.append(signKey);
                String sign = MD5Utils.digestAsHex(sb.toString());
                //通知买花接口
                String buyFlowerNotifyUrl = ConfigurationUtils.get("ppay.chargeSendFlower.NotifyUrl");
                param.put("sign", sign);
                LOG.info("charge notify sendFlower,request params[order={},user={},gid={},zid={},gold={},amount={}]"
                        ,new Object[]{order.getId(),account.getUserName(),order.getGameId(),order.getZoneId(),order.getGold(),order.getAmount()});
                response = Backend.post(buyFlowerNotifyUrl, param);
                if(response != null)
                {
                    status = response.getResponseCode();
                    content = response.getResponseContent();
                }
                LOG.info("charge notify sendFlower response[status={},content={},order={}]",new Object[]{status,content,order.getId()});
            }
            //调用通知返回的结果判断是否需要重新发送通知
            if((response == null || response.getResponseCode() != 200 
                    || !"success".equalsIgnoreCase(response.getResponseContent()))
                    && index > 0) {
                buyFlowerTaskExec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        chargeSendFlower(order, index - 1);
                    }
                }, 180, TimeUnit.SECONDS);
            }
        }
        return response;
    }
    
    public Backend.BackendResponse chargeSendFlower(final OrderObject order)
    {
        return chargeSendFlower(order, 3);
    }
}
