package com.sz7road.userplatform.pay.game;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.Backend;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestScoped
public class XddtChargeHandler implements GameRechargeHandler{

    static final Logger log = LoggerFactory.getLogger(XddtChargeHandler.class.getName());

    private int code = 408; // 默认为连接超时
    private String content = null;

    @Override
    public Backend.BackendResponse charge(GameTable.GameEntry gameEntry, ServerInfo serverInfo, ServerInfo mainServerInfo, OrderObject order) {
        Backend.BackendResponse response = null;

        // 获取充值KEY
        final String aliasName = "xddt";
        final String chargeKey = ConfigurationUtils.get(aliasName + ".chargeKey");

        if (Strings.isNullOrEmpty(chargeKey)) {
            throw new IllegalStateException("新弹弹堂无效的游戏充值KEY");
        }

        final int userId = order.getUserId();
        final int pid = order.getPlayerId();
        final CharSequence id = order.getId();
        final int amount = order.getGold();
        final CharSequence channelId = order.getChannelId();

        // if (userId > 0 && pid > 0 && id.length() == 21 && amount > 0)
        if (userId > 0 && pid > 0 && amount > 0) {
            final Map<String, Object> param = Maps.newHashMap();
            param.put("pid", pid);
            param.put("order", id);
            param.put("uname", userId);
            param.put("amount", amount / 10);

            final List<String> list = Lists.newArrayList(param.keySet());
            Collections.sort(list);

            final StringBuilder sb = new StringBuilder();
            for (String p : list) {
                sb.append(p).append("=").append(param.get(p));
            }

            sb.append(chargeKey);
            param.put("sign", MD5Utils.digestAsHex(sb.toString()));
            param.put("sid", serverInfo.getServerNo());
            param.put("c", channelId);

            String dest = String.format(ConfigurationUtils.get(aliasName + ".chargeUrl"), mainServerInfo.getServerNo());

            response = Backend.post(dest, param);

            if (null != response) {
                code = response.getResponseCode();
                content = response.getResponseContent();
            }
        } else {
            log.error("要求发放无效充值订单游戏币：{}", order);
        }
        return response;
    }

    @Override
    public boolean isRechargeNeed() {
        boolean rechargeNeeded = false;
        if (code > 0 && code != 200) {
            rechargeNeeded = true;
        } else if (code == 200 && !Strings.isNullOrEmpty(content)) {
            rechargeNeeded = content.trim().startsWith("failure");
        }
        return rechargeNeeded;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getContent() {
        return content;
    }
}
