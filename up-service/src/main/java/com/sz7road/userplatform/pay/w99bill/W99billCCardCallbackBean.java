package com.sz7road.userplatform.pay.w99bill;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.SimplePostBean;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@RequestScoped
class W99billCCardCallbackBean extends SimplePostBean {

    private static final Map<String, String> merchantAcctIdMapping = Maps.newHashMap();

    boolean notify = true;

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

    private String merchantAcctId;
    private String version;
    private String language;
    private String payType;
    private String cardNumber;
    private String cardPwd;
    private String orderId;
    private long orderAmount;
    private String dealId;
    private String orderTime;
    private String ext1;
    private String ext2;
    private long payAmount;
    private String billOrderTime;
    private int payResult;
    private String signType;
    private String bossType;
    private String receiveBossType;
    private String receiverAcctId;
    private String signMsg;

    @Inject
    W99billCCardCallbackBean(final HttpServletRequest request) {
        super();

        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(7);
        if (servletPath.length() > 2) {
            if (servletPath.substring(0, servletPath.length() - 2).toUpperCase().equals("RETURN")) {
                notify = false;
            }
        }
    }

    @Inject
    void parseValues(final HeadlessServletRequest request) {
        merchantAcctId = request.getParameter("merchantAcctId");
        version = request.getParameter("version");
        language = request.getParameter("language");
        payType = request.getParameter("payType");
        cardNumber = request.getParameter("cardNumber");
        cardPwd = request.getParameter("cardPwd");
        orderId = request.getParameter("orderId");
        orderAmount = request.getLongParameter("orderAmount");
        dealId = request.getParameter("dealId");
        orderTime = request.getParameter("orderTime");
        ext1 = request.getParameter("ext1");
        ext2 = request.getParameter("ext2");
        payAmount = request.getLongParameter("payAmount");
        billOrderTime = request.getParameter("billOrderTime");
        payResult = request.getIntParameter("payResult");
        signType = request.getParameter("signType");
        bossType = request.getParameter("bossType");
        receiveBossType = request.getParameter("receiveBossType");
        receiverAcctId = request.getParameter("receiverAcctId");
        signMsg = request.getParameter("signMsg");
    }

    public String getOrderSign() {
        return getExt1();
    }

    public String getKey() {
        String key = null;
        for(Map.Entry<String,String> entry : merchantAcctIdMapping.entrySet()){
            String tMerchantAcctId = ConfigurationUtils.get(entry.getValue() + ".merchantAcctId");
            if (!Strings.isNullOrEmpty(merchantAcctId) && merchantAcctId.equals(tMerchantAcctId)) {
                key = ConfigurationUtils.get(entry.getValue() + ".keyValue");
                break;
            }
        }
        return key;
    }

    public String getMerchantAcctId() {
        return merchantAcctId;
    }

    public String getVersion() {
        return version;
    }

    public String getLanguage() {
        return language;
    }

    public String getPayType() {
        return payType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderAmount() {
        return orderAmount;
    }

    public String getDealId() {
        return dealId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        if(Strings.isNullOrEmpty(ext2)) {
            try {
                ext2 = new String(ext2.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {}
        }
        return ext2;
    }

    public long getPayAmount() {
        return payAmount;
    }

    public String getBillOrderTime() {
        return billOrderTime;
    }

    public int getPayResult() {
        return payResult;
    }

    public String getSignType() {
        return signType;
    }

    public String getBossType() {
        return bossType;
    }

    public String getReceiveBossType() {
        return receiveBossType;
    }

    public String getReceiverAcctId() {
        return receiverAcctId;
    }

    public String getSignMsg() {
        return signMsg;
    }

    /**
     * 校验是否为有效的回调请求。
     *
     * @return true or false
     */
    public boolean isValid() {
        return signMsg == null ? false:MD5Utils.digestAsHex(getSTR()).toUpperCase().equals(signMsg);
    }

    @Override
    public Map<String, Object> toMap() {
        // nothing to do.
        return null;
    }

    @Override
    public String getSTR() {
        final StringBuilder sb = new StringBuilder();
        appendQueryString(sb, "merchantAcctId", merchantAcctId);
        appendQueryString(sb, "version", Strings.emptyToNull(getVersion()));
        appendQueryString(sb, "language", Strings.emptyToNull(getLanguage()));
        appendQueryString(sb, "payType", Strings.emptyToNull(getPayType()));
        appendQueryString(sb, "cardNumber", Strings.emptyToNull(getCardNumber()));
        appendQueryString(sb, "cardPwd", Strings.emptyToNull(getCardPwd()));
        appendQueryString(sb, "orderId", Strings.emptyToNull(getOrderId()));
        appendQueryString(sb, "orderAmount", getOrderAmount());
        appendQueryString(sb, "dealId", Strings.emptyToNull(getDealId()));
        appendQueryString(sb, "orderTime", Strings.emptyToNull(getOrderTime()));
        appendQueryString(sb, "ext1", Strings.emptyToNull(getExt1()));
        appendQueryString(sb, "ext2", Strings.emptyToNull(getExt2()));
        appendQueryString(sb, "payAmount", getPayAmount());
        appendQueryString(sb, "billOrderTime", Strings.emptyToNull(getBillOrderTime()));
        appendQueryString(sb, "payResult", getPayResult());
        appendQueryString(sb, "signType", Strings.emptyToNull(getSignType()));
        appendQueryString(sb, "bossType", Strings.emptyToNull(getBossType()));
        appendQueryString(sb, "receiveBossType", Strings.emptyToNull(getReceiveBossType()));
        appendQueryString(sb, "receiverAcctId", Strings.emptyToNull(getReceiverAcctId()));
        //appendQueryString(sb, "signMsg", Strings.emptyToNull(getSignMsg()));
        appendQueryString(sb, "key", Strings.emptyToNull(getKey()));
        return sb.toString();
    }

    @Override
    public void updateSign() {
        // nothing to do.
    }

    public boolean isNotify() {
        return notify;
    }
}
