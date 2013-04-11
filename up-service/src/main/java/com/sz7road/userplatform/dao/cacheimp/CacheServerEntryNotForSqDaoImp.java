package com.sz7road.userplatform.dao.cacheimp;

import com.google.common.base.Strings;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryDao;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryNotForSqDao;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-16
 * Time: 上午2:12
 * Description: 服务区缓存操作实现类
 */
public class CacheServerEntryNotForSqDaoImp extends AbstractCacheDaoImp<ServerTable.ServerEntry> implements CacheServerEntryNotForSqDao {
    private static Logger log = LoggerFactory.getLogger(CacheServerEntryNotForSqDaoImp.class);

    /**
     * 重新加载
     *
     * @param dataWantToLoad
     * @param key
     * @return 加载完成之后的结果
     */
    @Override
    public boolean reloadCache(Collection<ServerTableNotForSq.ServerEntryNotForSq> dataWantToLoad, String key) {
        Jedis jedis = jedisFactory.getJedisInstance();
        int rel = 0;
        boolean flag = false;
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_ENTRIES_NOTSQ_KEY;
        }
        if (jedis.exists(key)) {
            jedis.del(key);
        }
        if (dataWantToLoad == null || dataWantToLoad.isEmpty()) {
            log.info("参数dataWantToLoad为空!");
            flag = true;
        } else {
            try {
                for (ServerTableNotForSq.ServerEntryNotForSq cacheDataItem : dataWantToLoad) {
                    String server = mapper.writeValueAsString(cacheDataItem);
                    rel += jedis.hset(key,String.valueOf(cacheDataItem.getGameId())+"@"+String.valueOf(cacheDataItem.getServerId()), server);
                }
                flag = (rel == dataWantToLoad.size());

            } catch (Exception ex) {
                log.error("重新加载服务区缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
                if (flag) {
                    log.info("系统已经重新加载非神曲的服务区缓存数据");
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
    public long updateSingleCacheDataItem(ServerTableNotForSq.ServerEntryNotForSq cacheDataItem, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_ENTRIES_NOTSQ_KEY;
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
                    log.info("插入key为：" + mapKey + "的非神曲的缓存数据!");
                } else {
                    jedis.hdel(key, mapKey);
                    rel = jedis.hset(key, mapKey, server);
                    log.info("更新key为：" + mapKey + "的非神曲的缓存数据!");
                }
            } catch (Exception ex) {
                log.error("更新单条非神曲的服务区缓存数据出现异常!" + ex.getMessage());
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
    public long batchUpdateCacheDataItems(Collection<ServerTableNotForSq.ServerEntryNotForSq> cacheDataItems, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_ENTRIES_NOTSQ_KEY;
        }
        if (cacheDataItems == null || cacheDataItems.isEmpty()) {
            log.info("参数cacheDataItems为空!");
            rel = 0l;
        } else {
            try {
                for (ServerTableNotForSq.ServerEntryNotForSq cacheDataItem : cacheDataItems) {
                    String mapKey = String.valueOf(cacheDataItem.getGameId())+"@"+String.valueOf(cacheDataItem.getServerId());
                    String server = mapper.writeValueAsString(cacheDataItem);
                    if (!jedis.hexists(key, mapKey)) {
                        rel += jedis.hsetnx(key, mapKey, server);
                        log.info("插入key为：" + mapKey + "的非神曲的服务区缓存数据!");
                    } else {
                        jedis.hdel(key, mapKey);
                        rel += jedis.hset(key, mapKey, server);
                        log.info("更新key为：" + mapKey + "的非神曲的服务区缓存数据!");
                    }
                }

            } catch (Exception ex) {
                log.error("批量更新非神曲的服务区缓存数据出现异常!" + ex.getMessage());
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
        return super.deleteSingleCacheDataItem(cacheItemKey, CACHE_SERVER_ENTRIES_NOTSQ_KEY);
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
        return super.batchDeleteCacheDataItems(cacheItemKeys, CACHE_SERVER_ENTRIES_NOTSQ_KEY);
    }

    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */
    @Override
    public List<ServerTableNotForSq.ServerEntryNotForSq> queryCacheItems(String key) {
        final Set<String> serverEntries = super.queryCacheItemsStringFromThread(CACHE_SERVER_ENTRIES_NOTSQ_KEY);
        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryList = new LinkedList<ServerTableNotForSq.ServerEntryNotForSq>();
        if (serverEntries != null && !serverEntries.isEmpty()) {
            for (String serverEntryStr : serverEntries) {
                try {
                    ServerTableNotForSq.ServerEntryNotForSq serverEntry = mapper.readValue(serverEntryStr, ServerTableNotForSq.ServerEntryNotForSq.class);
                    serverEntryList.add(serverEntry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(serverEntryList, new Comparator<ServerTableNotForSq.ServerEntryNotForSq>() {
                @Override
                public int compare(ServerTableNotForSq.ServerEntryNotForSq o1, ServerTableNotForSq.ServerEntryNotForSq o2) {
                    if (o1 == null || o2 == null) return 0;
                    if (o1.getServerNo() > o2.getServerNo()) {
                        return 1;
                    } else if (o1.getServerNo() < o2.getServerNo()) {
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
    public ServerTableNotForSq.ServerEntryNotForSq querySingleCacheItem(String key, String cacheItemKey) {
        final String gameEntryStr = super.querySingleCacheItemString(CACHE_SERVER_ENTRIES_NOTSQ_KEY, cacheItemKey);
        if (Strings.isNullOrEmpty(gameEntryStr)) {
            try {
                return mapper.readValue(gameEntryStr, ServerTableNotForSq.ServerEntryNotForSq.class);
            } catch (IOException e) {
                log.error("json转换为服务区对象失败!");
                e.printStackTrace();
            }
        }
        return null;
    }
}
