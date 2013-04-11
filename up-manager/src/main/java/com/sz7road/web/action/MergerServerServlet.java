package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-24
 * Time: 下午4:25
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class MergerServerServlet extends BaseServlet {

    @Inject
    private Provider<GameWithServerDecorateService> gameWithServerDecorateServiceProvider;

    public void showServerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        ServerTable.ServerEntry entry = null;
        if (!Strings.isNullOrEmpty(id)) {
            GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
            entry = gameWithServerDecorateService.getServerEntryById(Integer.parseInt(id));
        } else {
            ServletUtil.returnJson(response, "参数为空！");
        }
        if (entry != null) {
            ServletUtil.returnJson(response, entry);
        } else {
            ServletUtil.returnJson(response, "没有查到服务器的信息！");
        }
    }

    public void mergerServerInAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg();
        String ids = request.getParameter("checkedServers").trim();
        String serverNo = request.getParameter("checkedMainServer").trim();
        String serverId = request.getParameter("checkedMainServerId").trim();
        ObjectMapper objectMapper = new ObjectMapper();
        if (!Strings.isNullOrEmpty(ids) && !Strings.isNullOrEmpty(serverNo)) {
            String idss = "[" + ids.substring(0, ids.length() - 1) + "]";
            int[] idArray = objectMapper.readValue(idss, int[].class);
            boolean isSelf = false;
            for (int id : idArray) {
                if (id == Integer.parseInt(serverId)) {
                    msg.setCode(204);
                    msg.setMsg("不能合并自己");
                    isSelf = true;
                    break;
                }
            }

            if (isSelf == false) {
                GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
                boolean flag = gameWithServerDecorateService.mergerServers(serverNo, idArray);
                      //todo 合服和取消合服通知更新
                if (flag) {
                    msg.setCode(200);
                    msg.setMsg("合服成功!");
                } else {
                    msg.setCode(204);
                    msg.setMsg("合服失败");
                }
            } else {
                msg.setCode(200);
                msg.setMsg("不能自己合并自己！");
            }
        } else {
            msg.setCode(200);
            msg.setMsg("参数传递为空！");
        }
        ServletUtil.returnJson(response, msg);
    }

    public void cancelMergerInAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg();
        String ids = request.getParameter("checkedCancelServerSel").trim();
        ObjectMapper objectMapper = new ObjectMapper();
        if (!Strings.isNullOrEmpty(ids)) {
            String idss = "[" + ids.substring(0, ids.length() - 1) + "]";
            int[] idArray = objectMapper.readValue(idss, int[].class);
            GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();
            boolean flag = gameWithServerDecorateService.splitServers(idArray);
            if (flag) {
                msg.setCode(200);
                msg.setMsg("取消合服成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("取消合服失败");
            }
        } else {
            msg.setCode(200);
            msg.setMsg("参数传递为空！");
        }
        ServletUtil.returnJson(response, msg);
    }

    public void showServerTree(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String type = request.getParameter("type");

        if (!Strings.isNullOrEmpty(type)) {
            GameWithServerDecorateService gameWithServerDecorateService = gameWithServerDecorateServiceProvider.get();

            if ("all".equals(type)) {
                ServletUtil.returnJson(response, gameWithServerDecorateService.getServerTreeDataStruct());
            } else if ("main".equals(type)) {
                ServletUtil.returnJson(response, gameWithServerDecorateService.getServerTreeDataStructOfMain());
            } else if ("cancell".equals(type)) {
                ServletUtil.returnJson(response, gameWithServerDecorateService.getServerTreeDataStructOfCancell());
            }
        } else {

        }


    }
}
