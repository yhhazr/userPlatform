package com.sz7road.userplatform.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserAccount;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DigitUtils;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Singleton
public class UploadIdCardServlet extends HeadlessHttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UploadIdCardServlet.class);

    private static String[] allowFileExtArray = new String[]{"png", "jpg", "gif", "bmp"};

    @Inject
    private Provider<UserService> userServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        Msg msg = new Msg(0,"上传头像失败");
        String user = null;

        String strImgPath = ConfigurationUtils.get("path.img.appeal.idcard");
        if (strImgPath == null || strImgPath.equals("")) {
            strImgPath = getServletContext().getRealPath("/") + "upload/";
        }
        File fileImgPath = new File(strImgPath);
        String realFilePath = "";

        if(!fileImgPath.exists()){
            fileImgPath.mkdirs();
        }

        boolean isAllowFileExt = false;
        boolean isUploadFileSaved = false;
        boolean isValid = false;
        File file = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        // 前台限制5M,这里限制6M;防止混乱出错
        servletFileUpload.setSizeMax(1024*1024*6);
        try {
            List items = servletFileUpload.parseRequest(request);
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                FileItem item = (FileItem) iterator.next();
                if (!item.isFormField()) {

                    String uploadFieldName = item.getFieldName();
                    String uploadFileName = item.getName();
                    String uploadFileExt = FilenameUtils.getExtension(uploadFileName);
                    long uploadFileSize = item.getSize();
                    if(uploadFileExt == null) {
                        uploadFileExt = "";
                    }
                    String strRandFileName = DigitUtils.getRandomStrDigit() + "." + uploadFileExt;
                    realFilePath = strImgPath + strRandFileName;

                    for(String itemExt : allowFileExtArray) {
                        if (uploadFileExt.toLowerCase().equals(itemExt)) {
                            isAllowFileExt = true;
                        }
                    }
                    if (isAllowFileExt == false) {
                        throw new IllegalArgumentException("文件格式错误");
                    }

                    file = new File(realFilePath);
                    item.write(file);
                    isUploadFileSaved = true;

                    msg.setCode(1);
                    msg.setMsg("上传文件成功");
                    msg.setObject(strRandFileName);
                    log.info("成功，用户{}，上传文件:{}", user, realFilePath);
                } else {
                    if (item.getFieldName() != null ) {
                        if ("user".equals(item.getFieldName()) && item.getString() != null) {
                            user = new String(item.getString().getBytes("iso-8859-1"), "utf-8");
                        }
                    }
                    log.info(item.getFieldName() + ":" + new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
                }
            }

            if (isUploadFileSaved) {
                if (user != null && !user.equals("")) {
                    UserAccount userAccount = userServiceProvider.get().findAccountByUserName(user);
                    if (userAccount != null)
                        isValid = true;
                }

                if (!isValid) {
                    file.delete();
                    msg.setCode(0);
                    msg.setMsg("非法上传");
                    log.error("用户[{}]非法上传文件:{}", user, realFilePath);
                }
            }
        } catch (Exception ex) {
            log.error("用户[{}]上传文件:{}\n" + ex.getMessage(), user, realFilePath);
            ex.printStackTrace();
        }
        AppHelper.returnJson(response, msg);
    }
}
