/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */
package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.VerifyCode;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author leo.liao
 */
@Singleton
class VerifyCodeServlet extends HeadlessHttpServlet {

    @Inject
    private Provider<VerifyCodeProvider> verifyCodeProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        String _t = request.getParameter("_t");
        // 设置图片的长宽
        int width = 62, height = 22;
        // ////// 创建内存图像
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.createGraphics();
        // 设定图像背景色(因为是做背景，所以偏淡)
        g.setColor(getRandColor(180, 250));
        g.fillRect(0, 0, width, height);
        // 设置字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        // /////设置默认生成4个验证码
        int length = 4;
        java.util.Random rand = new Random(); // 设置随机种子

        // 设置备选验证码:包括"a-zA-Z"和数字"0-9"
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int size = base.length();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int start = rand.nextInt(size);
            String tmpStr = base.substring(start, start + 1);
            str.append(tmpStr);
            // 生成随机颜色(因为是做前景，所以偏深)
            g.setColor(getRandColor(10, 150));
            // 将此字画到图片上
            // g.drawString(str.toString(), 4, 17);
            g.drawString(tmpStr, 13 * i + 6 + rand.nextInt(5), 14 + rand.nextInt(6));
        }
        // 将认证码存入session
//        request.getSession().setAttribute("verifyCode", str.toString());
//        //输出图像到页面
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());  
//        encoder.encode(image);
        // 图象生效
        g.dispose();
        // 输出图象到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());
        VerifyCodeProvider service = verifyCodeProvider.get();
        String et = ConfigurationUtils.get("verifyCode.expiryTime");
        long expiryTime = Strings.isNullOrEmpty(et) ? 30000 : Long.parseLong(et);
        //service.add(new VerifyCode("verify_" + _t, str.toString(), Long.parseLong(_t) + expiryTime));
        service.add(new VerifyCode("verify_" + _t, str.toString(), System.currentTimeMillis() + expiryTime));
    }

    Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
