package com.sz7road.userplatform.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.ServerTableDao;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.utils.ServerInfoUtils;
import com.sz7road.web.pojos.MergerInfoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Version Information: Copyright© 2009 www.7road.com All rights reserved.
 * User: cutter.li
 * Date: 12-11-29
 * Time: 上午8:10
 * Description:合服信息服务类
 */
@Singleton
public class MergerService extends Injection {

    private final static Logger log = LoggerFactory.getLogger(MergerService.class);
    @Inject
    private Provider<ServerTableDao> serverTableDaoProvider;

    public List<MergerInfoBean> getCurrentMergerSortedList() {
        List<MergerInfoBean> mergerInfoBeanList = new LinkedList<MergerInfoBean>();
        try {
            //1，从缓存中得到服务器数据和服务器的配置数据
             Collection<ServerTable.ServerEntry> serverEntries = getInstance(GameWithServerService.class).getGameTable().getServerTable(1).values();
            if (serverEntries != null && !serverEntries.isEmpty()) {
                final Map<Integer, Integer> idAndNoServerMap = ServerInfoUtils.getIdAndNoServerMap(serverEntries);
                for (ServerTable.ServerEntry serverEntry : serverEntries) {
                    if(serverEntry.getGameId()==1&&(serverEntry.getServerStatus()==1||serverEntry.getServerStatus()==-2))
                    {
                    MergerInfoBean mergerInfoBean = new MergerInfoBean();
                    mergerInfoBean.setServerId(serverEntry.getId());
                    mergerInfoBean.setServerNo(serverEntry.getServerNo());
                    mergerInfoBean.setServerName(serverEntry.getServerName());
                    //2,得到合服信息列表
                    int mainServerNo = ServerInfoUtils.getMainServerNo(serverEntry);
                    int mainServerId = 0;
                    if (mainServerNo != 0) {
                        if (idAndNoServerMap != null && idAndNoServerMap.containsKey(mainServerNo)) {
                            mainServerId = idAndNoServerMap.get(mainServerNo);
                        } else {
                            mainServerId = 0;
                        }
                    } else {
                        mainServerNo = serverEntry.getServerNo();
                        mainServerId = serverEntry.getId();
                    }
                    mergerInfoBean.setMainServerId(mainServerId);
                    mergerInfoBean.setMainServerNo(mainServerNo);
                    mergerInfoBeanList.add(mergerInfoBean);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("获取合服信息异常!" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            Collections.sort(mergerInfoBeanList,new Comparator<MergerInfoBean>() {
                @Override
                public int compare(MergerInfoBean o1, MergerInfoBean o2) {
                    return o1.getServerNo()-o2.getServerNo();
                }
            });
            return mergerInfoBeanList;
        }
    }

    public List<MergerInfoBean> list(int gameId) {
        List<MergerInfoBean> mergerInfoBeanList = new LinkedList<MergerInfoBean>();
        try {
            //1，得到服务器数据和服务器的配置数据
            List<ServerTable.ServerEntry> serverEntries = serverTableDaoProvider.get().listsWithStatus();
            if (serverEntries != null && !serverEntries.isEmpty()) {
                final Map<Integer, Integer> idAndNoServerMap = ServerInfoUtils.getIdAndNoServerMap(serverEntries);
                for (ServerTable.ServerEntry serverEntry : serverEntries) {
                    if(serverEntry.getGameId()==gameId) {
                        MergerInfoBean mergerInfoBean = new MergerInfoBean();
                        mergerInfoBean.setServerId(serverEntry.getId());
                        mergerInfoBean.setServerNo(serverEntry.getServerNo());
                        mergerInfoBean.setServerName(serverEntry.getServerName());
                        //2,得到合服信息列表
                        int mainServerNo = ServerInfoUtils.getMainServerNo(serverEntry);
                        int mainServerId = 0;
                        if (mainServerNo != 0) {
                            if (idAndNoServerMap != null && idAndNoServerMap.containsKey(mainServerNo)) {
                                mainServerId = idAndNoServerMap.get(mainServerNo);
                            } else {
                                mainServerId = 0;
                            }
                        } else {
                            mainServerNo = serverEntry.getServerNo();
                            mainServerId = serverEntry.getId();
                        }
                        mergerInfoBean.setMainServerId(mainServerId);
                        mergerInfoBean.setMainServerNo(mainServerNo);
                        mergerInfoBeanList.add(mergerInfoBean);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("获取合服信息异常!" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            return mergerInfoBeanList;
        }
    }
}
