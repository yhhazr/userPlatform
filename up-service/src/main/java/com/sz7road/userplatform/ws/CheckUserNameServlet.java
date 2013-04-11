package com.sz7road.userplatform.ws;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.utils.Base64;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-5
 * Time: 下午4:24
 * 检查用户名，如果重复，给出合适的建议用户名
 */
@Singleton
public class CheckUserNameServlet extends HeadlessServlet {
    private static final Logger log = LoggerFactory.getLogger(CheckUserNameServlet.class.getName());
    @Inject
    private Provider<FilterCharacterProvider> dirtyProvider;
    @Inject
    Provider<UserService> userServiceProvider;
    @Inject
    Provider<VerifyCodeService> verifyCodeServiceProvider;

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("验证ok");
        try {
            if (!Strings.isNullOrEmpty(userName)) {
                userName = userName.trim();
            } else {
                msg.setCode(199);
                msg.setMsg("用户名为空!");
                return;
            }
            final UserService userService = userServiceProvider.get();
            final FilterCharacterProvider dirtyService = dirtyProvider.get();

            // 验证用户名
            if (!userService.isLegalNaming(userName)) {
                msg.setCode(199);
                msg.setMsg("6-20个英文字符,数字或下划线,以字母开头!");
                return;
            }
            if (!userService.isNameCanReg(userName)) {
                msg.setCode(199);
                msg.setMsg("已经存在的用户名!");
                msg.setObject(handlerUserName(userName));
                return;
            }
            if (dirtyService.isContainKey(userName)) {
                msg.setCode(199);
                msg.setMsg("用户名含有不雅字符!");
                return;
            }
            if (null != userService.findAccountByUserName(userName)) {
                msg.setCode(199);
                msg.setMsg("已经存在的用户名!");
                msg.setObject(handlerUserName(userName));
                return;
            }
            if (!DataUtil.isNotChinese(userName)) {
                msg.setCode(199);
                msg.setMsg("用户名不能含有中文!");
                return;
            }
        } catch (final Exception e) {
            log.error("检查用户名异常：{}", e.getMessage());
            response.sendError(404);
            e.printStackTrace();
        } finally {
            DataUtil.returnJson(response, msg);

        }
    }

    private List<String> handlerUserName(String userName) {
        if (!Strings.isNullOrEmpty(userName)) {
            if (userName.length() > 17) {
                userName = userName.substring(0, 17);
            }
            return Lists.asList(userName + (new Random().nextInt(899) + 100),
                    new String[]{userName + (new Random().nextInt(899) + 100),
                            userName + (new Random().nextInt(899) + 100),
                            userName + (new Random().nextInt(899) + 100),
                            userName + (new Random().nextInt(899) + 100)
                    });
        } else {
            return null;
        }
    }
}
