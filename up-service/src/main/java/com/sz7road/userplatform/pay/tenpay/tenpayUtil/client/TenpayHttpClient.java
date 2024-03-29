package com.sz7road.userplatform.pay.tenpay.tenpayUtil.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.google.inject.Singleton;
import com.sz7road.userplatform.pay.tenpay.tenpayUtil.util.HttpClientUtil;

/**
 * 财付通http或者https网络通信客户端<br/>
 * ========================================================================<br/>
 * api说明：<br/>
 * setReqContent($reqContent),设置请求内容，无论post和get，都用get方式提供<br/>
 * getResContent(), 获取应答内容<br/>
 * setMethod(method),设置请求方法,post或者get<br/>
 * getErrInfo(),获取错误信息<br/>
 * setCertInfo(certFile, certPasswd),设置证书，双向https时需要使用<br/>
 * setCaInfo(caFile), 设置CA，格式未pem，不设置则不检查<br/>
 * setTimeOut(timeOut)， 设置超时时间，单位秒<br/>
 * getResponseCode(), 取返回的http状态码<br/>
 * call(),真正调用接口<br/>
 * getCharset()/setCharset(),字符集编码<br/>
 *
 * ========================================================================<br/>
 *
 */
@Singleton
public class TenpayHttpClient {

    private static final String USER_AGENT_VALUE =
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";

    private static final String JKS_CA_FILENAME =
            "tenpay_cacert.jks";

    private static final String JKS_CA_ALIAS = "tenpay";

    private static final String JKS_CA_PASSWORD = "";

    /** ca证书文件 */
    private File caFile;

    /** 证书文件 */
    private File certFile;

    /** 证书密码 */
    private String certPasswd;

    /** 请求内容，无论post和get，都用get方式提供 */
    private String reqContent;

    /** 应答内容 */
    private String resContent;

    /** 请求方法 */
    private String method;

    /** 错误信息 */
    private String errInfo;

    /** 超时时间,以秒为单位 */
    private int timeOut;

    /** http应答编码 */
    private int responseCode;

    /** 字符编码 */
    private String charset;

    private InputStream inputStream;

    public TenpayHttpClient() {
        caFile = null;
        certFile = null;
        certPasswd = "";

        reqContent = "";
        resContent = "";
        method = "POST";
        errInfo = "";
        timeOut = 30;//30秒

        responseCode = 0;
        charset = "GBK";

        inputStream = null;
    }

    /**
     * 设置证书信息
     * @param certFile 证书文件
     * @param certPasswd 证书密码
     */
    public void setCertInfo(File certFile, String certPasswd) {
        this.certFile = certFile;
        this.certPasswd = certPasswd;
    }

    /**
     * 设置ca
     * @param caFile
     */
    public void setCaInfo(File caFile) {
        this.caFile = caFile;
    }

    /**
     * 设置请求内容
     * @param reqContent 表求内容
     */
    public void setReqContent(String reqContent) {
        this.reqContent = reqContent;
    }

    /**
     * 获取结果内容
     * @return String
     * @throws IOException
     */
    public String getResContent() {
        try {
            doResponse();
        } catch (IOException e) {
            errInfo = e.getMessage();
        }

        return resContent;
    }

    /**
     * 设置请求方法post或者get
     * @param method 请求方法post/get
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取错误信息
     * @return String
     */
    public String getErrInfo() {
        return errInfo;
    }

    /**
     * 设置超时时间,以秒为单位
     * @param timeOut 超时时间,以秒为单位
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 获取http状态码
     * @return int
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * 执行http调用。true:成功 false:失败
     * @return boolean
     */
    public boolean call() {
        boolean isRet = false;
        //http
        if(null == caFile && null == certFile) {
            try {
                callHttp();
                isRet = true;
            } catch (IOException e) {
                errInfo = e.getMessage();
                e.printStackTrace();

            }
            return isRet;
        }
        //https
        try {
            callHttps();
            isRet = true;
        } catch (UnrecoverableKeyException e) {
            errInfo = e.getMessage();
        } catch (KeyManagementException e) {
            errInfo = e.getMessage();
        } catch (CertificateException e) {
            errInfo = e.getMessage();
        } catch (KeyStoreException e) {
            errInfo = e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            errInfo = e.getMessage();
        } catch (IOException e) {
            errInfo = e.getMessage();
        }

        return isRet;

    }

    protected void callHttp() throws IOException {

        if("POST".equalsIgnoreCase(method)) {
            String url = HttpClientUtil.getURL(reqContent);
            String queryString = HttpClientUtil.getQueryString(reqContent);
            byte[] postData = queryString.getBytes(charset);
            httpPostMethod(url, postData);

            return ;
        }

        httpGetMethod(reqContent);

    }

