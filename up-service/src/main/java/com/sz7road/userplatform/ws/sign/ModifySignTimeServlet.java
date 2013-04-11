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
 * Date: 13-1-29
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ModifySignTimeServlet extends HeadlessServlet {
    @Inject
    private Provider<SignServiceInterface> signServiceInterfaceProvider;
    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {

        String uid=request.getParameter("uid");
        String gid=request.getParameter("gid");
        String days=request.getParameter("days");
        int rel=0;
        if(Strings.isNullOrEmpty(uid)||Strings.isNullOrEmpty(gid)|| !VerifyFormItem.isInteger(uid)||!VerifyFormItem.isInteger(gid)
                ||Integer.parseInt(uid)<=0||Integer.parseInt(gid)<=0||Strings.isNullOrEmpty(days)||!VerifyFormItem.isInteger(days)||
                Integer.parseInt(days)<=0)
        {
            final String message= "参数非法,uid:"+uid+" gid:"+gid+" days:"+days;
            response.sendFailure(message);
            log.error(message);
        }
        else
        {
            SignServiceInterface signService=signServiceInterfaceProvider.get();

            rel=signService.modifySignTime(Integer.parseInt(uid),Integer.parseInt(gid),Integer.parseInt(days));

            if(rel<=0)
            {
                response.sendFailure("修改时间失败!");
            }
            else
            {
                response.sendSuccess("成功修改时间！");
            }
        }

    }
}