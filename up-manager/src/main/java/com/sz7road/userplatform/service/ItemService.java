package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.ItemDao;
import com.sz7road.userplatform.pojos.Item;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 上午11:30
 * 获取下拉菜单的数据.
 */
@Singleton
public class ItemService {
    @Inject
    private Provider<ItemDao> itemDaoProvider;

    private ItemDao getItemDao() {
        ItemDao itemDao = itemDaoProvider.get();

        if (itemDao == null) {
            throw new NullPointerException("ItemDao null");
        }
        return itemDao;
    }

    public List<Item> getChannels() {
        return getItemDao().getChannels();
    }

    public List<Item> getSubTypes(final String channel) {
//        if (Strings.isNullOrEmpty(channel)) {
//            throw new IllegalArgumentException("illegalArgument channel!");
//        }
        return getItemDao().getSubTypes(channel);
    }

    public List<Item> getSubTypeTags(final String channel, final String subType) {

//        if (Strings.isNullOrEmpty(channel) || Strings.isNullOrEmpty(subType)) {
//            throw new IllegalArgumentException("illegalArgument channel or subType!");
//        }
        return getItemDao().getSubTypeTags(channel, subType);
    }

    public List<Item> getGames() {
        return getItemDao().getGames();
    }

    public List<Item> getServers(final String gameId) {
        if (Strings.isNullOrEmpty(gameId)) {
            throw new IllegalArgumentException("illegalArgument channel!");
        }
        return getItemDao().getServers(gameId);
    }

    public List<Item> getFaqKind() {
        return getItemDao().getFaqKind();
    }

    public List<Item> getFaqKindOfParent() {
        return getItemDao().getFaqKindOfParent();
    }
}
