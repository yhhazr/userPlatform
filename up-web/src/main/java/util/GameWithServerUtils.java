package util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.GameTable;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.GameWithServerService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.serverdata.ServerDataService;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.userplatform.web.exception.EnterGameException;
import com.sz7road.utils.Backend;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * User: leo.liao
 * Date: 12-6-6
 * Time: 下午3:44
 */
@Singleton
public class GameWithServerUtils {

    private static final Logger logger = LoggerFactory.getLogger(GameWithServerUtils.class.getName());

    @Inject
    private Provider<GameWithServerService> gwsServiceProvider;
    @Inject
    private com.google.inject.Provider<UserService> userServiceProvider;


    public ServerTable.ServerEntry getServerEntry(int gameId, int gameZoneId) {
        final GameTable gameTable = gwsServiceProvider.get().getGameTable();

        if (null != gameTable) {
            final ServerTable serverTable = gameTable.getServerTable(gameId);
            if (null != serverTable) {
                final ServerTable.ServerEntry serverEntry = serverTable.get(gameZoneId);
                if (serverEntry == null) {
                    throw new EnterGameException(4, "未找到游戏服务区");
                }
                return serverEntry;
            }
        }
        return null;
    }

    /**
     * 获取角色
     *
     * @return
     */
    public Map<String, String> getRole(String username,int gameId, int serverId) throws Exception {
        Map<String, String> roleList = null;
        try {
            int serverNo = 0;
            String serverSite = "";
            String selectRoleUrl = "";

            //获取服务器的serverNo和site值
            GameWithServerService gameWithServerService = gwsServiceProvider.get();
            GameTable gameTable = gameWithServerService.getGameTable();
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

            //获取用户id
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(username);
            int userId = userAccount.getId();
            //String getRoleUrlKey = "sq.getRoleUrl";
            //String siteKey = "sq.site";
            String aliasName = gameTable.get(gameId).getAliasName();
            String getRoleUrlKey = aliasName + ".getRoleUrl";
            String siteKey = aliasName + ".site";
            if (serverEntry != null) {
                serverNo = serverEntry.getServerNo();
                selectRoleUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, getRoleUrlKey);
                serverSite = GameWithServerService.getServerFormatValueByKey(serverEntry, siteKey);
            }

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", Integer.toString(userId)));
            pairs.add(new BasicNameValuePair("site", serverSite));
            String xml = HttpClientUtil.getContent(selectRoleUrl, pairs);

            logger.info("selecturl:"+selectRoleUrl);
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            String roleName = "";
            String roleId = "";
            Iterator<?> it = root.elements().iterator();
            if(root.elements().size()>0){
                roleList = Maps.newHashMap();
            }

            while (it.hasNext()) {
                Element info = (Element) it.next();
                roleName = info.attributeValue("NickName");
                roleId = info.attributeValue("ID");
                roleList.put(roleId, URLDecoder.decode(roleName, "UTF-8"));//取角色名
            }
        } catch (Exception e) {
            logger.error("Get role fail:{}{}",e.getMessage(),e);
        }
        return roleList;
    }

    /**
     * 获取角色
     *
     * @return
     */
    public Map<String, String> _getRole(String username,int gameId, int serverId) throws Exception {
        Map<String, String> roleList = null;
        try {
            int serverNo = 0;
            String serverSite = "";
            String selectRoleUrl = "";

            //获取服务器的serverNo和site值
            GameWithServerService gameWithServerService = gwsServiceProvider.get();
            GameTable gameTable = gameWithServerService.getGameTable();
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

            //获取用户id
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(username);
            int userId = userAccount.getId();
            //String getRoleUrlKey = "sq.getRoleUrl";
            //String siteKey = "sq.site";
            String aliasName = gameTable.get(gameId).getAliasName();
            String getRoleUrlKey = aliasName + ".getRoleUrl";
            String siteKey = aliasName + ".site";
            if (serverEntry != null) {
                serverNo = serverEntry.getServerNo();
                selectRoleUrl = GameWithServerService.getServerFormatValueByKey(serverEntry, getRoleUrlKey);
                serverSite = GameWithServerService.getServerFormatValueByKey(serverEntry, siteKey);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", Integer.toString(userId));
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
                if (!userName.equals(Integer.toString(userId))) {
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


    public ServerTable.ServerEntry getServerEntryBySite(int gameId,String site){
        if(!(gameId > 0 && !Strings.isNullOrEmpty(site)))
            return null;
        GameWithServerService gameWithServerService = gwsServiceProvider.get();
        GameTable gameTable = gameWithServerService.getGameTable();
        if(gameTable != null){
            ServerTable serverTable = gameTable.getServerTable(gameId);
            Collection<ServerTable.ServerEntry> list = serverTable.values();
            Iterator<ServerTable.ServerEntry> it = list.iterator();
            while(it.hasNext()){
                ServerTable.ServerEntry serverEntry = it.next();
                String tmpSite = GameWithServerService.getServerFormatValueByKey(serverEntry, "sq.site");
                if(site.equalsIgnoreCase(tmpSite)){
                    return serverEntry;
                }
            }
        }
        return null;
    }
}
