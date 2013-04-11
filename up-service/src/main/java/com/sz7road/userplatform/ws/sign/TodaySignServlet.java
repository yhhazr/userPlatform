package com.sz7road.userplatform.ws.sign;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.utils.DataUtil;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-18
 * Time: 下午1:54
 * 今天签到的接口
 */
@Singleton
public class TodaySignServlet extends HeadlessServlet{
    @Inject
    private Provider<SignServiceInterface> signServiceInterfaceProvider;
    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {

        String uid=request.getParameter("uid");
        String gid=request.getParameter("gid");
        Sign sign=new Sign();
        if(Strings.isNullOrEmpty(uid)||Strings.isNullOrEmpty(gid)|| !VerifyFormItem.isInteger(uid)||!VerifyFormItem.isInteger(gid)||Integer.parseInt(uid)<=0||Integer.parseInt(gid)<=0)
        {
            final  String message="参数uid,gid非法,uid:"+uid+" gid:"+gid;
            sign.setCode(100);
            sign.setMsg(message);
            log.error(message);
        }
         else
        {
        SignServiceInterface signService=signServiceInterfaceProvider.get();

         sign=signService.todaySign(Integer.parseInt(uid),Integer.parseInt(gid));
        }
        DataUtil.returnJson(response,sign);
    }
}
