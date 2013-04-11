package com.sz7road.web.action;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Item;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.manager.ItemManager;
import com.sz7road.web.utils.ServletUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 上午9:30
 * 初始化下拉菜单的servlet
 */
@Singleton
public class InitDropdownMenuServlet extends BaseServlet {
    @Inject
    private Provider<ItemManager> itemManagerProvider;


    public void doInitOrderChannelsDrowdownMenu(HttpServletRequest request, HttpServletResponse response) {
        try {
            ItemManager itemManager = itemManagerProvider.get();

            List<Item> channels = itemManager.getChannels();
            response.setContentType("application/x-json");

            ObjectMapper objectMapper = new ObjectMapper();

            String channelsItems = objectMapper.writeValueAsString(channels);

            response.getWriter().print(channelsItems);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception ex) {
            log.info("返回支付网关信息异常！");
        }
    }

    public void doInitOrderSubTypeDrowdownMenu(HttpServletRequest request, HttpServletResponse response) {
        try {
            ItemManager itemManager = itemManagerProvider.get();

            String channelId = request.getParameter("channelId");

            List<Item> channels = itemManager.getSubTypes(channelId);

            response.setContentType("application/x-json");
            ObjectMapper objectMapper = new ObjectMapper();

            String channelsItems = objectMapper.writeValueAsString(channels);

            response.getWriter().print(channelsItems);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception ex) {
            log.info("返回支付方式信息异常！");
        }
    }

    public void doInitOrderSubTypeTagDrowdownMenu(HttpServletRequest request, HttpServletResponse response) {
        try {

            ItemManager itemManager = itemManagerProvider.get();

            String channelId = request.getParameter("channelId");
            String subType = request.getParameter("subType");
            List<Item> channels = itemManager.getSubTypeTags(channelId, subType);
            response.setContentType("application/x-json");
            ObjectMapper objectMapper = new ObjectMapper();

            String channelsItems = objectMapper.writeValueAsString(channels);

            response.getWriter().print(channelsItems);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception ex) {
            log.info("返回支付渠道信息异常");
        }
    }

    public void doInitOrderGameNameDrowdownMenu(HttpServletRequest request, HttpServletResponse response) {
        try {

            ItemManager itemManager = itemManagerProvider.get();

            List<Item> channels = itemManager.getGames();
            response.setContentType("application/x-json");
            ObjectMapper objectMapper = new ObjectMapper();

            String channelsItems = objectMapper.writeValueAsString(channels);

            response.getWriter().print(channelsItems);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception ex) {
            log.error("返回游戏信息异常！");
        }
    }

    public void doInitOrderServerNameDrowdownMenu(HttpServletRequest request, HttpServletResponse response) {
        try {

            ItemManager itemManager = itemManagerProvider.get();

            String gameId = request.getParameter("gameID");
            List<Item> channels = itemManager.getServers(gameId);
            response.setContentType("application/x-json");
            ObjectMapper objectMapper = new ObjectMapper();

            String channelsItems = objectMapper.writeValueAsString(channels);

            response.getWriter().print(channelsItems);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception ex) {
            log.info("返回服务器信息异常!");
            ex.printStackTrace();
        }
    }

    public void doInitFaqKindDropDownList(HttpServletRequest request, HttpServletResponse response) {
        try {
            ItemManager itemManager = itemManagerProvider.get();
            List<Item> channels = itemManager.getFaqKind();
            ServletUtil.returnJson(response, channels);
        } catch (Exception ex) {
            log.info("返回服务器信息异常!");
        }
    }

    public void doInitFaqKindDropDownListOfParent(HttpServletRequest request, HttpServletResponse response) {
        try {
            ItemManager itemManager = itemManagerProvider.get();
            List<Item> channels = itemManager.getFaqKindOfParent();
            ServletUtil.returnJson(response, channels);
        } catch (Exception ex) {
            log.info("返回服务器信息异常!");
        }
    }
}
