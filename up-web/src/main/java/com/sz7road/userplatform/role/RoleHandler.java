package com.sz7road.userplatform.role;

import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import org.dom4j.DocumentException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Author: jiangfan.zhou
 */

public interface RoleHandler {

    Map<String, String> getRoles(UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws DocumentException, UnsupportedEncodingException;

}
