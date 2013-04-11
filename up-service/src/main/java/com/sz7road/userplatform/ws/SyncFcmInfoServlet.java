package com.sz7road.userplatform.ws;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

@Singleton
public class SyncFcmInfoServlet extends HeadlessServlet {
    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {

       try{
        String userName = request.getNotNullParameter("userName");
        String icn = request.getNotNullParameter("icn");
        String sign = request.getNotNullParameter("sign");

        String signStr= MD5Utils.digestAsHex(userName + ConfigurationUtils.get("addictionKey")).toUpperCase();
        log.info(signStr);
        UserService userService = userServiceProvider.get();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(userName)
                && !Strings.isNullOrEmpty(icn) && userService.isLegalIcn(icn),
                "参数非法，userName ,icn :", userName, icn);
        Preconditions.checkArgument(sign.equals(MD5Utils.digestAsHex(userName + ConfigurationUtils.get("addictionKey")).toUpperCase()), "sign异常");
        int userId = userService.findUserIdByUserName(userName);
        if (userService.updateFcmInfo(userId, null, icn)) {
            response.sendSuccess();
        } else {
            response.sendFailure();
        }
       }catch (Exception ex)
       {
           response.sendFailure();
       }
    }
}