package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.CsInfoDecorateService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.ServletUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 下午2:24
 * 客服信息管理的servlet
 */
@Singleton
public class CsInfoServlet extends BaseServlet {

    @Inject
    private Provider<CsInfoDecorateService> csInfoSeviceProvider;

    private String savePath = "//images"; //上传文件的保存路径
    private ServletContext sc;  //application对象

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        sc = servletConfig.getServletContext();
    }

    public void csInfoManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/csInfo.jsp").forward(request, response);
    }

    public void queryCsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
                try {
                    filters = request.getParameter("filters");
                    pageIndex = request.getParameter("page");
                    pageSize = request.getParameter("rows");
                    sortField = request.getParameter("sidx");
                    sortOrder = request.getParameter("sord");
                } catch (Exception ex) {
                    log.error("接收csInfo页面参数异常!");
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

                CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();

                ServletUtil.returnJson(response, csInfoService.queryCsInfo(conditions, start, iPageSize, sortField, sortOrder));
            } else if ("simple".equals(type)) {
                String id = request.getParameter("id");

                if (!Strings.isNullOrEmpty(id) && VerifyFormItem.isInteger(id)) {
                    CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();

                    ServletUtil.returnJson(response, csInfoService.queryCsInfoById(Integer.parseInt(id)));
                }
            }
        }

    }

    public void saveCsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Msg msg = new Msg();
        CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();
        String oper = request.getParameter("oper");
        if ("del".equals(oper)) {
            String ids = request.getParameter("ids");
            String[] idArray = ids.split(",");
            int[] idArr = new int[idArray.length];
            for (int i = 0; i < idArray.length; i++) {
                idArr[i] = Integer.parseInt(idArray[i]);
            }
            int rel = csInfoService.BatchDeleteQQGroupOrAdPhoto(idArr);
            if (rel >= 1) {
                msg.setCode(200);
                msg.setMsg("删除成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("删除失败!");
            }
        } else if ("add".equals(oper)) {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String ext = request.getParameter("ext");
            String text = request.getParameter("text");
            InfoObject faqKindObject = new InfoObject();
            faqKindObject.setName(name);
            faqKindObject.setExt(ext);
            faqKindObject.setText(text);
            int rel = csInfoService.addQQGroupOrAdPhoto(faqKindObject);
            if (rel >= 1) {
                msg.setCode(200);
                msg.setMsg("增加成功!");
            } else {
                msg.setCode(204);
                msg.setMsg("增加失败!");
            }
        } else if ("edit".equals(oper)) {
            String id = request.getParameter("edit_qqInfoId");
            String ext = request.getParameter("edit_qqInfoExt");
            String text = request.getParameter("edit_qqInfoText");
            InfoObject faqKindObject = null;
            if (!Strings.isNullOrEmpty(id)) {
                faqKindObject = csInfoService.getCsInfo(Integer.parseInt(id));
            }
            faqKindObject.setExt(ext);
            faqKindObject.setText(text);
            int rel = csInfoService.updateInfo(faqKindObject);
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


    public void getCsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String type = request.getParameter("type");

        if (!Strings.isNullOrEmpty(type)) {
            CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();

            if ("csInfo".equalsIgnoreCase(type)) {

                ServletUtil.printToHtml(response, csInfoService.getCsInfo());

            } else if ("adPhoto".equalsIgnoreCase(type)) {
                List<InfoObject> infoObjectList = csInfoService.getAdPhotos();
                //相对对路径加上郁闷
                String domain = request.getServerName();
                for (InfoObject path : infoObjectList) {
                    String imgPath = path.getText();
                    path.setText(domain + imgPath);
                }
                Msg msg = new Msg();
                if (infoObjectList != null && !infoObjectList.isEmpty()) {
                    msg.setCode(200);
                    msg.setMsg("成功获取推播图片信息信息!");
                    msg.setObject(infoObjectList);
                } else {
                    msg.setCode(204);
                    msg.setMsg("获取推播图片信息失败!");
                }
                ServletUtil.printToHtml(response, msg);
            } else {
                ServletUtil.printToHtml(response, "type参数不合法！");
            }
        } else {
            ServletUtil.printToHtml(response, "type参数为空");
        }
    }

    public void getDomain(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String domain = request.getRemoteAddr() + "IP:" + request.getLocalAddr() + " 域名:" + request.getServerName();

        ServletUtil.printToHtml(response, domain);

    }

    public void addQQInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Msg msg = new Msg();
        String csInfoText = request.getParameter("cs_qqInfoText");
        String csInfoExt = request.getParameter("cs_qqInfoExt");
        InfoObject infoObject = new InfoObject();
        if (!Strings.isNullOrEmpty(csInfoExt) && !Strings.isNullOrEmpty(csInfoText)) {
            infoObject.setText(csInfoText);
            infoObject.setExt(csInfoExt);
            infoObject.setName("cs_qqgroup" + new Date().getTime());
            CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();
            int rel = csInfoService.addQQGroupOrAdPhoto(infoObject);
            msg.setCode(200);
            msg.setMsg("增加客服信息成功!");
            msg.setObject(rel);
        } else {
            msg.setCode(204);
            msg.setMsg("参数传递错误!");
        }
        ServletUtil.returnJson(response, msg);
    }

    //文件上传
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");
        String id = null;
        Msg msg = new Msg();
        String csInfoExt = null;
        InfoObject infoObject = new InfoObject();
        //1,设置请求的编码类型
        request.setCharacterEncoding("utf-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        try {
            List items = servletFileUpload.parseRequest(request);
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                FileItem item = (FileItem) iterator.next();
                if (!item.isFormField()) {
                    String formParamName = item.getFieldName();
                    String formParamValue = item.getName();
                    log.info("表单参数名：" + formParamName + " 表单参数值：" + formParamValue);
                    //完成文件的上传
                    File tempFile = new File(item.getName());
                    File saveDir = new File(sc.getRealPath("/") + savePath, tempFile.getName());
                    if (saveDir.exists()) {
                        log.info("存在同名文件···");
                        saveDir.delete();
                    }
                    item.write(saveDir);
                    infoObject.setText("/images/" + item.getName());
                    infoObject.setName("cs_adphoto" + new Date().getTime());
                } else {
                    if ("cs_photoInfoExt".equals(item.getFieldName())) {
                        csInfoExt = item.getString("utf-8").trim();
                        infoObject.setExt(csInfoExt);
                    }
                    if ("edit_photoInfoExt".equals(item.getFieldName())) {
                        csInfoExt = item.getString("utf-8").trim();
                        infoObject.setExt(csInfoExt);
                    }
                    if ("edit_photoInfoId".equals(item.getFieldName())) {
                        id = item.getString("utf-8").trim();
                    }

                }
            }
        } catch (Exception ex) {
            log.error("上传推播图片异常!");
            ex.printStackTrace();
        } finally {
            if ("add".equals(type)) {
                if (!Strings.isNullOrEmpty(csInfoExt)) {
                    CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();
                    int rel = csInfoService.addQQGroupOrAdPhoto(infoObject);
                    msg.setCode(200);
                    msg.setMsg("增加客服轮播图片成功!");
                    msg.setObject(rel);
                } else {
                    msg.setCode(204);
                    msg.setMsg("参数传递错误!");
                }
            } else if ("edit".equals(type)) {
                if (!Strings.isNullOrEmpty(id)) {
                    CsInfoDecorateService csInfoService = csInfoSeviceProvider.get();
                    InfoObject newInfoObject = csInfoService.getCsInfo(Integer.parseInt(id));
                    newInfoObject.setName(infoObject.getName());
                    newInfoObject.setText(infoObject.getText());
                    newInfoObject.setExt(infoObject.getExt());
                    int rel = csInfoService.updateInfo(newInfoObject);
                    msg.setCode(200);
                    msg.setMsg("更新客服轮播图片成功!");
                    msg.setObject(rel);
                } else {
                    msg.setCode(204);
                    msg.setMsg("参数传递错误!");
                }
            }
            ServletUtil.returnJson(response, msg);
        }
    }
}
