package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.FaqObject;
import com.sz7road.userplatform.pojos.JqObject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-14
 * Time: 下午6:22
 * To change this template use File | Settings | File Templates.
 */
public interface FaqDecorateDao extends FaqDao {
    /**
     * 增加Faq
     *
     * @param faqObject
     * @return
     */
    public int addFaq(FaqObject faqObject);

    /**
     * 批量删除Faq
     *
     * @param ids
     * @return
     */
    public int batchDeleteFaq(int[] ids);

    /**
     * 更新faq
     *
     * @param faqObject
     * @return
     */
    public int updateFaq(FaqObject faqObject);

    /**
     * 更新点击次数
     *
     * @param ids
     * @return
     */
    public boolean updateVisitSum(int[] ids);

    /**
     * 查询faq
     *
     * @param conditions
     * @param start
     * @param pageSize
     * @param sortFiled
     * @param sortOrder
     * @return
     */
    public JqObject queryFaq(Map conditions, int start, int pageSize, String sortFiled, String sortOrder);

    /**
     * 全文搜索Faq
     *
     * @param searchWord
     * @return
     */
    public List<FaqObject> fullTextQuery(String searchWord);

    /**
     * 按照id搜索faq
     *
     * @param id
     * @return
     */
    public JqObject queryFaqById(int id);
}
