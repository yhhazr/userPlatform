/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sz7road.userplatform.web.account;

import com.google.inject.servlet.ServletModule;
import com.sz7road.userplatform.web.*;
import com.sz7road.userplatform.web.cscenter.CsInfoServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sz7road.userplatform.ws.TestCacheServlet;

/**
 * @author leo.liao
 */
public class AccountWebServiceModule extends ServletModule {

    private static final Logger log = LoggerFactory.getLogger(AccountWebServiceModule.class.getName());

    @Override
    protected void configureServlets() {
        serve("/accountcenter.html").with(AccountCenterServlet.class);
        serve("/AccountInfoSubmit").with(AccountInfoSubmitServlet.class);
        serve("/PasswordModifySubmit").with(PasswordModifySubmitServlet.class);
        serve("/EmailVerifySend").with(EmailVerifySendServlet.class);
        serve("/EmailActivate").with(EmailActivateServlet.class);
        serve("/PlayGame").with(PlayGameServlet.class);
        serve("/VerifyCode").with(VerifyCodeServlet.class);
        serve("/CookieTest").with(CookieTestServlet.class);
        serve("/forgetpwd.html").with(ForgetPwdServlet.class);
        serve("/ForgetPwdSubmit").with(ForgetPwdSubmitServlet.class);

        serve("/resetpwd.html").with(ResetPwServlet.class);
        serve("/ResetPwdSubmit").with(ResetPwdSubmitServlet.class);
        //登录和注册----------新页面-----------------------------------
        serve("/login.html").with(LoginPageServlet.class);
        serve("/register.html").with(RegisterPageServlet.class);
        serve("/fcm_rz.html").with(RegisterFcmRzPageServlet.class);
        serve("/fcmrz").with(RegisterFcmRzServlet.class);
        serve("/login").with(LoginServlet.class);
        serve("/register").with(RegisterServlet.class);
        serve("/register2").with(Register_fcmServlet.class);
        serve("/verifyCode").with(CaptchaServlet.class);
        serve("/nav").with(NavServlet.class);
        serve("/logout").with(LogoutServlet.class);
        //-------------------------------------------------------------
        // 邮箱绑定,解除绑定
        serve("/bindEmail").with(BindEmailServlet.class);
        serve("/sendBindEmail").with(SendCodeBindEmailServlet.class);
        serve("/sendBindEmailSubmit").with(SendCodeBindEmailSubmitServlet.class);

        // 密保问题设置
        serve("/bindQuestion").with(QuestionServlet.class);
        serve("/questionSubmit").with(QuestionSubmitServlet.class);

        // 密保手机绑定
        serve("/bindMobile").with(BindMobileServlet.class);
        serve("/sendMobileCode").with(SendMobileCodeServlet.class);
        serve("/sendMobileCodeSubmit").with(SendMobileCodeSubmitServlet.class);

        // 验证码验证
        serve("/CheckVerifyCode").with(CheckVerifyCodeServlet.class);

        // 密保问题找回密码
        serve("/forget.html").with(ForgetServlet.class);
        serve("/findPass").with(FindPassServlet.class);
        serve("/findPassWay").with(FindPassWayServlet.class);
        serve("/findPassQuestion").with(FindPassQuestionServlet.class);
        serve("/resetPass").with(ResetPassServlet.class);
        serve("/resetPassSubmit").with(ResetPassSubmitServlet.class);
        serve("/resetPassMobile").with(ResetPassMobileServlet.class);
        serve("/resetPassMobileSubmit").with(ResetPassMobileSubmitServlet.class);

        serve("/csInfo.html").with(CsInfoServlet.class);
        //头像上传
        serve("/avatar").with(UploadAvaterServlet.class);
        serve("/imgCrop").with(ImgCropServlet.class);
        log.info("Finished install the AccountCenter WebService module.");
    }
}
