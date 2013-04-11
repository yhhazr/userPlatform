package com.sz7road.userplatform.dao.cacheimp;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sz7road.userplatform.dao.cachedao.CacheServerMaintain2Dao;
import com.sz7road.userplatform.pojos.ServerMaintain2;
import com.sz7road.utils.JedisFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-10-29
 * Time: 上午3:03
 * Description: 缓存维护信息的实现类
 */
public class CacheServerMaintain2DaoImp extends AbstractCacheDaoImp<ServerMaintain2>  implements CacheServerMaintain2Dao {

    private static Logger log = LoggerFactory.getLogger(CacheServerMaintain2DaoImp.class);
    private static JedisFactory jedisFactory = new JedisFactory();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 重新加载
     *
     * @param dataWantToLoad
     * @param key
     * @return 加载完成之后的结果
     */
    @Override
    public boolean reloadCache(Collection<ServerMaintain2> dataWantToLoad, String key) {
        Jedis jedis = jedisFactory.getJedisInstance();
        int rel = 0;
        boolean flag = false;
        if (Strings.isNullOrEmpty(key)) {
            key = AbstractCacheDaoImp.CACHE_SERVER_MAINTAINS_KEY;
        }
        if (jedis.exists(key)) {
            jedis.del(key);
        }
        if (dataWantToLoad == null || dataWantToLoad.isEmpty()) {
            log.info("参数dataWantToLoad为空!");
            flag = true;
        } else {
            try {
                for (ServerMaintain2 cacheDataItem : dataWantToLoad) {
                    String maintain2 = mapper.writeValueAsString(cacheDataItem);
                    rel += jedis.hset(key,String.valueOf(cacheDataItem.getGameId())+"@"+String.valueOf(cacheDataItem.getServerId()), maintain2);
                }
                flag = (rel == dataWantToLoad.size());

            } catch (Exception ex) {
                log.error("重新加载维护信息缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
                if (flag) {
                    log.info("系统已经重新加载维护信息缓存数据");
                }
            }
        }
        return flag;
    }

    /**
     * 更新一条缓存数据，如果没有，插入；否则，更新。
     *
     * @param cacheDataItem
     * @param key
     * @return
     */
    @Override
    public long updateSingleCacheDataItem(ServerMaintain2 cacheDataItem, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_MAINTAINS_KEY;
        }
        if (cacheDataItem == null) {
            log.info("参数cacheDataItem为空!");
            rel = 0l;
        } else {
            try {
                String mapKey = String.valueOf(cacheDataItem.getGameId())+"@"+String.valueOf(cacheDataItem.getServerId());
                String server = mapper.writeValueAsString(cacheDataItem);
                if (!jedis.hexists(key, mapKey)) {
                    rel = jedis.hsetnx(key, mapKey, server);
                    log.info("插入key为：" + mapKey + "的维护信息的缓存数据!");
                } else {
                    jedis.hdel(key, mapKey);
                    rel = jedis.hset(key, mapKey, server);
                    log.info("更新key为：" + mapKey + "的维护信息的缓存数据!");
                }
            } catch (Exception ex) {
                log.error("更新单条维护信息的服务区缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
            }
        }
        return rel;
    }

    /**
     * 批量更新缓存数据，如果没有，插入；否则，更新。
     *
     * @param cacheDataItems
     * @param key
     * @return
     */
    @Override
    public long batchUpdateCacheDataItems(Collection<ServerMaintain2> cacheDataItems, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_MAINTAINS_KEY;
        }
        if (cacheDataItems == null || cacheDataItems.isEmpty()) {
            log.info("参数cacheDataItems为空!");
            rel = 0l;
        } else {
            try {
                for (ServerMaintain2 cacheDataItem : cacheDataItems) {
                    String mapKey = String.valueOf(cacheDataItem.getGameId())+"@"+String.valueOf(cacheDataItem.getServerId());
                    String server = mapper.writeValueAsString(cacheDataItem);
                    if (!jedis.hexists(key, mapKey)) {
                        rel += jedis.hsetnx(key, mapKey, server);
                        log.info("插入key为：" + mapKey + "的维护信息的服务区缓存数据!");
                    } else {
                        jedis.hdel(key, mapKey);
                        rel += jedis.hset(key, mapKey, server);
                        log.info("更新key为：" + mapKey + "的维护信息的服务区缓存数据!");
                    }
                }

            } catch (Exception ex) {
                log.error("批量更新维护信息的服务区缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
            }
        }
        return rel;
    }

    /**
     * 删除一条缓存中的数据
     *
     * @param cacheItemKey
     * @param key
     * @return
     */
    @Override
    public long deleteSingleCacheDataItem(String cacheItemKey, String key) {
        return super.deleteSingleCacheDataItem(cacheItemKey, CACHE_SERVER_MAINTAINS_KEY);
    }

    /**
     * 批量删除缓存中的数据
     *
     * @param cacheItemKeys
     * @param key
     * @return
     */
    @Override
    public long batchDeleteCacheDataItems(String[] cacheItemKeys, String key) {
        return super.batchDeleteCacheDataItems(cacheItemKeys, CACHE_SERVER_MAINTAINS_KEY);
    }

    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */
    @Override
    public List<ServerMaintain2> queryCacheItems(String key) {
        final Set<String> serverEntries = super.queryCacheItemsStringFromThread(CACHE_SERVER_MAINTAINS_KEY);
        List<ServerMaintain2> serverEntryList = Lists.newLinkedList();
        if (serverEntries != null && !serverEntries.isEmpty()) {
            for (String serverEntryStr : serverEntries) {
                try {
                    ServerMaintain2 serverEntry = mapper.readValue(serverEntryStr, ServerMaintain2.class);
                    serverEntryList.add(serverEntry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(serverEntryList, new Comparator<ServerMaintain2>() {
                @Override
                public int compare(ServerMaintain2 o1, ServerMaintain2 o2) {
                    if (o1 == null || o2 == null) return 0;
                    if (o1.getEndTime().before( o2.getEndTime())) {
                        return 1;
                    } else if (o1.getEndTime().after(o2.getEndTime())) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return serverEntryList;
    }

    /**
     * 查询单条缓存数据
     *
     * @param key
     * @param cacheItemKey
     * @return
     */
    @Override
    public ServerMaintain2 querySingleCacheItem(String key, String cacheItemKey) {
        final String gameEntryStr = super.querySingleCacheItemString(CACHE_SERVER_MAINTAINS_KEY, cacheItemKey);
        if (Strings.isNullOrEmpty(gameEntryStr)) {
            try {
                return mapper.readValue(gameEntryStr, ServerMaintain2.class);
            } catch (IOException e) {
                log.error("json转换为维护对象失败!");
                e.printStackTrace();
            }
        }
        return null;
    }
}
