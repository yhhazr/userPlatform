package com.sz7road.userplatform.ws.getRole;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.UserDao;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.ws.mbk.MbkImg;
import com.sz7road.userplatform.ws.mbk.PswSafeCard;
import com.sz7road.userplatform.ws.mbk.PswSafeCardDao;
import com.sz7road.utils.CaptchaCode;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-29
 * Time: 下午5:00
 * 根据用户名，返回用户拥有角色的sites
 */
@Singleton
public class GetRoleByUserNameServlet extends HeadlessServlet {

    final Logger log = LoggerFactory.getLogger(GetRoleByUserNameServlet.class);
    @Inject
    private Provider<UserService> userServiceProvider;

    final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {

        final String userName = request.getNotNullParameter("userName");
        final String sign = request.getNotNullParameter("sign");
        Map map = Maps.newHashMap();
        map.put("id", 0);
        map.put("sites", null);
        log.info("sign: " + MD5Utils.digestAsHex("7road_pingTai_User=" + userName));
        if (sign.equals(MD5Utils.digestAsHex("7road_pingTai_User=" + userName))) {
            final int userId = userServiceProvider.get().findUserIdByUserName(userName);
            if (userId > 0) {
                map.put("id", userId);
                map.put("sites", userServiceProvider.get().getRoleInfoByUserId(userId));
            }
        }
        PrintWriter out = null;
        response.setContentType("application/json");
        try {
            out = response.getWriter();

            mapper.writeValue(out, map);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }
}
