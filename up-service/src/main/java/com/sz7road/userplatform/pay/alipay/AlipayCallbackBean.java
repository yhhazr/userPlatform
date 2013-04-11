/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay.alipay;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.SimplePostBean;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jeremy
 */
@RequestScoped
class AlipayCallbackBean extends SimplePostBean {

    /**
     * Constructs by google-guice.
     */
    @Inject
    private AlipayCallbackBean(final HttpServletRequest request) {
        super();

        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(7);
        if (servletPath.length() > 2) {
            if (servletPath.substring(0, servletPath.length() - 2).toUpperCase().equals("RETURN")) {
                notify = false;
            }
        }
    }

    boolean notify = true;
    private Map<String, Object> parameters = Maps.newHashMap();
    private String key;
    private String sign;
    private String endOrder;
    private String order;
    private float amount;
    private String subject;
    private String body;
    private String buyerEmail;
    private String tradeStatus;
    private String orderSign;

    @Inject
    private final void parseValues(final HttpServletRequest request) throws IOException {
        key = ConfigurationUtils.get("alipay.keyValue");
        //获取支付宝GET过来反馈信息
        Map requestParams = request.getParameterMap();
        for (final Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            if (!notify)
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            parameters.put(name, valueStr);
        }

        endOrder = request.getParameter("trade_no"); // 支付宝交易号
        order = request.getParameter("out_trade_no"); // 交易订单号
         System.out.println("支付宝返回来的订单号:　"+order);
        if (null != request.getParameter("total_fee"))
            amount = Float.valueOf(request.getParameter("total_fee"));
        subject = new String(Strings.nullToEmpty(request.getParameter("subject")).getBytes(Charsets.ISO_8859_1), Charsets.UTF_8);
        body = new String(Strings.nullToEmpty(request.getParameter("body")).getBytes(Charsets.ISO_8859_1), Charsets.UTF_8);
        buyerEmail = request.getParameter("buyer_email");
        tradeStatus = request.getParameter("trade_status");
        if (null != request.getParameter("extra_common_param"))
            orderSign = Strings.nullToEmpty(request.getParameter("extra_common_param"));
    }

    public Map<String, Object> toMap() {
        return parameters;
    }

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

    public void updateSign() {
        setSign(MD5Utils.digestAsHex(getSTR() + getKey()));
    }

    public String getSign() {
        return sign;
    }

    protected void setSign(String sign) {
        this.sign = sign;
    }

    protected String getKey() {
        return key;
    }

    public String getEndOrder() {
        return endOrder;
    }

    public String getOrder() {
        return order;
    }

    public float getAmount() {
        return amount;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public String getOrderSign() {
        return orderSign;
    }

    public boolean isNotify() {
        return notify;
    }
}
