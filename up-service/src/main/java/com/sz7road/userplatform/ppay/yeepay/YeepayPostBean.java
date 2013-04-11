/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.yeepay;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.SimplePostBean;

import java.io.Serializable;
import java.util.Map;

/**
 * @author jeremy
 */
class YeepayPostBean extends SimplePostBean implements Serializable {

    /**
     * 密钥
     */
    private String key;
    /**
     * 支付网关地址
     */
    private String payUrl;
    /**
     * 在线支付请求，固定值为：Buy
     */
    private final String cmd = "Buy";
    /**
     * 客户编号
     */
    private String merchantAcctId;
    /**
     * 订单号
     */
    private String order;
    /**
     * 支付金额
     */
    private float amount;
    /**
     * 支付币种
     */
    private String currency = "CNY";
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品种类
     */
    private String productCategory;
    /**
     * 商品描述
     */
    private String productDescription;
    /**
     * 接收成功数据的回调地址
     */
    private String callbackUrl;
    /**
     * 是否需要填写送货信息，1/0
     */
    private String saf = "0";
    /**
     * 商户扩展信息
     */
    private String mp;
    /**
     * 支付通道编码
     */
    private String bankId;
    /**
     * 默认为1,应答机制
     */
    private int needResponse = 0;
    /**
     * MD5-HMAC签名
     */
    private String hmac;

    private String encoding;

    /**
     * Construct by default.
     */
    public YeepayPostBean() {
        super();

        key = ConfigurationUtils.get("yeepay.keyValue");
        payUrl = ConfigurationUtils.get("yeepay.commonReqURL");
        merchantAcctId = ConfigurationUtils.get("yeepay.merchantAcctId");
        encoding = ConfigurationUtils.get("yeepay.encoding");
        if (Strings.isNullOrEmpty(encoding)) {
            encoding = "UTF-8";
        }
    }

    public String getCmd() {
        return cmd;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getMerchantAcctId() {
        return merchantAcctId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getSaf() {
        return saf;
    }

    public void setSaf(String saf) {
        this.saf = saf;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public boolean isNeedResponse() {
        return needResponse == 1;
    }

    public void setNeedResponse(boolean needResponse) {
        this.needResponse = needResponse ? 1 : 0;
    }

    public String getHmac() {
        if (!Strings.isNullOrEmpty(hmac)) {
            updateSign();
        }
        return hmac;
    }

    public String getEncoding() {
        return encoding;
    }

    /**
     * 更新签名，在更新参数后，必定要记住调用该方法。
     */
    @Override
    public void updateSign() {
        hmac = MD5HmacDigestUtil.digestAsHex(getSTR(), key);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> map = Maps.newHashMap();
        map.put("p0_Cmd", getCmd());
        map.put("p1_MerId", getMerchantAcctId());
        map.put("p2_Order", getOrder());
        map.put("p3_Amt", getAmount());
        map.put("p4_Cur", getCurrency());
        map.put("p5_Pid", getProductName());
        map.put("p6_Pcat", getProductCategory());
        map.put("p7_Pdesc", getProductDescription());
        map.put("p8_Url", getCallbackUrl());
        map.put("p9_SAF", getSaf());
        map.put("pa_MP", getMp());
        map.put("pd_FrpId", getBankId());
        map.put("pr_NeedResponse", needResponse);
        map.put("hmac", getHmac());
        return map;
    }

    public String getSTR() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getCmd());
        sb.append(getMerchantAcctId());
        sb.append(getOrder());
        sb.append(getAmount());
        sb.append(getCurrency());
        sb.append(Strings.nullToEmpty(getProductName()));
        sb.append(Strings.nullToEmpty(getProductCategory()));
        sb.append(Strings.nullToEmpty(getProductDescription()));
        sb.append(getCallbackUrl());
        sb.append(Strings.nullToEmpty(getSaf()));
        sb.append(Strings.nullToEmpty(getMp()));
        sb.append(Strings.nullToEmpty(getBankId()));
        sb.append(needResponse);
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : toMap().entrySet()) {
            String value = (entry.getValue() == null ? "" : Strings.nullToEmpty(entry.getValue().toString()));
            appendQueryString(sb, entry.getKey(), value);
        }
        return sb.toString();
    }
}
