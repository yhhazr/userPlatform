package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProductOrder implements Serializable
{
    public static final int PAY_SUCCESS = 1;//支付成功状态
    public static final int PAY_FAIL = 0;//未支付状态
    private CharSequence id;
    private CharSequence channelId;
    private int subType;
    private String subTag;
    private float amount;
    private int gold;
    private int rechargeAmount;
    private byte status;
    private byte currency;
    private int userId;
    private int productId;
    private String productName;
    private int productAmount;
    private Timestamp payTime;
    private Timestamp assertTime;
    private String endOrder;

    private String clientIp;
    private String ext1;
    private String ext2;
    private String ext3;
    
    public ProductOrder() {
        
    }

    public CharSequence getId()
    {
        return id;
    }

    public void setId(CharSequence id)
    {
        this.id = id;
    }

    public CharSequence getChannelId()
    {
        return channelId;
    }

    public void setChannelId(CharSequence channelId)
    {
        this.channelId = channelId;
    }

    public int getSubType()
    {
        return subType;
    }

    public void setSubType(int subType)
    {
        this.subType = subType;
    }

    public String getSubTag()
    {
        return subTag;
    }

    public void setSubTag(String subTag)
    {
        this.subTag = subTag;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public int getGold()
    {
        return gold;
    }

    public void setGold(int gold)
    {
        this.gold = gold;
    }

    public int getRechargeAmount()
    {
        return rechargeAmount;
    }

    public void setRechargeAmount(int rechargeAmount)
    {
        this.rechargeAmount = rechargeAmount;
    }

    public byte getStatus()
    {
        return status;
    }

    public void setStatus(byte status)
    {
        this.status = status;
    }

    public byte getCurrency()
    {
        return currency;
    }

    public void setCurrency(byte currency)
    {
        this.currency = currency;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public int getProductAmount()
    {
        return productAmount;
    }

    public void setProductAmount(int productAmount)
    {
        this.productAmount = productAmount;
    }

    public Timestamp getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Timestamp payTime)
    {
        this.payTime = payTime;
    }

    public Timestamp getAssertTime()
    {
        return assertTime;
    }

    public void setAssertTime(Timestamp assertTime)
    {
        this.assertTime = assertTime;
    }

    public String getEndOrder()
    {
        return endOrder;
    }

    public void setEndOrder(String endOrder)
    {
        this.endOrder = endOrder;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }

    public String getExt1()
    {
        return ext1;
    }

    public void setExt1(String ext1)
    {
        this.ext1 = ext1;
    }

    public String getExt2()
    {
        return ext2;
    }

    public void setExt2(String ext2)
    {
        this.ext2 = ext2;
    }

    public String getExt3()
    {
        return ext3;
    }

    public void setExt3(String ext3)
    {
        this.ext3 = ext3;
    }
}
