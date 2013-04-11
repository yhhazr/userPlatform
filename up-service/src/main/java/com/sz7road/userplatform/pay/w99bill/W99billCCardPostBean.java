package com.sz7road.userplatform.pay.w99bill;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 快钱充值卡网关提交表单对象。
 *
 * @author jeremy
 */
@RequestScoped
public class W99billCCardPostBean extends W99billPostBean {

    private static final Map<String, String> merchantAcctIdMapping = Maps.newHashMap();

    //块钱充值卡: 0:神州行，1:联通,3:电信,4:骏网,9:任意,10:盛大,11:征途,12:完美,13:搜狐,14:网易,15:纵游,16:Q币
    static {
        merchantAcctIdMapping.put("0", "99bill.ccard.szx");
        merchantAcctIdMapping.put("1", "99bill.ccard.lt");
        merchantAcctIdMapping.put("3", "99bill.ccard.dx");
        merchantAcctIdMapping.put("4", "99bill.ccard.jw");
        //merchantAcctIdMapping.put("9", "99bill.ccard.ry");
        merchantAcctIdMapping.put("10", "99bill.ccard.sd");
        merchantAcctIdMapping.put("11", "99bill.ccard.zt");
        merchantAcctIdMapping.put("12", "99bill.ccard.wm");
        merchantAcctIdMapping.put("13", "99bill.ccard.sh");
        merchantAcctIdMapping.put("14", "99bill.ccard.wy");
        merchantAcctIdMapping.put("15", "99bill.ccard.zy");
        //merchantAcctIdMapping.put("16", "99bill.ccard.qq");
    }

    private String cardNumber;
    private String cardPwd;
    private String fullAmountFlag = "0";
    private String bossType;
    private String key;
    private String version = "v2.0";

    /**
     * Constructs by google-guice.
     */
    @Inject
    private W99billCCardPostBean() {
        super();

        //setSignType("1"); // 固定值
        //setPayType("42"); // 暂定为固定值
        setGatewayUrl(ConfigurationUtils.get("99bill.ccard.gatewayUrl"));
    }

    public String getCardNumber() {
        return cardNumber;
    }

    protected void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    protected void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    public String getFullAmountFlag() {
        return fullAmountFlag;
    }

    public String getBossType() {
        return bossType;
    }

    public void setBossType(String bossType) {
        this.bossType = bossType;

        // Set the specifial merchantAcctId.
        if (merchantAcctIdMapping.containsKey(bossType)) {
            final String key = merchantAcctIdMapping.get(bossType);
            setMerchantAcctId(ConfigurationUtils.get(key + ".merchantAcctId"));
            setKey(ConfigurationUtils.get(key + ".keyValue"));
        }
    }

    public String getKey() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getVersion(){
        return version;
    }

    @Override
    public void setProductName(String productName) {
        if (!Strings.isNullOrEmpty(productName)) {
            try {
                super.setProductName(URLEncoder.encode(productName, Charsets.UTF_8.name()));
            } catch (IOException ingnored) {
            }
        }
    }

    @Override
    public void setProductDesc(String productDesc) {
        if (!Strings.isNullOrEmpty(productDesc)) {
            try {
                super.setProductDesc(URLEncoder.encode(productDesc, Charsets.UTF_8.name()));
            } catch (IOException ingnored) {
            }
        }
    }

    @Override
    public void setExt2(String ext2) {
        if (!Strings.isNullOrEmpty(ext2)) {
            try {
                super.setExt2(URLEncoder.encode(ext2, Charsets.UTF_8.name()));
            } catch (IOException ingnored) {
            }
        }
    }

    @Override
    public String getSTR() {
        final StringBuilder sb = new StringBuilder();
        appendQueryString(sb, "inputCharset", Strings.emptyToNull(getInputCharset()));
        appendQueryString(sb, "bgUrl", Strings.emptyToNull(getBgUrl()));
        appendQueryString(sb, "pageUrl", Strings.emptyToNull(getPageUrl()));
        appendQueryString(sb, "version", Strings.emptyToNull(getVersion()));
        appendQueryString(sb, "language", Strings.emptyToNull(getLanguage()));
        appendQueryString(sb, "signType", Strings.emptyToNull(getSignType()));
        appendQueryString(sb, "merchantAcctId", Strings.emptyToNull(getMerchantAcctId()));
        appendQueryString(sb, "payerName", Strings.emptyToNull(getPayerName()));
        appendQueryString(sb, "payerContactType", Strings.emptyToNull(getPayerContactType()));
        appendQueryString(sb, "payerContact", Strings.emptyToNull(getPayerContact()));
        appendQueryString(sb, "orderId", Strings.emptyToNull(getOrderId()));
        appendQueryString(sb, "orderAmount", String.valueOf(getAmount()));
        appendQueryString(sb, "payType", Strings.emptyToNull(getPayType()));
        appendQueryString(sb, "cardNumber", Strings.emptyToNull(getCardNumber()));
        appendQueryString(sb, "cardPwd", Strings.emptyToNull(getCardPwd()));
        appendQueryString(sb, "fullAmountFlag", Strings.emptyToNull(getFullAmountFlag()));
        appendQueryString(sb, "orderTime", Strings.emptyToNull(getOrderTime()));
        appendQueryString(sb, "productName", Strings.emptyToNull(getProductName()));
        appendQueryString(sb, "productNum", String.valueOf(getProductNum()));
        appendQueryString(sb, "productId", getProductId());
        appendQueryString(sb, "productDesc", Strings.emptyToNull(getProductDesc()));
        appendQueryString(sb, "ext1", Strings.emptyToNull(getExt1()));
        appendQueryString(sb, "ext2", Strings.emptyToNull(getExt2()));
        appendQueryString(sb, "bossType", Strings.emptyToNull(getBossType()));
        appendQueryString(sb, "key", key);
        return sb.toString();
    }

    @Override
    public void updateSign() {
        setSign(MD5Utils.digestAsHex(getSTR().getBytes(Charsets.UTF_8)).toUpperCase());
    }

    /*@Override
    public Map<String, Object> toMap() {
        final Map<String, Object> map = super.toMap();
        map.put("cardNumber", getCardNumber());
        map.put("cardPwd", getCardPwd());
        map.put("fullAmountFlag", getFullAmountFlag());
        map.put("bossType", getBossType());
        return map;
    }*/

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> map = Maps.newHashMap();
        map.put("inputCharset", getInputCharset());
        map.put("pageUrl", getPageUrl());
        map.put("bgUrl", getBgUrl());
        map.put("version", getVersion());
        map.put("language", getLanguage());
        map.put("signType", getSignType());
        map.put("merchantAcctId", getMerchantAcctId());
        map.put("payerName", Strings.emptyToNull(getPayerName()));
        map.put("payerContactType", Strings.emptyToNull(getPayerContactType()));
        map.put("payerContact", Strings.emptyToNull(getPayerContact()));
        map.put("orderId", getOrderId());
        map.put("orderAmount", getAmount());
        map.put("payType", getPayType());
        map.put("cardNumber", getCardNumber());
        map.put("cardPwd", getCardPwd());
        map.put("fullAmountFlag", getFullAmountFlag());
        map.put("orderTime", getOrderTime());
        map.put("productName", getProductName());
        map.put("productNum", getProductNum());
        map.put("productId", getProductId());
        map.put("productDesc", getProductDesc());
        map.put("ext1", getExt1());
        map.put("ext2", getExt2());
        map.put("bossType", getBossType());
        map.put("signMsg", getSign());
        return map;
    }
}
