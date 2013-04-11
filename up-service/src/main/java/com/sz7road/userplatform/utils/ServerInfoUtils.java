package com.sz7road.userplatform.utils;

import com.google.common.base.Strings;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;

import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-29
 * Time: 上午8:33
 * Description: 对服务器信息的一些操作方法
 */
public class ServerInfoUtils {

    public final static String SQ_GAME_PLAY_GAME_URL = "sq.game.playGameUrl";

    private final static int SQ_GAME_ID=1;
    /**
     * 得到服务器的主服务器编号
     *
     * @param serverEntry
     * @return
     */
    public static int getMainServerNo(ServerTable.ServerEntry serverEntry) {
        int serverNo = 0;
        if (serverEntry != null && serverEntry.getVariables() != null&&serverEntry.getVariables().containsKey(SQ_GAME_PLAY_GAME_URL)) {
            String config = serverEntry.getVariables().get(SQ_GAME_PLAY_GAME_URL);
            //http://s141.shenquol.com/client/game.jsp 截取serverNo
            if (!Strings.isNullOrEmpty(config)&&config.contains(".")) {
                config = config.trim();
                serverNo = Integer.parseInt(config.substring("http://s".length(), config.indexOf('.')));
            }
        }
        return serverNo;
    }

    /**
     * no和Id的map 方便查找
     *
     * @param serverEntryList
     * @return
     */
    public static Map<Integer, Integer> getIdAndNoServerMap(Collection<ServerTable.ServerEntry> serverEntryList) {
        Map<Integer, Integer> idAndNoServerMap = new HashMap<Integer, Integer>();
        if (serverEntryList != null && !serverEntryList.isEmpty()) {
            for (ServerTable.ServerEntry entry : serverEntryList) {
                if (entry.getGameId()==SQ_GAME_ID&&(entry.getServerStatus() == 1 || entry.getServerStatus() == -2)) {
                    idAndNoServerMap.put(entry.getServerNo(), entry.getId());
                }
            }
        }
        return idAndNoServerMap;
    }
}
