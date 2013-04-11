package com.sz7road.userplatform.ws;


import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.service.GameWithServerService;
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

/**
 * @author jiangfan.zhou
 */
@Singleton
public class DataGameServlet extends HeadlessServlet {

    private static final Logger log = LoggerFactory.getLogger(DataGameServlet.class.getName());

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        int gameId = request.getIntParameter("gameId");

        final PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            GameWithServerService service = getInstance(GameWithServerService.class);
            GameTable gameTable = service.getGameTable();
            if (gameId <= 0) {
                mapper.writeValue(out, gameTable);
            } else {
               mapper.writeValue(out, gameTable.get(gameId));
            }
        } catch (final Exception e) {
            notFound(response);
            log.error("获取游戏异常：{}", e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }
}