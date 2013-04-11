package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.ws.sign.Sign;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-17
 * Time: 下午6:00
 * dt_sign表的dao接口
 */
public interface SignDao {
    /**
     * 找到并更新签到历史然后返回数据
     * 首先，根据uid和gid查询签到记录，如果不存在，插入数据（uid,gid,signCount,integration,signHistory,lastModifyTime,ext）
     * value(uid,gid,1,1,35天没有签到的标志,new Date(),'首次登陆签到时间：')；
     * 如果存在，查看最后更新时间跟当前时间对比，有n天，后面的n天设置为没有签到，同时去掉最前面的n天。
     * 最后返回签到的信息实体（）
     * @param uid
     * @param gid
     * @return
     */
     Sign querySign(int uid,int gid);

    /**
     * 签到然后返回签到信息实体
     * 首先，根据uid和gid查询得到签到记录，查看最后修改时间，如果是在今天之内，返回不能签到的提示，并把所需的签到信息实体返回；
     * 如果最后修改时间在昨天，说明是连续签到，连续签到天数+1，积分根据积分规则增加，同时，更新签到历史和最后修改时间；
     * 如果最后修改时间在昨天以前，说明不是连续签到，连续签到set为1，积分根据不连续签到的规则增加积分，同时更新签到历史和最后修改时间；
     * 最后返回签到信息实体
     * @param uid
     * @param gid
     * @return
     */
    Sign  signThenReturn(int uid,int gid);

    /**
     * 领取礼包
     * 首先根据udi和gid查询到签到记录，得到总积分；
     * 如果总积分大于等于礼包积分，说明可以兑换礼包，总积分减去礼包积分，然后返回最新的签到信息实体；
     * 如果总积分小于礼包积分，说明不能兑换，总积分不变，然后返回签到实体。
     * @param uid
     * @param gid
     * @param giftPackScore
     * @return
     */
    Sign   getGiftPackThenReturn(int uid,int gid,int giftPackScore);

    /**
     * 修改时间，方便测试
     * @param uid
     * @param gid
     * @param days 修改为当前时间的前几天
     */
    int modifySignTime(int uid, int gid, int days);
}
