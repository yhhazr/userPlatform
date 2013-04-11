package com.sz7road.web.manager;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.Item;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.userplatform.service.ItemService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ItemManager {
    @Inject
    private Provider<ItemService> dropdownMenuServiceProvider;

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;

    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    /**
     * 拿到支付渠道数据
     *
     * @return
     */
    public List<Item> getChannels() {

        ItemService itemService = dropdownMenuServiceProvider.get();
        return itemService.getChannels();
    }

    /**
     * 拿到支付渠道的支付方式数据
     *
     * @param channel 支付渠道
     * @return
     */
    public List<Item> getSubTypes(String channel) {
        ItemService itemService = dropdownMenuServiceProvider.get();
        return itemService.getSubTypes(channel);
    }

    /**
     * 拿到支付渠道下，支付方式下的具体支付方式
     *
     * @param channel 支付渠道
     * @param subType 支付方式第一层
     * @return
     */
    public List<Item> getSubTypeTags(String channel, String subType) {
        ItemService itemService = dropdownMenuServiceProvider.get();
        return itemService.getSubTypeTags(channel, subType);
    }

    public List<Item> getGames() {
        // 拿到游戏集合
        GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
        Collection<GameTable.GameEntry> gameEntryCollection = gameWithServerDecorateService.getGameTable().values();

        List<Item> games = new ArrayList<Item>();

        for (GameTable.GameEntry gameEntry : gameEntryCollection) {
            Item gameItem = new Item();
            gameItem.setLabel(gameEntry.getGameName());
            gameItem.setValue(String.valueOf(gameEntry.getId()));
            games.add(gameItem);
        }
        return games;
//        ItemService itemService = dropdownMenuServiceProvider.get();
//        return itemService.getGames();
    }

    public List<Item> getServers(String gameId) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(gameId),"gameId 为空");

        if(Strings.isNullOrEmpty(gameId)||"empty".equals(gameId)||"null".equals(gameId))
        {
            gameId="1";
        }

        List<ServerInfo> serverInfoList=serverDataServiceProvider.get().list(Ints.tryParse(gameId));

        if(serverInfoList!=null&&!serverInfoList.isEmpty())
        Collections.sort(serverInfoList,new Comparator<ServerInfo>() {
            @Override
            public int compare(ServerInfo o1, ServerInfo o2) {
                return o1.getServerNo()-o2.getServerNo();
            }
        });

        List<Item> servers = new ArrayList<Item>();
        for (ServerInfo serverInfo : serverInfoList) {    //状态为-3的排除
            if (serverInfo.getServerStatus() != -3) {
                Item serverItem = new Item();
                serverItem.setLabel(serverInfo.getServerName());
                serverItem.setValue(String.valueOf(serverInfo.getServerId()));
                servers.add(serverItem);
            }
        }

        return servers;
    }

    public List<Item> getFaqKind() {
        ItemService itemService = dropdownMenuServiceProvider.get();
        return itemService.getFaqKind();
    }

    public List<Item> getFaqKindOfParent() {
        ItemService itemService = dropdownMenuServiceProvider.get();
        return itemService.getFaqKindOfParent();
    }

}
