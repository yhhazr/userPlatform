/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.configuration;

import com.google.common.base.Strings;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

/**
 * @author jeremy
 */
@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "props")
public class Props implements Serializable {

    @XmlAccessorType(value = XmlAccessType.NONE)
    @XmlRootElement(name = "prop")
    static class Prop implements Serializable {

        @XmlAttribute(name = "key", required = true)
        private String key;
        @XmlValue
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    @XmlElement(name = "prop")
    private List<Prop> list = null;
    @XmlTransient
    private Properties props = new Properties();

    void afterUnmarshal(final Unmarshaller u, final Object parent) {
        if (null != list && !list.isEmpty()) {
            for (Prop p : list) {
                String key = p.getKey();
                String value = p.getValue();
                if (!Strings.isNullOrEmpty(key))
                    props.put(key, value);
            }
        }
    }

    public Properties getProperties() {
        return props;
    }
}
