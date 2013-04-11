package com.sz7road.web.action;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.PayTable;
import com.sz7road.userplatform.service.AppealService;
import com.sz7road.userplatform.service.PayService;
import com.sz7road.userplatform.service.PayTableService;
import com.sz7road.userplatform.service.VerifyCodeProvider;
import com.sz7road.web.BaseServlet;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Singleton
public class PaySwitchTypeServlet extends BaseServlet {

    @Inject
    private Provider<PayService> payServiceProvider;

    @Inject
    private Provider<PayTableService> payTableServiceProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<PayTable.PayEntry> list = payTableServiceProvider.get().list();
        if (list != null)
            request.setAttribute("list", list);
        request.getRequestDispatcher("/pay/paySwitchType.jsp").forward(request, response);
    }


}
