package com.sz7road.web.ws;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.ServerTableDecorateDao;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.pojos.ServerMaintain2;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.ConfigurationService;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.userplatform.service.ServerMaintainDecorateService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-11
 * Time: 下午2:08
 * 多游戏的维护增加更新接口的调用
 */
@Singleton
public class ServerMaintainForMultiGameServlet extends BaseServlet {
    @Inject
    private Provider<ServerMaintainDecorateService> serverMaintainServiceProvider;

    @Inject
    private Provider<ServerTableDecorateDao> serverTableDecorateDaoProvider;

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;
    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    private ServerMaintainDecorateService serverMaintainService;

    @Inject
    private Provider<ConfigurationService> configurationServiceProvider;

    //更新维护信息  对于allFlag和serverIdArrayStr两者二选一的情况
    public void updateMaintain(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");
        //1，获取参数
        String maintainStr = null;
        String allFlag = null;
        String serverIdArrayStr = null;
        String _g=null;
        try { //赋值
            maintainStr = request.getParameter("maintainJson");
//            maintainStr=  new String(maintainStr.getBytes("iso-8859-1"),"UTF-8");
            allFlag = request.getParameter("allFlag");
            serverIdArrayStr = request.getParameter("serverIdArray");
            _g=request.getParameter("gameId");
            log.info("ServerMaintainServlet的updateMaintain接收的参数是：maintainStr: " + maintainStr + " allFlag: " + allFlag + " serverIdArrayStr: " + serverIdArrayStr+" gameId:"+_g);
        } catch (Exception ex) {
            log.error("参数异常！");
            ex.printStackTrace();
        }

        Msg msg = new Msg();
        ObjectMapper mapper = new ObjectMapper();
        //传递过来的维护信息，除了服务器ID之外，组装成实体。
        if(!Strings.isNullOrEmpty(maintainStr))
        {
//            maintainStr=  new String(maintainStr.getBytes("iso-8859-1"),"UTF-8");
        ServerMaintain2 serverMaintainParam = mapper.readValue(maintainStr, ServerMaintain2.class);
        serverMaintainParam.setGameId(Ints.tryParse(_g));

        log.info("开始时间：" + CommonDateUtils.getDate(serverMaintainParam.getStartTime().getTime()) + " 结束时间：" + CommonDateUtils.getDate(serverMaintainParam.getEndTime().getTime()));
        boolean startTimeEqualEndTimeFlag = serverMaintainParam.getStartTime().getTime() == serverMaintainParam.getEndTime().getTime();
        final int gameId = serverMaintainParam.getGameId();
        serverMaintainService = serverMaintainServiceProvider.get();
        //拿到有效的维护信息map(serverId,维护实体)
        final Map<Integer, ServerMaintain2> serverMaintainMap = serverMaintainService.getMaintainServerInfoByGameId(gameId,new Timestamp(System.currentTimeMillis()));
        //拿到所有状态的服务器信息列表
        final List<ServerInfo> serverInfoList = gameWithServerDecorateServiceProvider.get().getServerListsByGameId(gameId);
        List<Integer> maintainStatusServerIds = Lists.newLinkedList();
        List<Integer> hotStatusServerIds = Lists.newLinkedList();
        if (serverInfoList != null && !serverInfoList.isEmpty()) {
            for (ServerInfo serverInfo : serverInfoList) {
                int status = serverInfo.getServerStatus();
                int serverId = serverInfo.getServerId();
                if (1 == status) {
                    hotStatusServerIds.add(serverId);
                } else if (-2 == status) {
                    maintainStatusServerIds.add(serverId);
                }
            }
        }
        List<Integer> maintainIds = Lists.newLinkedList();
        if (serverMaintainMap != null && !serverMaintainMap.isEmpty()) {
            for (ServerMaintain2 serverMaintain : serverMaintainMap.values()) {
                maintainIds.add(serverMaintain.getId());
            }
        }


        //2，验证  转换 分支
        if (!Strings.isNullOrEmpty(maintainStr) && !Strings.isNullOrEmpty(allFlag) && Strings.isNullOrEmpty(serverIdArrayStr)) { //所有服务区维护
            if ("all".equals(allFlag)) {
                if (startTimeEqualEndTimeFlag) {
                    msg = openAllServer(msg, serverMaintainParam, maintainStatusServerIds, maintainIds);
                } else {

                    msg = stopAllServer(msg, serverMaintainParam, hotStatusServerIds, serverInfoList);
                }
                log.info((startTimeEqualEndTimeFlag == true ? "全区开服" : "全区停服") + "执行结果：" + msg.getMsg());
            }
        }
        if (!Strings.isNullOrEmpty(maintainStr) && Strings.isNullOrEmpty(allFlag) && !Strings.isNullOrEmpty(serverIdArrayStr)) { //部分服务区维护

            int[] serverIdArray = mapper.readValue(serverIdArrayStr, int[].class);
            String logTitle = "部分开服";
            List<Integer> serverIdWantOpen = Lists.newLinkedList();
            List<Integer> serverIdWantStop = Lists.newLinkedList();
            List<Integer> serverIdInMaintainTable = Lists.newLinkedList();
            List<Integer> serverIdList=Lists.newLinkedList();
            if (serverIdArray != null && serverIdArray.length > 0) {
                for (int serverId : serverIdArray) {
                    if (maintainStatusServerIds != null && maintainStatusServerIds.contains(serverId)) {
                        serverIdWantOpen.add(serverId);
                    }
                    if (hotStatusServerIds != null && hotStatusServerIds.contains(serverId)) {
                        serverIdWantStop.add(serverId);
                    }
                    if (maintainIds != null && maintainIds.contains(serverId)) {
                        serverIdInMaintainTable.add(serverId);
                    }
                    serverIdList.add(serverId);
                }
            }

            if (startTimeEqualEndTimeFlag) {
                msg = openPartServer(msg, serverMaintainParam, serverIdWantOpen, serverIdList);
            } else {
                msg = stopPartServer(msg, serverMaintainParam, serverIdWantStop,serverIdArray);
                logTitle = "部分停服";
            }
            log.info(logTitle + "执行结果：" + msg.getMsg() + "-----服务器id是：" + serverIdArrayStr);
        }
            ConfigurationService configurationService = configurationServiceProvider.get();
            configurationService.updateTableTimestamp();
        } else
        {
            msg.setCode(204);
            msg.setMsg("传递的参数为空。");
        }
        // 5，返回  写回处理结果
        ServletUtil.returnJson(response, msg);
    }

