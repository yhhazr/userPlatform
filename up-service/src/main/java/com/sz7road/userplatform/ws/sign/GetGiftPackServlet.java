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
 * 领取礼包的接口
 */
@Singleton
public class GetGiftPackServlet extends HeadlessServlet{
    @Inject
    private Provider<SignServiceInterface> signServiceInterfaceProvider;
    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {

        String uid=request.getParameter("uid");
        String gid=request.getParameter("gid");
        String giftPackScore=request.getParameter("giftPackScore");
        Sign sign=new Sign();
        if(Strings.isNullOrEmpty(uid)||Strings.isNullOrEmpty(gid)||Strings.isNullOrEmpty(giftPackScore)|| !VerifyFormItem.isInteger(giftPackScore)||
                !VerifyFormItem.isInteger(uid)||!VerifyFormItem.isInteger(gid)||Integer.parseInt(gid)<=0||
                Integer.parseInt(uid)<=0||Integer.parseInt(giftPackScore)<=0)
        {
           final String message= "参数非法,uid:"+uid+" gid:"+gid+" giftPackScore:"+giftPackScore;
            sign.setCode(100);
            sign.setMsg(message);
            log.error(message);
        }
         else
        {
        SignServiceInterface signService=signServiceInterfaceProvider.get();

         sign=signService.getGiftPack(Integer.parseInt(uid),Integer.parseInt(gid),Integer.parseInt(giftPackScore));
        }
        DataUtil.returnJson(response,sign);

    }
}
