package com.sz7road.web.ws;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.ConfigurationService;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.utils.ListData;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.DataUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-25
 * Time: 下午3:18
 * 开发相关接口
 */
@Singleton
public class GameWithServerServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(GameWithServerServlet.class.getName());

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;
    @Inject
    private Provider<ConfigurationService> configurationServiceProvider;

    //增加服务器的同时，改变缓存
    public void add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg(0, "添加成功");
        try {
            String reqJson = request.getParameter("reqJson");
            final ObjectMapper mapper = new ObjectMapper();
            ServerTable.ServerEntry[] serverEntries = mapper.readValue(reqJson, ServerTable.ServerEntry[].class);
            GameWithServerDecorateService gameWithServerService = gameWithServerDecorateServiceProvider.get();
            List<ServerTable.ServerEntry> list = Lists.newArrayList();
            int ret = 0;
            for (ServerTable.ServerEntry entry : serverEntries) {
                ServerTable.ServerEntry serverEntry = gameWithServerService.getServerEntryById(entry.getId());
                if (serverEntry != null && serverEntry.getServerStatus() == -1) {
                    list.add(entry);
                } else {
                    ret = gameWithServerService.addGameServer(entry);
                }
            }
            if (list.size() > 0) {
                ret = gameWithServerService.batchUpdate(list.toArray(new ServerTable.ServerEntry[list.size()]));
            }
            if (ret <= 0) {
                throw new IllegalArgumentException("添加游戏服务区出现异常");
            }
            else
            {
            ConfigurationService configurationService = configurationServiceProvider.get();
            configurationService.updateTableTimestamp();
            }
        } catch (Exception e) {
            msg.build(1, "添加游戏区出现异常");
            log.error("添加游戏区出现异常");
            e.printStackTrace();

        }
        render(msg, response);
    }


    public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg(0, "更新成功");
        try {
            String reqJson = request.getParameter("reqJson");
            log.info("传递过来的参数：" + reqJson);
            final ObjectMapper mapper = new ObjectMapper();
            ServerTable.ServerEntry[] serverEntries = mapper.readValue(reqJson, ServerTable.ServerEntry[].class);
            GameWithServerDecorateService gameWithServerService = gameWithServerDecorateServiceProvider.get();
            ServerTable.ServerEntry[] newServerEntry = null;
            if (serverEntries != null && serverEntries.length > 0) {
                newServerEntry = new ServerTable.ServerEntry[serverEntries.length];
                for (int i = 0; i < serverEntries.length; i++) {
                    ServerTable serverTable = gameWithServerService.getGameTable().getServerTable(serverEntries[i].getGameId());
                    ServerTable.ServerEntry currServerEntry = serverTable.get(serverEntries[i].getId());
                    newServerEntry[i] = buildServerEntry(serverEntries[i], currServerEntry);
                }
            }
            int ret = gameWithServerService.batchUpdate(newServerEntry);
            log.info("游戏服务区执行修改服务区信息结果:{}", (ret > 0) ? "成功" : "失败");
            if (ret == 0) {
                msg.build(1, "更新失败");
            } else {
                ConfigurationService configurationService = configurationServiceProvider.get();
                configurationService.updateTableTimestamp();
            }
        } catch (Exception e) {
            msg.build(1, "更新游戏服务区失败");
            log.error("更新游戏服务区列表网关异常：{}", e.getMessage());
        }
        render(msg, response);
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
        try {
            int gameId = Integer.parseInt(request.getParameter("_g"));
            int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            String order = request.getParameter("order");
            String sortStr = request.getParameter("sort");
            log.info("gameId:" + gameId + "pageIndex:" + pageIndex + "pageSize:" + pageSize + "order:" + order + "sort:" + sortStr);
            int sort = 0;
            if (!Strings.isNullOrEmpty(sortStr)) {
                sort = Integer.parseInt(sortStr);
            }
            String query = request.getParameter("query");
            Map<String, Object> queryMap = null;
            if (!Strings.isNullOrEmpty(query)) {
                ObjectMapper mapper = new ObjectMapper();
                queryMap = mapper.readValue(query, Map.class);
            }
            if (pageIndex <= 0) pageIndex = 1;
            int start = (pageIndex - 1) * pageSize;
            ListData<ServerTable.ServerEntry> listData = gameWithServerDecorateService.list(gameId, order, sort, start, pageSize, queryMap);
            final PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            try {
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(out, transToServerEntity(listData));
            } finally {
                out.flush();
                out.close();
            }
        } catch (final Exception e) {
            log.error("获取游戏服务区列表网关异常：{}" + e.getMessage(), e);
        }
    }

    private ListData<ServerEntity> transToServerEntity(ListData<ServerTable.ServerEntry> serverEntryListData) {
        List<ServerTable.ServerEntry> serverEntryList = serverEntryListData.getList();
        List<ServerEntity> serverEntityList = new LinkedList<ServerEntity>();
        if (serverEntryListData != null && serverEntryList != null && !serverEntryList.isEmpty()) {

            for (ServerTable.ServerEntry entry : serverEntryList) {
                ServerEntity serverEntity = new ServerEntity();
                serverEntity.setId(entry.getId());
                serverEntity.setCreateTime(entry.getCreateTime());
                serverEntity.setGameId(entry.getGameId());
                serverEntity.setOpeningTime(entry.getOpeningTime());
                serverEntity.setRecommand(entry.isRecommand());
                serverEntity.setServerName(entry.getServerName());
                serverEntity.setServerNo(entry.getServerNo());
                serverEntity.setServerStatus(entry.getServerStatus());
                serverEntityList.add(serverEntity);
            }
        }

        return new ListData<ServerEntity>(serverEntityList, serverEntryListData.getTotal());
    }

    public void us(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg(0, "更新服务区状态成功");
        try {
            String reqJson = request.getParameter("reqJson");
            String _s = request.getParameter("s");
            log.info("GameWithServerServlet的us方法的参数：reqJson: " + reqJson + " s: " + _s);
            if (Strings.isNullOrEmpty(_s) || !DataUtil.isLegalStatus(_s)) {
                throw new IllegalArgumentException("参数错误");
            }
            int ret = 0;
            int status = 0;
            final ObjectMapper mapper = new ObjectMapper();
            List<Integer> ids = mapper.readValue(reqJson, List.class);
            final GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
            if (!Strings.isNullOrEmpty(_s) && DataUtil.isLegalStatus(_s) && ids != null && !ids.isEmpty()) {
                status = Integer.parseInt(_s);
                ret = gameWithServerDecorateService.batchUpdateStatus(ids, status);
            }
            log.info("游戏服务区执行修改状态结果:ret = {}", ret);
//            if (ret > 0) {
//                ConfigurationService configurationService = configurationServiceProvider.get();
//                configurationService.updateTableTimestamp();
//            }
        } catch (Exception e) {
            msg.build(1, e.getMessage());
            log.error("更新服务区状态出现异常：{}", e.getMessage());
            e.printStackTrace();
        }
        render(msg, response);
    }

    private ServerTable.ServerEntry buildServerEntry(ServerTable.ServerEntry serverEntry, ServerTable.ServerEntry currServerEntry) {
        if (serverEntry != null && currServerEntry != null) {
            if (serverEntry.getGameId() > 0 && serverEntry.getGameId() != currServerEntry.getGameId()) {
                currServerEntry.setGameId(serverEntry.getGameId());
            }
            if (serverEntry.getServerNo() > 0 && serverEntry.getServerNo() != currServerEntry.getServerNo()) {
                currServerEntry.setServerNo(serverEntry.getServerNo());
            }
            if (!Strings.isNullOrEmpty(serverEntry.getServerName()) && !serverEntry.getServerName().equals(currServerEntry.getServerName())) {
                currServerEntry.setServerName(serverEntry.getServerName());
            }
            if (serverEntry.getServerStatus() != currServerEntry.getServerStatus()) {
                currServerEntry.setServerStatus(serverEntry.getServerStatus());
            }
            if (serverEntry.getCreateTime() != null) {
                currServerEntry.setCreateTime(serverEntry.getCreateTime());
            }
            if (serverEntry.getOpeningTime() != null) {
                currServerEntry.setOpeningTime(serverEntry.getOpeningTime());
            }
            if (serverEntry.isRecommand() != currServerEntry.isRecommand()) {
                currServerEntry.setRecommand(serverEntry.isRecommand());
            }
            return currServerEntry;
        }
        return null;
    }
}
