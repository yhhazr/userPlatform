package com.sz7road.web.action;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.cachedao.CacheServerMaintain2Dao;
import com.sz7road.userplatform.service.CacheDataItemsService;
import com.sz7road.web.BaseServlet;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-10-30
 * Time: 上午6:53
 * Description:
 */
@Singleton
public class TestServerMaintainCacheServlet  extends BaseServlet {

    @Inject
    private Provider<CacheDataItemsService> cacheDataItemsServiceProvider;

    @Inject
     private Provider<CacheServerMaintain2Dao> cacheMaintainDaoProvider;
    ObjectMapper mapper = new ObjectMapper();

    //测试维护信息的缓存
    public void testCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CacheServerMaintain2Dao cacheMaintainDao= cacheMaintainDaoProvider.get();
        try {
            String type = request.getParameter("type");
            if ("reload".equals(type)) {
//                boolean flag= cacheDataItemsServiceProvider.get().reloadCacheDataOfServerMaintainEntries();
//                printTowebPage(response, flag);
            } else if ("get".equals(type)) {
//                printTowebPage(response,cacheMaintainDao.queryCacheItems(CacheDataItemsService.CACHE_MAINTAINS_KEY));
            } else if ("getOne".equals(type)) {
                String id = request.getParameter("mapKey");
//                printTowebPage(response,cacheMaintainDao.querySingleCacheItem(CacheDataItemsService.CACHE_MAINTAINS_KEY, id));
            }
            else if ("updateOne".equals(type)) {
                String entry=new String(request.getParameter("entry").getBytes("ISO8859_1"),"utf8");
                String id=request.getParameter("mapKey");
//                printTowebPage(response, cacheMaintainDao.updateSingleCacheDataItem(mapper.readValue(entry,ServerMaintain.class), CacheDataItemsService.CACHE_MAINTAINS_KEY));
            }
//            else if ("update".equals(type)) {
//                String entry=request.getParameter("entry");
//                String id=request.getParameter("mapKey");
//                printTowebPage(response,cacheMaintainDao.batchUpdateCacheDataItems(mapper.readValue(entry,Collection<ServerTable.ServerEntry>class),CacheDataItemsService.CACHE_MAINTAINS_KEY));
//            }
            else if ("delOne".equals(type)) {
                String id = request.getParameter("mapKey");
//                printTowebPage(response, cacheMaintainDao.deleteSingleCacheDataItem(id, CacheDataItemsService.CACHE_MAINTAINS_KEY));
            }
//            else if("del".equals(type))
//            {
//                printTowebPage(response,"清空缓存数据之后的结果："+ JedisUtil.clearJedisDataByKey(CacheDataItemsService.CACHE_MAINTAINS_KEY));
//            }
        } catch (Exception ex) {
            log.info("测试缓存异常!");
            ex.printStackTrace();
        }
    }

    public void printTowebPage(HttpServletResponse response, Object serverEntrySet) {
        PrintWriter out = null;
        response.setContentType("application/json");
        try {
            out = response.getWriter();

            mapper.writeValue(out, serverEntrySet);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            out.flush();
            out.close();
        }
    }

}
