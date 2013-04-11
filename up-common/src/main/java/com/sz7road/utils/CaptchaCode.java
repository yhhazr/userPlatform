/**
 生成验证码帮助类
 *
 */
package com.sz7road.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class CaptchaCode {

    public String sRand = "";

    public Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public BufferedImage creatImage() {
        char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'k', 'm', 'n', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'G', 'N', '2', '3', '4', '5', '6', '7', '8', '9'};
        int width = 70, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(new Color(255, 255, 255)); //set the background color
        g.fillRect(0, 0, width, height);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 24));

        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(codeSequence[random.nextInt(62)]).toUpperCase();
            sRand += rand;

            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 16 * i + 4, 30);
        }

        g.dispose();
        return image;
    }


    public String getSRand() {
        return sRand;
    }

}
