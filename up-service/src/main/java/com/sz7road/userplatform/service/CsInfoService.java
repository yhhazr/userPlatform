package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.InfoDao;
import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.SiteObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 下午2:27
 * 客服信息的服务类
 */
@Singleton
public class CsInfoService extends Injection {
    @Inject
    private Provider<InfoDao> infoDaoProvider;


    private InfoDao getInfoDao() {
        InfoDao infoDao = infoDaoProvider.get();

        if (infoDao == null) {
            throw new NullPointerException("InfoDao null");
        }

        return infoDao;
    }


    /**
     * 对外接口返回客服信息
     *
     * @return
     */
    public SiteObject getCsInfo() {
        SiteObject siteObject = getInfoDao().getCsInfo();

        return siteObject;
    }

    /**
     * 对外接口返回轮播图片信息
     *
     * @return
     */
    public List<InfoObject> getAdPhotos() {
        return getInfoDao().getAdPhotos();
    }

}
