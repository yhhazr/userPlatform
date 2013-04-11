package com.sz7road.userplatform.ppay.tenpay;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.SimplePostBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-19
 * Time: 上午7:21
 * Description:财付通的后台通知对象
 */
@RequestScoped
public class TenpayCallbackBean extends SimplePostBean {

    private static final Logger log = LoggerFactory.getLogger(TenpayCallbackBean.class.getName());
    /**
     * 密钥
     */
    private String key;
    /**
     * 重新生成的签名
     */
    private String mySign;

    public String getMySign() {
        return mySign;
    }

    public void setMySign(String mySign) {
        this.mySign = mySign;
    }

    private Map<String, Object> parameters = new HashMap<String, Object>();

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private boolean notify = true;

    /**
     * 签名类型，取值：MD5、RSA，默认：MD5
     * 非必填
     */
    private String sign_type = "MD5";
    /**
     * 版本号，默认为1.0
     * 非必填
     */
    private String service_version = "1.0";
    /**
     * 字符编码,取值：GBK、UTF-8，默认：GBK。
     * 非必须
     */
    private String input_charset = "GBK";
    /**
     * 签名
     * 必填
     */
    private String sign;
    /**
     * 多密钥支持的密钥序号，默认1
     * 非必填
     */
    private int sign_key_index = 1;
    /**
     * 1-即时到账其他保留
     * 必填
     */
    private int trade_mode;
    /**
     * 支付结果：0—成功 1—失败
     * 必填
     */
    private int trade_state;
    /**
     * 支付结果信息，支付成功时为空
     * 非必填
     */
    private String pay_info;
    /**
     * 商户号,由财付通统一分配的10位正整数(120XXXXXXX)号
     * 必填
     */
    private String partner;
    /**
     * 银行类型
     * 必填
     */
    private String bank_type;
    /**
     * 银行订单号，若为财付通余额支付则为空
     * 非必填
     */
    private String bank_billno;
    /**
     * 支付金额，单位为分，如果discount有值，通知的total_fee + discount = 请求的total_fee
     * 必填
     */
    private int total_fee;
    /**
     * 现金支付币种,目前只支持人民币,默认值是1-人民币
     * 必填
     */
    private int fee_type = 1;
    /**
     * 支付结果通知id，对于某些特定商户，只返回通知id，要求商户据此查询交易结果
     * 必填
     */
    private String notify_id;
    /**
     * 财付通交易号，28位长的数值，其中前10位为商户号，之后8位为订单产生的日期，如20090415，最后10位是流水号。
     * 必填
     */
    private String transaction_id;
    /**
     * 商户系统的订单号，与请求一致。
     * 必填
     */
    private String out_trade_no;
    /**
     * 商家数据包，原样返回
     */
    private String attach;
    /**
     * 支付完成时间，格式为yyyyMMddhhmmss，如2009年12月27日9点10分10秒表示为20091227091010。时区为GMT+8 beijing。该时间取自财付通服务器
     * 必填
     */
    private String time_end;

    /**
     * 物流费用，单位分，默认0。如果有值，必须保证transport_fee +  product_fee = total_fee
     * 非必填
     */
    private int transport_fee;
    /**
     * 物品费用，单位分。如果有值，必须保证transport_fee + product_fee=total_fe
     * 非必填
     */
    private int product_fee;
    /**
     * 折扣价格，单位分，如果有值，通知的total_fee + discount = 请求的total_fee
     * 非必填
     */
    private int discount;

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getService_version() {
        return service_version;
    }

    public void setService_version(String service_version) {
        this.service_version = service_version;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getSign_key_index() {
        return sign_key_index;
    }

    public void setSign_key_index(int sign_key_index) {
        this.sign_key_index = sign_key_index;
    }

    public int getTrade_mode() {
        return trade_mode;
    }

    public void setTrade_mode(int trade_mode) {
        this.trade_mode = trade_mode;
    }

    public int getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(int trade_state) {
        this.trade_state = trade_state;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getBank_billno() {
        return bank_billno;
    }

    public void setBank_billno(String bank_billno) {
        this.bank_billno = bank_billno;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getFee_type() {
        return fee_type;
    }

    public void setFee_type(int fee_type) {
        this.fee_type = fee_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public int getTransport_fee() {
        return transport_fee;
    }

    public void setTransport_fee(int transport_fee) {
        this.transport_fee = transport_fee;
    }

    public int getProduct_fee() {
        return product_fee;
    }

    public void setProduct_fee(int product_fee) {
        this.product_fee = product_fee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    /**
     * 对应买家账号的一个加密串
     * 非必填
     */

    private String buyer_alias;

    public String getBuyer_alias() {
        return buyer_alias;
    }

    public void setBuyer_alias(String buyer_alias) {
        this.buyer_alias = buyer_alias;
    }


    @Inject
    private TenpayCallbackBean(final HttpServletRequest request) {
        key = ConfigurationUtils.get("tenpay.keyValue");
        parameters.put("key", key);
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(7);
        if (servletPath.length() > 2) {
            if (servletPath.substring(0, servletPath.length() - 2).toUpperCase().equals("RETURN")) {
                notify = false;
            }
        }
    }

    @Inject
    private final void parseValues(final HttpServletRequest request) throws IOException {
        //获取财付通反馈信息的参数信息
        Map requestParams = request.getParameterMap();
        for (final Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            if (!notify) {
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            }
            parameters.put(name, valueStr);
            if ("input_charset".equals(name)) {
                setInput_charset(valueStr);
            }
        }
    }


    @Override
    public Map<String, Object> toMap() {
        parameters.put("key", getKey());
        return parameters;
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

        final List<String> keys = Lists.newArrayList(parameters.keySet());
        Collections.sort(keys);
        final StringBuilder sb = new StringBuilder();
        for (String keyStr : keys) {
            if (!Strings.isNullOrEmpty(keyStr) && !keyStr.equals("sign") && !keyStr.equals("key"))
                TenpayHandler.appendQueryStringForTenPay(sb, keyStr, parameters.get(keyStr));
        }
        return sb.toString();
    }

    @Override
    public void updateSign() {
        StringBuffer sb = new StringBuffer(getSTR());
        sb.append("&key=" + getKey());
        String sign = MD5Utils.MD5Encode(sb.toString(), getInput_charset()).toLowerCase();
        setMySign(sign);
    }

    public String updateAndGetSign() {
        StringBuffer sb = new StringBuffer(getSTR());
        sb.append("&key=" + getKey());
        return MD5Utils.MD5Encode(sb.toString(), "GBK").toUpperCase();
    }

    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        parameters.put(parameter, v);
    }
}
