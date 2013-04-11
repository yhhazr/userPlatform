/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.configuration;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author jeremy
 */
@XmlRootElement(name = "beans")
@XmlAccessorType(value = XmlAccessType.NONE)
public class BeanConfigs implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(BeanConfigs.class.getName());

    @XmlElement(name = "bean")
    private List<BeanConfig> beanList = null;

    private Map<String, Bean> beans = Maps.newHashMap();

    void afterUnmarshal(final Unmarshaller u, final Object parent) {
        try {
            if (null != beanList && !beanList.isEmpty()) {
                // 把所有的BeanConfig进行解析、生成，并放入Bean Map。
                for (BeanConfig config : beanList) {
                    beans.put(config.getId(), config);
                }
            }
        } finally {
            beanList = null;
        }
    }

    public Map<String, Bean> getBeans() {
        return beans;
    }

    /**
     * @author jeremy
     */
    @XmlRootElement(name = "bean")
    @XmlAccessorType(value = XmlAccessType.NONE)
    static class BeanConfig implements Bean {

        // 未定义ID的Bean实例的数量索引
        private static int INSTANCE_INDEX = 0;

        // ID不应该为空，在为空时定义随机的ID。
        @XmlAttribute(name = "id")
        private String id;
        @XmlAttribute(name = "name")
        private String name;
        @XmlAttribute(name = "class")
        private Class<? extends SelfMakeBean> clazz;
        @XmlElement(name = "property")
        private List<PropertyObject> propList = null;
        private Properties props = new Properties();

        void afterUnmarshal(final Unmarshaller u, final Object parent) {
            try {
                if (id == null) {
                    // 生成随机ID。
                    INSTANCE_INDEX++;
                    this.id = "bean#" + INSTANCE_INDEX;
                }

                // putting the properties.
                if (null != propList && !propList.isEmpty()) {
                    for (PropertyObject po : propList) {
                        props.put(po.getName(), po.getValue());
                    }
                }
            } finally {
                propList = null;
            }
        }

        @Override
        public Object get(final String name) {
            return props.get(name) == null ? null : props.get(name);
        }

        @Override
        public <T> T get(final String name, final Class<T> classType) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<? extends SelfMakeBean> getWrapClass() {
            return clazz;
        }
    }

    @XmlRootElement(name = "property")
    @XmlAccessorType(value = XmlAccessType.NONE)
    static class PropertyObject {

        @XmlAttribute(name = "name")
        private String name;
        @XmlAttribute(name = "value")
        private String value;
        @XmlAnyElement
        private Object valueObject;

        void afterUnmarshal(final Unmarshaller u, final Object parent) {
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return Strings.isNullOrEmpty(value) ? valueObject : value;
        }
    }
}