    protected void callHttps() throws IOException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException {

        // ca目录
        String caPath = caFile.getParent();
        File jksCAFile = new File(caPath + "/"
                + TenpayHttpClient.JKS_CA_FILENAME);
        if (!jksCAFile.isFile()) {
            X509Certificate cert = (X509Certificate) HttpClientUtil
                    .getCertificate(caFile);
            FileOutputStream out = new FileOutputStream(jksCAFile);
            // store jks file
            HttpClientUtil.storeCACert(cert, TenpayHttpClient.JKS_CA_ALIAS,
                    TenpayHttpClient.JKS_CA_PASSWORD, out);
            out.close();
        }
        FileInputStream trustStream = new FileInputStream(jksCAFile);
        FileInputStream keyStream = new FileInputStream(certFile);

        SSLContext sslContext = HttpClientUtil.getSSLContext(trustStream,
                TenpayHttpClient.JKS_CA_PASSWORD, keyStream, certPasswd);
        //关闭流
        keyStream.close();
        trustStream.close();

        if("POST".equals(method.toUpperCase())) {
            String url = HttpClientUtil.getURL(reqContent);
            String queryString = HttpClientUtil.getQueryString(reqContent);
            byte[] postData = queryString.getBytes(charset);
            httpsPostMethod(url, postData, sslContext);
            return ;
        }
        httpsGetMethod(reqContent, sslContext);

    }

    /**
     * 以http post方式通信
     * @param url
     * @param postData
     * @throws IOException
     */
    protected void httpPostMethod(String url, byte[] postData)
            throws IOException {

        HttpURLConnection conn = HttpClientUtil.getHttpURLConnection(url);

        doPost(conn, postData);
    }

    /**
     * 以http get方式通信
     *
     * @param url
     * @throws IOException
     */
    protected void httpGetMethod(String url) throws IOException {

        HttpURLConnection httpConnection = HttpClientUtil.getHttpURLConnection(url);

        setHttpRequest(httpConnection);

        httpConnection.setRequestMethod("GET");

        responseCode = httpConnection.getResponseCode();

        inputStream = httpConnection.getInputStream();

    }

    /**
     * 以https get方式通信
     * @param url
     * @param sslContext
     * @throws IOException
     */
    protected void httpsGetMethod(String url, SSLContext sslContext)throws IOException {

        SSLSocketFactory sf = sslContext.getSocketFactory();

        HttpsURLConnection conn = HttpClientUtil.getHttpsURLConnection(url);

        conn.setSSLSocketFactory(sf);

        doGet(conn);

    }

    protected void httpsPostMethod(String url, byte[] postData, SSLContext sslContext) throws IOException {

        SSLSocketFactory sf = sslContext.getSocketFactory();

        HttpsURLConnection conn = HttpClientUtil.getHttpsURLConnection(url);

        conn.setSSLSocketFactory(sf);

        doPost(conn, postData);

    }

    /**
     * 设置http请求默认属性
     * @param httpConnection
     */
    protected void setHttpRequest(HttpURLConnection httpConnection) {

        //设置连接超时时间
        httpConnection.setConnectTimeout(timeOut * 1000);

        //User-Agent
        httpConnection.setRequestProperty("User-Agent",
                TenpayHttpClient.USER_AGENT_VALUE);

        //不使用缓存
        httpConnection.setUseCaches(false);

        //允许输入输出
        httpConnection.setDoInput(true);
        httpConnection.setDoOutput(true);

    }

    /**
     * 处理应答
     * @throws IOException
     */
    protected void doResponse() throws IOException {

        if(null == inputStream) {
            return;
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, charset) );

        //获取应答内容
        resContent = HttpClientUtil.bufferedReader2String(reader);

        //关闭流
        reader.close();

        //关闭输入流
        inputStream.close();

    }

    /**
     * post方式处理
     * @param conn
     * @param postData
     * @throws IOException
     */
    protected void doPost(HttpURLConnection conn, byte[] postData)
            throws IOException {
        // 以post方式通信
        conn.setRequestMethod("POST");
        // 设置请求默认属性
        setHttpRequest(conn);
        // Content-Type
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
        final int len = 1024; // 1KB
        HttpClientUtil.doOutput(out, postData, len);
        // 关闭流
        out.close();
        // 获取响应返回状态码
        responseCode = conn.getResponseCode();
        // 获取应答输入流
        inputStream = conn.getInputStream();
    }

    /**
     * get方式处理
     * @param conn
     * @throws IOException
     */
    protected void doGet(HttpURLConnection conn) throws IOException {

        //以GET方式通信
        conn.setRequestMethod("GET");

        //设置请求默认属性
        setHttpRequest(conn);

        //获取响应返回状态码
        responseCode = conn.getResponseCode();

        //获取应答输入流
        inputStream = conn.getInputStream();
    }


}
