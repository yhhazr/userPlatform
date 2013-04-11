package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.ServerTableDecorateDao;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.userplatform.service.ServerMaintainDecorateService;
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
 * 维护增加更新接口的调用
 */
@Singleton
public class ServerMaintainServlet extends BaseServlet {
    @Inject
    private Provider<ServerMaintainDecorateService> serverMaintainServiceProvider;

    @Inject
    private Provider<ServerTableDecorateDao> serverTableDecorateDaoProvider;

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;
    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    //更新维护信息  对于allFlag和serverIdArrayStr两者二选一的情况
    public void updateMaintain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1，获取参数
        String maintainStr = null;
        String allFlag = null;
        String serverIdArrayStr = null;
        try { //赋值
            maintainStr = request.getParameter("maintainJson");
            allFlag = request.getParameter("allFlag");
            serverIdArrayStr = request.getParameter("serverIdArray");
            log.info("ServerMaintainServlet的updateMaintain接收的参数是：maintainStr: " + maintainStr + " allFlag: " + allFlag + " serverIdArrayStr: " + serverIdArrayStr);
        } catch (Exception ex) {
            log.info("参数异常！");
        }

        Msg msg = new Msg();
        ObjectMapper mapper = new ObjectMapper();
        //传递过来的维护信息，除了服务器ID之外，组装成实体。
        ServerMaintain serverMaintainInfoExceptServerId = mapper.readValue(maintainStr, ServerMaintain.class);
        boolean startTimeEqualEndTimeFlag = serverMaintainInfoExceptServerId.getStartTime().getTime() == serverMaintainInfoExceptServerId.getEndTime().getTime();
        ServerMaintainDecorateService serverMaintainService = serverMaintainServiceProvider.get();
        //2，验证  转换 分支
        if (!Strings.isNullOrEmpty(maintainStr) && !Strings.isNullOrEmpty(allFlag) && Strings.isNullOrEmpty(serverIdArrayStr)) { //所有服务区维护
            if ("all".equals(allFlag)) {
                msg = updateAll(msg, serverMaintainInfoExceptServerId, serverMaintainService, startTimeEqualEndTimeFlag);
                log.info((startTimeEqualEndTimeFlag == true ? "全区开服" : "全区停服") + "执行结果：" + msg.getMsg());
            }
        }
        if (!Strings.isNullOrEmpty(maintainStr) && Strings.isNullOrEmpty(allFlag) && !Strings.isNullOrEmpty(serverIdArrayStr)) { //部分服务区维护
            int[] serverIdArray = mapper.readValue(serverIdArrayStr, int[].class);
            msg = update(msg, serverMaintainInfoExceptServerId, serverMaintainService, serverIdArray);
            log.info((startTimeEqualEndTimeFlag == true ? "部分开服" : "部分停服") + "执行结果：" + msg.getMsg() + "-----服务器id是：" + serverIdArrayStr);
        }


