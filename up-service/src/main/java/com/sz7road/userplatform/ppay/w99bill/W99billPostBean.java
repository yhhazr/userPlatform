/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.w99bill;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pay.w99bill.Pkipair;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.web.SimplePostBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author jeremy
 */
@RequestScoped
class W99billPostBean extends SimplePostBean {

    private String merchantAcctId;
    private String inputCharset;
    private String pageUrl;
    private String bgUrl;
    private String version = "v2.0";
    private String language = "1";
    private String signType = "4";
    private String payerName;
    private String payerContactType = "1";
    private String payerContact;
    private String orderId;
    private long amount;
    private String orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    private String productName;
    private int productNum = 1;
    private String productId;
    private String productDesc;
    private String ext1;
    private String ext2;
    private String payType = "00";
    private String bankId;
    private String redoFlag;
    private String pid;
    private String sign;
    private String gatewayUrl;

    /**
     * Constructs by default.
     */
    W99billPostBean() {
        super();
        gatewayUrl = ConfigurationUtils.get("99bill.gatewayUrl");
        merchantAcctId = ConfigurationUtils.get("99bill.merchantAcctId");
        inputCharset = ConfigurationUtils.get("99bill.inputCharset");
        setProductName(ConfigurationUtils.get("pay.common.description"));
    }

    public String getMerchantAcctId() {
        return merchantAcctId;
    }

    protected void setMerchantAcctId(String merchantAcctId) {
        this.merchantAcctId = merchantAcctId;
    }

    public String getPid() {
        return pid;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getVersion() {
        return version;
    }

    public String getLanguage() {
        return language;
    }

    public String getSignType() {
        return signType;
    }

    protected void setSignType(String signType) {
        this.signType = signType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductNum() {
        return productNum;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getRedoFlag() {
        return redoFlag;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerContactType() {
        return payerContactType;
    }

    public void setPayerContactType(String payerContactType) {
        this.payerContactType = payerContactType;
    }

    public String getPayerContact() {
        return payerContact;
    }

    public void setPayerContact(String payerContact) {
        this.payerContact = payerContact;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getSign() {
        return sign;
    }

    protected void setSign(String sign) {
        this.sign = sign;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> map = Maps.newHashMap();
        map.put("inputCharset", getInputCharset());
        map.put("pageUrl", getPageUrl());
        map.put("bgUrl", getBgUrl());
        map.put("version", getVersion());
        map.put("language", getLanguage());
        map.put("signType", getSignType());
        map.put("signMsg", getSign());
        map.put("merchantAcctId", merchantAcctId);
        map.put("payerName", getPayerName());
        map.put("payerContactType", getPayerContactType());
        map.put("payerContact", getPayerContact());
        map.put("orderId", getOrderId());
        map.put("orderAmount", getAmount());
        map.put("orderTime", getOrderTime());
        map.put("productName", getProductName());
        map.put("productNum", getProductNum());
        map.put("productId", getProductId());
        map.put("productDesc", getProductDesc());
        map.put("ext1", getExt1());
        map.put("ext2", getExt2());
        map.put("payType", getPayType());
        map.put("bankId", getBankId());
        map.put("redoFlag", getRedoFlag());
        map.put("pid", getPid());
        return map;
    }

    @Override
    public String getSTR() {
        final StringBuilder sb = new StringBuilder();
        appendQueryString(sb, "inputCharset", Strings.emptyToNull(getInputCharset()));
        appendQueryString(sb, "pageUrl", Strings.emptyToNull(getPageUrl()));
        appendQueryString(sb, "bgUrl", Strings.emptyToNull(getBgUrl()));
        appendQueryString(sb, "version", Strings.emptyToNull(getVersion()));
        appendQueryString(sb, "language", Strings.emptyToNull(getLanguage()));
        appendQueryString(sb, "signType", Strings.emptyToNull(getSignType()));
        appendQueryString(sb, "merchantAcctId", Strings.emptyToNull(merchantAcctId));
        appendQueryString(sb, "payerName", Strings.emptyToNull(getPayerName()));
        appendQueryString(sb, "payerContactType", Strings.emptyToNull(getPayerContactType()));
        appendQueryString(sb, "payerContact", Strings.emptyToNull(getPayerContact()));
        appendQueryString(sb, "orderId", Strings.emptyToNull(getOrderId()));
        appendQueryString(sb, "orderAmount", String.valueOf(getAmount()));
        appendQueryString(sb, "orderTime", Strings.emptyToNull(getOrderTime()));
        appendQueryString(sb, "productName", Strings.emptyToNull(getProductName()));
        appendQueryString(sb, "productNum", String.valueOf(getProductNum()));
        appendQueryString(sb, "productId", getProductId());
        appendQueryString(sb, "productDesc", Strings.emptyToNull(getProductDesc()));
        appendQueryString(sb, "ext1", Strings.emptyToNull(getExt1()));
        appendQueryString(sb, "ext2", Strings.emptyToNull(getExt2()));
        appendQueryString(sb, "payType", Strings.emptyToNull(getPayType()));
        appendQueryString(sb, "bankId", Strings.emptyToNull(getBankId()));
        appendQueryString(sb, "redoFlag", Strings.emptyToNull(getRedoFlag()));
        appendQueryString(sb, "pid", Strings.emptyToNull(getPid()));
        return sb.toString();
    }

    @Override
    public void updateSign() {
        Pkipair pki = new Pkipair();
        setSign(pki.signMsg(getSTR()));
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    protected void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    @Override
    public String toString() {
        return "W99billPostBean{" +
                "merchantAcctId='" + merchantAcctId + '\'' +
                ", inputCharset='" + inputCharset + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", bgUrl='" + bgUrl + '\'' +
                ", version='" + version + '\'' +
                ", language='" + language + '\'' +
                ", signType='" + signType + '\'' +
                ", payerName='" + payerName + '\'' +
                ", payerContactType='" + payerContactType + '\'' +
                ", payerContact='" + payerContact + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", orderTime='" + orderTime + '\'' +
                ", productName='" + productName + '\'' +
                ", productNum=" + productNum +
                ", productId='" + productId + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", payType='" + payType + '\'' +
                ", bankId='" + bankId + '\'' +
                ", redoFlag='" + redoFlag + '\'' +
                ", pid='" + pid + '\'' +
                ", sign='" + sign + '\'' +
                ", gatewayUrl='" + gatewayUrl + '\'' +
                '}';
    }
}
