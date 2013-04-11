/**
 * 生成验证码的serverlet
 */

package com.sz7road.userplatform.web;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.VerifyCodeService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.utils.CaptchaCode;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Singleton
public class CaptchaServlet extends HeadlessHttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    @Inject
    Provider<VerifyCodeService> verifyCodeServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String timeStr= request.getParameter("time");
        if(!Strings.isNullOrEmpty(timeStr))
        {
            timeStr= HtmlUtils.html2Entity(timeStr);
        }
        response.setContentType("image/jpeg");
        CaptchaCode image = new CaptchaCode();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream os = null;
        try {
            BufferedImage img = image.creatImage();
            request.getSession().setAttribute(KAPTCHA_SESSION_KEY, image.getSRand());
            //存到数据库
            VerifyCodeService verifyCodeService= verifyCodeServiceProvider.get();
            VerifyCode verifyCode=new VerifyCode();
            verifyCode.setCode(image.getSRand());
            long expireTime=new Date().getTime()+600000;
            verifyCode.setExpiryTime(expireTime);
            verifyCode.setVerify("captcha_"+timeStr+"_rl");
            verifyCodeService.add(verifyCode);
            os = response.getOutputStream();
            ImageIO.write(img, "JPEG", os);
            os.flush();

        } catch (Exception e) {
            System.out.println("errors:" + e);
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
