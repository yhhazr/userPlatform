package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Appeal;
import com.sz7road.userplatform.service.AppealService;
import com.sz7road.utils.DigitUtils;
import com.sz7road.utils.ShowImageUtils;
import com.sz7road.web.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


@Singleton
public class ShowIdCardImgServlet extends BaseServlet {

    @Inject
    private Provider<AppealService> appealServiceProvider;

    public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strId = request.getParameter("id");
        String path = request.getParameter("_p");
        int id = DigitUtils.parserInt(strId);
        if (Strings.isNullOrEmpty(strId) || id == 0) {
            response.setStatus(404);
            return;
        }

        response.setContentType("image/jpg");
        OutputStream out = response.getOutputStream();
        byte[] data = null;
        String imgPath = null;
        if (!Strings.isNullOrEmpty(path)) {
            imgPath = path;
        } else {
            Appeal entity = appealServiceProvider.get().get(id);
            imgPath = entity.getIdCardImgPath();
        }

        if (imgPath == null || imgPath.trim().equals("") || imgPath.indexOf(".") == -1) {
            //out.flush();
            out.close();
        } else {
            ShowImageUtils.show(out, imgPath);
        }
    }
}
