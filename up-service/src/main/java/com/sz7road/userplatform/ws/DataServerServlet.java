package com.sz7road.userplatform.ws;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.MergerService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.web.Parameter;
import com.sz7road.web.pojos.MergerInfoBean;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author jiangfan.zhou
 */
@Singleton
public class DataServerServlet extends HeadlessServlet {

    private static final Logger log = LoggerFactory.getLogger(DataServerServlet.class.getName());

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        int gameId = request.getIntParameter("gameId");
        int serverId = request.getIntParameter("serverId");
        int serverNo = request.getIntParameter("serverNo");
        int num = request.getIntParameter("num");

        final PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            /*GameWithServerService service = getInstance(GameWithServerService.class);
            GameTable gameTable = service.getGameTable();
            GameTable.GameEntry gameEntry = gameTable.get(gameId);

            // 未排序，没有主区信息
            if (gameEntry != null) {
                if (serverId > 0) {
                    mapper.writeValue(out, gameTable.getServerTable(gameId).get(serverId));
                } else {
                    mapper.writeValue(out, gameTable.getServerTable(gameId));
                }
            } else {
                mapper.writeValue(out, null);
            }*/

            /*MergerService mService = getInstance(MergerService.class);
            List<MergerInfoBean> list = mService.list(gameId);
            if (serverId > 0 && list != null){
                for(MergerInfoBean entry : list) {
                    if (serverId == entry.getServerId()) {
                        mapper.writeValue(out, entry);
                        break;
                    }
                }
            } else if (serverNo > 0 && list != null) {
                if (serverNo <= list.size())
                    mapper.writeValue(out, list.get(serverNo - 1));
            } else {
                mapper.writeValue(out, list);
            }*/

            ServerDataService service = getInstance(ServerDataService.class);
            if (serverId > 0) {
                mapper.writeValue(out, service.get(gameId, serverId));
            } else if (serverNo > 0) {
                mapper.writeValue(out, service.list(gameId, serverNo));
            } else if (gameId > 0) {
                if (num > 0) {//获得某个游戏的num条数据
                    mapper.writeValue(out, service.listNumOfGame(gameId, num));
                } else {
                    mapper.writeValue(out, service.list(gameId));
                }
            } else if (num > 0) {//获得所有区服的num条数据
                mapper.writeValue(out, service.listNum(num));
            } else {
                mapper.writeValue(out, service.list());
            }
        } catch (final Exception e) {
            notFound(response);
            log.error("获取区服异常：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}