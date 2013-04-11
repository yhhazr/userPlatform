package com.sz7road.web.ws;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.*;
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
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-25
 * Time: 下午3:18
 * 开服相关的接口
 */
@Singleton
public class GameWithServerForMultiGameServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(GameWithServerForMultiGameServlet.class.getName());

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;
    @Inject
    private Provider<ConfigurationService> configurationServiceProvider;

    //增加服务器的同时，改变缓存
    public void add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=utf8");
        Msg msg = new Msg(0, "添加成功");
        try {
            String reqJson = request.getParameter("reqJson");
            log.info("before:add方法传递过来的参数："+reqJson);
//            reqJson= new String(reqJson.getBytes("iso-8859-1"),"UTF-8");
//            log.info("after:add方法传递过来的参数："+reqJson);
            final ObjectMapper mapper = new ObjectMapper();
            ServerTableNotForSq.ServerEntryNotForSq[] serverEntries = mapper.readValue(reqJson, ServerTableNotForSq.ServerEntryNotForSq[].class);
            GameWithServerDecorateService gameWithServerService = gameWithServerDecorateServiceProvider.get();
            List<ServerTableNotForSq.ServerEntryNotForSq> list = Lists.newArrayList();
            int ret = 0;
            for (ServerTableNotForSq.ServerEntryNotForSq entry : serverEntries) {
                ServerTableNotForSq.ServerEntryNotForSq serverEntry = gameWithServerService.getServerEntryByGameIdAndServerId(entry.getGameId(), entry.getServerId());
                if (serverEntry != null && serverEntry.getStatus() == -1) {
                    list.add(entry);
                } else {
                    ret = gameWithServerService.addGameServerNotForSq(entry);
                }
            }
            if (list.size() > 0) {
                ret = gameWithServerService.batchUpdateNotForSq(list.toArray(new ServerTableNotForSq.ServerEntryNotForSq[list.size()]));
            }
            if (ret <= 0) {
                throw new IllegalArgumentException("添加非神曲游戏服务区出现异常");
            }
            else
            {
            ConfigurationService configurationService = configurationServiceProvider.get();
            configurationService.updateTableTimestamp();
            }
        } catch (Exception e) {
            msg.build(1, "添加非神曲游戏区出现异常:请保证每个游戏的serverid和serverNo不重复");
            log.error("添加非神曲游戏区出现异常");
            e.printStackTrace();

        }
        render(msg, response);
    }


    public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        Msg msg = new Msg(0, "更新成功");
        try {
            String reqJson = request.getParameter("reqJson");
//            log.info("before:"+reqJson);
//            reqJson=  new String(reqJson.getBytes("iso-8859-1"),"UTF-8");
            log.info("update方法传递过来的参数：after:" + reqJson);
            final ObjectMapper mapper = new ObjectMapper();
            ServerTableNotForSq.ServerEntryNotForSq[] serverEntries = mapper.readValue(reqJson, ServerTableNotForSq.ServerEntryNotForSq[].class);
            GameWithServerDecorateService gameWithServerService = gameWithServerDecorateServiceProvider.get();
            ServerTableNotForSq.ServerEntryNotForSq[] newServerEntry = null;
            if (serverEntries != null && serverEntries.length > 0) {
                newServerEntry = new ServerTableNotForSq.ServerEntryNotForSq[serverEntries.length];
                for (int i = 0; i < serverEntries.length; i++) {
                    ServerTableNotForSq.ServerEntryNotForSq currServerEntry = gameWithServerService.getServerEntryByGameIdAndServerId(serverEntries[i].getGameId(), serverEntries[i].getServerId());
                    newServerEntry[i] = buildServerEntry(serverEntries[i], currServerEntry);
                }
            }
            int ret = gameWithServerService.batchUpdateNotForSq(newServerEntry);
            log.info("非神曲游戏服务区执行修改服务区信息结果:{}", (ret > 0) ? "成功" : "失败");
            if (ret == 0) {
                msg.build(1, "更新失败");
            } else {
                ConfigurationService configurationService = configurationServiceProvider.get();
                configurationService.updateTableTimestamp();
            }
        } catch (Exception e) {
            msg.build(1, "更新非神曲游戏服务区失败"+e.getMessage());
            log.error("更新非神曲游戏服务区列表网关异常：{}", e.getMessage());
        }
        render(msg, response);
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
        final ObjectMapper mapper = new ObjectMapper();
        final PrintWriter out = response.getWriter();
        try {
            int gameId = Integer.parseInt(request.getParameter("_g"));
            int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            String order = request.getParameter("order");
            String sortStr = request.getParameter("sort");
            log.info("list方法接受的参数：gameId: " + gameId + "  pageIndex:" + pageIndex + "  pageSize:" + pageSize + "  order:" + order + "  sort:" + sortStr);
            int sort = 0;
            if (!Strings.isNullOrEmpty(sortStr)) {
                sort = Integer.parseInt(sortStr);
            }
            String query = request.getParameter("query");
            Map<String, Object> queryMap = null;
            if (!Strings.isNullOrEmpty(query)) {
                queryMap = mapper.readValue(query, Map.class);
            }
            if (pageIndex <= 0) pageIndex = 1;
            int start = (pageIndex - 1) * pageSize;
            ListData<ServerTableNotForSq.ServerEntryNotForSq> listData = gameWithServerDecorateService.listNotForSq(gameId, order, sort, start, pageSize, queryMap);

            response.setContentType("application/json");
                mapper.writeValue(out, transToServerEntity(listData));
        } catch (final Exception e) {
            mapper.writeValue(out,"获取非神曲游戏服务区列表网关异常:"+e.getMessage() );
            log.error("获取非神曲游戏服务区列表网关异常：{}" + e.getMessage(), e);
        }
        finally {
            out.flush();
            out.close();
        }
    }

    private ListData<ServerTableNotForSqEntity.ServerEntryNotForSqEntity> transToServerEntity(ListData<ServerTableNotForSq.ServerEntryNotForSq> serverEntryListData) {
        List<ServerTableNotForSq.ServerEntryNotForSq> serverEntryList = serverEntryListData.getList();
        List<ServerTableNotForSqEntity.ServerEntryNotForSqEntity> serverEntityList = Lists.newLinkedList();
        if (serverEntryListData != null && serverEntryList != null && !serverEntryList.isEmpty()) {

            for (ServerTableNotForSq.ServerEntryNotForSq entry : serverEntryList) {
                ServerTableNotForSqEntity.ServerEntryNotForSqEntity serverEntity = new ServerTableNotForSqEntity.ServerEntryNotForSqEntity();
                serverEntity.setId(entry.getId());
                serverEntity.setServerId(entry.getServerId());
                serverEntity.setMainId(entry.getMainId());
                serverEntity.setCreateTime(entry.getCreateTime());
                serverEntity.setGameId(entry.getGameId());
                serverEntity.setOpenTime(entry.getOpenTime());
                serverEntity.setRecommand(entry.isRecommand());
                serverEntity.setServerName(entry.getServerName());
                serverEntity.setServerNo(entry.getServerNo());
                serverEntity.setStatus(entry.getStatus());
                serverEntityList.add(serverEntity);
            }
        }

        return new ListData<ServerTableNotForSqEntity.ServerEntryNotForSqEntity>(serverEntityList, serverEntryListData.getTotal());
    }

    public void us(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg(0, "更新服务区状态成功");
        try {
            String reqJson = request.getParameter("reqJson");
            String _s = request.getParameter("s");
            String _g=request.getParameter("_g");
            log.info("GameWithServerServlet的us方法的参数：reqJson: " + reqJson + " s: " + _s+" _g:"+_g);
            if (Strings.isNullOrEmpty(_s) || !DataUtil.isLegalStatus(_s)||Strings.isNullOrEmpty(_g)||Integer.parseInt(_g)<=0) {
                throw new IllegalArgumentException("参数错误非法");
            }
            int ret = 0;
            int status = 0;
            final ObjectMapper mapper = new ObjectMapper();
            List<Integer> ids = mapper.readValue(reqJson, List.class);
            final GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
            if (!Strings.isNullOrEmpty(_s) && DataUtil.isLegalStatus(_s) && ids != null && !ids.isEmpty()) {
                status = Integer.parseInt(_s);
                ret = gameWithServerDecorateService.batchUpdateStatusNotForSq(ids, status,Integer.parseInt(_g));
            }
            log.info("非神曲游戏服务区执行修改状态结果:ret = {}", ret);
//            if (ret > 0) {
//                ConfigurationService configurationService = configurationServiceProvider.get();
//                configurationService.updateTableTimestamp();
//            }
        } catch (Exception e) {
            msg.build(1, e.getMessage());
            log.error("更新非神曲游戏服务区状态出现异常：{}", e.getMessage());
            e.printStackTrace();
        }
        render(msg, response);
    }

    private ServerTableNotForSq.ServerEntryNotForSq buildServerEntry(ServerTableNotForSq.ServerEntryNotForSq serverEntry, ServerTableNotForSq.ServerEntryNotForSq currServerEntry) {
        if (serverEntry != null && currServerEntry != null) {
//            if (serverEntry.getGameId() > 0 && serverEntry.getGameId() != currServerEntry.getGameId()) {
//                currServerEntry.setGameId(serverEntry.getGameId());
//            }
//
//            if (serverEntry.getServerId() > 0 && serverEntry.getServerId() != currServerEntry.getServerId()) {
//                currServerEntry.setServerId(serverEntry.getServerId());
//            }

            if (serverEntry.getServerNo() > 0 && serverEntry.getServerNo() != currServerEntry.getServerNo()) {
                currServerEntry.setServerNo(serverEntry.getServerNo());
            }
            if (!Strings.isNullOrEmpty(serverEntry.getServerName()) && !serverEntry.getServerName().equals(currServerEntry.getServerName())) {
                currServerEntry.setServerName(serverEntry.getServerName());
            }
            if (serverEntry.getStatus() != currServerEntry.getStatus()) {
                currServerEntry.setStatus(serverEntry.getStatus());
            }

            if (serverEntry.getOpenTime() != null) {
                currServerEntry.setOpenTime(serverEntry.getOpenTime());
            }
            if (serverEntry.isRecommand() != currServerEntry.isRecommand()) {
                currServerEntry.setRecommand(serverEntry.isRecommand());
            }
//            if (serverEntry.getMainServerId()>0&&serverEntry.getMainServerId() != currServerEntry.getMainServerId()) {
//                currServerEntry.setMainServerId(serverEntry.getMainServerId());
//            }

            return currServerEntry;
        }
        return null;
    }
}
