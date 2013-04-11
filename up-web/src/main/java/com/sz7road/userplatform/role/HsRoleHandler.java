package com.sz7road.userplatform.role;

import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.utils.ConfigurationUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import util.HttpClientUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author: jiangfan.zhou
 */
@RequestScoped
public class HsRoleHandler extends Injection implements RoleHandler {

    @Override
    public Map<String, String> getRoles(UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo)  {
        String aliasName = "hs";
        // 7road_%04d
        String site = String.format(ConfigurationUtils.get(aliasName + ".site"), serverInfo.getServerNo());
        // http://s%d.shenquol.com/loginselectlist
        String roleUrl = String.format(ConfigurationUtils.get(aliasName + ".getRoleUrl"), mainServerInfo.getServerNo());

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("username", String.valueOf(userAccount.getId())));
        pairs.add(new BasicNameValuePair("site", site));
        String xml = HttpClientUtil.getContent(roleUrl, pairs);

        Map<String, String> roleList = null;

        try {
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
        } catch (Exception e){
            e.printStackTrace();
        }
        return roleList;
    }
}
