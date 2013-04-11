/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author jeremy
 */
public final class BeanManager {

    private static final Logger log = LoggerFactory.getLogger(BeanManager.class.getName());
    private static BeanConfigs beans = null;

    public static Bean get(String id) {
        return beans.getBeans().get(id);
    }

    public static Iterator<Bean> beanIterator() {
        return beans.getBeans().values().iterator();
    }

    /**
     * 加载配置。
     */
    public static void load() throws Exception {
        // 默认在当前的类路径下找beans-config.xml文件。
        final InputStream in = BeanManager.class.getClassLoader().getResourceAsStream("beans-config.xml");
        try {
            if (null != in && in.available() > 0) {
                // 加载配置
                JAXBContext context = JAXBContext.newInstance(BeanConfigs.class);
                final Unmarshaller unmarshaller = context.createUnmarshaller();
                final Object resultObject = unmarshaller.unmarshal(in);
                if (null != resultObject && resultObject instanceof BeanConfigs) {
                    beans = (BeanConfigs) resultObject;
                }
            }
        } catch (final Exception e) {
            log.error("初始化Bean配置管理异常：{}", e.getMessage());
            throw e;
        } finally {
            if (null != in)
                try {
                    in.close();
                } catch (final IOException e) {
                    // nothing to do .
                }
        }
    }
}
