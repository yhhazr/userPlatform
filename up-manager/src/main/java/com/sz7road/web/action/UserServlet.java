package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.UserInfoObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.UserManagerService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.manager.SessionManager;
import com.sz7road.web.pojos.User;
import com.sz7road.web.utils.ServletUtil;
import com.sz7road.web.utils.TimeStampUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午11:40
 */
@Singleton
public class UserServlet extends BaseServlet {

    @Inject
    private Provider<UserManagerService> userServiceProvider;

    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String forward = "login.jsp";
        Msg msg = new Msg(0, "登陆成功");
        try {
            if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
                throw new IllegalArgumentException("用户名或者密码不能为空");
            }
            UserManagerService userService = userServiceProvider.get();
            User user = userService.authenticated(username, password);
            if (user == null) {
                request.getRequestDispatcher("/" + forward).forward(request, response);
                throw new IllegalArgumentException("用户名或者密码错误");

            } else {
                HttpSession session = request.getSession();
                String sid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
                session.setAttribute("sid", sid);

                user.setLastLoginTime(TimeStampUtil.getNowTimeStamp());
                userService.updateLastLoginTime(user);
                log.info("用户名：" + username + "密码：" + password);
                msg.setObject(user);
//                request.setAttribute("user",user);
//                session.setAttribute(BaseServlet.LOGIN_LISTENER, new LoginOrLogoutSessionListener(sid, user));
//                forward = "index.jsp";
            }
//            request.getRequestDispatcher("/" + forward).forward(request, response);
        } catch (Exception e) {
            msg.build(1, e.getMessage());
        }
        return render(msg, response);
    }

    public String signUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Msg msg = new Msg(0, "注册成功");
        try {
            if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
                throw new IllegalArgumentException("用户名或者密码不能为空");
            }
            UserManagerService userService = userServiceProvider.get();
            if (null != userService.getUserByUsername(username)) {
                throw new IllegalArgumentException("此用户名已存在");
            }
            User user = userService.add(username, password);
            msg.setObject(user);
        } catch (Exception e) {
            msg.build(1, e.getMessage());
        }
        return render(msg, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String sid = (String) session.getAttribute("sid");
        SessionManager.remove(sid);
    }

    public void modifyPwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String sid = (String) session.getAttribute("sid");
        User user = null;
