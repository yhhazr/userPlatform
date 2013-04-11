package com.sz7road.web.action;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojo.DataStatisticalObject;
import com.sz7road.userplatform.service.DataStatisticalService;
import com.sz7road.web.BaseServlet;
import com.sz7road.web.utils.POIUtil;
import com.sz7road.web.utils.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-17
 * Time: 下午4:56
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class DataStatisticalServlet extends BaseServlet {

    @Inject
    private Provider<DataStatisticalService> dataStatisticalServiceProvider;


    public void exportStatisticalData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1,接收参数 //2,转换成条件
        Map<String, Object> conditions = ServletUtil.getExportCustomizeCondition(request);
        //3，调用业务方法返回数据
        DataStatisticalService dataStatisticalService = dataStatisticalServiceProvider.get();
        DataStatisticalObject dataStatisticalObject = dataStatisticalService.getDataStatisticalByCustomize(conditions);
        if (!dataStatisticalObject.getMessage().equals("查询统计数据失败!")) {
            //3,得到导出文件的名字
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            String fileName = "statisticsData" + sf.format(new Date()) + ".xls";
            response.setHeader("content-disposition", "attachment;filename=" + response.encodeURL(new String(fileName.getBytes(), "utf-8")));
            OutputStream out = response.getOutputStream();
            POIUtil.buildDataStaticWorkbook(dataStatisticalObject, out);
        }
    }


    //基本的统计信息
    public void showBasicDataStatistical(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletUtil.returnJson(response, dataStatisticalServiceProvider.get().getBasicDataStatistical());
    }

    //定制的统计信息
    public void showCustomizeDataStatistical(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1,接收参数 //2,转换成条件
        Map<String, Object> conditions = ServletUtil.getCustomizeCondition(request);
        //3，调用业务方法返回数据
        DataStatisticalService dataStatisticalService = dataStatisticalServiceProvider.get();
        DataStatisticalObject dataStatisticalObject = dataStatisticalService.getDataStatisticalByCustomizeByPage(conditions);
        //4,写会数据到客户端完成显示
        ServletUtil.returnJson(response, dataStatisticalObject);
    }
}
