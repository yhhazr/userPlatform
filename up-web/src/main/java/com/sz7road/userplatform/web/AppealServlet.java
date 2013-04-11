package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Appeal;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.AppealService;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DigitUtils;
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
 * @author jiangfan.zhou
 */

@Singleton
class AppealServlet extends HeadlessHttpServlet {

    private final static Logger log = LoggerFactory.getLogger(AppealServlet.class.getName());
    @Inject
    private Provider<UserService> userServiceProvider;
    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;
    @Inject
    private Provider<LogService> logServiceProvider;
    @Inject
    private Provider<AppealService> appealServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String realName = request.getParameter("realName");
        String idCard = request.getParameter("idCard");
        String oftenPlayGame = request.getParameter("oftenPlayGame");
        String serverName = request.getParameter("serverName");
        String email = request.getParameter("email");
        String playerName = request.getParameter("playerName");
        String strPlayerLevel = request.getParameter("playerLevel");
        int playerLevel = 0;
        String createDate = request.getParameter("createDate");
        String createCity = request.getParameter("createCity");
        String exceptionDate = request.getParameter("exceptionDate");
        String lastLoginDate = request.getParameter("lastLoginDate");
        String strPay = request.getParameter("pay");
        int pay = 0;
        String[] orderArray = request.getParameterValues("orderId");
        String orderIds = "";
        String idCardImgPath = request.getParameter("idCardImgPath");
        String otherInfo = request.getParameter("otherInfo");
        int gainPoints = 0;
        int status = 0;
        String auditor = request.getParameter("auditor");
        Timestamp auditorTime = null;
        Timestamp appealTime = new Timestamp(System.currentTimeMillis());

        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(realName) || Strings.isNullOrEmpty(idCard)
                || Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(oftenPlayGame) || Strings.isNullOrEmpty(serverName)
                || Strings.isNullOrEmpty(playerName) || Strings.isNullOrEmpty(strPlayerLevel) || Strings.isNullOrEmpty(createDate)
                || Strings.isNullOrEmpty(createCity)) {
            response.setStatus(404);
            return;
        }

        playerLevel = DigitUtils.parserInt(strPlayerLevel);
        pay = DigitUtils.parserInt(strPay);
        if (pay == 0 || pay == 1) {} else {
            pay = 0;
        }

        if (pay == 1) {
            if (orderArray != null) {
                for(String orderId: orderArray) {
                    if (orderId != null && !orderId.equals(""))
                        orderIds = orderIds + "," + orderId;
                }
                if (orderIds.length() > 0)
                    orderIds = orderIds.substring(1);
            }
        }

        realName = HtmlUtils.html2FullWidthChar(realName);
        idCard = HtmlUtils.html2FullWidthChar(idCard);
        oftenPlayGame = HtmlUtils.html2FullWidthChar(oftenPlayGame);
        serverName = HtmlUtils.html2FullWidthChar(serverName);
        email = HtmlUtils.html2FullWidthChar(email);
        playerName = HtmlUtils.html2FullWidthChar(playerName);
        createDate = HtmlUtils.html2FullWidthChar(createDate);
        createCity = HtmlUtils.html2FullWidthChar(createCity);
        exceptionDate = HtmlUtils.html2FullWidthChar(exceptionDate);
        lastLoginDate = HtmlUtils.html2FullWidthChar(lastLoginDate);
        orderIds = HtmlUtils.html2FullWidthChar(orderIds);
        otherInfo = HtmlUtils.html2FullWidthChar(otherInfo);

        if (realName.length() > 10) {
            realName = realName.substring(0, 10);
        }
        if (idCard.length() > 18) {
            idCard = idCard.substring(0, 18);
        }
        if (oftenPlayGame.length() > 10) {
            oftenPlayGame = oftenPlayGame.substring(0, 10);
        }
        if (serverName.length() > 10) {
            serverName = serverName.substring(0, 10);
        }
        if (email.length() > 30) {
            email = email.substring(0, 30);
        }
        if (playerName.length() > 10) {
            playerName = playerName.substring(0, 10);
        }
        if (createCity.length() > 20) {
            createCity = createCity.substring(0, 20);
        }
        if (exceptionDate.length() > 20) {
            exceptionDate = exceptionDate.substring(0, 20);
        }
        if (lastLoginDate.length() > 20) {
            lastLoginDate = lastLoginDate.substring(0, 20);
        }
        if (orderIds.length() > 150) {
            orderIds = orderIds.substring(0, 150);
        }
        if (otherInfo == null){
            otherInfo = "";
        }
        if (otherInfo.length() > 1000) {
            otherInfo = otherInfo.substring(0, 1000);
        }

        Msg msg = new Msg(0, "申诉失败");
        try {
            UserService userService = userServiceProvider.get();
            UserAccount userAccount = userService.findAccountByUserName(userName);
            if (userAccount == null) {
                throw new IllegalArgumentException("账户不存在");
            }
            Appeal entity = new Appeal();
            entity.setUserName(userName);
            entity.setUserId(userAccount.getId());
            entity.setRealName(realName);
            entity.setIdCard(idCard);
            entity.setOftenPlayGame(oftenPlayGame);
            entity.setServerName(serverName);
            entity.setEmail(email);
            entity.setPlayerName(playerName);
            entity.setPlayerLevel(playerLevel);
            entity.setCreateDate(createDate);
            entity.setCreateCity(createCity);
            entity.setExceptionDate(exceptionDate);
            entity.setLastLoginDate(lastLoginDate);
            entity.setPay(pay);
            entity.setOrderIds(orderIds);
            //entity.setIdCardImgPath(idCardImgPath);
            entity.setOtherInfo(otherInfo);
            entity.setGainPoints(gainPoints);
            entity.setStatus(status);
            entity.setAuditor(auditor);
            entity.setAuditorTime(auditorTime);
            entity.setAppealTime(appealTime);

            String strImgPath = ConfigurationUtils.get("path.img.appeal.idcard");
            if (strImgPath == null || strImgPath.equals("")) {
                strImgPath = getServletContext().getRealPath("/") + "upload/";
            }
            if ( idCardImgPath == null || "".equals(idCardImgPath.trim()) || idCardImgPath.indexOf(".") == -1) {
                entity.setIdCardImgPath("");
            } else {
                entity.setIdCardImgPath(strImgPath + idCardImgPath);
            }

            AppealService service = appealServiceProvider.get();
            int ret = service.add(entity);
            if (ret > 0) {
                msg.setCode(1);
                msg.setMsg("申诉成功");
            }
        } catch (Exception e) {
            msg.setCode(0);
            msg.setMsg(e.getMessage());
        }

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String resp = mapper.writeValueAsString(msg);
        writer.write(resp);
        writer.flush();
        writer.close();
    }
}
