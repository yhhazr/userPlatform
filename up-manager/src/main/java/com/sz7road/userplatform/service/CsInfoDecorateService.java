package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.InfoDecorateDao;
import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.JqObject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 下午2:27
 * 客服信息的服务类
 */
@Singleton
public class CsInfoDecorateService extends CsInfoService {
    @Inject
    private Provider<InfoDecorateDao> infoDaoProvider;


    private InfoDecorateDao getInfoDao() {
        InfoDecorateDao infoDao = infoDaoProvider.get();

        if (infoDao == null) {
            throw new NullPointerException("InfoDao null");
        }

        return infoDao;
    }

    /**
     * 更改客服信息
     *
     * @param info
     * @return
     */
    public int updateInfo(InfoObject info) {
        int rel = 0;
        if (info != null) {
            rel = getInfoDao().updateInfo(info);
        }
        return rel;
    }

    /**
     * 增加轮播图片或者QQ群
     *
     * @param info
     * @return
     */
    public int addQQGroupOrAdPhoto(InfoObject info) {
        int rel = 0;
        if (info != null) {
            rel = getInfoDao().addQQGroupOrAdPhoto(info);
        }
        return rel;
    }

    /**
     * 批量删除轮播图片或者QQ群，当多于4个的时候
     *
     * @param ids
     * @return
     */
    public int BatchDeleteQQGroupOrAdPhoto(int[] ids) {
        int rel = 0;
        if (ids != null && ids.length >= 1) {
            rel = getInfoDao().BatchDeleteQQGroupOrAdPhoto(ids);
        }
        return rel;
    }

    /**
     * 显示并查询客服信息
     *
     * @param conditions
     * @param start
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public JqObject queryCsInfo(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder) {
        return getInfoDao().queryCsInfo(conditions, start, pageSize, sortField, sortOrder);
    }

    public JqObject queryCsInfoById(int id) {
        return getInfoDao().queryCsInfoById(id);
    }



    /**
     * 对外接口返回轮播图片信息
     *
     * @return
     */
    public List<InfoObject> getAdPhotos() {
        return getInfoDao().getAdPhotos();
    }

    public InfoObject getCsInfo(int id) {
        return getInfoDao().getCsInfoById(id);
    }


}
