package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sz7road.userplatform.dao.FaqKindDao;
import com.sz7road.userplatform.pojos.FaqKindObject;
import com.sz7road.userplatform.pojos.JqObject;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-2
 * Time: 下午8:16
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class FaqKindService {
    @Inject
    private Provider<FaqKindDao> faqKindDaoProvider;

    private FaqKindDao getFaqKindDao() {
        FaqKindDao faqKindDao = faqKindDaoProvider.get();

        if (faqKindDao == null) {
            throw new NullPointerException("faqKindDao null");
        }
        return faqKindDao;
    }


    public JqObject query(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder) {
        return getFaqKindDao().query(conditions, start, pageSize, sortField, sortOrder);
    }

    public JqObject queryTree(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder, int nodeId) {
        return getFaqKindDao().queryTree(conditions, start, pageSize, sortField, sortOrder, nodeId);
    }

    public FaqKindObject getFaqKindById(int id) {
        return getFaqKindDao().getFaqKindById(id);
    }


    public List<FaqKindObject> getChildFaqKindByParentId(int parentId) {
        return getFaqKindDao().getChildFaqKindByParentId(parentId);
    }


    public int deleteFaqKindById(int id) {
        return getFaqKindDao().deleteFaqKindById(id);
    }

    public int batchDeleteFaqKindById(int[] ids) {
        return getFaqKindDao().batchDeleteFaqKindByIds(ids);
    }

    public int update(FaqKindObject entity) throws SQLException {
        return getFaqKindDao().update(entity);
    }

    public int add(FaqKindObject entity) throws SQLException {
        return getFaqKindDao().add(entity);
    }

    public JqObject queryFaqKindById(int id) {
        return getFaqKindDao().queryFaqKindById(id);
    }

}
