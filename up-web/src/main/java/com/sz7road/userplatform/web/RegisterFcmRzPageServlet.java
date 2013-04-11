package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.Backend;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.pojos.FcmObject;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-10-22
 * Time: 下午3:46
 * 防沉迷认证页面跳转
 */
   @Singleton
public class RegisterFcmRzPageServlet extends HeadlessHttpServlet{

    private Logger log= LoggerFactory.getLogger(RegisterFcmRzPageServlet.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Inject
    Provider<UserService> userServiceProvider;
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1,判断是不是已经登录
        String userName = AppHelper.getUserName(request);
        if(Strings.isNullOrEmpty(userName))
        { //没有登录
            response.sendRedirect("/login.html?fromUrl=fcm_rz.html");
        }
         else
        {
          //如果已经注册了防沉迷，得到防沉迷认证的信息
          final FcmObject fcmInfo=getFcmInfoByUserName(userName);
          if(fcmInfo!=null&&!Strings.isNullOrEmpty(fcmInfo.getCardId())&&fcmInfo.getCardId().length()>12)
          {
             fcmInfo.setCardId( DataUtil.getHandledIcn(fcmInfo.getCardId()));
          }
          request.setAttribute("fcmInfo",fcmInfo);
           String realName= userServiceProvider.get().getUserObject(userName).getRealName();
            if(Strings.isNullOrEmpty(realName))
            {
                realName="***";
            } else
            {
                realName=realName.substring(0,1)+"**";
            }
          request.setAttribute("realName",  realName);

          request.getRequestDispatcher("fcm_rz.jsp").forward(request, response);
        }

    }

    /**
     * 根据用户名得到用户的防沉迷信息
     * @param userName   用户名
     * @return      防沉迷信息实体
     */
    private FcmObject getFcmInfoByUserName(String userName) throws IOException {
        //4，插入到防沉迷服务器
        final String addictionServerUrl= ConfigurationUtils.get("queryAddictionServerUrl");
        final String addictionKey=ConfigurationUtils.get("addictionKey");
        Map<String,Object> param= Maps.newHashMap();
        param.put("userName",userName);
        param.put("proxyName","7road");
        param.put("sign", MD5Utils.digestAsHex(userName + addictionKey).toUpperCase());
        Backend.BackendResponse back = Backend.post(addictionServerUrl, param);
        if (back == null) {
            log.info("连接防沉迷服务器失败");
            return new FcmObject();
        }
        else
        {
            final String fcmInfo= back.getResponseContent();
            log.info("【"+ userName+"】的防沉迷注册查询结果是："+fcmInfo);
            return  mapper.readValue(fcmInfo,FcmObject.class);
        }
    }
}
