package com.sz7road.web.utils;

import com.google.common.base.Strings;
import com.sz7road.userplatform.pojo.DataStatisticalObject;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.utils.ListData;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-4
 * Time: 下午5:31
 * POI帮助类
 */
public class POIUtil {

    private static final Logger log = LoggerFactory.getLogger(POIUtil.class);

    private static final int CELLCOUNT = 14;  //总列数

    private static final int PAGENUMBER = 65000;  //每个excel的记录条数

    /**
     * 构造xls结构，导出数据
     *
     * @param queryData 要到导出的数据
     * @param
     * @return 导出的流
     */


    public static void builderHSSFWorkbook(ListData<OrderViewObject> queryData, List<String> heads, OutputStream outputStream) {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        HSSFRow hssfRow = null;

        HSSFCell hssfCell = null;

        HSSFFont titleFont = null;

        HSSFFont contentFont = null;

        HSSFCellStyle headStyle = null;

        titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight((short) 14);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setItalic(true);

        contentFont = hssfWorkbook.createFont();
        contentFont.setFontHeightInPoints((short) 10);
        contentFont.setBoldweight((short) 10);
        contentFont.setColor(HSSFColor.BLACK.index);


        headStyle = hssfWorkbook.createCellStyle();
        headStyle.setFont(titleFont);
        headStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("($#,##0_);[Red]($#,##0)"));
        headStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        HSSFCellStyle contentStyle = hssfWorkbook.createCellStyle();
        contentStyle.setFont(contentFont);

//        HSSFCellStyle summaryStyle = hssfWorkbook.createCellStyle();
//        summaryStyle.setFont(titleFont);

        hssfWorkbook.setSheetName(0, "订单导出数据");

        //构造数据

        CreationHelper creationHelper = hssfWorkbook.getCreationHelper();

        //构造第一列的表头
        hssfRow = hssfSheet.createRow(0);

        for (int i = 0; i < heads.size(); i++) {
            hssfCell = hssfRow.createCell(i);
            hssfCell.setCellStyle(headStyle);
            hssfSheet.setColumnWidth(i, (int) ((50 * 8) / ((double) 1 / 18)));
            hssfCell.setCellValue(creationHelper.createRichTextString(heads.get(i)));
        }

        int count = 1;

        for (OrderViewObject orderViewObject : queryData.getList()) {
            hssfRow = hssfSheet.createRow(count);

            //构造第一列：订单号
            hssfCell.setCellStyle(contentStyle);
            hssfCell = hssfRow.createCell(0);
            hssfSheet.setColumnWidth(0, (int) ((50 * 8) / ((double) 1 / 15)));
            hssfCell.setCellValue(orderViewObject.getOrderId());
            //构造第二列：用户ID
            hssfCell = hssfRow.createCell(1);
            hssfSheet.setColumnWidth(1, (int) ((50 * 8) / ((double) 1 / 5)));
            hssfCell.setCellValue(orderViewObject.getUserId());

            //构造第三列：用户名
            hssfCell = hssfRow.createCell(2);
            hssfSheet.setColumnWidth(2, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getUserName());

            //构造第四列：玩家ID
            hssfCell = hssfRow.createCell(3);
            hssfSheet.setColumnWidth(3, (int) ((50 * 8) / ((double) 1 / 5)));
            hssfCell.setCellValue(orderViewObject.getPlayerId());

            //构造第五列：金额
            hssfCell = hssfRow.createCell(4);
            hssfSheet.setColumnWidth(4, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getAmount());

            //构造第六列：游戏币
            hssfCell = hssfRow.createCell(5);
            hssfSheet.setColumnWidth(5, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getGold());

            //构造第七列：确认时间
            hssfCell = hssfRow.createCell(6);
            hssfSheet.setColumnWidth(6, (int) ((50 * 8) / ((double) 1 / 15)));
            hssfCell.setCellValue(orderViewObject.getAssertTime() == null ? "无" : orderViewObject.getAssertTime().toString());

            //构造第八列：支付时间
            hssfCell = hssfRow.createCell(7);
            hssfSheet.setColumnWidth(7, (int) ((50 * 8) / ((double) 1 / 15)));
            hssfCell.setCellValue(orderViewObject.getPayTime() == null ? "无" : orderViewObject.getPayTime().toString());

            //构造第九列：订单状态
            hssfCell = hssfRow.createCell(8);
            hssfSheet.setColumnWidth(8, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getStatus());

            //构造第十列：服务器名称
            hssfCell = hssfRow.createCell(9);
            hssfSheet.setColumnWidth(9, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getServerName());

            //构造第十一列：充值网关
            hssfCell = hssfRow.createCell(10);
            hssfSheet.setColumnWidth(10, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getChannelName());

            //构造第十二列：充值方式
            hssfCell = hssfRow.createCell(11);
            hssfSheet.setColumnWidth(11, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getSubTypeName());

            //构造第十三列：充值渠道
            hssfCell = hssfRow.createCell(12);
            hssfSheet.setColumnWidth(12, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getSubTypeTagName());

            //构造第十四列：网关订单
            hssfCell = hssfRow.createCell(13);
            hssfSheet.setColumnWidth(13, (int) ((50 * 8) / ((double) 1 / 10)));
            hssfCell.setCellValue(orderViewObject.getEndOrder() == null ? "无" : orderViewObject.getEndOrder());

            count++;
        }