//                SessionManager.get(sid);
        String password = request.getParameter("_p");
        Msg msg = new Msg(0, "修改密码成功");
        try {
            if (user == null) {
                throw new IllegalArgumentException("user null");
            }
            UserManagerService userManagerService = userServiceProvider.get();
            int ret = userManagerService.updatePwd(user, password);
        } catch (Exception e) {
            msg.build(1, e.getMessage());
        }
        render(msg, response);
    }

    public void swapToUserAccountPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("accountQuery.jsp").forward(request, response);
    }

    /**
     * 拿到用户的信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getAccountInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String accountId = null;

        String accountType = null;
        try {
            accountId = request.getParameter("accountId");
            accountType = request.getParameter("queryAccountType");
        } catch (Exception ex) {
            log.error("url中没有传输accountId，或者传输出错！");
        }
        Msg msg = new Msg();
        if (Strings.isNullOrEmpty(accountType)) {
            log.info("没有选择查询类型!");
            msg.setCode(200);
            msg.setMsg("没有选择查询类型");
        } else {
            UserManagerService userService = userServiceProvider.get();

            List<UserInfoObject> userAccount = null;
            boolean flag = !Strings.isNullOrEmpty(accountId);
            if ("userId".equals(accountType) && flag) {
                if (userAccount == null) {
                    userAccount = new ArrayList<UserInfoObject>();
                }
                if (userService.getAccountById(Integer.parseInt(accountId)) != null)
                    userAccount.add(userService.getAccountById(Integer.parseInt(accountId)));
            } else if ("userName".equals(accountType) && flag) {
                userAccount = userService.getAccountByName(accountId);
            }
            if (userAccount == null || userAccount.isEmpty()) {
                msg.setCode(204);
                msg.setMsg("查询的用户不存在!");
            } else {
                msg.setCode(200);
                msg.setMsg("查询到" + userAccount.size() + "条用户信息数据!");
                msg.setObject(userAccount);
            }
        }
        ServletUtil.returnJson(response, msg);

    }

    public void fullTextSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("testLucene.jsp").forward(request, response);
    }

    public void resetMailAndPsw(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg();
        String type = request.getParameter("type");
        Map<String, Object> user = (Map<String, Object>) request.getSession(true).getAttribute("user");
        String userName = user.get("user_comment").toString();
        if (Strings.isNullOrEmpty(type)) {
            msg.setCode(204);
            msg.setMsg("参数为空！");
        } else {
            if ("psw".equals(type)) {
                String userId = request.getParameter("userId").trim();
                String password = request.getParameter("userNewPsw").trim();
                String passwordAgain = request.getParameter("userNewPswAgain").trim();
                if (!Strings.isNullOrEmpty(userId)&&!Strings.isNullOrEmpty(password) && !Strings.isNullOrEmpty(passwordAgain) && password.equals(passwordAgain)) {
                    int resl = userServiceProvider.get().resetPwd(Integer.parseInt(userId), password, userName);
                    if (resl ==2) {
                        msg.setCode(200);
                        msg.setMsg("改密成功!");
                        msg.setObject(resl);
                    } else {
                        msg.setCode(204);
                        msg.setMsg("改密失败!");
                    }
                } else {
                    msg.setCode(204);
                    msg.setMsg("请输入密码（非空格），并确保两次密码一致！");
                }
            } else if ("mail".equals(type)) {
                String userId = request.getParameter("email_userId");
                String email = request.getParameter("emailAddress");
                if (!Strings.isNullOrEmpty(email) && VerifyFormItem.checkEmail(email)
                        &&!Strings.isNullOrEmpty(userId)&&VerifyFormItem.isInteger(userId)) {
                    int resl = userServiceProvider.get().resetEmail(Integer.parseInt(userId), email, userName);
                    if (resl== 2) {
                        msg.setCode(200);
                        msg.setMsg("改邮箱成功!");
                        msg.setObject(resl);
                    } else {
                        msg.setCode(204);
                        msg.setMsg("改邮箱失败!");
                    }
                } else {
                    msg.setCode(204);
                    msg.setMsg("请输入邮箱，并确保邮箱地址合法！");
                }
            }
        }
        ServletUtil.returnJson(response, msg);
    }

    public void resetEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg();
        String userId = request.getParameter("email_userId");
        String email = request.getParameter("emailAddress");

        Map<String, Object> user = (Map<String, Object>) request.getSession(true).getAttribute("user");
        String userName = ((Map<String, Object>) user.get("data")).get("user_comment").toString();
        if (!Strings.isNullOrEmpty(email) && VerifyFormItem.checkEmail(email)) {
            int resl = userServiceProvider.get().resetEmail(Integer.parseInt(userId), email, userName);
            msg.setCode(200);
            msg.setMsg("改邮箱成功!");
            msg.setObject(resl);
        } else {
            msg.setCode(204);
            msg.setMsg("请输入邮箱，并确保邮箱地址合法！");
        }
        ServletUtil.returnJson(response, msg);
    }

    /**
     * 查询用户资料的修改历史
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void queryUserLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accountId = null;
        String accountType = null;
        int start = 0;
        int iPageSize = 0;
        String sortOrderFlag = null;
        //1,定义并且接收参数
        String filters = null;
        String pageIndex = null;
        String pageSize = null;
        String sortField = null;
        String sortOrder = null;
        JqObject operationLogs = null;
        try {
            filters = request.getParameter("filters");
            pageIndex = request.getParameter("page");
            pageSize = request.getParameter("rows");
            sortField = request.getParameter("sidx");
            sortOrder = request.getParameter("sord");
            accountId = request.getParameter("accountId");
            accountType = request.getParameter("queryAccountType");
        } catch (Exception ex) {
            log.error("url中没有传输accountId，或者传输出错！");
        }
        if (ServletUtil.isIntegerAndNotNull(pageSize)) {
            iPageSize = Integer.parseInt(pageSize);
        }
        if (ServletUtil.isIntegerAndNotNull(pageIndex)) {
            int index = Integer.parseInt(pageIndex);
            start = (index - 1) * iPageSize;
        }
        if (sortOrder == null) {
            sortOrder = "ASC";
        }
        if (Strings.isNullOrEmpty(accountType)) {
//            log.info("没有选择查询类型!");
        } else {
            UserManagerService userService = userServiceProvider.get();

            boolean flag = !Strings.isNullOrEmpty(accountId);
            if ("userId".equals(accountType) && flag) {
                operationLogs = userService.getProfileHistoryById(Integer.parseInt(accountId), start, iPageSize);
            } else if ("userName".equals(accountType) && flag) {
                operationLogs = userService.getProfileHistoryByName(accountId, start, iPageSize);
            }
        }
        ServletUtil.returnJson(response, operationLogs);
    }

    public void mergerServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/mergerServer.jsp").forward(request, response);
    }
}
