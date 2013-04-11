/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.configuration;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeremy
 */
public class PropertiesConfigBean extends AbstractModule implements SelfMakeBean {

    private static final Logger log = LoggerFactory.getLogger(PropertiesConfigBean.class.getName());

    private String configPath;
    private Includes includes;
    private Excludes excludes;

    @Override
    public SelfMakeBean make(final Bean bean) {
        if (null == bean.get("configPath")) {
            throw new IllegalStateException("没有找到配置的加载路径！即然配置了PropertiesConfigBean就不应不配置属性configPath。");
        }

        configPath = bean.get("configPath").toString();
        if (bean.get("includes") instanceof Node) {
            JAXBContext jabc;
            try {
                jabc = JAXBContext.newInstance(Includes.class);
                final Unmarshaller u = jabc.createUnmarshaller();
                this.includes = (Includes) u.unmarshal((Node) bean.get("includes"));
            } catch (JAXBException e) {
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
            }
            log.info(Arrays.toString(includes.getIncludes().toArray()));
        }
        if (bean.get("excludes") instanceof Node) {
            log.info("Excludes: " + ((Node) bean.get("excludes")).getNodeValue());
        }

        return this;
    }

    @Override
    protected void configure() {
    }

    @XmlAccessorType(value = XmlAccessType.NONE)
    @XmlRootElement(name = "includes")
    static class Includes {

        @XmlElement(name = "include")
        private List<Object> includes = Lists.newArrayList();

        public List<Object> getIncludes() {
            return includes;
        }
    }

    @XmlAccessorType(value = XmlAccessType.NONE)
    @XmlRootElement(name = "excludes")
    static class Excludes {

        @XmlElement(name = "excludes")
        private List<Object> excludes = Lists.newArrayList();

        public List<Object> getExcludes() {
            return excludes;
        }
    }
}
