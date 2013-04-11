package com.sz7road.userplatform.ws;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.ServerTableDao;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.LoginGameService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.utils.JedisFactory;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-25
 * Time: 上午3:07
 * 测试缓存机制servlet
 */
@Singleton
public class TestCacheServlet extends HeadlessHttpServlet {

    Logger log = LoggerFactory.getLogger(TestCacheServlet.class);

    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;


    @Inject
    private Provider<ServerDataService> serverDataServiceProvider;

    @Inject

    private Provider<LoginGameService> loginGameServiceProvider;

    ObjectMapper mapper = new ObjectMapper();
    @Inject
    private Provider<ServerTableDao> serverTableDaoProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String type = request.getParameter("type");
            String gameId = request.getParameter("gid");
            String serverNo = request.getParameter("sno");
            String serverId = request.getParameter("sid");
            String userId = request.getParameter("uid");
            if ("reload".equals(type)) {
                boolean flag = cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntries();
                boolean flag2 = cacheDataItemsServiceProvider.get().reloadCacheDataOfGameEntries();
                boolean flag3 = cacheDataItemsServiceProvider.get().reloadCacheDataOfServerEntriesNotForSq();
                boolean flag4 = cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainInfo(new Timestamp(System.currentTimeMillis()));
                printTowebPage(response, flag && flag2 && flag3 && flag4);
            } else if ("getServer".equals(type)) {
                printTowebPage(response, cacheDataItemsServiceProvider.get().getServerEntriesFromCache());
            } else if ("getServerNotForSq".equals(type)) {
                printTowebPage(response, cacheDataItemsServiceProvider.get().getServerEntriesFromCacheNotForSq());
            } else if ("getGame".equals(type)) {
                printTowebPage(response, cacheDataItemsServiceProvider.get().getGameEntriesFromCache());
            } else if ("showServer".equals(type)) {
                printTowebPage(response, serverTableDaoProvider.get().listsWithStatus());
            } else if ("listServer".equals(type)) {
                printTowebPage(response, serverDataServiceProvider.get().get(Integer.parseInt(gameId), Integer.parseInt(serverId)));
            } else if ("gameHistory".equals(type)) {
                printTowebPage(response, loginGameServiceProvider.get().getGameHistoryByUserId(Integer.parseInt(userId)));
            } else if ("listMaintain".equals(type)) {
                if (Strings.isNullOrEmpty(gameId)) {
                    printTowebPage(response, cacheDataItemsServiceProvider.get().getAllServerMaintainInfoFromCache(new Timestamp(System.currentTimeMillis())));
                } else {
                    printTowebPage(response, cacheDataItemsServiceProvider.get().getAllServerMaintainInfoFromCache(Ints.tryParse(gameId), new Timestamp(System.currentTimeMillis())));
                }
            }
        } catch (Exception ex) {
            log.info("测试缓存异常!");
            ex.printStackTrace();
        }
    }

    public List<String> getTestData() {
        JedisFactory factory = new JedisFactory();
        Jedis jedis = factory.getJedisInstance();
        jedis.lpush("lists", "vector");
        jedis.lpush("lists", "ArrayList");
        jedis.lpush("lists", "LinkedList");
        return jedis.lrange("lists", 0, -1);
    }

    public boolean putIntoServerInfo() {
        return false;
    }


    public void printTowebPage(HttpServletResponse response, Object serverEntrySet) {
        PrintWriter out = null;
        response.setContentType("application/json");
        try {
            out = response.getWriter();

            mapper.writeValue(out, serverEntrySet);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            out.flush();
            out.close();
        }
    }

    //拿到排好序的服务器列表
    public List<ServerTable.ServerEntry> getServers(String gameId) {

        GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
        Collection<ServerTable.ServerEntry> serverEntryCollection = gameWithServerService.getGameTable().getServerTable(Integer.parseInt(gameId)).values();
        List<ServerTable.ServerEntry> serverEntryList = new ArrayList<ServerTable.ServerEntry>();
        for (ServerTable.ServerEntry serverEntry : serverEntryCollection)
            if (serverEntry.getServerStatus() == 1 || serverEntry.getServerStatus() == -2)
                serverEntryList.add(serverEntry);
        return serverEntryList;
    }
}
