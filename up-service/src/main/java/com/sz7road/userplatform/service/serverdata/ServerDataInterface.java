package com.sz7road.userplatform.service.serverdata;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-21
 * Time: 下午4:56
 * 取得服务器信息的接口
 * 所有记录逆序排序，按createTime
 * 筛选规则(-1:关闭状态不在List内)
 */
public interface ServerDataInterface {

    ServerInfo get(int id);

    ServerInfo get(int gameId, int serverId);

    List<ServerInfo> list();

    List<ServerInfo> listWithStatus(int status);

    List<ServerInfo> list(int gameId);

    /**
     * 获取所有游戏的num条数据
     *
     * @param num
     * @return
     */
    List<ServerInfo> listNum(int num);

    /**
     * 获得某个游戏的num条数据
     *
     * @param gameId
     * @param num
     * @return
     */
    List<ServerInfo> listNumOfGame(int gameId, int num);

    ServerInfo list(int gameId, int serverNo);

    /**
     * 获取某游戏的最新开的服务器
     *
     * @param gameId
     * @return
     */
    ServerInfo getNewOpenedServer(int gameId);

}
