/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author leo.liao
 */

public class DesUtils {

    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    private static final String PUB_KEY = "12345678";
    private static final String EMAIL_PRI_KEY = "FSDKLFJSALDKFASKLDFJASDKLFJLKASDFJSDKLFJASLDKFJASDLKFSADLFJSDKLFJSDKFJSDLKFJSDLFJSDKLFSDJKLFJSDKFJSD";

    public static String encrypt(String text) throws Exception {
        return encode(EMAIL_PRI_KEY, text);
    }

    public static String decrypt(String text) throws Exception {
        return decode(EMAIL_PRI_KEY, text);
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String encode(String key, String data) throws Exception {
        return encode(key, data.getBytes());
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String encode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(PUB_KEY.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);
            String str = new String(Base64.encode(bytes));
            return str;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] decode(String key, byte[] data) throws Exception {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(PUB_KEY.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 获取编码后的值
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String decode(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            if (System.getProperty("os.name") != null && (System.getProperty("os.name").equalsIgnoreCase("sunos") || System.getProperty("os.name").equalsIgnoreCase("linux"))) {
                datas = decode(key, Base64.decode(data));
            } else {
                datas = decode(key, Base64.decode(data));
            }

            value = new String(datas);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

//    public static void main(String[] args) throws Exception {
//        String mw = DesUtils.encode("fsdfdsfsdfsdfsd", "cychai");
//        System.out.println("明：cychai ；密：" + mw);
//        System.out.println("密：" + mw + "明：" + DesUtils.decode("fsdfdsfsdfsdfsd", mw));
//    }
}
