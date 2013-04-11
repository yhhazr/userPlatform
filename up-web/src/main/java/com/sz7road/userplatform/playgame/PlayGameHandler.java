package com.sz7road.userplatform.playgame;

import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.serverdata.ServerInfo;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import java.io.IOException;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 上午11:43
 */
public interface PlayGameHandler {

    void process(HeadlessServletRequest request, HeadlessServletResponse response);

    void process(HeadlessServletRequest request, HeadlessServletResponse response, UserAccount userAccount, ServerInfo serverInfo, ServerInfo mainServerInfo) throws IOException;
}
