package com.sz7road.userplatform.dao.cacheimp;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.service.GetCacheDataThread;
import com.sz7road.utils.JedisFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-16
 * Time: 上午2:22
 * Description:封装公共的应用代码
 */
public abstract class AbstractCacheDaoImp<T> {

    private static Logger log = LoggerFactory.getLogger(AbstractCacheDaoImp.class);

    public static final String CACHE_SERVER_ENTRIES_KEY = "servers";

    public static final String CACHE_SERVER_ENTRIES_NOTSQ_KEY = "notsqservers";

    public static final String CACHE_GAME_ENTRIES_KEY = "games";

    public static final String CACHE_SERVER_MAINTAINS_KEY = "maintains";

    protected static JedisFactory jedisFactory = new JedisFactory();

    protected static ObjectMapper mapper = new ObjectMapper();


    protected static ExecutorService exs = Executors.newCachedThreadPool();

    /**
     * 删除一条缓存中的数据
     *
     * @param cacheItemKey
     * @param key
     * @return
     */

    protected long deleteSingleCacheDataItem(String cacheItemKey, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(cacheItemKey)) {
            log.info("参数key或者cacheItemKey为空!");
            rel = 0l;
        } else {
            try {
                if (jedis.exists(key) && jedis.hexists(key, cacheItemKey)) {
                    rel = jedis.hdel(key, cacheItemKey);
                } else {
                    log.info("缓存中不存在key为：" + cacheItemKey + " 的数据!");
                }

            } catch (Exception ex) {
                log.error("删除单条缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
            }
        }
        return rel;
    }

    /**
     * 批量删除缓存中的数据
     *
     * @param cacheItemKeys
     * @param key
     * @return
     */

    protected long batchDeleteCacheDataItems(String[] cacheItemKeys, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key) || cacheItemKeys == null || cacheItemKeys.length == 0) {
            log.info("参数key或者cacheItemKeys为空!");
            rel = 0l;
        } else {
            try {
                for (String cacheItemKey : cacheItemKeys) {
                    if (jedis.exists(key) && jedis.hexists(key, cacheItemKey)) {
                        rel += jedis.hdel(key, cacheItemKey);
                    } else {
                        log.info("缓存中不存在key为：" + cacheItemKey + " 的数据!");
                    }
                }
            } catch (Exception ex) {
                log.error("批量删除缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
            }
        }
        return rel;
    }

    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */

    protected Set<String> queryCacheItemsString(String key) {
        Set<String> serverEntrySet = new HashSet<String>();
        try {
            Jedis jedis = jedisFactory.getJedisInstance();
            if (Strings.isNullOrEmpty(key)) {
                serverEntrySet = null;
                log.info("参数key为空!");
            } else {
                try {
                    Set<String> servers = jedis.hkeys(key);
                    if (servers != null && !servers.isEmpty()) {
                        for (String server : servers) {
                            String serverStr = jedis.hget(key, server);
                            serverEntrySet.add(serverStr);
                        }
                        log.info("查询到的-" + key + "-信息总数：" + serverEntrySet.size() + "#####");
                    } else {
                        log.info("没有查到缓存中的服务器信息列表!");
                    }
                } catch (Exception ex) {
                    log.error("查询缓存中的服务器信息列表异常!" + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    jedisFactory.release(jedis);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverEntrySet;
    }


    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */

    protected Set<String> queryCacheItemsStringFromThread(String key) {
        Set<String> resultCollection = new HashSet<String>();
        try {
            final Future<Set<String>> future = exs.submit(new GetCacheDataThread(jedisFactory, key));
            if (future != null) {
                resultCollection = future.get();
            }
//            log.info("线程获取的数据 " + key + " 数目：" + (resultCollection == null ? "空" : resultCollection.size()));
        } catch (Exception e) {
            log.error("线程读取缓存数据异常!" + e.getMessage());
            e.printStackTrace();
        }
        return resultCollection;
    }


    /**
     * 查询单条缓存数据
     *
     * @param key
     * @param cacheItemKey
     * @return
     */

    protected String querySingleCacheItemString(String key, String cacheItemKey) {
        Jedis jedis = jedisFactory.getJedisInstance();
        String serverEntry = "";
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(cacheItemKey)) {
            serverEntry = null;
            log.info("参数key或者cacheItemKey为空!");
        }
        try {
            String entry = jedis.hgetAll(key).get(cacheItemKey);
            log.info("查询的服务器信息为：" + entry);
            if (!Strings.isNullOrEmpty(entry)) {
                serverEntry = entry;
            } else {
                log.info("没有查到缓存中key为 " + cacheItemKey + " 的服务器信息!");
            }
        } catch (Exception ex) {
            log.error("查询缓存中key为 " + cacheItemKey + " 的服务器信息异常!" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            jedisFactory.release(jedis);
        }
        return serverEntry;
    }

}
