package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.FaqKindObject;
import com.sz7road.userplatform.pojos.JqObject;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-2
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */
public interface FaqKindDecorateDao extends FaqKindDao {
    /**
     * 根据条件查询faq问题分类信息
     *
     * @param conditions 搜索关键词
     * @param start      开始位置
     * @param pageSize   每页的记录数
     * @param sortField  排序的字段
     * @param sortOrder  排序的命令 asc  desc
     * @return
     */
    public JqObject query(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder);

    /**
     * 通过Id拿到种类
     *
     * @param id
     * @return
     */
    public FaqKindObject getFaqKindById(int id);

    /**
     * 通过Id拿到子种类
     *
     * @param parentId
     * @return
     */
    public List<FaqKindObject> getChildFaqKindByParentId(int parentId);

    /**
     * 删除指定ID的faqkind，如果有子节点，顺带删除子节点
     *
     * @param id
     * @return
     */
    public int deleteFaqKindById(int id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public int batchDeleteFaqKindByIds(int[] ids);


    public JqObject queryTree(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder, int nodeId);


    public JqObject queryFaqKindById(int id);


}