        count += 2;


//        float  sum=0;
//        for(OrderViewObject orderViewObject:queryData.getList())
//        {
//                 sum+=orderViewObject.getAmount();
//        }

//         hssfRow=  hssfSheet.createRow(count);
//        hssfCell=hssfRow.createCell(5);
//        hssfSheet.setDefaultRowHeight((short)13);
//        hssfCell.setCellNum((short)4);
//        hssfCell.setCellValue("数据汇总：数据条数 "+queryData.getTotal()+" 。   总交易额:"+sum+" 元");


        try {
            hssfWorkbook.write(outputStream);
        } catch (Exception e) {
            log.info("poi 写入数据异常！");
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭输出流异常！");
                e.printStackTrace();
            }
        }

//        return fileOutputStream;
    }

    /**
     * 数量超过65000的多文件压缩
     */
    public static void builderHSSFWorkbookALot(ListData<OrderViewObject> queryData, List<String> heads, OutputStream outputStream) {

        List<File> fileList = new ArrayList<File>(); //文件列表

        List<OrderViewObject> datas = queryData.getList(); //数据列表

        int size = queryData.getTotal(); //记录条数

        int pageCount = ServletUtil.getTotalPages(size, PAGENUMBER);//可以分成几个文件

        FileOutputStream out = null;

        //写文件数据满65000条的
        for (int i = 1; i < pageCount; i++) { //外层循环分文件
            String fileName = "orderFile_" + i + ".xls";
            try {

                out = new FileOutputStream(fileName);

                fileList.add(new File(fileName));
                //构造workbook 和表头
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

                HSSFSheet hssfSheet = hssfWorkbook.createSheet();

                HSSFRow hssfRow = null;

                HSSFCell hssfCell = null;

                HSSFFont titleFont = hssfWorkbook.createFont();
                titleFont.setFontHeightInPoints((short) 12);
                titleFont.setBoldweight((short) 14);
                titleFont.setColor(HSSFColor.BLACK.index);
                titleFont.setItalic(true);

                HSSFFont contentFont = hssfWorkbook.createFont();
                contentFont.setFontHeightInPoints((short) 10);
                contentFont.setBoldweight((short) 10);
                contentFont.setColor(HSSFColor.BLACK.index);


                HSSFCellStyle headStyle = hssfWorkbook.createCellStyle();
                headStyle.setFont(titleFont);
                headStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("($#,##0_);[Red]($#,##0)"));
                headStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
                headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);


                HSSFCellStyle contentStyle = hssfWorkbook.createCellStyle();
                contentStyle.setFont(contentFont);

//        HSSFCellStyle summaryStyle = hssfWorkbook.createCellStyle();
//        summaryStyle.setFont(titleFont);

                hssfWorkbook.setSheetName(0, "订单导出数据");

                //构造数据

                CreationHelper creationHelper = hssfWorkbook.getCreationHelper();

                //构造第一列的表头
                hssfRow = hssfSheet.createRow(0);

                for (int headIndex = 0; headIndex < heads.size(); headIndex++) {
                    hssfCell = hssfRow.createCell(headIndex);
                    hssfCell.setCellStyle(headStyle);
                    hssfSheet.setColumnWidth(headIndex, (int) ((50 * 8) / ((double) 1 / 18)));
                    hssfCell.setCellValue(creationHelper.createRichTextString(heads.get(headIndex)));
                }
                //内层循环 填充数据
                for (int j = 1; j <= PAGENUMBER; j++) {
                    int index = (i - 1) * PAGENUMBER + j - 1;  //在list中的序号

                    hssfRow = hssfSheet.createRow(j);

                    //构造第一列：订单号
                    hssfCell.setCellStyle(contentStyle);
                    hssfCell = hssfRow.createCell(0);
                    hssfSheet.setColumnWidth(0, (int) ((50 * 8) / ((double) 1 / 15)));
                    hssfCell.setCellValue(datas.get(index).getOrderId());
                    //构造第二列：用户ID
                    hssfCell = hssfRow.createCell(1);
                    hssfSheet.setColumnWidth(1, (int) ((50 * 8) / ((double) 1 / 5)));
                    hssfCell.setCellValue(datas.get(index).getUserId());

                    //构造第三列：用户名
                    hssfCell = hssfRow.createCell(2);
                    hssfSheet.setColumnWidth(2, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getUserName());

                    //构造第四列：玩家ID
                    hssfCell = hssfRow.createCell(3);
                    hssfSheet.setColumnWidth(3, (int) ((50 * 8) / ((double) 1 / 5)));
                    hssfCell.setCellValue(datas.get(index).getPlayerId());

                    //构造第五列：金额
                    hssfCell = hssfRow.createCell(4);
                    hssfSheet.setColumnWidth(4, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getAmount());

                    //构造第六列：游戏币
                    hssfCell = hssfRow.createCell(5);
                    hssfSheet.setColumnWidth(5, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getGold());

                    //构造第七列：确认时间
                    hssfCell = hssfRow.createCell(6);
                    hssfSheet.setColumnWidth(6, (int) ((50 * 8) / ((double) 1 / 15)));
                    hssfCell.setCellValue(datas.get(index).getAssertTime() == null ? "无" : datas.get(index).getAssertTime().toString());

                    //构造第八列：支付时间
                    hssfCell = hssfRow.createCell(7);
                    hssfSheet.setColumnWidth(7, (int) ((50 * 8) / ((double) 1 / 15)));
                    hssfCell.setCellValue(datas.get(index).getPayTime() == null ? "无" : datas.get(index).getPayTime().toString());

                    //构造第九列：订单状态
                    hssfCell = hssfRow.createCell(8);
                    hssfSheet.setColumnWidth(8, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getStatus());

                    //构造第十列：服务器名称
                    hssfCell = hssfRow.createCell(9);
                    hssfSheet.setColumnWidth(9, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getServerName());

                    //构造第十一列：充值网关
                    hssfCell = hssfRow.createCell(10);
                    hssfSheet.setColumnWidth(10, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getChannelName());

                    //构造第十二列：充值方式
                    hssfCell = hssfRow.createCell(11);
                    hssfSheet.setColumnWidth(11, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getSubTypeName());

                    //构造第十三列：充值渠道
                    hssfCell = hssfRow.createCell(12);
                    hssfSheet.setColumnWidth(12, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getSubTypeTagName());

                    //构造第十四列：网关订单
                    hssfCell = hssfRow.createCell(13);
                    hssfSheet.setColumnWidth(13, (int) ((50 * 8) / ((double) 1 / 10)));
                    hssfCell.setCellValue(datas.get(index).getEndOrder() == null ? "无" : datas.get(index).getEndOrder());
                }

                hssfWorkbook.write(out);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                log.info("文件数据写完：" + fileName);
            }
        }
        //写文件数据不满65000条的
        String fileName = "orderFile_" + pageCount + ".xls";
        try {

            out = new FileOutputStream(fileName);

            fileList.add(new File(fileName));
            //构造workbook 和表头
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

            HSSFSheet hssfSheet = hssfWorkbook.createSheet();

            HSSFRow hssfRow = null;

            HSSFCell hssfCell = null;

            HSSFFont titleFont = hssfWorkbook.createFont();
            titleFont.setFontHeightInPoints((short) 12);
            titleFont.setBoldweight((short) 14);
            titleFont.setColor(HSSFColor.BLACK.index);
            titleFont.setItalic(true);

            HSSFFont contentFont = hssfWorkbook.createFont();
            contentFont.setFontHeightInPoints((short) 10);
            contentFont.setBoldweight((short) 10);
            contentFont.setColor(HSSFColor.BLACK.index);


            HSSFCellStyle headStyle = hssfWorkbook.createCellStyle();
            headStyle.setFont(titleFont);
            headStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("($#,##0_);[Red]($#,##0)"));
            headStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);


            HSSFCellStyle contentStyle = hssfWorkbook.createCellStyle();
            contentStyle.setFont(contentFont);

