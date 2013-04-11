package com.sz7road.web.ws;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.ReChargeOrderService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.utils.GameWithServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * 充值测试
 * User: leo.liao
 * Date: 12-9-24
 * Time: 上午10:13
 */
@Singleton
public class RechargeTestServlet extends HttpServlet {

    final static Logger LOGGER = LoggerFactory.getLogger(RechargeTestServlet.class.getName());
    
    @Inject
    private ReChargeOrderService reChargeOrderService;

    @Inject
    private Provider<GameWithServerUtil> gwsuProvider;

    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String channelId = "0";
        final int subType = 0;
        final String subTag = "0";
        String sign = request.getParameter("sign");
        String un = request.getParameter("u"); //用户名
        String gameId = request.getParameter("_g");
        String zoneId = request.getParameter("_z");
        String amount = request.getParameter("amount");
        String clientIp = request.getRemoteAddr();
        PrintWriter out = response.getWriter();
//
        try {
            if(Strings.isNullOrEmpty(amount)||Strings.isNullOrEmpty(un)
                    ||Strings.isNullOrEmpty(gameId)||Strings.isNullOrEmpty(zoneId)||Strings.isNullOrEmpty(sign)){
                throw new IllegalArgumentException("参数输入不正确");
            }
            StringBuilder sb = new StringBuilder();
            //u=uid|p=pid|_g=gid|_z=zid|amount=amount|chargeKey
            sb.append("u=").append(un).append("|")
                    .append("_g=").append(gameId).append("|")
                    .append("_z=").append(zoneId).append("|")
                    .append("amount=").append(amount).append("|");
            String testChargeKey = ConfigurationUtils.get("game.test.chargeKey");
            sb.append(testChargeKey);
            String cSign = MD5Utils.digestAsHex(sb.toString());
            if(!cSign.equalsIgnoreCase(sign)) {
                throw new IllegalArgumentException("非法请求");
            }
            UserAccount acount = userServiceProvider.get().findAccountByUserName(un);
            if(acount == null) {
                throw new IllegalArgumentException("不存在此用户");
            }
            int userId = acount.getId();
            int gid = Integer.parseInt(gameId);
            int zid = Integer.parseInt(zoneId);
            Map<String,String> roleMap = gwsuProvider.get().getRole(userId,gid,zid);
            int playerId = 0;
            if(roleMap != null && !roleMap.isEmpty()) {
                Iterator<Map.Entry<String,String>> it = roleMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String,String> entry = it.next();
                    playerId = Integer.parseInt(entry.getKey());
                    break;
                }
            }
            if(!(playerId > 0)) {
                throw new IllegalArgumentException("该用户没在该游戏区创建角色");
            }
            OrderObject order = new OrderObject();
            order.setChannelId(channelId);
            order.setSubType(subType);
            order.setSubTag(subTag);
            order.setAmount(Integer.parseInt(amount));
            order.setUserId(userId);
            order.setPlayerId(playerId);
            order.setGameId(gid);
            order.setZoneId(zid);
            order.setClientIp(clientIp);
            com.sz7road.utils.Backend.BackendResponse resp = reChargeOrderService.rechargeTest(order);
            int statusCode = 500;
            String result = "充值测试出现异常情况";
            if(resp != null) {
                statusCode = resp.getResponseCode();
                result = resp.getResponseContent();
            }
            result = String.format("%d => %s", statusCode, result);
            out.write(result);
        }catch (Exception e) {
            LOGGER.error("充值测试出现异常：{}",e.getMessage());
            out.write(String.format("%d => %s",500,e.getMessage()));
        }finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
