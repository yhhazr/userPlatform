package com.sz7road.userplatform.ppay.w99bill;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pay.w99bill.Pkipair;
import com.sz7road.web.SimplePostBean;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author jeremy
 */
@RequestScoped
class W99billCallbackBean extends SimplePostBean {

    private String merchantAcctId;
    private String version;
    private String language;
    private String signType;
    private String payType;
    private String bankId;
    private String orderId;
    private String orderTime;
    private long amount;
    private String dealId;
    private String bankDealId;
    private String dealTime;
    private long payAmount;
    private String fee;
    private String ext1;
    private String ext2;
    private int payResult;
    private String errCode;
    private String signMsg;

    @Inject
    W99billCallbackBean() {
        super();
    }

    @Inject
    void parseValues(final HeadlessServletRequest request) {
        merchantAcctId = request.getParameter("merchantAcctId");
        version = request.getParameter("version");
        language = request.getParameter("language");
        signType = request.getParameter("signType");
        payType = request.getParameter("payType");
        bankId = request.getParameter("bankId");
        orderId = request.getParameter("orderId");
        orderTime = request.getParameter("orderTime");
        amount = request.getLongParameter("orderAmount");
        dealId = request.getParameter("dealId");
        bankDealId = request.getParameter("bankDealId");
        dealTime = request.getParameter("dealTime");
        payAmount = request.getLongParameter("payAmount");
        fee = request.getParameter("fee");
        ext1 = request.getParameter("ext1");
        ext2 = request.getParameter("ext2");
        payResult = request.getIntParameter("payResult");
        errCode = request.getParameter("errCode");
        signMsg = request.getParameter("signMsg");
    }

    public String getOrderSign() {
        return getExt1();
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

    public String getPayType() {
        return payType;
    }

    public String getBankId() {
        return bankId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public long getAmount() {
        return amount;
    }

    public String getDealId() {
        return dealId;
    }

    public String getBankDealId() {
        return bankDealId;
    }

    public String getDealTime() {
        return dealTime;
    }

    public long getPayAmount() {
        return payAmount;
    }

    public String getFee() {
        return fee;
    }

    public String getExt1() {
        return ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public int getPayResult() {
        return payResult;
    }

    public String getErrCode() {
        return errCode;
    }

    /**
     * 校验是否为有效的回调请求。
     *
     * @return true or false
     */
    public boolean isValid() {
        return new Pkipair().enCodeByCer(getSTR(), signMsg);
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
        appendQueryString(sb, "signType", Strings.emptyToNull(getSignType()));
        appendQueryString(sb, "payType", Strings.emptyToNull(getPayType()));
        appendQueryString(sb, "bankId", Strings.emptyToNull(getBankId()));
        appendQueryString(sb, "orderId", Strings.emptyToNull(getOrderId()));
        appendQueryString(sb, "orderTime", Strings.emptyToNull(getOrderTime()));
        appendQueryString(sb, "orderAmount", getAmount());
        appendQueryString(sb, "dealId", Strings.emptyToNull(getDealId()));
        appendQueryString(sb, "bankDealId", Strings.emptyToNull(getBankDealId()));
        appendQueryString(sb, "dealTime", Strings.emptyToNull(getDealTime()));
        appendQueryString(sb, "payAmount", getPayAmount());
        appendQueryString(sb, "fee", Strings.emptyToNull(getFee()));
        appendQueryString(sb, "ext1", Strings.emptyToNull(getExt1()));
        appendQueryString(sb, "ext2", Strings.emptyToNull(getExt2()));
        appendQueryString(sb, "payResult", getPayResult());
        appendQueryString(sb, "errCode", Strings.emptyToNull(getErrCode()));
        return sb.toString();
    }

    @Override
    public void updateSign() {
        // nothing to do.
    }
}
