package com.sz7road.userplatform.playgame.sq;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.sz7road.userplatform.playgame.GenericPlayGameBean;
import com.sz7road.userplatform.playgame.PlayGameManager;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * User: leo.liao
 * Date: 12-6-6
 * Time: 下午4:07
 */
@RequestScoped
public class SqPlayGameBean extends GenericPlayGameBean {
    @Inject
    public SqPlayGameBean(final PlayGameManager playGameManager){
        super(playGameManager);
    }

    @Inject
    @Override
    protected void validate(HttpServletRequest request) {
        super.validate(request);
        String _u = request.getParameter("_u");
        String status = request.getParameter("status");
        //status不等于0则要请求游戏状态
        if(!"0".equals(status) && !Strings.isNullOrEmpty(_u)){
            setUserName(_u);
        }
    }
}
