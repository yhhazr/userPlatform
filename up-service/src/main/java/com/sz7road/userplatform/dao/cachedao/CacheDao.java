package com.sz7road.userplatform.dao.cachedao;

import java.util.Collection;
import java.util.List;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-10-29
 * Time: 上午1:37
 * Description: 缓存操作接口
 */
public interface CacheDao<T> {

    /**
     * 重新加载
     * @param dataWantToLoad
     * @param key
     * @return 加载完成之后的结果
     */
    public boolean reloadCache(Collection<T> dataWantToLoad, String key);

    /**
     * 更新一条缓存数据，如果没有，插入；否则，更新。
     * @param cacheDataItem
     * @param key
     * @return
     */
    public long updateSingleCacheDataItem(T cacheDataItem, String key);

    /**
     * 批量更新缓存数据，如果没有，插入；否则，更新。
     * @param cacheDataItems
     * @param key
     * @return
     */
    public long batchUpdateCacheDataItems(Collection<T> cacheDataItems, String key);

    /**
     * 删除一条缓存中的数据
     * @param cacheItemKey
     * @param key
     * @return
     */
    public long deleteSingleCacheDataItem(String cacheItemKey, String key);

    /**
     * 批量删除缓存中的数据
     * @param cacheItemKeys
     * @param key
     * @return
     */
    public long batchDeleteCacheDataItems(String[] cacheItemKeys, String key);

    /**
     * 查询key对应的缓存数据map
     * @param key
     * @return
     */
    public List<T> queryCacheItems(String key);

    /**
     * 查询单条缓存数据
     * @param key
     * @param cacheItemKey
     * @return
     */
    public T querySingleCacheItem(String key, String cacheItemKey);


}
