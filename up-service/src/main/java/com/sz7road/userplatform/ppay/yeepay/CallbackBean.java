/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay.yeepay;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.utils.ConfigurationUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author jeremy
 */
@RequestScoped
class CallbackBean {

    private String keyValue;
    private String cmd;
    private String merchantAcctId;
    private String code;
    private String endOrder;
    private float amount = 0;
    private String currency;
    private String productName;
    private String order;
    private String uid;
    private String mp;
    private String btype;
    private String hmac;
    private Timestamp time = new Timestamp(new Date().getTime());
    private boolean isOk = false;
    private String encoding;

    @Inject
    private CallbackBean() {
        super();

        keyValue = ConfigurationUtils.get("yeepay.keyValue");
        encoding = ConfigurationUtils.get("yeepay.encoding");
        if (Strings.isNullOrEmpty(encoding))
            encoding = "UTF-8";
    }

    /**
     * 解析请求参数。
     *
     * @param request HttpServlet请求参数
     */
    @Inject
    final void parseValues(final HttpServletRequest request) {
        cmd = request.getParameter("r0_Cmd");
        merchantAcctId = request.getParameter("p1_MerId");
        code = request.getParameter("r1_Code");
        endOrder = request.getParameter("r2_TrxId");
        amount = Float.parseFloat(Strings.nullToEmpty(request.getParameter("r3_Amt")));
        currency = request.getParameter("r4_Cur");
        productName = new String(Strings.nullToEmpty(request.getParameter("r5_Pid")).getBytes(Charsets.ISO_8859_1), Charset.forName(encoding));
        order = request.getParameter("r6_Order");
        uid = request.getParameter("r7_Uid");
        mp = new String(Strings.nullToEmpty(request.getParameter("r8_MP")).getBytes(Charsets.ISO_8859_1), Charset.forName(encoding));
        btype = request.getParameter("r9_BType");
        hmac = request.getParameter("hmac");

        validateValues();
    }

    /**
     * 验证当前这个回调Bean的有效性。
     */
    final void validateValues() {
        final String _hmac = MD5HmacDigestUtil.digestAsHex(getSTR(), keyValue);
        isOk = _hmac.equals(hmac);
    }

    /**
     * 告诉其他组件当前这个回调Bean是否为有效状态。
     *
     * @return true如果是有效的，false其他
     */
    public boolean isValid() {
        return isOk;
    }

    public String getSTR() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getMerchantAcctId());
        sb.append(getCmd());
        sb.append(getCode());
        sb.append(getEndOrder());
        sb.append(getAmount());
        sb.append(getCurrency());
        sb.append(getProductName());
        sb.append(getOrder());
        sb.append(getUid());
        sb.append(getMp());
        sb.append(getBtype());
        return sb.toString();
    }

    public Timestamp getTime() {
        return time;
    }

    public float getAmount() {
        return amount;
    }

    public String getBtype() {
        return btype;
    }

    public String getCmd() {
        return cmd;
    }

    public String getCode() {
        return code;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEndOrder() {
        return endOrder;
    }

    public String getMerchantAcctId() {
        return merchantAcctId;
    }

    public String getMp() {
        return mp;
    }

    public String getOrder() {
        return order;
    }

    public String getProductName() {
        return productName;
    }

    public String getUid() {
        return uid;
    }
}
