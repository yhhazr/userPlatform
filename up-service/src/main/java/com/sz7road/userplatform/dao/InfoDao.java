package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.SiteObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 上午10:42
 * 操作网站信息的接口
 */
public interface InfoDao extends Dao<InfoObject> {
    /**
     * 对外接口返回客服信息
     *
     * @return
     */
    public SiteObject getCsInfo();

    /**
     * 对外接口返回轮播图片信息
     *
     * @return
     */
    public List<InfoObject> getAdPhotos();
}
