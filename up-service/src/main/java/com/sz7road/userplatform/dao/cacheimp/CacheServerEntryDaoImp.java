package com.sz7road.userplatform.dao.cacheimp;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sz7road.userplatform.dao.cachedao.CacheServerEntryDao;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
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
public class CacheServerEntryDaoImp extends AbstractCacheDaoImp<ServerTable.ServerEntry> implements CacheServerEntryDao {
    private static Logger log = LoggerFactory.getLogger(CacheServerEntryDaoImp.class);

    /**
     * 重新加载
     *
     * @param dataWantToLoad
     * @param key
     * @return 加载完成之后的结果
     */
    @Override
    public boolean reloadCache(Collection<ServerTable.ServerEntry> dataWantToLoad, String key) {
        Jedis jedis = jedisFactory.getJedisInstance();
        int rel = 0;
        boolean flag = false;
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_ENTRIES_KEY;
        }
        if (jedis.exists(key)) {
            jedis.del(key);
        }
        if (dataWantToLoad == null || dataWantToLoad.isEmpty()) {
            log.info("参数dataWantToLoad为空!");
            flag = false;
        } else {
            try {

                for (ServerTable.ServerEntry gameEntry : dataWantToLoad) {
                    String server = mapper.writeValueAsString(gameEntry);
                    rel += jedis.hset(key, String.valueOf(gameEntry.getId()), server);
                }
                flag = (rel == dataWantToLoad.size());
                if (flag) {
                    log.info("系统已经重新加载神曲服务区缓存数据");
                }
            } catch (Exception ex) {
                log.error("重新加载神曲服务区缓存数据出现异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                jedisFactory.release(jedis);
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
    public long updateSingleCacheDataItem(ServerTable.ServerEntry cacheDataItem, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_ENTRIES_KEY;
        }
        if (cacheDataItem == null) {
            log.info("参数cacheDataItem为空!");
            rel = 0l;
        } else {
            try {
                String mapKey = String.valueOf(cacheDataItem.getId());
                String server = mapper.writeValueAsString(cacheDataItem);
                if (!jedis.hexists(key, mapKey)) {
                    rel = jedis.hsetnx(key, mapKey, server);
                    log.info("插入key为：" + mapKey + "的缓存数据!");
                } else {
                    jedis.hdel(key, mapKey);
                    rel = jedis.hset(key, mapKey, server);
                    log.info("更新key为：" + mapKey + "的缓存数据!");
                }
            } catch (Exception ex) {
                log.error("更新单条神曲服务区缓存数据出现异常!" + ex.getMessage());
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
    public long batchUpdateCacheDataItems(Collection<ServerTable.ServerEntry> cacheDataItems, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_SERVER_ENTRIES_KEY;
        }
        if (cacheDataItems == null || cacheDataItems.isEmpty()) {
            log.info("参数cacheDataItems为空!");
            rel = 0l;
        } else {
            try {
                for (ServerTable.ServerEntry cacheDataItem : cacheDataItems) {
                    String mapKey = String.valueOf(cacheDataItem.getId());
                    String server = mapper.writeValueAsString(cacheDataItem);
                    if (!jedis.hexists(key, mapKey)) {
                        rel += jedis.hsetnx(key, mapKey, server);
                        log.info("插入key为：" + mapKey + "的神曲服务区缓存数据!");
                    } else {
                        jedis.hdel(key, mapKey);
                        rel += jedis.hset(key, mapKey, server);
                        log.info("更新key为：" + mapKey + "的神曲服务区缓存数据!");
                    }
                }

            } catch (Exception ex) {
                log.error("批量更新神曲服务区缓存数据出现异常!" + ex.getMessage());
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
        return super.deleteSingleCacheDataItem(cacheItemKey, CACHE_SERVER_ENTRIES_KEY);
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
        return super.batchDeleteCacheDataItems(cacheItemKeys, CACHE_SERVER_ENTRIES_KEY);
    }

    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */
    @Override
    public List<ServerTable.ServerEntry> queryCacheItems(String key) {
        final Set<String> serverEntries = super.queryCacheItemsStringFromThread(CACHE_SERVER_ENTRIES_KEY);
        List<ServerTable.ServerEntry> serverEntryList = new LinkedList<ServerTable.ServerEntry>();
        if (serverEntries != null && !serverEntries.isEmpty()) {
            for (String serverEntryStr : serverEntries) {
                try {
                    ServerTable.ServerEntry serverEntry = mapper.readValue(serverEntryStr, ServerTable.ServerEntry.class);
                    serverEntryList.add(serverEntry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(serverEntryList, new Comparator<ServerTable.ServerEntry>() {
                @Override
                public int compare(ServerTable.ServerEntry o1, ServerTable.ServerEntry o2) {
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
    public ServerTable.ServerEntry querySingleCacheItem(String key, String cacheItemKey) {
        final String gameEntryStr = super.querySingleCacheItemString(CACHE_SERVER_ENTRIES_KEY, cacheItemKey);
        if (Strings.isNullOrEmpty(gameEntryStr)) {
            try {
                return mapper.readValue(gameEntryStr, ServerTable.ServerEntry.class);
            } catch (IOException e) {
                log.error("json转换为神曲服务区对象失败!");
                e.printStackTrace();
            }
        }
        return null;
    }
}
