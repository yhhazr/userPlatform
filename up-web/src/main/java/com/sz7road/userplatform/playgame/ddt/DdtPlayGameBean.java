package com.sz7road.userplatform.playgame.ddt;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.playgame.GenericPlayGameBean;
import com.sz7road.userplatform.playgame.PlayGameManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: jiangfan.zhou
 */
@RequestScoped
public class DdtPlayGameBean extends GenericPlayGameBean {

    @Inject
    public DdtPlayGameBean(final PlayGameManager playGameManager) {
        super(playGameManager);
    }

    @Inject
    @Override
    protected void validate(HttpServletRequest request) {
        super.validate(request);
        String _u = request.getParameter("_u");
        String status = request.getParameter("status");
        //status不等于0则要请求游戏状态
        if (!"0".equals(status) && !Strings.isNullOrEmpty(_u)) {
            setUserName(_u);
        }
    }
}
