package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-5
 * Time: 上午4:11
 * Description:神曲官网的选服页所需的服务器列表,不使用缓存
 */
@Singleton
public class EnterGameServerListServlet extends BaseServlet{

    private static final Logger log = LoggerFactory.getLogger(EnterGameServerListServlet.class.getName());

    private static final int SERVER_HOT_STATUS=1;

    private static final int SERVER_MAINTAIN_STATUS=-2;

    @Inject
    private Provider<GameWithServerService> gameWithServerServiceProvider;

    public void getServerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String gameId = request.getParameter("_g");
        int _gid=0;
        if(!Strings.isNullOrEmpty(gameId)&& VerifyFormItem.isInteger(gameId))
        {
            _gid=Integer.parseInt(gameId);
        }
        if (_gid <= 0) {
            notFound(response);
            return;
        } else {
            try {
                Set<ServerEntity> serverEntrySet = Sets.newTreeSet(new Comparator<ServerEntity>() {
                    @Override
                    public int compare(ServerEntity o1, ServerEntity o2) {
                        if (o1 == null || o2 == null) return 0;
                        if (o1.getServerNo() > o2.getServerNo()) {
                            return 1;
                        } else if (o1.getServerNo() < o2.getServerNo()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                GameWithServerService gameWithServerService = gameWithServerServiceProvider.get();
                Collection<ServerEntity> serverEntries = gameWithServerService.getSortedServerEntries(_gid);
                Iterator<ServerEntity> it = serverEntries.iterator();
                while (it.hasNext()) {
                    ServerEntity serverEntry = it.next();
                    Timestamp current=new Timestamp(System.currentTimeMillis()+100000);
                    int status=serverEntry.getServerStatus();
                    //返回火爆或者维护，并且开服时间在之前100秒的。
                    if (serverEntry.getOpeningTime().before(current) &&(status== SERVER_HOT_STATUS || status ==SERVER_MAINTAIN_STATUS)) {
                        serverEntrySet.add(serverEntry);
                    }
                }
                final PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                try {
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(out, serverEntrySet);
                } finally {
                    out.flush();
                    out.close();
                }
            } catch (final Exception e) {
                notFound(response);
                log.error("获取游戏服务区列表网关异常：{}", e.getMessage());
                e.printStackTrace();
            }
        }

    }


    protected void notFound(HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(null);
        response.setStatus(404);
        response.getWriter().close();
    }
}
