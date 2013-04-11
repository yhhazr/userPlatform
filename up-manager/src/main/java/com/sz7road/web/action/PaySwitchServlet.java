package com.sz7road.web.action;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.service.ConfigurationService;
import com.sz7road.userplatform.service.PaySwitchService;
import com.sz7road.web.BaseServlet;
import org.apache.commons.io.IOUtils;
import org.apache.mina.core.IoUtil;
import org.apache.poi.util.StringUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Singleton
public class PaySwitchServlet extends BaseServlet {

    @Inject
    private Provider<PaySwitchService> paySwitchServiceProvider;
    @Inject
    private Provider<ConfigurationService> configurationServiceProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PaySwitchService paySwitchService = paySwitchServiceProvider.get();
        request.setAttribute("map", paySwitchService.getPaySwitchMap());
        request.setAttribute("list", paySwitchService.getPaySwitchList());
        request.getRequestDispatcher("/pay/paySwitch.jsp").forward(request, response);
    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int[] oIds = getIntParameters(request, "oId");
        int[] id = getIntParameters(request, "id");

        Msg msg = new Msg(0, "操作失败");
        try{
            PaySwitchService paySwitchService = paySwitchServiceProvider.get();
            if (id != null && oIds != null) {
                paySwitchService.updateStatus(id, oIds);

                configurationServiceProvider.get().updateTableTimestamp();

                msg.setCode(1);
                msg.setMsg("操作成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printJson(response, msg);
    }

    public void updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = getIntParameter(request, "id");

        Msg msg = new Msg(0, "操作失败");
        try{
            PaySwitchService paySwitchService = paySwitchServiceProvider.get();
            if (id > 0) {
                paySwitchService.updateStatus(id);

                configurationServiceProvider.get().updateTableTimestamp();
                msg.setCode(1);
                msg.setMsg("操作成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printJson(response, msg);
    }

    public int getIntParameter(HttpServletRequest request, String name) {
        if (Strings.isNullOrEmpty(name))
            return 0;

        String value = request.getParameter(name);
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public int[] getIntParameters(HttpServletRequest request, String name) {
        if (Strings.isNullOrEmpty(name))
            return null;

        String[] values = request.getParameterValues(name);
        int[] result = null;
        try {
            if (values != null && values.length > 0) {
                result = new int[values.length];
                for(int i=0; i<values.length; i++){
                    result[i] = Integer.parseInt(values[i]);
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public void printJson(HttpServletResponse response, Object obj){
        PrintWriter out = null;
        try {
            out = response.getWriter();
            objectMapper.writeValue(out, obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }

    }
}
