/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import com.sz7road.configuration.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 配置工具类，用于获取系统中的配置。
 *
 * @author jeremy
 */
public class ConfigurationUtils {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationUtils.class.getName());

    @Inject
    private static ConfigurationUtils instance;

    private ConfigurationProvider provider;

    @Inject
    private ConfigurationUtils(Provider<ConfigurationProvider> provider) {
        super();
        this.provider = provider.get();
    }

    /**
     * Get the value with the specified key from the configuration.
     *
     * @param key the specified key
     * @return value
     */
    public static String get(String key) {
        if (instance.provider.containsKey(key)) {
            final Object value = instance.provider.get(key);
            return value == null ? null : value.toString();
        }
        return null;
    }
}
