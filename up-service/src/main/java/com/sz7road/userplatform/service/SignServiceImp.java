package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.dao.SignDao;
import com.sz7road.userplatform.ws.sign.Sign;
import com.sz7road.userplatform.ws.sign.SignServiceInterface;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-17
 * Time: 下午5:53
 * 签到服务接口实现类.
 */
public class SignServiceImp implements SignServiceInterface {

    @Inject
   private Provider<SignDao> signDaoProvider;

    @Override
    public Sign todaySign(int uid, int gid) {

        SignDao signDao=signDaoProvider.get();
        return signDao.signThenReturn(uid,gid);
    }

    @Override
    public Sign querySignInfo(int uid, int gid) {

        SignDao signDao=signDaoProvider.get();
        return signDao.querySign(uid,gid);
    }

    @Override
    public Sign getGiftPack(int uid, int gid, int giftPackScore) {
        SignDao signDao=signDaoProvider.get();
        return signDao.getGiftPackThenReturn(uid,gid,giftPackScore);
    }

    @Override
    public int modifySignTime(int uid, int gid, int days) {
        SignDao signDao=signDaoProvider.get();
       return   signDao.modifySignTime(uid,gid,days);
    }
}