        // 5，返回  写回处理结果
        ServletUtil.returnJson(response, msg);
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
     * 更新操作
     *
     * @param msg                          消息实体
     * @param serverMaintainExceptServerId 维护信息
     * @param serverMaintainService
     * @param serverIdArray                要维护的服务器ID数组
     * @return
     * @throws SQLException
     */
    private Msg update(Msg msg, ServerMaintain serverMaintainExceptServerId, ServerMaintainDecorateService serverMaintainService, int[] serverIdArray) throws SQLException {
        Map<Integer, ServerMaintain> serverMaintainMap = serverMaintainService.getMaintainServerInfo();//拿到正在维护的维护信息列表
        List<Integer> overDue = new ArrayList<Integer>();//存放过期的 ServerId
        List<Integer> maintainIds = new ArrayList<Integer>();//存放在维护的或者将要维护的维护信息id
        List<Integer> maintainServerIds = new ArrayList<Integer>();
        boolean serverMaintainNotNullFlag = (serverMaintainMap != null && !serverMaintainMap.isEmpty());
        boolean serverIdArrayNotNullFlag = (serverIdArray != null && serverIdArray.length > 0);
        if (serverIdArrayNotNullFlag)
            for (Integer serverId : serverIdArray) {
                //拿到维护的id，如果存在，存到新增维护列表maintains 中
                if (serverMaintainNotNullFlag && containInt(serverId, serverMaintainMap.values())) { //如果维护列表中含有这个服务器Id，增加到维护列表maintains中
                    maintainIds.add(serverMaintainMap.get(new Integer(serverId)).getId());
                    maintainServerIds.add(serverId);
                } else {
                    // 否则增加到维护过期列表overDue 中
                    overDue.add(serverId);
                }
            }
        long startTime = serverMaintainExceptServerId.getStartTime().getTime();
        long endTime = serverMaintainExceptServerId.getEndTime().getTime();
        long current = new Date().getTime();
        //1,已经过期或者不存在 增加新的
        if (overDue != null && overDue.size() > 0) {
            List<ServerMaintain> serverMaintainList = new ArrayList<ServerMaintain>();
            if (startTime < endTime) { //维护
                if (startTime <= current) {
                    int updateToMaintainRel = serverMaintainService.batchUpdateServerStatus(overDue, ServerEntity.SERVER_STATUS.SERVER_STATUS_MAINTAIN.getServerStatusCode());
                    if (updateToMaintainRel > 0) {
                        log.info("提前更新为维护状态成功!");
                    } else {
                        log.info("提前更新为维护状态失败!");
                    }
                }
                for (Integer serverId : overDue) {
                    ServerMaintain serverMaintain = new ServerMaintain();
                    serverMaintain.setStartTime(serverMaintainExceptServerId.getStartTime());
                    serverMaintain.setEndTime(serverMaintainExceptServerId.getEndTime());
                    serverMaintain.setMessage(serverMaintainExceptServerId.getMessage());
                    serverMaintain.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    serverMaintain.setServerId(serverId);
                    serverMaintainList.add(serverMaintain);
                }
                msg = serverMaintainService.addServerMaintain(serverMaintainList);
            } else if (startTime == endTime) {//开服
                // 把-2状态的服务器改为1的
                int updateToHotRel = serverMaintainService.batchUpdateServerStatus(overDue, ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode());
                if (updateToHotRel > 0) {
                    log.info("立刻开服成功!");
                    msg.setCode(200);
                    msg.setMsg("部分开服成功!");
                } else {
                    log.info("立刻开服失败!");
                }
                }

        }
        //2,如果不是 更新已有的
        if (maintainIds != null && maintainIds.size() > 0) {
            if (startTime < endTime) {
                //如果start!=end 更新
                if (startTime <= current) {
                    serverMaintainService.batchUpdateServerStatus(maintainServerIds, ServerEntity.SERVER_STATUS.SERVER_STATUS_MAINTAIN.getServerStatusCode());
                }
                msg = serverMaintainService.batchUpdateMaintainServers(serverMaintainExceptServerId, maintainIds);

            } else if (startTime == endTime) {//立即停止维护
                // 把-2状态的服务器改为1的
                int updateToHotRel = serverMaintainService.batchUpdateServerStatus(maintainServerIds, ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode());
                if (updateToHotRel > 0) {
                    log.info("立刻开服成功!");
                    serverMaintainExceptServerId.setEndTime(new Timestamp(new Date().getTime()));
                    serverMaintainExceptServerId.setMessage("立即开服!" + serverMaintainExceptServerId.getEndTime());
                    msg = serverMaintainService.batchUpdateMaintainServers(serverMaintainExceptServerId, maintainIds);
                } else {
                    log.info("立刻开服失败!");
                    msg.setCode(204);
                    msg.setMsg("更新服务器状态为1失败！");
                }
            }
        }
        return msg;
    }

