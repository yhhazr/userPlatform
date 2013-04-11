package com.sz7road.userplatform.playgame;

import com.google.inject.Inject;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 下午2:46
 */

public class GenericPlayGameLocatorBean implements PlayGameLocatorBean {

    private char game;
    
    private int subGame = 0;

    private PlayGameManager playGameManager;

    @Inject
    protected GenericPlayGameLocatorBean(final PlayGameManager playGameManager){
        this.playGameManager = playGameManager;
    }

    @Override
    public int getSubGame() {
        return subGame;
    }

    @Override
    public char getGame() {
        return game;
    }

    public void setSubGame(int subGame) {
        this.subGame = subGame;
    }

    public void setGame(char game) {
        this.game = game;
    }

    @Override
    public PlayGameHandler getHandler() {
        return playGameManager.get(getPlayGameHash());
    }

    @Override
    public int getPlayGameHash() {
        return PlayGameManager.hash(getGame(),getSubGame());
    }

    @Override
    public PlayGameManager getManager() {
        return playGameManager;
    }

    protected boolean isValidGame() {
        char c = getGame();
        return (c >= '1' && c <= '9') || (c >= 'A' && c <= 'Z');
    }
}
