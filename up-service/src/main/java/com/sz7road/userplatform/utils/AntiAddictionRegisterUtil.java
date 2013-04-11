package com.sz7road.userplatform.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.sz7road.utils.Backend;
import com.sz7road.utils.ConfigurationUtils;
import com.sz7road.utils.MD5Utils;
import com.sz7road.web.pojos.AddictionParam;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-14
 * Time: 上午2:27
 * Description:增加用户信息到防沉迷系统的应用类
 */
@Singleton
public class AntiAddictionRegisterUtil {

    //查询防沉迷注册的结果
    private static final int NOT_REGISTER=0; //未注册
    private static final int HAVE_REGISTER=1; //已注册
    private static final int EXCEPTION_REGISTER=-1;//参数不对或者游戏异常返回异常结果
     //注册结果
    private static final int REGISTER_SUCCESS=0; //成功
    private static final int REGISTER_FAILURE=-1; //失败

    private AddictionParam param=null;
    private String addictionUrl= ConfigurationUtils.get("addictionUrl");
    private String sign=null;
    //查询防沉迷信息
    private int queryAddiction()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("op",AddictionParam.QUERY_ADDICTION);
        map.put("userid",param.getUserid());
        map.put("userName",param.getUserName());
        map.put("site",param.getSite());
        map.put("sign",sign);
        Backend.BackendResponse back = Backend.post(addictionUrl, map);
        if (back == null) {
            return EXCEPTION_REGISTER;
        }
        String returnCodeStr = back.getResponseContent();
      return Integer.parseInt(returnCodeStr);
    }
    //增加防沉迷信息
    private int addAddiction()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("op",AddictionParam.ADD_ADDICTION);
        map.put("userid",param.getUserid());
        map.put("userName",param.getUserName());
        map.put("site",param.getSite());
        map.put("cardId",param.getCardId());
        map.put("cardName",param.getCardName());
        map.put("sign",sign);
        Backend.BackendResponse back = Backend.post(addictionUrl, map);
        if (back == null) {
            return EXCEPTION_REGISTER;
        }
        String returnCodeStr = back.getResponseContent();
        return Integer.parseInt(returnCodeStr);
    }
    //加密构造签名
    private String buildSign()
    {
        StringBuffer md5Text=new StringBuffer().append(param.getUserName())
                .append("love7road").append(param.getSite()).append(param.getUserid());
       return MD5Utils.digestAsHex(md5Text.toString());
    }

    public int addAntiAddictionItem(AddictionParam param)
    {
        this.param=param;
        if(param.getUserid()!=0&&!Strings.isNullOrEmpty(param.getUserName())&&!Strings.isNullOrEmpty(param.getSite()))
        {
            sign=buildSign();
        }
        if(queryAddiction()==NOT_REGISTER)
        {
           return addAddiction();
        }
        return REGISTER_FAILURE;
    }
}
