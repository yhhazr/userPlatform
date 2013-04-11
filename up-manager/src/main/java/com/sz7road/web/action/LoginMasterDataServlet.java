package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.LoginMasterService;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.LoginOrLogoutSessionListener;
import com.sz7road.web.utils.DataUtil;
import com.sz7road.web.utils.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午4:11
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class LoginMasterDataServlet extends BaseServlet {
    @Inject

    private Provider<LoginMasterService> loginMasterServiceProvider;

    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String userName = null;
        String password = null;
        HttpSession session = request.getSession();
        try {
            userName = request.getParameter("username");
            password = request.getParameter("password");
        } catch (Exception ex) {
            log.error("获取参数异常" + ex.getMessage(), ex);
        }
        String forward = "login.jsp";
        Msg msg = new Msg(0, "登陆成功");
        try {
            if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(password)) {
                throw new IllegalArgumentException("用户名或者密码不能为空");
            }
            LoginMasterService loginMasterService = loginMasterServiceProvider.get();
         final    Map<String, Map<String, Object>> masterUserObject = loginMasterService.getAccountInfo(userName, password);
            if (masterUserObject == null) {
                throw new IllegalArgumentException("主数据通信异常，请检查主数据代理...");
            } else {

                String sid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
                session.setAttribute("sid", sid);
                Map<String, Object> user = masterUserObject.get("data");
                if (user != null) {
                    Map<String, String> permissions = DataUtil.getPermissions(masterUserObject);
                    if(checkPermission(permissions))
                   {
                       session.setAttribute("user", user);
                       session.setAttribute("permission", permissions);
                   }
                    else
                   {
                      msg.setCode(203);
                       msg.setMsg("您没有使用该系统的权限！请联系主数据管理人员申请权限!");
                   }

                } else {

                    String errMsg= entity2Html( masterUserObject.toString());
                    String returnMsg="用户名或者密码错误，或者密码过期!请到主数据端修改密码！";
                    if(errMsg.contains("err=502"))
                    {
                        msg.setCode(204);
                        returnMsg=errMsg.substring(errMsg.indexOf("！")+1,errMsg.indexOf("。")+1)+"请联系主数据管理人员申请账号和权限！";
                        msg.setMsg(returnMsg);
                    }
                  else  if(errMsg.contains("err=505"))
                    {
                        msg.setCode(205);
                        returnMsg=errMsg.substring(errMsg.indexOf("！")+1,errMsg.indexOf("<")-1)+"确定跳转到主数据修改密码页面？";
                        String returnUrl=errMsg.substring(errMsg.indexOf("http"),errMsg.lastIndexOf("{0}")+3);
                        msg.setMsg(returnMsg);
                        msg.setObject(returnUrl);
                    } else
                    {
                        msg.setCode(206);
                        msg.setMsg(returnMsg);
                    }
                }
                session.setAttribute(BaseServlet.LOGIN_LISTENER, new LoginOrLogoutSessionListener(sid, masterUserObject));
            }
        } catch (Exception e) {
            msg.build(1, "主数据通信异常，请联系开发人员！");
            e.printStackTrace();
        }
        return render(msg, response);
    }

    /**
     * 获取从主数据读取到的权限信息
     * @param request
     * @param response
     * @throws Exception
     */
    public void getPermissionInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");
        Map<String, Object> permissions = (Map<String, Object>) session.getAttribute("permission");
        if (user != null && !user.isEmpty()) {
            List<Map<String, Object>> tables = (List<Map<String, Object>>) user.get("__pConfigTable");
            if (tables != null && !tables.isEmpty()) {
                for (Map<String, Object> ta : tables) {
                    permissions.put(ta.get("config_item_name").toString(), "true");
                }
            }
        }
        ServletUtil.returnJson(response, permissions);
    }

    /**
     * 检查用户是不是有使用该系统的权限
     * @param permissions
     * @return
     */
    private boolean checkPermission(Map<String, String> permissions)
    {
      ImmutableMap<String,String> allPermissions=ImmutableMap.copyOf(DataUtil.getAllPermissions());
      boolean hasPermission=false;
      if(permissions==null||permissions.isEmpty())
      {

      } else
      {
        for(String str:permissions.keySet())
        {
            if(allPermissions.containsKey(str))
            {
                hasPermission=true;
                break;
            }
        }
      }
        return  hasPermission;
    }

    /**
     * 把特殊字符转换回正常的html标签
     * @param html
     * @return
     */
    private  String entity2Html(String html){
        if (html == null || html.trim().equals(""))
            return html;
        html = html.replaceAll("&quot;","\"");
        html = html.replaceAll("&apos;","'");
        html = html.replaceAll("&lt;","<");
        html = html.replaceAll("&gt;",">");
        html = html.replaceAll("&amp;","&");
        return html;
    }
}
