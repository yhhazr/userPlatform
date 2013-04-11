package com.sz7road.userplatform.playgame;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.web.utils.AppHelper;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 上午11:54
 */
@RequestScoped
public class GenericPlayGameBean extends GenericPlayGameLocatorBean implements PlayGameBean {

    private String userName;

    private int gameId;

    private int gameZoneId;

    @Inject
    public GenericPlayGameBean(final PlayGameManager manager) {
        super(manager);
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setGameZoneId(int gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUserName() {
        return userName;
    }
    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public int getGameZoneId() {
        return gameZoneId;
    }

    @Inject
    protected void validate(final HttpServletRequest request) {
        String user = AppHelper.getUserName(request);
        String gameId = request.getParameter("g");
        String _z = request.getParameter("z");
        
        String game = request.getParameter("game");
        String subGame = request.getParameter("subGame");
        
        if(!Strings.isNullOrEmpty(user)){
            setUserName(user.trim());
        }
        if(!Strings.isNullOrEmpty(gameId)){
            setGameId(Integer.parseInt(gameId.trim()));
        }
        if(!Strings.isNullOrEmpty(_z)&&"null"!=_z){
            setGameZoneId(Integer.parseInt(_z.trim()));
        }
        if(!Strings.isNullOrEmpty(game)){
            setGame(game.toUpperCase().charAt(0));
        }
        if(!Strings.isNullOrEmpty(subGame)){
            setSubGame(Integer.parseInt(subGame));
        }
    }

    @Override
    public boolean isAvailableForSubmit() {
        boolean flag;
        flag = isValidGame();
        flag = flag && getSubGame() >= 0;
        flag = flag && getGameId() != 0;
        flag = flag && getGameZoneId() != 0;
//        flag = flag && (!Strings.isNullOrEmpty(getUserName()));
        flag = flag && null != getHandler();
        return flag;
    }

}