    /**
     * 部分停服
     *
     * @return
     */
    private Msg stopPartServer(Msg msg, ServerMaintain2 serverMaintainParam, List<Integer> serverIdWantStop, int[] serverIdArray) throws Exception {

        List<ServerMaintain2> serverMaintainList = Lists.newLinkedList();
        if (serverIdArray != null && serverIdArray.length>0) {
            for (Integer serverId : serverIdArray) {
                ServerMaintain2 serverMaintain = new ServerMaintain2();
                serverMaintain.setStartTime(serverMaintainParam.getStartTime());
                serverMaintain.setEndTime(serverMaintainParam.getEndTime());
                serverMaintain.setMessage(serverMaintainParam.getMessage());
                serverMaintain.setCreateTime(new Timestamp(System.currentTimeMillis()));
                serverMaintain.setServerId(serverId);
                serverMaintain.setGameId(serverMaintainParam.getGameId());
                serverMaintainList.add(serverMaintain);
            }
            msg = serverMaintainService.addServerMaintainNotForSq(serverMaintainList);
        }
        final long startTime = serverMaintainParam.getStartTime().getTime();
        final long endTime = serverMaintainParam.getEndTime().getTime();
        final long current = System.currentTimeMillis();
        if (startTime <= current && serverIdWantStop != null && !serverIdWantStop.isEmpty()) {
            int updateToMaintainRel = serverMaintainService.batchUpdateServerStatusNotForSq(serverIdWantStop, -2, serverMaintainParam.getGameId());
            if (updateToMaintainRel > 0) {
                log.info("提前更新为维护状态成功!");
                msg.setCode(200);
                msg.setMsg("提前更新为维护状态成功");
            } else {
                log.info("提前更新为维护状态失败!");
                msg.setCode(204);
                msg.setMsg("提前更新为维护状态失败");
            }
        }
        return msg;
    }

