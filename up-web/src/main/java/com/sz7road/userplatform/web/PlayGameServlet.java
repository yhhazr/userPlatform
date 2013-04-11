package com.sz7road.userplatform.web;

import com.google.inject.Singleton;
import com.sz7road.userplatform.playgame.GenericPlayGameBean;
import com.sz7road.userplatform.playgame.PlayGameHandler;
import com.sz7road.web.Parameter;
import com.sz7road.web.servlet.HeadlessHttpServlet;
import com.sz7road.web.servlet.headless.HeadlessServlet;
import com.sz7road.web.servlet.headless.HeadlessServletRequest;
import com.sz7road.web.servlet.headless.HeadlessServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 下午2:31
 */
@Singleton
@Parameter(value = {"g:","z:", "game:","subGame:"})
class PlayGameServlet extends HeadlessServlet {
    @Override
    protected void doPost(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        GenericPlayGameBean playGameBean = getInstance(GenericPlayGameBean.class);
        if(playGameBean != null){
            PlayGameHandler handler = playGameBean.getHandler();
            if(handler != null){
                handler.process(request,response);
            }
            return;
        }
    }

    @Override
    protected void doGet(HeadlessServletRequest request, HeadlessServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