//        HSSFCellStyle summaryStyle = hssfWorkbook.createCellStyle();
//        summaryStyle.setFont(titleFont);

            hssfWorkbook.setSheetName(0, "订单导出数据");

            //构造数据

            CreationHelper creationHelper = hssfWorkbook.getCreationHelper();

            //构造第一列的表头
            hssfRow = hssfSheet.createRow(0);

            for (int headIndex = 0; headIndex < heads.size(); headIndex++) {
                hssfCell = hssfRow.createCell(headIndex);
                hssfCell.setCellStyle(headStyle);
                hssfSheet.setColumnWidth(headIndex, (int) ((50 * 8) / ((double) 1 / 18)));
                hssfCell.setCellValue(creationHelper.createRichTextString(heads.get(headIndex)));
            }
            //内层循环 填充数据
            for (int j = 1; j <= size - PAGENUMBER * (pageCount - 1); j++) {
                int index = (pageCount - 1) * PAGENUMBER + j - 1;  //在list中的序号

                hssfRow = hssfSheet.createRow(j);

                //构造第一列：订单号
                hssfCell.setCellStyle(contentStyle);
                hssfCell = hssfRow.createCell(0);
                hssfSheet.setColumnWidth(0, (int) ((50 * 8) / ((double) 1 / 15)));
                hssfCell.setCellValue(datas.get(index).getOrderId());
                //构造第二列：用户ID
                hssfCell = hssfRow.createCell(1);
                hssfSheet.setColumnWidth(1, (int) ((50 * 8) / ((double) 1 / 5)));
                hssfCell.setCellValue(datas.get(index).getUserId());

                //构造第三列：用户名
                hssfCell = hssfRow.createCell(2);
                hssfSheet.setColumnWidth(2, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getUserName());

                //构造第四列：玩家ID
                hssfCell = hssfRow.createCell(3);
                hssfSheet.setColumnWidth(3, (int) ((50 * 8) / ((double) 1 / 5)));
                hssfCell.setCellValue(datas.get(index).getPlayerId());

                //构造第五列：金额
                hssfCell = hssfRow.createCell(4);
                hssfSheet.setColumnWidth(4, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getAmount());

                //构造第六列：游戏币
                hssfCell = hssfRow.createCell(5);
                hssfSheet.setColumnWidth(5, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getGold());

                //构造第七列：确认时间
                hssfCell = hssfRow.createCell(6);
                hssfSheet.setColumnWidth(6, (int) ((50 * 8) / ((double) 1 / 15)));
                hssfCell.setCellValue(datas.get(index).getAssertTime() == null ? "无" : datas.get(index).getAssertTime().toString());

                //构造第八列：支付时间
                hssfCell = hssfRow.createCell(7);
                hssfSheet.setColumnWidth(7, (int) ((50 * 8) / ((double) 1 / 15)));
                hssfCell.setCellValue(datas.get(index).getPayTime() == null ? "无" : datas.get(index).getPayTime().toString());

                //构造第九列：订单状态
                hssfCell = hssfRow.createCell(8);
                hssfSheet.setColumnWidth(8, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getStatus());

                //构造第十列：服务器名称
                hssfCell = hssfRow.createCell(9);
                hssfSheet.setColumnWidth(9, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getServerName());

                //构造第十一列：充值网关
                hssfCell = hssfRow.createCell(10);
                hssfSheet.setColumnWidth(10, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getChannelName());

                //构造第十二列：充值方式
                hssfCell = hssfRow.createCell(11);
                hssfSheet.setColumnWidth(11, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getSubTypeName());

                //构造第十三列：充值渠道
                hssfCell = hssfRow.createCell(12);
                hssfSheet.setColumnWidth(12, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getSubTypeTagName());

                //构造第十四列：网关订单
                hssfCell = hssfRow.createCell(13);
                hssfSheet.setColumnWidth(13, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(datas.get(index).getEndOrder() == null ? "无" : datas.get(index).getEndOrder());
            }

            hssfWorkbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            log.info("文件数据写完：" + fileName);
        }


        File zipFile = new File("orderdata.zip");
        convertToZip(fileList, zipFile);
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(zipFile);
            byte[] buf = new byte[4096];
            int readLength;
            while (((readLength = inStream.read(buf)) != -1)) {
                outputStream.write(buf, 0, readLength);
            }
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception ex) {
                log.error("关闭文件流异常！");
            }
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭输出流异常！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 把文件列表压缩成zip文件
     *
     * @param files   文件列表
     * @param zipFile 压缩文件名
     */
    public static void convertToZip(List<File> files, File zipFile) {
        byte[] byteBs = new byte[1024];
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));

            for (File file : files) {
                FileInputStream inputStream = new FileInputStream(file);

                out.putNextEntry(new ZipEntry(file.getName()));

                int len = 0;

                while ((len = inputStream.read(byteBs)) > 0) {
                    out.write(byteBs, 0, len);
                }
                out.closeEntry();
                inputStream.close();
            }

        } catch (Exception ex) {
            log.error("压缩文件异常！");
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                log.error("关闭zip流异常！");
            }
        }
    }


    //构建xls的结构
    public static void buildDataStaticWorkbook(DataStatisticalObject queryData, OutputStream outputStream) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        HSSFRow hssfRow = null;

        HSSFCell hssfCell = null;

        HSSFFont titleFont = null;

        HSSFFont contentFont = null;

        HSSFCellStyle headStyle = null;

        titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight((short) 14);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setItalic(true);

        contentFont = hssfWorkbook.createFont();
        contentFont.setFontHeightInPoints((short) 10);
        contentFont.setBoldweight((short) 10);
        contentFont.setColor(HSSFColor.BLACK.index);


        headStyle = hssfWorkbook.createCellStyle();
        headStyle.setFont(titleFont);
        headStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("($#,##0_);[Red]($#,##0)"));
        headStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        HSSFCellStyle contentStyle = hssfWorkbook.createCellStyle();
        contentStyle.setFont(contentFont);

