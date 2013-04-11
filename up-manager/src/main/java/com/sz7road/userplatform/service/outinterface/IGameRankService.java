package com.sz7road.userplatform.service.outinterface;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-11
 * Time: 上午9:43
 * 拿到游戏排名信息的接口
 */
public interface IGameRankService {

    /**
     * 根据传入的游戏ID，得到一个json数据，
     * json数据的格式是｛“规则”，rule,"data",[{"gameId",gameId,"url",url}...],"total",total｝
     *
     * @param gameId
     * @return
     */
    String getGameRankInfo(int gameId);


}
