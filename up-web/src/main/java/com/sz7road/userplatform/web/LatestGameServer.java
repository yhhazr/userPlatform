package com.sz7road.userplatform.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-7-9
 * Time: 上午10:28
 */
@Singleton
public class LatestGameServer extends HeadlessServlet {

    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        int gameId = request.getIntParameter("gameId");
        GameTable.GameEntry gameEntry = null;
        if (gameId > 0) {
            gameEntry = getGameEntry(gameId);
        } else {
            gameEntry = getNewlyGameEntry();
        }

        GameTable gameTable = gameWithServerServiceProvider.get().getGameTable();
        ServerTable serverTable = gameTable.getServerTable(gameEntry);
        ServerTable.ServerEntry serverEntry = getNewlyServerEntry(serverTable);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        try{
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, DataUtil.transToServerEntity(serverEntry));
        }catch (Exception e){
            notFound(response);
        }finally {
            out.flush();
            out.close();
        }
    }

    private GameTable.GameEntry getGameEntry(int gameId) {
        GameWithServerService gws = gameWithServerServiceProvider.get();
        //取最新的游戏对象
        GameTable gameTable = gws.getGameTable();
        return gameTable.get(gameId);
    }

    private GameTable.GameEntry getNewlyGameEntry() {
        GameWithServerService gws = gameWithServerServiceProvider.get();
        //取最新的游戏对象
        GameTable gameTable = gws.getGameTable();
        int gameId = 0;
        GameTable.GameEntry gameEntry = null;
        for (Map.Entry<Integer, GameTable.GameEntry> e : gameTable.entrySet()) {
            if (e.getKey() > gameId) {
                gameEntry = e.getValue();
                gameId = e.getKey();
            }
        }
        return gameEntry;
    }

    private ServerTable.ServerEntry getNewlyServerEntry(ServerTable serverTable) {
        int serverNo = 0;
        ServerTable.ServerEntry serverEntry = null;
        if (serverTable != null) {
            for (Map.Entry<Integer, ServerTable.ServerEntry> entry : serverTable.entrySet()) {
                ServerTable.ServerEntry tmp = entry.getValue();
                if (tmp.getServerNo() > serverNo && tmp.getServerStatus()==1&&tmp.getOpeningTime().before(new Timestamp(System.currentTimeMillis()))) {
                    serverNo = tmp.getServerNo();
                    serverEntry = tmp;
                }
            }
        }
        return serverEntry;
    }
}