//        HSSFCellStyle summaryStyle = hssfWorkbook.createCellStyle();
//        summaryStyle.setFont(titleFont);

//        if(!Strings.isNullOrEmpty(queryData.getOverAll()))
        hssfWorkbook.setSheetName(0, "统计数据");
        //构造数据

        CreationHelper creationHelper = hssfWorkbook.getCreationHelper();
        if (!Strings.isNullOrEmpty(queryData.getOverAll())) {
            hssfRow = hssfSheet.createRow(0);
            hssfCell = hssfRow.createCell(1);
            hssfCell.setCellStyle(headStyle);
            hssfSheet.setColumnWidth(1, (int) ((50 * 8) / ((double) 1 / 48)));
            hssfCell.setCellValue(creationHelper.createRichTextString(queryData.getOverAll()));

        }
        //构造第一列的表头
        List<String> heads = (List<String>) queryData.getHead();
        hssfRow = hssfSheet.createRow(1);
        if (heads != null && heads.size() > 0) for (int i = 0; i < heads.size(); i++) {
            hssfCell = hssfRow.createCell(i);
            hssfCell.setCellStyle(headStyle);
            hssfSheet.setColumnWidth(i, (int) ((50 * 8) / ((double) 1 / 18)));
            hssfCell.setCellValue(creationHelper.createRichTextString(heads.get(i)));
        }

        List<Map<String, Object>> contend = (List<Map<String, Object>>) queryData.getDataContent();
        int count = 2;
        if (contend != null && contend.size() > 0) for (Map<String, Object> statistical : contend) {
            hssfRow = hssfSheet.createRow(count);
            int cellCount = -1;
            if (statistical.get("gameName") != null && !Strings.isNullOrEmpty(statistical.get("gameName").toString())) {
                cellCount++;
                hssfCell.setCellStyle(contentStyle);
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 15)));
                hssfCell.setCellValue(statistical.get("gameName").toString());
            }
            if (statistical.get("serverName") != null && !Strings.isNullOrEmpty(statistical.get("serverName").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 15)));
                hssfCell.setCellValue(statistical.get("serverName").toString());
            }

            if (statistical.get("channelName") != null && !Strings.isNullOrEmpty(statistical.get("channelName").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("channelName").toString());
            }

            if (statistical.get("subTypeName") != null && !Strings.isNullOrEmpty(statistical.get("subTypeName").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("subTypeName").toString());
            }

            if (statistical.get("subTagName") != null && !Strings.isNullOrEmpty(statistical.get("subTagName").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("subTagName").toString());
            }

            if (statistical.get("status") != null && !Strings.isNullOrEmpty(statistical.get("status").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("status").toString());
            }

            if (statistical.get("sumMoney") != null && !Strings.isNullOrEmpty(statistical.get("sumMoney").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("sumMoney").toString());
            }

            if (statistical.get("goldSum") != null && !Strings.isNullOrEmpty(statistical.get("goldSum").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("goldSum").toString());
            }

            if (statistical.get("userSum") != null && !Strings.isNullOrEmpty(statistical.get("userSum").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("userSum").toString());
            }

            if (statistical.get("playerSum") != null && !Strings.isNullOrEmpty(statistical.get("playerSum").toString())) {
                cellCount++;
                hssfCell = hssfRow.createCell(cellCount);
                hssfSheet.setColumnWidth(cellCount, (int) ((50 * 8) / ((double) 1 / 10)));
                hssfCell.setCellValue(statistical.get("playerSum").toString());
            }

            count++;
        }

        count += 2;


        try {
            hssfWorkbook.write(outputStream);
        } catch (Exception e) {
            log.info("poi 写入数据异常！");
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭输出流异常！");
                e.printStackTrace();
            }
        }
    }
}
