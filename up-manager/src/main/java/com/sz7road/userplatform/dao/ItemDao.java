package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.Item;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 下午12:04
 * To change this template use File | Settings | File Templates.
 */
public interface ItemDao extends Dao<Item> {

    /**
     * 拿到支付渠道数据
     *
     * @return
     */
    List<Item> getChannels();

    /**
     * 拿到支付渠道的支付方式数据
     *
     * @param channel 支付渠道
     * @return
     */
    List<Item> getSubTypes(String channel);

    /**
     * 拿到支付渠道下，支付方式下的具体支付方式
     *
     * @param channel 支付渠道
     * @param subType 支付方式第一层
     * @return
     */
    List<Item> getSubTypeTags(String channel, String subType);

    /**
     * 拿到游戏的数据
     *
     * @return
     */
    List<Item> getGames();

    /**
     * 拿到服务器的数据
     *
     * @param gameId 游戏ID
     * @return
     */
    List<Item> getServers(String gameId);

    /**
     * 拿到所有的常见问题分类
     *
     * @return
     */
    List<Item> getFaqKind();

    /**
     * 拿到一级分类
     *
     * @return
     */
    List<Item> getFaqKindOfParent();
}
