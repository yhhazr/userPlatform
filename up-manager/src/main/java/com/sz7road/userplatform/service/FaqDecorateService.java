package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.FaqDecorateDao;
import com.sz7road.userplatform.pojos.FaqObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.utils.LuceneUtil;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 下午4:47
 * 常见问题的服务类
 */
@Singleton
public class FaqDecorateService extends FaqService {

    @Inject
    private Provider<FaqDecorateDao> faqDaoProvider;


    private FaqDecorateDao getFaqDao() {
        FaqDecorateDao faqDecorateDao = faqDaoProvider.get();

        if (faqDecorateDao == null) {
            throw new NullPointerException("faqDao null");
        }

        return faqDecorateDao;
    }

    /**
     * 增加Faq
     *
     * @param faqObject
     * @return
     */
    public int addFaq(FaqObject faqObject) {
        int rel = 0;
        if (faqObject != null) {
            rel = getFaqDao().addFaq(faqObject);
        }
        return rel;
    }

    /**
     * 批量删除Faq
     *
     * @param ids
     * @return
     */
    public int batchDeleteFaq(int[] ids) {
        int rel = 0;
        if (ids != null && ids.length > 0) {
            rel = getFaqDao().batchDeleteFaq(ids);
        }
        return rel;
    }

    /**
     * 更新faq
     *
     * @param faqObject
     * @return
     */
    public int updateFaq(FaqObject faqObject) {
        int rel = 0;
        if (faqObject != null) {
            rel = getFaqDao().updateFaq(faqObject);
        }
        return rel;
    }

    /**
     * 更新点击次数
     *
     * @param ids
     * @return
     */
    public boolean updateVisitSum(int[] ids) {
        boolean rel = false;
        if (ids != null && ids.length > 0) {
            rel = getFaqDao().updateVisitSum(ids);
        }
        return rel;
    }

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
    public JqObject queryFaq(Map conditions, int start, int pageSize, String sortFiled, String sortOrder) {
        return getFaqDao().queryFaq(conditions, start, pageSize, sortFiled, sortOrder);
    }

    public JqObject queryFaqById(int id) {
        return getFaqDao().queryFaqById(id);
    }

    /**
     * 全文搜索Faq
     *
     * @param searchWord
     * @return
     */
    public List<FaqObject> fullTextQuery(String searchWord) {
        List<FaqObject> faqObjectList = null;
        FaqDecorateDao faqDecorateDao = getFaqDao();
        try {
            faqObjectList = faqDecorateDao.fullTextQuery(searchWord);
            if (faqObjectList != null) {
                LuceneUtil.buildIndex(faqObjectList);
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return faqObjectList;
    }


}
