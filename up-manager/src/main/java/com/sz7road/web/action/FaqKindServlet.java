package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.FaqKindObject;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.FaqKindDecorateService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-2
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class FaqKindServlet extends BaseServlet {

    @Inject
    private Provider<FaqKindDecorateService> faqKindServiceProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 查询faq分类的信息，可以排序和查找
     */

    public void queryTreeFaq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");

        if (!Strings.isNullOrEmpty(type)) {
            if ("load".equals(type)) {
                int start = 0;
                int iPageSize = 0;
                String sortOrderFlag = null;
                //1,定义并且接收参数
                String filters = null;
                String pageIndex = null;
                String pageSize = null;
                String sortField = null;
                String sortOrder = null;
                String nodeId = null;
                try {
                    filters = request.getParameter("filters");
                    pageIndex = request.getParameter("page");
                    pageSize = request.getParameter("rows");
                    sortField = request.getParameter("sidx");
                    sortOrder = request.getParameter("sord");
                    nodeId = request.getParameter("nodeid");
                } catch (Exception ex) {
                    log.error("接收FaqKindView页面参数异常!");
                }
                //2,转换成需要的参数
                if (ServletUtil.isIntegerAndNotNull(pageSize)) {
                    iPageSize = Integer.parseInt(pageSize);
                }
                if (ServletUtil.isIntegerAndNotNull(pageIndex)) {
                    int index = Integer.parseInt(pageIndex);
                    start = (index - 1) * iPageSize;
                }
                if (sortOrder == null) {
                    sortOrder = "ASC";
                }
                Map<String, Object> conditions = null;
                if (filters != null && !Strings.isNullOrEmpty(filters)) {
                    conditions = objectMapper.readValue(filters, Map.class);
                }
                int parentId = 0;
                if (!Strings.isNullOrEmpty(nodeId)) {
                    parentId = Integer.parseInt(nodeId);
                }
                FaqKindDecorateService faqKindDecorateService = faqKindServiceProvider.get();

                ServletUtil.returnJson(response, faqKindDecorateService.queryTree(conditions, start, iPageSize, sortField, sortOrder, parentId));
            } else if ("simple".equals(type)) {
                String id = request.getParameter("id");
                FaqKindDecorateService faqKindDecorateService = faqKindServiceProvider.get();
                if (!Strings.isNullOrEmpty(id) && VerifyFormItem.isInteger(id)) {

                    ServletUtil.returnJson(response, faqKindDecorateService.queryFaqKindById(Integer.parseInt(id)));
                } else if (Strings.isNullOrEmpty(id)) {
                    ServletUtil.returnJson(response, faqKindDecorateService.queryTree(null, 0, 10, "id", "asc", 0));
                }
            }
        }

    }

    public void queryFaq(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int start = 0;
        int iPageSize = 0;
        String sortOrderFlag = null;
        //1,定义并且接收参数
        String filters = null;
        String pageIndex = null;
        String pageSize = null;
        String sortField = null;
        String sortOrder = null;
        try {
            filters = request.getParameter("filters");
            pageIndex = request.getParameter("page");
            pageSize = request.getParameter("rows");
            sortField = request.getParameter("sidx");
            sortOrder = request.getParameter("sord");
        } catch (Exception ex) {
            log.error("接收FaqKindView页面参数异常!");
        }
        //2,转换成需要的参数
        if (ServletUtil.isIntegerAndNotNull(pageSize)) {
            iPageSize = Integer.parseInt(pageSize);
        }
        if (ServletUtil.isIntegerAndNotNull(pageIndex)) {
            int index = Integer.parseInt(pageIndex);
            start = (index - 1) * iPageSize;
        }
        if (sortOrder == null) {
            sortOrder = "ASC";
        }
        Map<String, Object> conditions = null;
        if (filters != null && !Strings.isNullOrEmpty(filters)) {
            conditions = objectMapper.readValue(filters, Map.class);
        }

        FaqKindDecorateService faqKindDecorateService = faqKindServiceProvider.get();

        ServletUtil.returnJson(response, faqKindDecorateService.query(conditions, start, iPageSize, sortField, sortOrder));
    }


    public void toFaqKindPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/treeGrid.jsp").forward(request, response);
    }


    public void getFaqKindList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String json = "1:尼玛;2:操你妈";

        ServletUtil.returnJsonString(response, json);
    }

    public void toFaqKindTreePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/treeGrid.jsp").forward(request, response);
    }

    public void saveFaqKinds(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FaqKindDecorateService faqKindDecorateService = faqKindServiceProvider.get();
        String oper = request.getParameter("oper");
        if ("del".equals(oper)) {
            String ids = request.getParameter("id");
            String[] idArray = ids.split(",");
            int[] idArr = new int[idArray.length];
            for (int i = 0; i < idArray.length; i++) {
                idArr[i] = Integer.parseInt(idArray[i]);
            }
            faqKindDecorateService.batchDeleteFaqKindById(idArr);
        } else if ("add".equals(oper)) {
            String name = request.getParameter("name");
            String ext = request.getParameter("ext");
            String sortNo = request.getParameter("sortNo");
            String parentId = request.getParameter("parentId");
            FaqKindObject faqKindObject = new FaqKindObject();
            faqKindObject.setName(name);
            faqKindObject.setExt(ext);
            faqKindObject.setSortNo(Integer.parseInt(sortNo));
            faqKindObject.setParentId(Integer.parseInt(parentId));
            faqKindDecorateService.add(faqKindObject);
        } else if ("edit".equals(oper)) {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String ext = request.getParameter("ext");
            String sortNo = request.getParameter("sortNo");
            String parentId = request.getParameter("parentId");
            FaqKindObject faqKindObject = new FaqKindObject();
            faqKindObject.setId(Integer.parseInt(id));
            faqKindObject.setName(name);
            faqKindObject.setExt(ext);
            faqKindObject.setSortNo(Integer.parseInt(sortNo));
            faqKindObject.setParentId(Integer.parseInt(parentId));
            faqKindDecorateService.update(faqKindObject);
        }
    }

    public void saveFaqKindsTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg();
        FaqKindDecorateService faqKindDecorateService = faqKindServiceProvider.get();
        String oper = request.getParameter("oper");
        if ("del".equals(oper)) {
            String ids = request.getParameter("ids");
            String[] idArray = ids.split(",");
            int[] idArr = new int[idArray.length];
            for (int i = 0; i < idArray.length; i++) {
                idArr[i] = Integer.parseInt(idArray[i]);
            }
            int rel = faqKindDecorateService.batchDeleteFaqKindById(idArr);
            if (rel >= 1) {
                msg.setCode(200);
                msg.setMsg("删除成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("删除失败!");
            }
        } else if ("add".equals(oper)) {
            String name = request.getParameter("faqKind_name");
            String ext = request.getParameter("faqKind_ext");
            String sortNo = request.getParameter("faqKind_sortNo");
            String parentId = request.getParameter("faqKind_parent");
            if (!Strings.isNullOrEmpty(sortNo) && VerifyFormItem.isInteger(sortNo)) {
                FaqKindObject faqKindObject = new FaqKindObject();
                faqKindObject.setName(name);
                faqKindObject.setExt(ext);
                faqKindObject.setSortNo(Integer.parseInt(sortNo));
                faqKindObject.setParentId(Integer.parseInt(parentId));
                int rel = faqKindDecorateService.add(faqKindObject);
                if (rel >= 1) {
                    msg.setCode(200);
                    msg.setMsg("增加成功!");
                } else {
                    msg.setCode(204);
                    msg.setMsg("增加失败!");
                }
            } else {
                msg.setCode(204);
                msg.setMsg("排序号为空或者不为合法整数!");
            }
        } else if ("edit".equals(oper)) {
            String id = request.getParameter("u_faqKind_id");
            String name = request.getParameter("u_faqKind_name");
            String ext = request.getParameter("u_faqKind_ext");
            String sortNo = request.getParameter("u_faqKind_sortNo");
            String parentId = request.getParameter("u_faqKind_parent");

            if (!Strings.isNullOrEmpty(sortNo) && VerifyFormItem.isInteger(sortNo)) {
                FaqKindObject faqKindObject = faqKindDecorateService.getFaqKindById(Integer.parseInt(id));
                faqKindObject.setName(name);
                faqKindObject.setExt(ext);
                faqKindObject.setSortNo(Integer.parseInt(sortNo));
                faqKindObject.setParentId(Integer.parseInt(parentId));
                int rel = faqKindDecorateService.update(faqKindObject);
                if (rel >= 1) {
                    msg.setCode(200);
                    msg.setMsg("更新成功!");
                } else {
                    msg.setCode(204);
                    msg.setMsg("更新失败!");
                }
            } else {
                msg.setCode(204);
                msg.setMsg("排序号为空或者不为合法整数!");
            }

        }
        ServletUtil.returnJson(response, msg);
    }
}
