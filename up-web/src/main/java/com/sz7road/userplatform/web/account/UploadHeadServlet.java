package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-10
 * Time: 下午3:09
 * 上传头像的处理类
 */
@Singleton
public class UploadHeadServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UploadHeadServlet.class);

    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int rel = 0;
        try {
            String userId = request.getParameter("userId").trim();
            String headDir = request.getParameter("headDir").trim();
            if (!Strings.isNullOrEmpty(userId) && VerifyFormItem.isInteger(userId) && !Strings.isNullOrEmpty(headDir)) {
                UserService userService = userServiceProvider.get();
//                rel = userService.updateUserAvatar(Integer.parseInt(userId), headDir);
            }
        } catch (Exception ex) {
            log.error("参数传递为空");
            ex.printStackTrace();
        }


    }
}
