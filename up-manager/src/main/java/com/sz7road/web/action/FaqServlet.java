package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.FaqObject;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.FaqDecorateService;
import com.sz7road.userplatform.utils.LuceneUtil;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 下午4:45
 * 处理常见问题的servlet
 */
@Singleton
public class FaqServlet extends BaseServlet {

    @Inject
    private Provider<FaqDecorateService> faqServiceProvider;


    public void faqManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/faq.jsp").forward(request, response);
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

        String type = request.getParameter("type");
        if (!Strings.isNullOrEmpty(type)) {
            if ("load".equals(type)) {
                try {
                    filters = request.getParameter("filters");
                    pageIndex = request.getParameter("page");
                    pageSize = request.getParameter("rows");
                    sortField = request.getParameter("sidx");
                    sortOrder = request.getParameter("sord");
                } catch (Exception ex) {
                    log.error("接收faq页面参数异常!");
                    ex.printStackTrace();
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
                ObjectMapper objectMapper = new ObjectMapper();
                if (filters != null && !Strings.isNullOrEmpty(filters)) {
                    conditions = objectMapper.readValue(filters, Map.class);
                }

                FaqDecorateService faqDecorateService = faqServiceProvider.get();

                ServletUtil.returnJson(response, faqDecorateService.queryFaq(conditions, start, iPageSize, sortField, sortOrder));
            } else if ("simple".equals(type)) {
                String id = request.getParameter("id");

                if (!Strings.isNullOrEmpty(id) && VerifyFormItem.isInteger(id)) {
                    FaqDecorateService faqDecorateService = faqServiceProvider.get();

                    ServletUtil.returnJson(response, faqDecorateService.queryFaqById(Integer.parseInt(id)));
                }
            }
        }


    }

    public void saveFaq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        Msg msg = new Msg();
        FaqDecorateService faqDecorateService = faqServiceProvider.get();
        String oper = request.getParameter("oper");
        if ("del".equals(oper)) {
            String ids = request.getParameter("ids");
            String[] idArray = ids.split(",");
            int[] idArr = new int[idArray.length];
            for (int i = 0; i < idArray.length; i++) {
                idArr[i] = Integer.parseInt(idArray[i]);
            }
            int rel = faqDecorateService.batchDeleteFaq(idArr);
            if (rel >= 1) {
                msg.setCode(200);
                msg.setMsg("删除成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("删除失败!");
            }
        } else if ("add".equals(oper)) {
            String cid = request.getParameter("faq_faqName");
            String question = request.getParameter("faq_question").trim();
            String answer = request.getParameter("faq_answer").trim();
            String sortNum = request.getParameter("faq_sortNum");
            FaqObject faqKindObject = new FaqObject();
            faqKindObject.setCid(Integer.parseInt(cid));
            faqKindObject.setQuestion(question);
            faqKindObject.setAnswer(answer);
            faqKindObject.setVisitSum(0);
            faqKindObject.setSortNum(Integer.parseInt(sortNum));
            int rel = faqDecorateService.addFaq(faqKindObject);
            if (rel >= 1) {
                msg.setCode(200);
                msg.setMsg("增加成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("增加失败!");
            }
        } else if ("edit".equals(oper)) {
            String id = request.getParameter("u_faq_id");
            String cid = request.getParameter("u_faq_faqName");
            String question = request.getParameter("u_faq_question").trim();
            String answer = request.getParameter("u_faq_answer").trim();
            String sortNum = request.getParameter("u_faq_sortNo");
            String visitSum = request.getParameter("u_faq_visitSum");
            FaqObject faqKindObject = new FaqObject();
            faqKindObject.setId(Integer.parseInt(id));
            faqKindObject.setCid(Integer.parseInt(cid));
            faqKindObject.setQuestion(question);
            faqKindObject.setAnswer(answer);
            faqKindObject.setVisitSum(Integer.parseInt(visitSum));
            faqKindObject.setSortNum(Integer.parseInt(sortNum));
            int rel = faqDecorateService.updateFaq(faqKindObject);
            if (rel >= 1) {
                msg.setCode(200);
                msg.setMsg("更新成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("更新失败!");
            }
        }
        ServletUtil.returnJson(response, msg);
    }


    public void faqQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {


        FaqDecorateService faqDecorateService = faqServiceProvider.get();
        List<FaqObject> list = faqDecorateService.fullTextQuery("");
        log.info("生成索引中···");
        Msg msg = new Msg();
        if (list != null && !list.isEmpty()) {
            msg.setCode(200);
            msg.setMsg("生成索引成功！");
        } else {
            msg.setCode(204);
            msg.setMsg("生成索引失败!");
        }
        ServletUtil.returnJson(response, msg);


    }

    public void QueryFaq(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setCharacterEncoding("utf-8");
        String keyWord = request.getParameter("keyword");
//        String keyWord =new String(key.getBytes("UTF8"),"GB2312");

        if (!Strings.isNullOrEmpty(keyWord)) {
            ServletUtil.returnJson(response, LuceneUtil.search(keyWord));
        } else {
            ServletUtil.returnJson(response, "0");
        }

    }


}
