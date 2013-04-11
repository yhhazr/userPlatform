package com.sz7road.userplatform.ppay.tenpay;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.SimplePostBean;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-13
 * Time: 上午6:59
 * Description: 财付通充值对象
 */
public class TenpayPostBean extends SimplePostBean implements Serializable {

    private Map<String, Object> parameters = new HashMap<String, Object>();


    public TenpayPostBean() {
        //必须的资料从数据库取得
        gateWayUrl = ConfigurationUtils.get("tenpay.gateway");
        key = ConfigurationUtils.get("tenpay.keyValue");
        notify_url = ConfigurationUtils.get("tenpay.notifyUrl");
        return_url = ConfigurationUtils.get("tenpay.returnUrl");
        partner = ConfigurationUtils.get("tenpay.merchantAcctId");
        input_charset = ConfigurationUtils.get("tenpay.inputCharset");
    }

    @Override
    public Map<String, Object> toMap() {
        parameters.put("bank_type", getBank_type());
        parameters.put("body", getBody());
        parameters.put("fee_type", getFee_type());
        parameters.put("key", getKey());
        parameters.put("input_charset", getInput_charset());
        parameters.put("sign_key_index", getSign_key_index());
        parameters.put("service_version", getService_version());
        parameters.put("notify_url", getNotify_url());
        parameters.put("out_trade_no", getOut_trade_no());
        parameters.put("partner", getPartner());
        parameters.put("return_url", getReturn_url());
        parameters.put("time_start", getTime_start());
        parameters.put("total_fee", getTotal_fee());
        parameters.put("spbill_create_ip", getSpbill_create_ip());
        parameters.put("sign_type", getSign_type());
        return parameters;
    }

    @Override
    public String getSTR() {//组装加密的字符串
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
        setSign((MD5Utils.digestAsHex(getSTR())).toUpperCase());


    }

    public void createSign() {//加密获得签名
        StringBuffer sb = new StringBuffer(getSTR());
        sb.append("&key=" + getKey());
        String sign = MD5Utils.MD5Encode(sb.toString(), getInput_charset()).toLowerCase();
        setParameter("sign", sign);
    }


    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 请求网关url
     */
    private String gateWayUrl;

    /**
     * 商户密钥，签名需要
     */
    private String key;


    /**
     * 请求方法为get或者post
     */
    private String requestMethod = "get";
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
     * 非必填
     */
    private String input_charset = "GBK";

    /**
     * 多密钥支持的密钥序号，默认1
     * 非必填
     */
    private int sign_key_index = 1;
    /**
     * 签名
     * 必填
     */
    private String sign;

    /**
     * 银行类型，默认为“DEFAULT”－财付通支付中心。银行直连编码及额度请与技术支持联系
     * 非必须
     */
    private String bank_type;
    /**
     * 商品描述
     * 必填
     */
    private String body;
    /**
     * 附加数据，原样返回
     * 非必填
     */
    private String attach;
    /**
     * 交易完成后跳转的URL，需给绝对路径，255字符内，格式如:http://wap.tenpay.com/ tenpay.asp，
     * 通过该路径直接将支付结果以Get的方式返回
     * 必填
     */
    private String return_url;
    /**
     * 接收财付通通知的URL，需给绝对路径，255字符内，格式如:http://wap.tenpay.com/ tenpay.asp
     * 必填
     */
    private String notify_url;
    /**
     * 买方的财付通账户(QQ 或EMAIL)。若商户没有传该参数，则在财付通支付页面，买家需要输入其财付通账户。
     * 非必填
     */
    private String buyer_id;
    /**
     * 商户号,由财付通统一分配的10位正整数(120XXXXXXX)号
     * 必填
     */
    private String partner;
    /**
     * 商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
     * 必填
     */
    private String out_trade_no;
    /**
     * 订单总金额，单位为分
     * 必填
     */
    private int total_fee;
    /**
     * 现金支付币种,取值：1（人民币）,默认值是1，暂只支持1
     * 必填
     */
    private int fee_type = 1;
    /**
     * 订单生成的机器IP，指用户浏览器端IP，不是商户服务器IP
     * 必填
     */
    private String spbill_create_ip;
    /**
     * 订单生成时间，格式为yyyyMMddhhmmss，
     * 如2009年12月25日9点10分10秒表示为20091225091010。
     * 时区为GMT+8 beijing。该时间取自商户服务器
     * 非必填
     */
    private String time_start = CommonDateUtils.date2String(new Date(), "yyyyMMddhhmmss");
    /**
     * 订单失效时间，格式为yyyyMMddhhmmss，如2009年12月27日
     * 9点10分10秒表示为20091227091010。时区为GMT+8 beijing。
     * 该时间取自商户服务器
     * 非必填
     */
    private String time_expire;

    /**
     * 物流费用，单位为分。如果有值，必须保证transport_fee + product_fee=total_fee
     * 非必填
     */

    private int transport_fee;
    /**
     * 商品费用，单位为分。如果有值，必须保证transport_fee + product_fee=total_fee
     * 非必填
     */
    private int product_fee;
    /**
     * 商品标记，优惠券时可能用到
     * 非必填
     */
    private String goods_tag;

    public String getGateWayUrl() {
        return gateWayUrl;
    }

    public void setGateWayUrl(String gateWayUrl) {
        this.gateWayUrl = gateWayUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
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

    public int getSign_key_index() {
        return sign_key_index;
    }

    public void setSign_key_index(int sign_key_index) {
        this.sign_key_index = sign_key_index;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
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

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
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

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
