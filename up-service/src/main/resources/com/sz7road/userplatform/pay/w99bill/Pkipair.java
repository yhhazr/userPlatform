/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay.w99bill;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 99bill
 */
public class Pkipair {

    private static final Logger log = Logger.getLogger(Pkipair.class.getName());

    public  String signMsg(String signMsg) {
        String base64 = "";
        try {
            // 密钥仓库
            KeyStore ks = KeyStore.getInstance("PKCS12");

            // 读取密钥仓库（相对路径）
//            String file = Pkipair.class.getResource("99bill-rsa.pfx").getPath().replaceAll("%20", " ");
//            FileInputStream ksfis = new FileInputStream(file);
//            BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
            final InputStream ksbufin = Pkipair.class.getResourceAsStream("99bill-rsa.pfx");

            char[] keyPwd = "123456".toCharArray();
            //char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
            ks.load(ksbufin, keyPwd);
            // 从密钥仓库得到私钥
            PrivateKey priK = (PrivateKey) ks.getKey("test-alias", keyPwd);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(priK);
            signature.update(signMsg.getBytes("utf-8"));
            sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            base64 = encoder.encode(signature.sign());

        } catch (FileNotFoundException e) {
            log.severe("文件找不到");
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
        return base64;
    }

    public  boolean enCodeByCer(String val, String msg) {
        boolean flag = false;
        try {
            //获得文件(相对路径)
//            String file = Pkipair.class.getResource("99bill[1].cert.rsa.20140803.cer").toURI().getPath();
//            FileInputStream inStream = new FileInputStream(file);
            final InputStream inStream = Pkipair.class.getResourceAsStream("99bill[1].cert.rsa.20140728.cer");

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
            //获得公钥
            PublicKey pk = cert.getPublicKey();
            //签名
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(pk);
            signature.update(val.getBytes());
            //解码
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            flag = signature.verify(decoder.decodeBuffer(msg));
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
        return flag;
    }
}


