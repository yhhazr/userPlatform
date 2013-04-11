package com.sz7road.userplatform.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Singleton
public class LatestGameServer2 extends HeadlessServlet {

    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        int gameId = request.getIntParameter("gameId");
        PrintWriter out = response.getWriter();
        try{
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, getInstance(ServerDataService.class).getNewOpenedServer(gameId));
        }catch (Exception e){
            notFound(response);
            e.printStackTrace();
        }finally {
            out.flush();
            out.close();
        }
    }
}
