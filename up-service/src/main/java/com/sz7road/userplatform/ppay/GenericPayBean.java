/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.ppay;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pojos.PayTable;
import com.sz7road.userplatform.service.ProductPayService;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 充值数据对象。抽象参数用于提交至充值网关代理接口。</p>
 * 抽象游戏ID、游戏大区、用户账号、游戏角色、充值金额等。
 *
 * @author leo.liao
 */
@RequestScoped
public class GenericPayBean extends GenericPayLocatorBean implements PayBean {

    /**
     * 产品描述
     */
    private String productDescription;

    /**
     * 用户账号ID
     */
    private int userId;
    /**
     * 用户账号名称
     */
    private String userName;
    /**
     * 商品ID
     */
    private int productId;
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品数量
     */
    private int productAmount;
    /**
     * 充值金额，默认为1000,即最少不可低于10元
     */
    private float payAmount = 0.01f;

    /**
     * 订单ID，根据充值渠道及年、月、日、时、分、秒及配置+生成ID组成
     */
    private String orderId;

    /**
     * 创建时间
     */
    private Timestamp time = new Timestamp(new Date().getTime());

    /**
     * 提交的客户ＩＰ
     */
    private String clientIp;

    private ProductPayService payService;
    
    private String sign;

    /**
     * Constructs with google-guice.
     *
     * @param manager the manager facede for pay
     */
    @Inject
    protected GenericPayBean(PayManager manager, ProductPayService payService) {
        super(manager);
        this.payService = payService;
    }

    /**
     * 参数注入，验证等。
     *
     * @param request HTTP请求对象
     */
    @Inject
    protected void validate(final HttpServletRequest request) {
        String _n = request.getParameter("_n"); // 充值订单号
        String _c = request.getParameter("_c"); // 充值渠道ID
        String _s = request.getParameter("_s"); // 充值渠道类型ID
        String _f = request.getParameter("_f"); // 充值渠道子标签
        String userId = request.getParameter("u"); // 用户账号ID
        String user = request.getParameter("user"); // 用户账号名称
        String productId = request.getParameter("_pid");
        String productName = request.getParameter("_pn");
        String productAmount = request.getParameter("_pa");
        String amount = request.getParameter("amount"); // 充值金额
        clientIp = CommonDateUtils.getRemoteIPAddress(request);
        productDescription = ConfigurationUtils.get("pay.common.description");
        
        sign = request.getParameter("sign");
        // 类型校验

        if (!Strings.isNullOrEmpty(_n)) {
            setOrderId(_n.trim());
        }
        if (!Strings.isNullOrEmpty(_c)) {
            setChannelId(_c.toUpperCase().charAt(0));
        }
        if (!Strings.isNullOrEmpty(_s)) {
            setSubTypeId(Integer.parseInt(_s.trim()));
        }
        if (!Strings.isNullOrEmpty(_f)) {
            setSubTag(_f);
        }
        if (!Strings.isNullOrEmpty(userId)) {
            setUserId(Integer.parseInt(userId.trim()));
        }
        if (!Strings.isNullOrEmpty(user)) {
            setUserName(user.trim());
        }
        if (!Strings.isNullOrEmpty(amount)) {
            setPayAmount(Float.parseFloat(amount.trim()));
        }
        if(!Strings.isNullOrEmpty(productId)) {
            setProductId(Integer.parseInt(productId));
        }
        if(!Strings.isNullOrEmpty(productName)) {
            setProductName(productName);
        }
        if(!Strings.isNullOrEmpty(productAmount)) {
            setProductAmount(Integer.parseInt(productAmount));
        }
    }

    @Override
    public String getOrderId() {
        if (Strings.isNullOrEmpty(orderId)) {
            orderId = getManager().nextOrder(getChannelId());
        }
        return orderId;
    }

    protected void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    protected void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public float getPayAmount() {
        return payAmount;
    }

    protected void setPayAmount(float payAmount) {
        this.payAmount = payAmount;
    }

    @Override
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * 验证当前的PayBean是否有效于提交充值请求。
     * 当且仅当当前PayBean中的各参数都全部为正确范围取值、所指向的充值渠道有效的情况下返回真。
     *
     * @return true 在验证正确的情况下，其余都返回false
     */
    @Override
    public boolean isAvailableForSubmit() {
        boolean flag;
        flag = isValidChannelId();
        flag = flag && !Strings.isNullOrEmpty(getOrderId());
        flag = flag && getOrderId().length() == 21;
        flag = flag && getOrderId().charAt(0) == getChannelId();
        flag = flag && !Strings.isNullOrEmpty(getOrderId());
        flag = flag && getSubTypeId() >= 0;
        flag = flag && getProductId() != 0;
        flag = flag && (!Strings.isNullOrEmpty(getProductName()));
        flag = flag && getProductAmount() > 0;
        flag = flag && (getUserId() > 0 || !Strings.isNullOrEmpty(getUserName()));
//        flag = flag && getPayAmount() >= getMinPayAmount();
        flag = flag && null != getHandler();

        final PayTable.PayEntryKey key = PayTable.getKey(String.valueOf(getChannelId()), getSubTypeId(), getSubTag());
        flag = flag && null != key;
        flag = flag && getPayService().isValidPayEntryKey(key);
        //参数签名验证
        flag = flag && validateParamSign();
        return flag;
    }
    
    //渠道+子渠道+银行标签+用户名+总额+商品ID+商品名称+商品数量
    private boolean validateParamSign() {
        if(!Strings.isNullOrEmpty(sign)) {
            StringBuilder paramSign = new StringBuilder();
            paramSign.append(getChannelTag()).append(getSubTag())
                .append(getUserName()).append((int)getPayAmount())
                .append(getProductId()).append(getProductName()).append(getProductAmount());
            String key = ConfigurationUtils.get("ppay.param.signKey");
            paramSign.append(key);
            String currSign = MD5Utils.digestAsHex(paramSign.toString());
            if(sign.equalsIgnoreCase(currSign)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 最小额度的支付金额。
     *
     * @return amount limit at minizie
     */
    protected int getMinPayAmount() {
//        final String minAmount = ConfigurationUtils.get("pay.min.amount");
        final String minAmount = ConfigurationUtils.get("pay.min.amount");
        try {
            if (!Strings.isNullOrEmpty(minAmount)) {
                return Integer.parseInt(minAmount);
            }
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public String getDescription() {
        return productDescription;
    }

    @Override
    public String getClientIp() {
        return clientIp;
    }

    protected ProductPayService getPayService() {
        return payService;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @Override
    public int getProductId()
    {
        return productId;
    }

    @Override
    public String getProductName()
    {
        return productName;
    }

    public void setProductAmount(int productAmount)
    {
        this.productAmount = productAmount;
    }

    @Override
    public int getProductAmount()
    {
        return productAmount;
    }
}
