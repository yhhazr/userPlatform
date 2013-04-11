/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.alipay;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.SimplePostBean;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
@RequestScoped
class AlipayPostBean extends SimplePostBean implements Serializable {

    /**
     * 网关URL
     */
    private String gateWayURL;
    /**
     * 密钥
     */
    private String key;
    /**
     * 网关服务名称。
     */
    private String service = "create_direct_pay_by_user";
    /**
     * 商户ID，即对应文档的partner
     */
    private String partner;
    /**
     * 提交字符集。
     */
    private String inputCharset = "UTF-8";
    /**
     * 签名加密方式。
     */
    private String signType;
    /**
     * 签名摘要串。
     */
    private String sign;
    /**
     * 通知页URL。
     */
    private String notifyUrl;
    /**
     * 返回页面，用于支付宝完成后的重写向。
     */
    private String returnUrl;
    /**
     * 订单号，对于文档中的out_trade_no
     */
    private String outTradeNo;

    /**
     * 支付商品名称
     */
    private String subject;

    /**
     * 支付类型
     */
    private String paymentType = "1";

    /**
     * 默认网银
     */
    private String defaultBank;

    /**
     * 卖家Email
     */
    private String sellerEmail;

    /**
     * 买家Email
     */
    private String buyerEmail;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 卖家账号名称
     */
    private String sellerAccountName;

    /**
     * 买家账号名称
     */
    private String buyerAccountName;

    /**
     * 商品单价
     */
    private float price;

    /**
     * 交易金额
     */
    private float totalFree;

    /**
     * 交易数量
     */
    private int quantity;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商品展示地址。
     */
    private String showUrl;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 是否需要CTU校验。
     */
    private String needCTUCheck = "Y";

    /**
     * 提成类型, 10（卖家给第三方提成）
     */
    private String royaltyType = "";

    /**
     * 提成信息集
     */
    private String royaltyParameters = "";

    /**
     * 防钓鱼时间戳
     */
    private String antiPhishiNgKey;

    /**
     * 客户端IP
     */
    private String exterInvokeIp;

    /**
     * 公用回传参数。
     */
    private String extraCommonParam;

    /**
     * 公用业务扩展参数
     */
    private String extendParam;

    /**
     * 超时时间
     */
    private String ItBPay;

    /**
     * 自动登录标识
     */
    private String defaultLogin;

    /**
     * 商户申请的产品类型。
     */
    private String productType;

    /**
     * Constructs by default.
     */
    public AlipayPostBean() {
        super();

        gateWayURL = ConfigurationUtils.get("alipay.gateway");
        key = ConfigurationUtils.get("alipay.keyValue");
        partner = ConfigurationUtils.get("alipay.merchantAcctId");
        inputCharset = ConfigurationUtils.get("alipay.inputCharset");
        signType = ConfigurationUtils.get("alipay.signType");
        notifyUrl = ConfigurationUtils.get("alipay.notifyUrl");
        returnUrl = ConfigurationUtils.get("alipay.returnUrl");
        sellerEmail = ConfigurationUtils.get("alipay.sellerEmail");
    }

    protected String getKey() {
        return key;
    }

    public String getGateWayURL() {
        return gateWayURL;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDefaultBank() {
        return defaultBank;
    }

    public void setDefaultBank(String defaultBank) {
        this.defaultBank = defaultBank;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerAccountName() {
        return sellerAccountName;
    }

    public void setSellerAccountName(String sellerAccountName) {
        this.sellerAccountName = sellerAccountName;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalFree() {
        return totalFree;
    }

    public void setTotalFree(float totalFree) {
        this.totalFree = totalFree;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getNeedCTUCheck() {
        return needCTUCheck;
    }

    public void setNeedCTUCheck(String needCTUCheck) {
        this.needCTUCheck = needCTUCheck;
    }

    public String getRoyaltyType() {
        return royaltyType;
    }

    public void setRoyaltyType(String royaltyType) {
        this.royaltyType = royaltyType;
    }

    public String getRoyaltyParameters() {
        return royaltyParameters;
    }

    public void setRoyaltyParameters(String royaltyParameters) {
        this.royaltyParameters = royaltyParameters;
    }

    public String getAntiPhishiNgKey() {
        return antiPhishiNgKey;
    }

    public void setAntiPhishiNgKey(String antiPhishiNgKey) {
        this.antiPhishiNgKey = antiPhishiNgKey;
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    public String getItBPay() {
        return ItBPay;
    }

    public void setItBPay(String itBPay) {
        ItBPay = itBPay;
    }

    public String getDefaultLogin() {
        return defaultLogin;
    }

    public void setDefaultLogin(String defaultLogin) {
        this.defaultLogin = defaultLogin;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("payment_type", getPaymentType());
        map.put("out_trade_no", getOutTradeNo());
        map.put("subject", getSubject());
        map.put("body", getBody());
        map.put("total_fee", String.valueOf(getTotalFree()));
        map.put("show_url", getShowUrl());
        map.put("paymethod", getPayMethod());
        map.put("defaultbank", getDefaultBank());
        map.put("anti_phishing_key", getAntiPhishiNgKey());
        map.put("exter_invoke_ip", getExterInvokeIp());
        map.put("extra_common_param", getExtraCommonParam());
        map.put("buyer_email", getBuyerEmail());
        map.put("royalty_type", getRoyaltyType());
        map.put("royalty_parameters", getRoyaltyParameters());

        map.put("service", getService());
        map.put("partner", getPartner());
        map.put("return_url", getReturnUrl());
        map.put("notify_url", getNotifyUrl());
        map.put("seller_email", getSellerEmail());
        map.put("_input_charset", getInputCharset());

        map.put("sign", getSign());
        map.put("sign_type", getSignType());

        return map;
    }

    @Override
    public String getSTR() {
        Map<String, Object> map = toMap();

        map = Maps.filterValues(map, new Predicate<Object>() {

            @Override
            public boolean apply(@Nullable Object value) {
                if (null == value || Strings.isNullOrEmpty(value.toString()))
                    return false;
                return true;
            }
        });

        final List<String> keys = Lists.newArrayList(map.keySet());
        Collections.sort(keys);

        final StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key.equals("sign") || key.equals("sign_type"))
                continue;
            appendQueryString(sb, key, map.get(key));
        }
        return sb.toString();
    }

    @Override
    public void updateSign() {
        setSign(MD5Utils.digestAsHex(getSTR() + getKey()));
    }
}


