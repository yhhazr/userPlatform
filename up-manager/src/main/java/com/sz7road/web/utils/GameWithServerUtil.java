package com.sz7road.web.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.service.GameWithServerDecorateService;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.utils.Backend;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-9-27
 * Time: 上午10:14
 */
@Singleton
public class GameWithServerUtil {

    private static final Logger logger = LoggerFactory.getLogger(GameWithServerUtil.class.getName());

    @Inject
    private Provider<GameWithServerDecorateService> gwsdProvider;

    public Map<String, String> getRole(int uid,int gameId, int serverId) throws Exception {
        Map<String, String> roleList = null;
        try {
            int serverNo = 0;
            String serverSite = "";
            String selectRoleUrl = "";

            //获取服务器的serverNo和site值
            GameWithServerDecorateService gwsds = gwsdProvider.get();
            GameTable gameTable = gwsds.getGameTable();
            ServerTable serverTable = gameTable.getServerTable(gameId);
            ServerTable.ServerEntry serverEntry = null;
            if (null != serverTable) {
                for (Map.Entry<Integer, ServerTable.ServerEntry> entry : serverTable.entrySet()) {
                    ServerTable.ServerEntry server = entry.getValue();
                    if (server.getId() == serverId) {
                        serverEntry = server;
                        break;
                    }
                }
            }

            String getRoleUrlKey = "sq.getRoleUrl";
            String siteKey = "sq.site";
            if (serverEntry != null) {
                serverNo = serverEntry.getServerNo();
                selectRoleUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, getRoleUrlKey);
                serverSite = GameWithServerService.getServerFormatValueByKey(serverEntry, siteKey);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", Integer.toString(uid));
            map.put("site", serverSite);

            Backend.BackendResponse back = Backend.post(selectRoleUrl, map);
            if (back == null) {
                return null;
            }
            byte[] data = back.getResponseData();
            String xml = new String(data);

            //解析xml
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            String roleName = "";
            String roleId = "";
            String userName = "";
            if(root.elements().size()>0){
                roleList = Maps.newHashMap();
            }
            Iterator<?> it = root.elements().iterator();
            while (it.hasNext()) {
                Element info = (Element) it.next();
                roleName = info.attributeValue("NickName");
                roleId = info.attributeValue("ID");
                userName = info.attributeValue("UserName");
                if (!userName.equals(Integer.toString(uid))) {
                    break;
                }
                if (Strings.isNullOrEmpty(roleName)) {
                    continue;
                }
                if (!serverSite.equals(info.attributeValue("Site"))) {
                    break;
                }
                roleList.put(roleId, URLDecoder.decode(roleName, "UTF-8"));//取角色名
            }
        } catch (Exception e) {
            logger.error("Get role fail:{}{}",e.getMessage(),e);
        }
        return roleList;
    }
}
