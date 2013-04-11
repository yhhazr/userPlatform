package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.pojos.FormItem;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.DateUtils;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.*;
import com.sz7road.web.pojos.FcmObject;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-18
 * Time: 下午4:11
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class RegisterFcmRzServlet extends HeadlessHttpServlet {
    private static final Logger log = LoggerFactory.getLogger(RegisterFcmRzServlet.class.getName());
    @Inject
    Provider<UserService> userServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = AppHelper.getUserName(request);
        int userId = AppHelper.getUserId(request);

        //1，接收参数
        String _rName = request.getParameter("rName");
        String _icn = request.getParameter("uIcn");
        Msg msg = new Msg();
        msg.setCode(200);
        //2，xss过滤,限制最大长度
        _rName = HtmlUtils.html2EntityAndCheckMaxLength(_rName, 8);
        _icn = HtmlUtils.html2EntityAndCheckMaxLength(_icn, 18);
        //3，服务器端判断用户输入的合法性
        try {
            List<FormItem> items = new LinkedList<FormItem>();
            items.add(new FormItem("rName", _rName, "真实姓名", 206));
            items.add(new FormItem("uIcn", _icn, "身份证号码", 207));
            Map<String, Object> checkResult = HtmlUtils.checkItemAndReturnMsg(items);
            if (checkResult != null && !checkResult.isEmpty() && (Boolean) checkResult.get("result")) {
                int code = (Integer) checkResult.get("code");
                String msgInfo = (String) checkResult.get("msg");
                msg.setCode(code);
                msg.setMsg(msgInfo);
                return;
            } else {
                UserService userService = userServiceProvider.get();
                if (!HtmlUtils.isChinese(_rName)) {
                    msg.setCode(206);
                    msg.setMsg("请输入正确的中文名字!");
                    return;
                }
                if (!userService.isLegalIcn(_icn)) {
                    msg.setCode(207);
                    msg.setMsg("请输入正确的身份证号码!");
                    return;
                }
                String icnStr = DataUtil.getHandledIcn(_icn);
                Map<String, Object> map = Maps.newHashMap();
                map.put("_rName", _rName);
                map.put("_icn", icnStr);
                if (!DateUtils.is18YearOld(_icn)) {
                    String submit = request.getParameter("submitStr");
                    if ("no".equals(submit)) {
                        msg.setCode(208);
                        msg.setMsg("身份证号码不满18岁!");
                        msg.setObject(map);
                        return;
                    } else if ("yes".equals(submit)) {
                        msg.setCode(200);
                    }
                }
                if (msg.getCode() == 200) {
                    //4，插入到防沉迷服务器

                    final String addictionServerUrl = ConfigurationUtils.get("addictionServerUrl");
                    final String addictionKey = ConfigurationUtils.get("addictionKey");
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("userName", userName);
                    param.put("gameAlias", "sq");
                    param.put("cardId", _icn);
                    param.put("proxyName", "7road");
                   final String sign= MD5Utils.digestAsHex(userName + addictionKey).toUpperCase();
                    param.put("sign",sign );
                    Backend.BackendResponse back = Backend.post(addictionServerUrl, param);
                    if (back == null) {
                        log.info("连接防沉迷服务器失败");
                    } else {
                        log.info("【" + userName + "】的防沉迷注册结果是：" + back.getResponseContent());
                    }
                    //5,更新用户的信息
                    boolean result = userService.updateFcmInfo(userId, _rName, _icn);
                    if (result) {
                        msg.setCode(200);
                        msg.setMsg("已经录入防沉迷系统，并且同步到用户平台!");
                    }
                } else {
                    msg.setCode(204);
                    msg.setMsg("防沉迷认证失败!");
                }
            }
        } catch (Exception ex) {
            log.error("防沉迷认证失败!");
            ex.printStackTrace();
        } finally {
            //5，返回结果
            DataUtil.returnJson(response, msg);
        }

    }
}
