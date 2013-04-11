package com.sz7road.userplatform.ws;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.service.MergerService;
import com.sz7road.utils.VerifyFormItem;
import com.sz7road.web.pojos.MergerInfoBean;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-29
 * Time: 上午9:53
 * Description: 获取合服信息接口
 */
@Singleton
public class SqMergerInfoServlet extends HeadlessServlet {
    @Inject
    private Provider<MergerService> mergerServiceProvider;

    @Override
    protected void doService(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        List<MergerInfoBean> mergerInfoBeanList = mergerServiceProvider.get().getCurrentMergerSortedList();
        if (mergerInfoBeanList != null && !mergerInfoBeanList.isEmpty()) {
            response.sendSuccess(mergerInfoBeanList);
        } else {
            response.sendFailure("没找到神曲的合服信息");
        }
    }
}