    /**
     * 部分开服
     *
     * @return
     */
    private Msg openPartServer(Msg msg, ServerMaintain2 serverMaintainParam, List<Integer> serverIdWantOpen, List<Integer> serverIdInMaintainTable) throws Exception {
        // 开服把-2状态的服务器改为1的，更改服务器状态
      Timestamp current=   new Timestamp(new Date().getTime());
        if (serverIdWantOpen != null && !serverIdWantOpen.isEmpty()) {

                if (serverIdInMaintainTable != null && !serverIdInMaintainTable.isEmpty()) {
                    serverMaintainParam.setEndTime(current);
                    serverMaintainParam.setStartTime(current);
                    serverMaintainParam.setMessage("立即开服!" + serverMaintainParam.getEndTime());
                    msg = serverMaintainService.batchUpdateMaintainServersNotForSq(serverMaintainParam, serverIdInMaintainTable);
                }
                int updateToHotRel = serverMaintainService.batchUpdateServerStatusNotForSq(serverIdWantOpen, 1, serverMaintainParam.getGameId());
                log.info("立刻开服成功!");
                msg.setCode(200);
                msg.setMsg("更新服务器状态为1成功！");
            } else {
                log.info("立刻开服失败!");
                msg.setCode(204);
                msg.setMsg("更新服务器状态为1失败！");
            }
        return msg;
    }

    /**
     * 全区停服
     * @return
     */
    private Msg stopAllServer(Msg msg, ServerMaintain2 serverMaintainParam, List<Integer> hotStatusServersIds, List<ServerInfo> serverInfoList) throws Exception { //全区停服
        List<ServerMaintain2> serverMaintainList = Lists.newLinkedList();
        if (serverInfoList != null && !serverInfoList.isEmpty())
            for (ServerInfo serverInfo : serverInfoList) {
                ServerMaintain2 serverMaintain = new ServerMaintain2();
                serverMaintain.setStartTime(serverMaintainParam.getStartTime());
                serverMaintain.setEndTime(serverMaintainParam.getEndTime());
                serverMaintain.setMessage("全区维护：" + serverMaintainParam.getMessage());
                serverMaintain.setCreateTime(new Timestamp(new Date().getTime()));
                serverMaintain.setServerId(serverInfo.getServerId());
                serverMaintain.setGameId(serverMaintainParam.getGameId());
                serverMaintainList.add(serverMaintain);
            }
        //修改或者增加维护信息
        msg = serverMaintainService.addServerMaintainNotForSq(serverMaintainList);
        if (200 == msg.getCode()) {  //提前修改所有火爆状态的为维护状态
            if (serverMaintainParam.getStartTime().getTime() <= new Date().getTime() && !hotStatusServersIds.isEmpty()) {
                int updateServerStatusRel = serverMaintainService.batchUpdateServerStatusNotForSq(hotStatusServersIds, -2, serverMaintainParam.getGameId());
                if (updateServerStatusRel > 0) {
                    log.info("提前更新为维护状态成功!");
                } else {
                    log.info("提前更新为维护状态失败!");
                }
            }
        }
        return msg;
    }

    /**
     * 全区开服
     */

