package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.JqObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 上午10:42
 * 操作网站信息的接口
 */
public interface InfoDecorateDao extends InfoDao {
    /**
     * 更改客服信息
     *
     * @param info
     * @return
     */
    public int updateInfo(InfoObject info);

    /**
     * 增加轮播图片或者QQ群
     *
     * @param info
     * @return
     */
    public int addQQGroupOrAdPhoto(InfoObject info);

    /**
     * 批量删除轮播图片或者QQ群，当多于4个的时候
     *
     * @param ids
     * @return
     */
    public int BatchDeleteQQGroupOrAdPhoto(int[] ids);

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
    public JqObject queryCsInfo(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder);

    public InfoObject getCsInfoById(int id);

    public JqObject queryCsInfoById(int id);

}
