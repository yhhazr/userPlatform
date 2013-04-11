package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.sz7road.userplatform.dao.cacheimp.AbstractCacheDaoImp;
import com.sz7road.utils.JedisFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-12-18
 * Time: 上午7:42
 * Description: 线程获取游戏区缓存数据
 */
public class GetCacheDataThread implements Callable<Set<String>> {

    private final static Logger log = LoggerFactory.getLogger(GetCacheDataThread.class);

    private JedisFactory jedisFactory = null;

    private String key = null;

    public GetCacheDataThread() {
    }

    public GetCacheDataThread(JedisFactory jedisFactory, String key) {
        this.jedisFactory = jedisFactory;
        this.key = key;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */

    @Override
    public Set<String> call() throws Exception {
        return queryCacheItemsString(key);
    }

    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */

    private Set<String> queryCacheItemsString(String key) {
        Set<String> serverEntrySet = new HashSet<String>();
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            serverEntrySet = null;
            log.info("参数key为空!");
        } else {
            try {
                final Set<String> servers = jedis.hkeys(key);
                if (servers != null && !servers.isEmpty()) {
                    for (String server : servers) {
                        String serverStr = jedis.hget(key, server);
                        serverEntrySet.add(serverStr);
                    }
                } else {
                    log.info("没有查到缓存中的" + key + "信息列表!");
                }
            } catch (Exception ex) {
                log.error("查询缓存中的" + key + "信息列表异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
            }
        }
        return serverEntrySet;
    }

}
