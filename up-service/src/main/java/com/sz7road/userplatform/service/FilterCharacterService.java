package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sz7road.configuration.FilterCharacterProvider;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.dao.FilterCharacterDao;
import com.sz7road.userplatform.pojos.FilterCharacter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author leo.liao
 */
@Singleton
public final class FilterCharacterService extends Injection implements FilterCharacterProvider {

    private static final Logger log = LoggerFactory.getLogger(FilterCharacterService.class.getName());
    private final ReentrantLock lock = new ReentrantLock();
    private static final Map<String, Object> dirtyConfig = new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors() * 2).makeMap();
    private final static Object OBJ = new Object();

    private FilterCharacterDao filterCharacterDao;

    @Inject
    private FilterCharacterService(FilterCharacterDao filterCharacterDao) {
        this.filterCharacterDao = filterCharacterDao;
        loadFilterCharacter();
    }

    @Inject
    void initialized(ConfigurationService configService) {
        configService.addListener(new ConfigurationService.ConfigurationUpdateListener() {
            @Override
            public void onUpdate(ConfigurationService service) {
                lock.lock();
                try {
                    loadFilterCharacter();
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public void loadFilterCharacter() {
        try {
            List<FilterCharacter> list = filterCharacterDao.listAll();
            dirtyConfig.clear();
            for (FilterCharacter fc : list) {
                dirtyConfig.put(fc.getContent().toLowerCase(), OBJ);
            }
        } catch (SQLException ex) {
            log.error("查询过滤的字符串出现异常:{}", ex.getMessage());
        }
    }

    @Override
    public boolean isContainKey(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return false;
        }
        key = key.toLowerCase();
        if (dirtyConfig.containsKey(key)) {
            return true;
        }
        int len = key.length();
        for (int j = 2; j < len; j++) { //从两个字符的开始拆分
            for (int i = 0; i < len; i++) {
                int end = i + j;
                if (end <= len) {
                    String tmp = word(key, i, end);
                    if (dirtyConfig.containsKey(tmp)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String word(String str, int start, int end) {
        if (!Strings.isNullOrEmpty(str) && start >= 0 && end <= str.length()) {
            String tmp = str.substring(start, end);
            return tmp;
        }
        return "";
    }

    @Override
    public boolean refresh() {
        try {
            loadFilterCharacter();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
