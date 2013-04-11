package com.sz7road.userplatform.ws.sign;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-17
 * Time: 下午5:37
 * 签到的服务接口
 */
public interface SignServiceInterface {
    /**
     * 当天签到之后返回签到信息
     * @param uid    用户id
     * @param gid    游戏id
     * @return       该用户的签到信息
     */
    Sign  todaySign(int uid,int gid);

    /**
     * 登陆的时候查询签到信息
     * @param uid  用户id
     * @param gid  游戏id
     * @return     该用户的签到信息
     */
    Sign  querySignInfo(int uid,int gid);

    /**
     * 领取礼包，减去积分
     * @param uid   用户id
     * @param gid   游戏id
     * @param giftPackScore  礼包积分
     * @return     领取礼包之后的签到信息
     */
    Sign  getGiftPack(int uid,int gid,int giftPackScore);

    int modifySignTime(int uid,int gid,int days);

}
