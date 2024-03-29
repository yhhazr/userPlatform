package com.sz7road.userplatform.pay.tenpay.tenpayUtil.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


import java.io.IOException;

import com.google.inject.Singleton;
import com.sz7road.userplatform.pay.tenpay.tenpayUtil.util.MD5Util;

import com.sz7road.utils.XMLUtil;
import org.jdom.JDOMException;
/**
 * 后台应答类<br/>
 * ========================================================================<br/>
 * api说明：<br/>
 * getKey()/setKey(),获取/设置密钥<br/>
 * getContent() / setContent(), 获取/设置原始内容<br/>
 * getParameter()/setParameter(),获取/设置参数值<br/>
 * getAllParameters(),获取所有参数<br/>
 * isTenpaySign(),是否财付通签名,true:是 false:否<br/>
 * getDebugInfo(),获取debug信息<br/>
 *
 * ========================================================================<br/>
 *
 */
@Singleton
public class ClientResponseHandler {

    /** 应答原始内容 */
    private String content;

    /** 应答的参数 */
    private SortedMap parameters;

    /** 密钥 */
    private String key;

    /** 字符集 */
    private String charset;

    public ClientResponseHandler() {
        content = "";
        parameters = new TreeMap();
        key = "";
        charset = "GBK";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws Exception {
        this.content = content;

        doParse();
    }

    /**
     * 获取参数值
     * @param parameter 参数名称
     * @return String
     */
    public String getParameter(String parameter) {
        String s = (String)parameters.get(parameter);
        return (null == s) ? "" : s;
    }

    /**
     * 设置参数值
     * @param parameter 参数名称
     * @param parameterValue 参数值
     */
    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if(null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    /**
     * 返回所有的参数
     * @return SortedMap
     */
    public SortedMap getAllParameters() {
        return parameters;
    }


    /**
     *获取密钥
     */
    public String getKey() {
        return key;
    }

    /**
     *设置密钥
     */
    public void setKey(String key) {
        this.key = key;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @return boolean
     */
    public boolean isTenpaySign() {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if(!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + getKey());
        //算出摘要
        String sign = MD5Util.MD5Encode(sb.toString(), charset);
        String tenpaySign = getParameter("sign");
        return tenpaySign.equalsIgnoreCase(sign);
    }

    /**
     * 是否财付通签名
     * @param signParameterArray 签名的参数数组
     * @return boolean
     */
    protected boolean isTenpaySign(String signParameterArray[]) {

        StringBuffer signPars = new StringBuffer();
        for(int index = 0; index < signParameterArray.length; index++) {
            String k = signParameterArray[index];
            String v = getParameter(k);
            if(null != v && !"".equals(v)) {
                signPars.append(k + "=" + v + "&");
            }
        }
        signPars.append("key=" + getKey());
        //算出摘要
        String sign = MD5Util.MD5Encode(signPars.toString(), charset).toLowerCase();
        String tenpaySign = getParameter("sign").toLowerCase();
        return tenpaySign.equals(sign);
    }



    /**
     * 解析XML内容
     */
    protected void doParse() throws JDOMException, IOException {
        String xmlContent = getContent();
        //解析xml,得到map
        Map m = XMLUtil.doXMLParse(xmlContent);
        //设置参数
        Iterator it = m.keySet().iterator();
        while(it.hasNext()) {
            String k = (String) it.next();
            String v = (String) m.get(k);
            setParameter(k, v);
        }

    }


}
