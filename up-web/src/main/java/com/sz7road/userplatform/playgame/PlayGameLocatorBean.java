package com.sz7road.userplatform.playgame;

import java.io.Serializable;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 下午5:07
 */
public interface PlayGameLocatorBean extends Serializable {

    char getGame();
    
    int getSubGame();
    /**
     * @return 进入游戏HASH值
     */
    int getPlayGameHash();

    /**
     * @return 进入游戏处理器
     */
    PlayGameHandler getHandler();

    /**
     * @return 进入游戏管理门面组件
     */
    PlayGameManager getManager();
}
