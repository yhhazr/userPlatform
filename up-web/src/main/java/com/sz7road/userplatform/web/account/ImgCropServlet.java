package com.sz7road.userplatform.web.account;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Log;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.UserObject;
import com.sz7road.userplatform.service.LogService;
import com.sz7road.userplatform.service.UserService;
import com.sz7road.userplatform.web.utils.AppHelper;
import com.sz7road.userplatform.web.utils.DateUtils;
import com.sz7road.userplatform.web.utils.HtmlUtils;
import com.sz7road.userplatform.web.utils.ImageCut;
import com.sz7road.utils.CommonDateUtils;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.DropDownDataUtil;
import com.sz7road.utils.RuleUtil;
import com.sz7road.web.servlet.HeadlessHttpServlet;
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
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-9-11
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ImgCropServlet extends HeadlessHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ImgCropServlet.class.getName());
    @Inject
    Provider<UserService> userServiceProvider;
    @Inject
    private Provider<LogService> logServiceProvider;

    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String savePath = ConfigurationUtils.get("user.resource.dir"); //上传的图片保存路径
        String showPath = ConfigurationUtils.get("user.resource.url"); //显示图片的路径
        int userId = AppHelper.getUserId(request);
        String userName = AppHelper.getUserName(request);

        Msg msg = new Msg();
        msg.setCode(204);
        msg.setMsg("剪切图片失败！");
        if (userId <= 0 || Strings.isNullOrEmpty(userName)) {
            msg.setMsg("请先登录");
            AppHelper.returnJson(response, msg);
            return;
        }
        // 用户经过剪辑后的图片的大小
        Integer x = (int) Float.parseFloat(request.getParameter("x"));
        Integer y = (int) Float.parseFloat(request.getParameter("y"));
        Integer w = (int) Float.parseFloat(request.getParameter("w"));
        Integer h = (int) Float.parseFloat(request.getParameter("h"));
        //获取原显示图片路径 和大小
        final String oldImgName = request.getParameter("oldImgName");
        String oldImgPath = savePath + "/tempImg/" + oldImgName;
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(oldImgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer width = bufferedImage.getWidth();
        Integer height = bufferedImage.getHeight();
        //图片后缀
        final String imgFileExt = oldImgName.substring(oldImgName.lastIndexOf('.') + 1, oldImgName.length());
        final String foldName = "/" + DateUtils.nowDatetoStrToMonth();
        final File userImgFolder = new File(savePath + foldName);
        //如果不存在，创建文件夹。
        if (!userImgFolder.exists()) {
            userImgFolder.mkdir();
        }
        final String imgName =foldName+ "/" + UUID.randomUUID() + "." + imgFileExt;
        //组装图片真实名称
        final String createImgPath = savePath + imgName;
        // 过滤特殊字符
        if (DropDownDataUtil.getEnableUploadExtMap().containsKey(imgFileExt.toLowerCase()) && !RuleUtil.isContainStrangeWord(oldImgName)) {
            try{
            boolean flag = ImageCut.abscut(oldImgPath, createImgPath, x * width / 300, y * height / 300, w * width / 300, h * height / 300);
            if (flag) {
                File f = new File(createImgPath);
                if (f.exists()) {
                    //把显示路径保存到用户信息下面。并记录日志
                    UserService userService = userServiceProvider.get();
                    int rel = userService.updateUserAvatar(userId, userName, showPath + imgName);
                    if (rel >= 1) {
                        msg.setCode(200);
                        msg.setMsg("剪切图片成功!");
                        File file = new File(oldImgPath);
                        boolean deleteFile = file.delete();
                        if (deleteFile == true) {
                            log.info("删除零时图片成功");
                        }
                        //筛选出时间在今天之前的的零时头像列表，删除它。
                        File[] files = file.getParentFile().listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File img) {
                                return CommonDateUtils.getOneDayFirstMoment(new Date()) > img.lastModified();
                            }
                        });
                        if(files!=null&&files.length>0)
                        for (File nonUsedImg : files) {
                            nonUsedImg.delete();
                        }
                    } else {
                        msg.setCode(204);
                        msg.setMsg("剪切图片失败!");
                        log.info("剪切图片失败!");
                    }
                }
            } else {
                msg.setCode(204);
                msg.setMsg("剪切图片失败!");
                log.info("剪切图片失败!");
            }
            }catch (Exception ex)
            {
                msg.setCode(204);
                msg.setMsg("剪切图片异常!");
                log.error("剪切图片异常!");
                ex.printStackTrace();
            }
            finally {
                AppHelper.returnJson(response, msg);
            }

        } else {
            msg.setCode(204);
            msg.setMsg("哥哥，你就别攻击我了!");
        }
        AppHelper.returnJson(response, msg);
    }

    private void log(final String headDir, final String userName) {
        if (!Strings.isNullOrEmpty(headDir) && !Strings.isNullOrEmpty(userName)) {
            StringBuilder sb = new StringBuilder();
            Log log = new Log();
            sb.append("修改头像为：").append(headDir);
            log.setUserName(userName);
            log.setLogType(Log.LogType.ACCOUNT_UPDATE);
            log.setLog_time(new Timestamp(System.currentTimeMillis()));
            log.setContent(sb.toString());
            logServiceProvider.get().addTask(log);
        }
    }
}
