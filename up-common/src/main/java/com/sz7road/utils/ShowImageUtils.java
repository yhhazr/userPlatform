/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import java.io.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangfan.zhou
 */

public class ShowImageUtils {

    //response.setContentType("image/*"); // 设置返回的文件类型
    //OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
    //toClient.write(data); // 输出数据
    //toClient.close();

    public static byte[] show(String path) throws IOException {
        InputStream fis = null;
        byte[] data = null;
        try{
            fis = new FileInputStream(path);
            int len = fis.available();
            data = new byte[len];
            fis.read(data);
            return data;
        } finally {
            if (fis != null) {
                fis.close();
                data = null;
            }
        }
    }

    public static void show(OutputStream out, String path) throws IOException {
        InputStream fis = null;
        byte[] buf = new byte[1024];
        try{
            File imgFile = new File(path);
            if (!imgFile.exists())
                return ;
            fis = new FileInputStream(path);
            int len = 0;
            while((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
                out.flush();
            }
        } finally {
            if (fis != null) {
                out.close();
                fis.close();
                buf = null;
            }
        }
    }

}
