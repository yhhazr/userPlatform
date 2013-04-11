package com.sz7road.userplatform.playgame;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 上午11:47
 */
public interface PlayGameBean {

    /**
     * 获取进入游戏的用户名
     * @return
     */
    String getUserName();

    /**
     * 获取游戏Id
     * @return
     */
    int getGameId();

    /**
     * 获取游戏服务器Id
     * @return
     */
    int getGameZoneId();

    boolean isAvailableForSubmit();

}
