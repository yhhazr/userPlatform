package com.sz7road.userplatform.ws.avatar;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.inject.Singleton;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-28
 * Time: 下午3:20
 * 接受swf传过来的流，保存图片
 */
@Singleton
public class UploadBySwfServlet extends HeadlessServlet {

    final static Logger log = LoggerFactory.getLogger(UploadBySwfServlet.class);

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {

        InputStream is=request.getInputStream();

        FileOutputStream fos=new FileOutputStream("c://head1.jpg");
        byte[] bytes=new byte[1024];
        while (is.read(bytes,0,1024)!=-1)
        {
           fos.write(bytes);
        }
        is.close();
        fos.close();

//        Files.copy((InputSupplier<? extends InputStream>) is,new File("c://head.jpg"));


    }
}
