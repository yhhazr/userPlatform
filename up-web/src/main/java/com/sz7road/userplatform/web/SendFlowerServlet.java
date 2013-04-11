package com.sz7road.userplatform.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.sz7road.userplatform.pojos.OrderObject;
import com.sz7road.userplatform.pojos.ProductOrder;
import com.sz7road.userplatform.service.PayService;
import com.sz7road.userplatform.service.ProductPayService;
import com.sz7road.userplatform.service.SendFlowerService;
import com.sz7road.utils.Backend.BackendResponse;
import com.sz7road.web.servlet.HeadlessHttpServlet;

@Singleton
public class SendFlowerServlet extends HeadlessHttpServlet
{

    @Inject
    private SendFlowerService sendFlowerService;
    
    @Inject ProductPayService productPayService;
    
    @Inject PayService payService;
    @Override
    protected void doServe(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        String type = request.getParameter("type");
        String orderId = request.getParameter("order");
        BackendResponse resp = null;
        if("buyFlower".equalsIgnoreCase(type)) 
        {
            ProductOrder order = null;
            if(!Strings.isNullOrEmpty(orderId)) 
            {
                order = productPayService.getOrder(orderId);
            }
            resp = sendFlowerService.buyFlower(order);
        }
        else if("charge".equalsIgnoreCase(type)) 
        {
            OrderObject order = payService.getOrder(orderId);
            resp = sendFlowerService.chargeSendFlower(order);
        }
        else
        {
            response.getWriter().write("param error");
        }
        if(resp != null)
        {
            response.getWriter().write("success[status="+resp.getResponseCode()+",content="+resp.getResponseContent()+"]");
        }
        else
        {
            response.getWriter().write("success[status="+408+",content="+"fail"+"]");
        }
        response.getWriter().flush();
        response.getWriter().close();
    }

}
