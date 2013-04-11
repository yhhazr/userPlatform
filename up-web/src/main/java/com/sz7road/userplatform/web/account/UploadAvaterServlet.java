package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Image;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.DateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DropDownDataUtil;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-10
 * Time: 下午5:36
 * commonUpload 文件上传
 */
@Singleton
public class UploadAvaterServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UploadAvaterServlet.class);

    private ServletContext sc;  //application对象

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        sc = servletConfig.getServletContext();
    }

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tempSavePath = ConfigurationUtils.get("user.resource.dir"); //上传的图片零时保存路径
        String tempShowPath = ConfigurationUtils.get("user.resource.url"); //用户保存的头像路径
        if (tempSavePath.equals("/img")) {
            tempSavePath = sc.getRealPath("/") + tempSavePath;
        }
        Msg msg = new Msg();
        msg.setCode(204);
        msg.setMsg("上传失败（图片要小于1M）");
        String type = request.getParameter("type");
        if (!Strings.isNullOrEmpty(type) && type.equals("first")) {
            request.setCharacterEncoding("utf-8");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024 * 1024);//// 设置最多只允许在内存中存储的数据,单位:字节
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            servletFileUpload.setFileSizeMax(1024 * 1024);
            try {
                List items = servletFileUpload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        {
                            File saveTemp = new File(tempSavePath + "/tempImg/");
                            String getItemName = new File(item.getName()).getName();
                            //服务器端判断文件的扩展名 确保不上传危险文件
                            String uploadFileExt = getItemName.substring(getItemName.lastIndexOf(".") + 1, getItemName.length());
                            if (DropDownDataUtil.getEnableUploadExtMap().containsKey(uploadFileExt) && !RuleUtil.isContainStrangeWord(getItemName)) {
                                String fileName = UUID.randomUUID() + "." + uploadFileExt;
                                File saveDir = new File(tempSavePath + "/tempImg/", fileName);
                                if (saveDir.exists()) {
                                    saveDir.delete();
                                }
                                item.write(saveDir);
                                log.info("上传头像成功!" + saveDir.getName());
                                msg.setCode(200);
                                msg.setMsg("上传头像成功!");
                                Image image = new Image();
                                BufferedImage bufferedImage = null;
                                try {
                                    bufferedImage = ImageIO.read(saveDir);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                image.setHeight(bufferedImage.getHeight());
                                image.setWidth(bufferedImage.getWidth());
                                image.setPath(tempShowPath + "/tempImg/" + fileName);
                                log.info(image.getPath());
                                image.setRealPath(fileName);
                                image.setFileExt(DropDownDataUtil.getEnableUploadExtValue(uploadFileExt));
                                msg.setObject(image);
                            } else {
                                msg.setCode(204);
                                msg.setMsg("请上传指定扩展名的图片！");
                            }
                        }
                    } else {
                        log.info("" + item.getFieldName());
                    }
                }
            } catch (Exception ex) {
                log.error("上传用户头像图片异常!" + ex.getMessage());
                ex.printStackTrace();
            } finally {
                AppHelper.returnJsonAjaxForm(response, msg);
            }
        }
    }
}
