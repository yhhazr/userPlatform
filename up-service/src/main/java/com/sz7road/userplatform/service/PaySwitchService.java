package com.sz7road.userplatform.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.PaySwitchDao;
import com.sz7road.userplatform.pojos.PaySwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jiangfan.zhou
 */

@Singleton
public class PaySwitchService extends Injection {

    private final static Logger log = LoggerFactory.getLogger(PaySwitchService.class.getName());
    private List<PaySwitch> paySwitchList;
    private Map<String, List<PaySwitch>> paySwitchMap;
    private final ReentrantLock lock = new ReentrantLock();

    @Inject
    private Provider<PaySwitchDao> payChannelDaoProvider;
    /*@Inject
    private Provider<SystemVariableDao> systemVariableDaoProvider;*/

    @Inject
    void initialized(ConfigurationService configService) {
        log.info("启动监听器支付渠道");

        configService.addListener(new ConfigurationService.ConfigurationUpdateListener() {
            @Override
            public void onUpdate(ConfigurationService service) {
                reload();
            }
        });
    }

    public PaySwitchDao getPaySwitchDao() {
        PaySwitchDao dao = payChannelDaoProvider.get();
        if (null == dao) {
            throw new NullPointerException("null getPaySwitchDao!");
        }
        return dao;
    }

    public void reload(){
        paySwitchList = null;
        paySwitchMap = null;
    }

    public List<PaySwitch> getPaySwitchList() {
        try {
            paySwitchList = listAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paySwitchList;
    }

    public List<PaySwitch> getPaySwitchListFromCache() {
        if (paySwitchList == null) {
            getPaySwitchList();
        }
        return paySwitchList;
    }

    public Map<String, List<PaySwitch>> getPaySwitchMap() {
        List<PaySwitch> list = null;
        try {
            list = listAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map<String, List<PaySwitch>> map = Maps.newConcurrentMap();
        if (list != null) {
            for (PaySwitch channel : list) {
                if (map.containsKey(channel.getShowType())) {
                    List<PaySwitch> pList = map.get(channel.getShowType());
                    pList.add(channel);
                } else {
                    List<PaySwitch> pList = Lists.newArrayList(channel);
                    map.put(channel.getShowType(), pList);
                }
            }
            paySwitchMap = map;
        }
        return paySwitchMap;
    }

    public Map<String, List<PaySwitch>> getPaySwitchMapFromCache() {
        if (paySwitchMap == null) {
            getPaySwitchMap();
        }
        return paySwitchMap;
    }

    public List<PaySwitch> listAll() throws SQLException {
        return getPaySwitchDao().listAll();
    }

    public PaySwitch get(int id) throws SQLException {
        return getPaySwitchDao().get(id);
    }

    public int add(PaySwitch entity) throws SQLException {
        return getPaySwitchDao().add(entity);
    }

    public int update(PaySwitch entity) throws SQLException {
        return getPaySwitchDao().update(entity);
    }

    public int delete(PaySwitch entity) throws SQLException {
        return getPaySwitchDao().delete(entity);
    }

    public void updateStatus(int id[], int[] ids) throws SQLException {
        if(id.length > 0 && ids.length > 0){
            for(int tid : ids) {
                PaySwitch entity = getPaySwitchDao().get(tid);
                if (entity != null) {
                    entity.setStatus((byte)-1);
                    getPaySwitchDao().update(entity);
                }
            }

            for(int tid : id) {
                PaySwitch entity = getPaySwitchDao().get(tid);
                if (entity != null) {
                    entity.setStatus((byte)0);
                    getPaySwitchDao().update(entity);
                }
            }
        }
    }

    public void updateStatus(int id) throws SQLException {
        PaySwitch entity = getPaySwitchDao().get(id);

        if (entity != null) {
            if (entity.getStatus() == (byte)0) {
                entity.setStatus((byte)-1);
            } else {
                entity.setStatus((byte)0);
            }
            getPaySwitchDao().update(entity);
        }
    }
}