    private Msg openAllServer(Msg msg, ServerMaintain2 serverMaintainParam, List<Integer> maintainStatusServerIds, List<Integer> maintainIds) throws Exception {

        serverMaintainParam.setEndTime(new Timestamp(System.currentTimeMillis()));
        serverMaintainParam.setMessage("全区立即开服!" + serverMaintainParam.getEndTime());
        //修改所有维护状态的服务器状态为火爆状态
        int updateServerStatusRel = 0;
        try {
            if (!maintainStatusServerIds.isEmpty())
                updateServerStatusRel = serverMaintainService.batchUpdateServerStatusNotForSq(maintainStatusServerIds, 1, serverMaintainParam.getGameId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //修改维护信息
        if (updateServerStatusRel > 0) {
            if (!maintainStatusServerIds.isEmpty()) {
                serverMaintainParam.setEndTime(new Timestamp(System.currentTimeMillis()));
                serverMaintainParam.setStartTime(new Timestamp(System.currentTimeMillis()));
                msg = serverMaintainService.batchUpdateMaintainServersNotForSqByIds(serverMaintainParam, maintainIds);
            } else {
                msg.setCode(200);
                msg.setMsg("没有需要修改的维护信息!");
            }
        } else {
            msg.setCode(204);
            msg.setMsg("全区开服修改状态失败或者没有需要开启的服务区!");
        }
        return msg;
    }

    /**
     * 拿到非关闭状态下的服务器id列表
     *
     * @param serversExceptClose
     * @return
     */
    private int[] getNonClosedServerId(Map<Integer, ServerEntity> serversExceptClose) {
        int[] serverIdArray = null;
        if (serversExceptClose != null && !serversExceptClose.isEmpty()) {
            int size = serversExceptClose.keySet().size();
            serverIdArray = new int[size];
            int i = 0;
            for (Integer id : serversExceptClose.keySet()) {
                ServerEntity serverEntry = serversExceptClose.get(id);
                serverIdArray[i] = serverEntry.getId();
                i++;
            }
        }
        return serverIdArray;
    }


    /**
     * 判断维护列表中是不是存在serverId为 element的
     *
     * @param element
     * @param serverMaintains
     * @return
     */
    private boolean containInt(int element, Collection<ServerMaintain2> serverMaintains) {
        boolean flag = false;
        for (ServerMaintain2 serverMaintain : serverMaintains) {
            if (element == serverMaintain.getServerId()) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    //查询维护服务器信息
    public void queryMaintainInfoByServerIdList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1，获取参数
        String ServerIdList = request.getParameter("ServerIdList");
        String gameId = request.getParameter("gid");
        //2，验证
        if (Strings.isNullOrEmpty(ServerIdList) || Strings.isNullOrEmpty(gameId) || Integer.parseInt(gameId) <= 0) {
            log.error("参数ServerIdList,gid非法！");
            return;
        }
        //3，转换
        ObjectMapper mapper = new ObjectMapper();
        int[] serverIdArray = mapper.readValue(ServerIdList, int[].class);
        //4，处理
        ServerMaintainDecorateService serverMaintainService = serverMaintainServiceProvider.get();
        Msg msg = serverMaintainService.queryMaintainServersByIdListAndGameId(serverIdArray, Integer.parseInt(gameId));
        // 5，返回  写回处理结果
//        log.info("查询以下服务器的维护信息：" + ServerIdList);
        ServletUtil.returnJson(response, msg);
    }

    //查询维护服务器信息
    public void deleteFutureMaintainInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1，获取参数
        String ServerIdList = request.getParameter("ServerIdArray");
        String gameId = request.getParameter("gid");
        log.info("删除没到时间的维护信息接受的参数：serverIdArray:" + ServerIdList + " gid:" + gameId);
        //2，验证
        if (Strings.isNullOrEmpty(ServerIdList)) {
            log.info("参数ServerIdArray为空！");
            return;
        }
        //3，转换
        ObjectMapper mapper = new ObjectMapper();
        int[] serverIdArray = mapper.readValue(ServerIdList, int[].class);
        //4，处理
        ServerMaintainDecorateService serverMaintainService = serverMaintainServiceProvider.get();
        Msg msg = serverMaintainService.batchDeleteFutureMaintainInfoByGameId(serverIdArray, Integer.parseInt(gameId));
        // 5，返回  写回处理结果
//        log.info("查询以下服务器的维护信息：" + ServerIdList);
        ServletUtil.returnJson(response, msg);
    }
}
