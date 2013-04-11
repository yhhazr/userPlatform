/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Log.LogType;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.DateUtils;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.DropDownDataUtil;
import com.sz7road.utils.IcnConfirmUtil;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * @author cutter.li
 */
@Singleton
class AccountInfoSubmitServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AccountInfoSubmitServlet.class);

    @Inject
    private Provider<UserService> userServiceProvider;

    @Inject
    private Provider<LogService> logServiceProvider;

    private final static Logger logger = LoggerFactory.getLogger(AccountInfoSubmitServlet.class);

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Msg msg = new Msg();
        msg.setMsg("修改资料成功");
        String type = request.getParameter("type");
        type = HtmlUtils.html2Entity(type);
        String _uname = AppHelper.getUserName(request);
        String userName = request.getParameter("userName");
        userName = HtmlUtils.html2EntityAndCheckMaxLength(userName, 20);
        if (Strings.isNullOrEmpty(_uname) || !_uname.equals(userName)) {
            response.setStatus(404);
            return;
        } else {
            msg = updateUserInfo(request, type, userName);
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String resp = mapper.writeValueAsString(msg);
            writer.write(resp);
            writer.flush();
            writer.close();
        }

    }

    private Msg updateUserInfo(HttpServletRequest request, String type, String userName) throws ServletException, IOException {
        Msg msg = new Msg();
        int rel = 0;
        UserService userService = userServiceProvider.get();
        UserObject userObject = userService.getUserObject(userName);
        if ("basic".equals(type)) {
            //----------------------------------接收参数------------------------------------------------
            String nickName = request.getParameter("nickName"); //昵称
            String realName = request.getParameter("realName");//真实姓名
            String city = request.getParameter("city");//所在城市
            String gender = request.getParameter("gender");//性别
            String birthday = request.getParameter("birthday");//生日
            String icn = request.getParameter("icn");//身份证号码
            String qq = request.getParameter("qq");//QQ
            String msn = request.getParameter("msn");//msn
            String linkPhone = request.getParameter("linkPhone");//联系电话
            String selfIntroduction = request.getParameter("selfIntroduction");//自我介绍
            //----------------------------------验证判断------------------------------------------------
            nickName = HtmlUtils.html2EntityAndCheckMaxLength(nickName, 20);
            realName = HtmlUtils.html2EntityAndCheckMaxLength(realName, 20);
            city = HtmlUtils.html2EntityAndCheckMaxLength(city, 80);
            gender = HtmlUtils.html2EntityAndCheckMaxLength(gender, 1);
            birthday = HtmlUtils.html2EntityAndCheckMaxLength(birthday, 10);
            icn = HtmlUtils.html2EntityAndCheckMaxLength(icn, 18);
            qq = HtmlUtils.html2EntityAndCheckMaxLength(qq, 20);
            msn = HtmlUtils.html2EntityAndCheckMaxLength(msn, 30);
            linkPhone = HtmlUtils.html2EntityAndCheckMaxLength(linkPhone, 20);
            selfIntroduction = HtmlUtils.html2EntityAndCheckMaxLength(selfIntroduction, 200);


            userObject.setNickName(nickName);
            userObject.setRealName(realName);
            userObject.setCity(city);
            userObject.setGender(Byte.parseByte(gender));
            userObject.setBirthday(DateUtils.getDate(birthday));
            userObject.setQq(qq);
            userObject.setMsn(msn);
            userObject.setLinkPhone(linkPhone);
            userObject.setSelfIntroduction(selfIntroduction);
            if (!Strings.isNullOrEmpty(icn) && IcnConfirmUtil.validateCard(icn)) { //身份证号码
                userObject.setIcn(icn);
            }
            rel = userService.updateUserBasicInfo(userObject);
        }
        if ("detail".equals(type)) {
            //----------------------------------接收参数------------------------------------------------
            String marryStatus = request.getParameter("marryStatus"); //婚姻状况
            String hobby = request.getParameter("hobby");//爱好
            String career = request.getParameter("career");//职业

            //----------------------------------验证判断------------------------------------------------
            marryStatus = HtmlUtils.html2EntityAndCheckMaxLength(marryStatus, 1);
            hobby = HtmlUtils.html2EntityAndCheckMaxLength(hobby, 30);
            career = HtmlUtils.html2EntityAndCheckMaxLength(career, 20);
            userObject.setMarryStatus(Integer.parseInt(marryStatus));
            userObject.setHobby(hobby);
            userObject.setCareer(career);
            rel = userService.updateUserDetailInfo(userObject);
        }
        if ("edu".equals(type)) {
            //----------------------------------接收参数------------------------------------------------
            String eduLevel = request.getParameter("eduLevel");
            String schoolType = request.getParameter("schoolType");
            String schoolName = request.getParameter("schoolName");
            String startEduYear = request.getParameter("startEduYear");
            //----------------------------------验证判断------------------------------------------------
            eduLevel = HtmlUtils.html2EntityAndCheckMaxLength(eduLevel, 3);
            schoolType = HtmlUtils.html2EntityAndCheckMaxLength(schoolType, 3);
            schoolName = HtmlUtils.html2EntityAndCheckMaxLength(schoolName, 20);
            startEduYear = HtmlUtils.html2EntityAndCheckMaxLength(startEduYear, 4);
            userObject.setSchoolType(Integer.parseInt(schoolType));
            userObject.setSchoolName(schoolName);
            userObject.setStartEduYear(Integer.parseInt(startEduYear));
            rel = userService.updateUserEduInfo(userObject);
        }
        if ("work".equals(type)) {
            //----------------------------------接收参数------------------------------------------------
            String companyName = request.getParameter("companyName");
            String workStartYear = request.getParameter("workStartYear");
            String workEndYear = request.getParameter("workEndYear");
            String workPost = request.getParameter("workPost");
            //----------------------------------验证判断------------------------------------------------
            companyName = HtmlUtils.html2EntityAndCheckMaxLength(companyName, 50);
            workStartYear = HtmlUtils.html2EntityAndCheckMaxLength(workStartYear, 4);
            workEndYear = HtmlUtils.html2EntityAndCheckMaxLength(workEndYear, 4);
            workPost = HtmlUtils.html2EntityAndCheckMaxLength(workPost, 20);
            userObject.setCompanyName(companyName);
            userObject.setWorkStartYear(Integer.parseInt(workStartYear));
            userObject.setWorkEndYear(Integer.parseInt(workEndYear));
            userObject.setWorkPost(workPost);
            rel = userService.updateUserWorkInfo(userObject);
        }
        if (rel > 0) {
            msg.setCode(200);
            msg.setMsg("更新用户信息成功!");
            msg.setObject(rel);
            logUserInfo(userObject);//记录日志
        } else {
            msg.setCode(204);
            msg.setMsg("更新用户信息失败!或者没有填写至少一个信息！");
            msg.setObject(rel);
        }
        return msg;
    }

    private void logUserInfo(UserObject userObject) {
        if (userObject != null) {
            StringBuilder sb = new StringBuilder();
            StringBuilder con1 = new StringBuilder();
            StringBuilder con2 = new StringBuilder();
            StringBuilder con3 = new StringBuilder();
            Log log = new Log();
            if (!Strings.isNullOrEmpty(userObject.getCompanyName())) {
                sb.append("|单位=").append(userObject.getCompanyName());
            }
            if (userObject.getWorkStartYear() >= 0) {//开始工作时间
                sb.append("|wsy=").append(userObject.getWorkStartYear());
            }
            if (userObject.getWorkEndYear() >= 0) { //结束工作时间
                sb.append("|wey=").append(userObject.getWorkEndYear());
            }
            if (!Strings.isNullOrEmpty(userObject.getWorkPost())) { //职业
                sb.append("|职业=").append(userObject.getWorkPost());
            }
            if (userObject.getEduLevel() >= 0) {//教育程度
                sb.append("|学历=").append(DropDownDataUtil.getEduLev(userObject.getEduLevel()));
            }
            if (!Strings.isNullOrEmpty(userObject.getIcn())) { //身份证号码
                con1.append("|icn=").append(userObject.getIcn());
            }
            if (userObject.getSchoolType() >= 0) {//学校类型
                con1.append("|学校=").append(DropDownDataUtil.getSchoolType(userObject.getSchoolType()));
            }
            if (!Strings.isNullOrEmpty(userObject.getSchoolName())) {
                con1.append("|校名=").append(userObject.getSchoolName());
            }
            if (userObject.getStartEduYear() >= 0) {
                con1.append("|ey=").append(userObject.getStartEduYear());
            }
            if (userObject.getGender() == 0 || userObject.getGender() == 1) {//设置性别
                con1.append("|性别=").append(String.valueOf(userObject.getGender()));
            }
            if (userObject.getBirthday() != null && !Strings.isNullOrEmpty(userObject.getBirthday().toString())) { //生日
                con1.append("|生日=").append(DateUtils.getDate(DateUtils.datetoStr(userObject.getBirthday())));
            }
            if (!Strings.isNullOrEmpty(userObject.getSelfIntroduction())) {//自我介绍
                con1.append("|简介=").append("...此处省略一万字...");
            }
            if (!Strings.isNullOrEmpty(userObject.getCity())) { //设置所在城市
                con2.append("|城市=").append(userObject.getCity());
            }
            if (!Strings.isNullOrEmpty(userObject.getQq())) {  //QQ
                con2.append("|QQ=").append(userObject.getQq());
            }
            if (!Strings.isNullOrEmpty(userObject.getLinkPhone())) { //联系电话
                con2.append("|电话=").append(userObject.getLinkPhone());
            }
            if (!Strings.isNullOrEmpty(userObject.getHobby())) {//爱好
                con2.append("|爱好=").append(userObject.getHobby());
            }
            if (!Strings.isNullOrEmpty(userObject.getMsn())) {//msn
                con3.append("|MSN=").append(userObject.getMsn());
            }
            if (userObject.getMarryStatus() >= 0) {//婚姻状况
                con3.append("|婚姻=").append(DropDownDataUtil.getMarryStatus(userObject.getMarryStatus()));
            }

            if (!Strings.isNullOrEmpty(userObject.getCareer())) { //职业
                con3.append("|职业=").append(userObject.getCareer());
            }
            if (!Strings.isNullOrEmpty(userObject.getNickName())) {//设置昵称
                con3.append("|昵称=").append(userObject.getNickName());
            }
            if (!Strings.isNullOrEmpty(userObject.getRealName())) {//设置真实姓名
                con3.append("|真名=").append(userObject.getRealName());
            }

            log.setUserName(userObject.getUserName());
            log.setLogType(LogType.ACCOUNT_UPDATE);
            log.setLog_time(new Timestamp(System.currentTimeMillis()));
            log.setContent(sb.toString());
            log.setExt1(con1.toString());
            log.setExt2(con2.toString());
            log.setExt3(con3.toString());
            try {
                LogService logService = logServiceProvider.get();
                logService.addTask(log);
            } catch (Exception ex) {
                logger.error("增加异步日志任务失败!" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


}
