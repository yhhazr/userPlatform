/**
 生成密保卡图片
 *
 */
package com.sz7road.userplatform.ws.mbk;

import com.google.common.collect.Table;
import com.sz7road.userplatform.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class MbkImg {

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

    public BufferedImage creatImage(PswSafeCard pswSafeCard) {
       final int width = 500, height = 420;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(255, 255, 255)); //set the background color
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("宋体", Font.PLAIN, 24));
        g.setColor(new Color(100, 160, 200));
        g.drawString("序列号："+pswSafeCard.getSequenceNum(), 32,40);//画序列号
        //画线
        for (int i = 0; i <=10; i++) {
            g.drawLine(27, i*25+75, 478,i*25+75);//横线
            g.drawLine(i*45+27,75,i*45+27,325);//竖线
        }
        //画边框文字
        g.setColor(new Color(100,10,130));
        for (int i = 0; i <=9; i++) {
            g.drawString(i+"", 42+i*45,70);
            g.drawString(Constant.MBK_CHAR_LIST.get(i),7,98+i*25);
        }
        int j=0;
        //画数字
        g.setColor(new Color(0,0,0));
        for(Object row:pswSafeCard.getContent().rowKeySet())
        {
            int k=1;
            for(Object cell:pswSafeCard.getContent().columnKeySet())
            {
                g.drawString(pswSafeCard.getContent().get(row, cell) + "", (32 + 45 * j), (72 + 25 * k));
                k++;
            }
            j++;
        }
        g.setColor(new Color(255,0,0));
        g.drawString("第七大道密保卡，请妥善保管好！", 35,355);//画序列号
        g.dispose();
        return image;
    }

}
