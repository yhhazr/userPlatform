package com.sz7road.userplatform.dao.cacheimp;

import com.google.common.base.Strings;
import com.sz7road.userplatform.dao.cachedao.CacheGameDao;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-16
 * Time: 上午2:11
 * Description:缓存游戏数据的实现类
 */
public class CacheGameDaoImp extends AbstractCacheDaoImp<GameTable.GameEntry> implements CacheGameDao {

    private static Logger log = LoggerFactory.getLogger(CacheGameDaoImp.class);

    /**
     * 重新加载
     *
     * @param dataWantToLoad
     * @param key
     * @return 加载完成之后的结果
     */
    @Override
    public boolean reloadCache(Collection<GameTable.GameEntry> dataWantToLoad, String key) {
        Jedis jedis = jedisFactory.getJedisInstance();
        int rel = 0;
        boolean flag = false;
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_GAME_ENTRIES_KEY;
        }
        if (jedis.exists(key)) {
            jedis.del(key);
        }
        if (dataWantToLoad == null || dataWantToLoad.isEmpty()) {
            log.info("参数dataWantToLoad为空!");
            flag = false;
        } else {
            try {

                for (GameTable.GameEntry gameEntry : dataWantToLoad) {
                    String server = mapper.writeValueAsString(gameEntry);
                    rel += jedis.hset(key, String.valueOf(gameEntry.getId()), server);
                }
                flag = (rel == dataWantToLoad.size());
                if (flag) {
                    log.info("系统已经重新加载游戏缓存数据");
                }
            } catch (Exception ex) {
                log.error("重新加载游戏缓存数据出现异常!" + ex.getMessage());
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
    public long updateSingleCacheDataItem(GameTable.GameEntry cacheDataItem, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_GAME_ENTRIES_KEY;
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
                log.error("更新单条游戏缓存数据出现异常!" + ex.getMessage());
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
    public long batchUpdateCacheDataItems(Collection<GameTable.GameEntry> cacheDataItems, String key) {
        long rel = 0l;
        Jedis jedis = jedisFactory.getJedisInstance();
        if (Strings.isNullOrEmpty(key)) {
            key = CACHE_GAME_ENTRIES_KEY;
        }
        if (cacheDataItems == null || cacheDataItems.isEmpty()) {
            log.info("参数cacheDataItems为空!");
            rel = 0l;
        } else {
            try {
                for (GameTable.GameEntry cacheDataItem : cacheDataItems) {
                    String mapKey = String.valueOf(cacheDataItem.getId());
                    String server = mapper.writeValueAsString(cacheDataItem);
                    if (!jedis.hexists(key, mapKey)) {
                        rel += jedis.hsetnx(key, mapKey, server);
                        log.info("插入key为：" + mapKey + "的游戏缓存数据!");
                    } else {
                        jedis.hdel(key, mapKey);
                        rel += jedis.hset(key, mapKey, server);
                        log.info("更新key为：" + mapKey + "的游戏缓存数据!");
                    }
                }

            } catch (Exception ex) {
                log.error("批量更新游戏缓存数据出现异常!" + ex.getMessage());
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
        return super.deleteSingleCacheDataItem(cacheItemKey, CACHE_GAME_ENTRIES_KEY);
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
        return super.batchDeleteCacheDataItems(cacheItemKeys, CACHE_GAME_ENTRIES_KEY);
    }

    /**
     * 查询key对应的缓存数据map
     *
     * @param key
     * @return
     */
    @Override
    public List<GameTable.GameEntry> queryCacheItems(String key) {
        final Set<String> gameEntries = super.queryCacheItemsStringFromThread(CACHE_GAME_ENTRIES_KEY);
        List<GameTable.GameEntry> gameEntryList = new LinkedList<GameTable.GameEntry>();
        if (gameEntries != null && !gameEntries.isEmpty()) {
            for (String gameEntryStr : gameEntries) {
                try {
                    GameTable.GameEntry gameEntry = mapper.readValue(gameEntryStr, GameTable.GameEntry.class);
                    gameEntryList.add(gameEntry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(gameEntryList, new Comparator<GameTable.GameEntry>() {
                @Override
                public int compare(GameTable.GameEntry o1, GameTable.GameEntry o2) {
                    return o1.getId() - o2.getId();
                }
            });
        }
        return gameEntryList;
    }

    /**
     * 查询单条缓存数据
     *
     * @param key
     * @param cacheItemKey
     * @return
     */
    @Override
    public GameTable.GameEntry querySingleCacheItem(String key, String cacheItemKey) {
        String gameEntryStr = super.querySingleCacheItemString(CACHE_GAME_ENTRIES_KEY, cacheItemKey);
        if (Strings.isNullOrEmpty(gameEntryStr)) {
            try {
                return mapper.readValue(gameEntryStr, GameTable.GameEntry.class);
            } catch (IOException e) {
                log.error("json转换为游戏对象失败!");
                e.printStackTrace();
            }
        }
        return null;
    }
}
