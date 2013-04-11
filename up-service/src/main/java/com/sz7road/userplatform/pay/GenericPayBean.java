/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pay;

import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.pojos.PayTable;
import com.sz7road.userplatform.service.PayService;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 充值数据对象。抽象参数用于提交至充值网关代理接口。</p>
 * 抽象游戏ID、游戏大区、用户账号、游戏角色、充值金额等。
 *
 * @author jeremy
 */
@RequestScoped
public class GenericPayBean extends GenericPayLocatorBean implements PayBean {

    /**
     * 产品描述
     */
    private String productDescription;

    /**
     * 游戏ID
     */
    private int gameId;
    /**
     * 游戏区ID
     */
    private int gameZoneId;
    /**
     * 用户账号ID
     */
    private int userId;
    /**
     * 用户账号名称
     */
    private String userName;
    /**
     * 游戏角色ID
     */
    private int playerId;
    /**
     * 游戏角色名称
     */
    private String playerName;
    /**
     * 充值金额，默认为1000,即最少不可低于10元
     */
    private float payAmount = 10f;

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

    private PayService payService;

    /**
     * Constructs with google-guice.
     *
     * @param manager the manager facede for pay
     */
    @Inject
    protected GenericPayBean(PayManager manager, PayService payService) {
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
        String _g = request.getParameter("_g"); // 游戏ID
        String _z = request.getParameter("_z"); // 游戏大区ID
        String _f = request.getParameter("_f"); // 充值渠道子标签
        String userId = request.getParameter("u"); // 用户账号ID
        String user = request.getParameter("user"); // 用户账号名称
        String playerId = request.getParameter("p"); // 游戏角色ID
        String player = request.getParameter("player"); // 游戏角色名称
        String amount = request.getParameter("amount"); // 充值金额
        clientIp = CommonDateUtils.getRemoteIPAddress(request);
        productDescription = ConfigurationUtils.get("pay.common.description");

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
        if (!Strings.isNullOrEmpty(_g)) {
            setGameId(Integer.parseInt(_g.trim()));
        }
        if (!Strings.isNullOrEmpty(_z)) {
            setGameZoneId(Integer.parseInt(_z.trim()));
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
        if (!Strings.isNullOrEmpty(playerId)) {
            setPlayerId(Integer.parseInt(playerId.trim()));
        }
        if (!Strings.isNullOrEmpty(player)) {
            setPlayerName(player.trim());
        }
        if (!Strings.isNullOrEmpty(amount)) {
            setPayAmount(Float.parseFloat(amount.trim()));
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
    public int getGameId() {
        return gameId;
    }

    protected void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public int getGameZoneId() {
        return gameZoneId;
    }

    protected void setGameZoneId(int gameZoneId) {
        this.gameZoneId = gameZoneId;
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
    public int getPlayerId() {
        return playerId;
    }

    protected void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    protected void setPlayerName(String playerName) {
        this.playerName = playerName;
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
//        flag = flag && !Strings.isNullOrEmpty(getOrderId());
        flag = flag && getSubTypeId() >= 0;
        flag = flag && getGameId() != 0;
        flag = flag && getGameZoneId() != 0;
        flag = flag && (getUserId() > 0 || !Strings.isNullOrEmpty(getUserName()));
        flag = flag && (getPlayerId() > 0 || !Strings.isNullOrEmpty(getPlayerName()));
        flag = flag && getPayAmount() >= getMinPayAmount();
        flag = flag && null != getHandler();

        final PayTable.PayEntryKey key = PayTable.getKey(String.valueOf(getChannelId()), getSubTypeId(), getSubTag());
        flag = flag && null != key;
        flag = flag && getPayService().isValidPayEntryKey(key);

        return flag;
    }

    /**
     * 最小额度的支付金额。
     *
     * @return amount limit at minizie
     */
    protected int getMinPayAmount() {
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

    protected PayService getPayService() {
        return payService;
    }
}