    /**
     * 全区开服和停服方法
     *
     * @param msg
     * @param serverMaintainService
     * @param openFlag              开服为true,停服为false
     * @return
     * @throws SQLException
     */
    private Msg updateAll(Msg msg, ServerMaintain serverMaintainExceptServerId, ServerMaintainDecorateService serverMaintainService, boolean openFlag) throws SQLException {
        //拿到正在维护的维护信息列表
        Map<Integer, ServerMaintain> serverMaintainMap = serverMaintainService.getMaintainServerInfo();
        //拿到所有的火爆状态的，维护状态的服务器列表
        List<Integer> maintainStatusServersIdList = new LinkedList<Integer>();
        List<Integer> maintainStatusServerMaintainInfoIdList = new LinkedList<Integer>();
        List<Integer> hotStatusServersIdList = new LinkedList<Integer>();
        Map<Integer, ServerEntity> serverEntityMap = gameWithServerDecorateServiceProvider.get().getAllServerMaps();
        for (ServerEntity serverEntity : serverEntityMap.values()) {
            int serverStatus = serverEntity.getServerStatus();
            int serverId = serverEntity.getId();
            if (ServerEntity.SERVER_STATUS.SERVER_STATUS_MAINTAIN.getServerStatusCode() == serverStatus) {
                maintainStatusServersIdList.add(serverId);
            }
            if (ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode() == serverStatus) {
                hotStatusServersIdList.add(serverId);
            }
        }
        //拿到维护信息的ID列表
        if (serverMaintainMap != null && !serverEntityMap.isEmpty())
            for (int serverId : maintainStatusServersIdList) {
                if (serverMaintainMap.containsKey(serverId)) {
                    int serverMaintainId = serverMaintainMap.get(serverId).getId();
                    maintainStatusServerMaintainInfoIdList.add(serverMaintainId);
                }
            }
        if (openFlag) {//全区开服
            serverMaintainExceptServerId.setEndTime(new Timestamp(new Date().getTime()));
            serverMaintainExceptServerId.setMessage("全区立即开服!" + serverMaintainExceptServerId.getEndTime());
            //修改所有维护状态的服务器状态为火爆状态
            int updateServerStatusRel = serverMaintainService.batchUpdateServerStatus(maintainStatusServersIdList, ServerEntity.SERVER_STATUS.SERVER_STATUS_HOT.getServerStatusCode());
            //修改维护信息
            if (updateServerStatusRel > 0) {
                if (!maintainStatusServerMaintainInfoIdList.isEmpty()) {
                    msg = serverMaintainService.batchUpdateMaintainServers(serverMaintainExceptServerId, maintainStatusServerMaintainInfoIdList);
                } else {
                    msg.setCode(200);
                    msg.setMsg("没有需要修改的维护信息!");
                }
            } else {
                msg.setCode(204);
                msg.setMsg("全区开服修改状态失败!");
            }

        } else { //全区停服
            //增加维护信息
            List<ServerMaintain> serverMaintainList = new ArrayList<ServerMaintain>();
            for (Integer serverId : hotStatusServersIdList) {
                ServerMaintain serverMaintain = new ServerMaintain();
                serverMaintain.setStartTime(serverMaintainExceptServerId.getStartTime());
                serverMaintain.setEndTime(serverMaintainExceptServerId.getEndTime());
                serverMaintain.setMessage("全区维护：" + serverMaintainExceptServerId.getMessage());
                serverMaintain.setCreateTime(new Timestamp(new Date().getTime()));
                serverMaintain.setServerId(serverId);
                serverMaintainList.add(serverMaintain);
            }
            msg = serverMaintainService.addServerMaintain(serverMaintainList);
            if (200 == msg.getCode()) {  //提前修改所有火爆状态的为维护状态
                if (serverMaintainExceptServerId.getStartTime().getTime() <= new Date().getTime()) {
                    int updateServerStatusRel = serverMaintainService.batchUpdateServerStatus(hotStatusServersIdList, ServerEntity.SERVER_STATUS.SERVER_STATUS_MAINTAIN.getServerStatusCode());
                    if (updateServerStatusRel > 0) {
                        log.info("提前更新为维护状态成功!");
                    } else {
                        log.info("提前更新为维护状态失败!");
                    }
                }
            }
        }
        return msg;
    }


    /**
     * 判断维护列表中是不是存在serverId为 element的
     *
     * @param element
     * @param serverMaintains
     * @return
     */
    private boolean containInt(int element, Collection<ServerMaintain> serverMaintains) {
        boolean flag = false;
        for (ServerMaintain serverMaintain : serverMaintains) {
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
        String ServerIdList = request.getParameter("ServerIdList").trim();
        //2，验证
        if (Strings.isNullOrEmpty(ServerIdList)) {
            log.info("参数ServerIdList为空！");
            return;
        }
        //3，转换
        ObjectMapper mapper = new ObjectMapper();
        int[] serverIdArray = mapper.readValue(ServerIdList, int[].class);
        //4，处理
        ServerMaintainDecorateService serverMaintainService = serverMaintainServiceProvider.get();
        Msg msg = serverMaintainService.queryMaintainServersByIdList(serverIdArray);
        // 5，返回  写回处理结果
//        log.info("查询以下服务器的维护信息：" + ServerIdList);
        ServletUtil.returnJson(response, msg);
    }

    //查询维护服务器信息
    public void deleteFutureMaintainInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1，获取参数
        String ServerIdList = request.getParameter("ServerIdArray");
        log.info("删除没到时间的维护信息接受的参数："+ServerIdList);
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
        Msg msg = serverMaintainService.batchDeleteFutureMaintainInfo(serverIdArray);
        // 5，返回  写回处理结果
//        log.info("查询以下服务器的维护信息：" + ServerIdList);
        ServletUtil.returnJson(response, msg);
    }
}
