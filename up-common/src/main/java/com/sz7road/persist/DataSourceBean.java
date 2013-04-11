/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.persist;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.sz7road.configuration.Bean;
import com.sz7road.configuration.Props;
import com.sz7road.configuration.SelfMakeBean;
import com.sz7road.utils.DesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.Properties;

/**
 * @author jeremy
 */
public class DataSourceBean extends AbstractModule implements SelfMakeBean {

    private static final Logger log = LoggerFactory.getLogger(DataSourceBean.class.getName());

    Properties dsp;
    BoneCPConfig config;
    Bean bean;
    DataSource dataSource;

    @Override
    public SelfMakeBean make(Bean bean) {
        this.bean = bean;
        // just make the dataSource properties.
        Object dataSourceProperties = bean.get("dataSourceProperties");
        if (dataSourceProperties != null && dataSourceProperties instanceof Node) {
            Node node = (Node) dataSourceProperties;
            JAXBContext jabc;
            try {
                jabc = JAXBContext.newInstance(Props.class);
                final Unmarshaller u = jabc.createUnmarshaller();
                final Props o = (Props) u.unmarshal(node);
                dsp = o.getProperties();

                // filters the properties.
                String jdbcUrl = dsp.containsKey("jdbcUrl") ? dsp.get("jdbcUrl").toString() : null;
                String username = dsp.containsKey("username") ? dsp.get("username").toString() : null;
                String password = dsp.containsKey("password") ? dsp.get("password").toString() : null;

                if (!Strings.isNullOrEmpty(jdbcUrl)) {
                    dsp.setProperty("jdbcUrl", DesUtils.decrypt(jdbcUrl));
                }

                if (!Strings.isNullOrEmpty(username)) {
                    dsp.setProperty("username", DesUtils.decrypt(username));
                }

                if (!Strings.isNullOrEmpty(password)) {
                    dsp.setProperty("password", DesUtils.decrypt(password));
                }

                config = new BoneCPConfig(dsp);
            } catch (JAXBException e) {
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return this;
    }

    @Override
    protected void configure() {
        // DataSource providered.
        bind(DataSource.class).annotatedWith(Names.named(bean.getId())).toInstance(getDataSource());
        if (bean.getId().equals("default")) {
            bind(DataSource.class).toInstance(getDataSource());
        }
    }

    private DataSource getDataSource() {
        if (null == dataSource) {
            dataSource = new BoneCPDataSource(config);
        }
        return dataSource;
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(DesUtils.encrypt("jdbc:mysql://127.0.0.1:2433/db_userplatform?characterEncoding=utf8"));
//        System.out.println(DesUtils.encrypt("root"));
//        System.out.println(DesUtils.encrypt("7roaddba"));
//
//        System.out.println(DesUtils.encrypt("jdbc:mysql://127.0.0.1:2433/log_userplatform?characterEncoding=utf8"));
//        System.out.println(DesUtils.encrypt("root"));
//        System.out.println(DesUtils.encrypt("7roaddba"));

//        System.out.println(DesUtils.encrypt("jdbc:mysql://192.168.1.10:2433/db_userplatform?characterEncoding=utf8"));
//        System.out.println(DesUtils.encrypt("root"));
//        System.out.println(DesUtils.encrypt("jeremytest"));
//
//        System.out.println(DesUtils.encrypt("jdbc:mysql://192.168.1.10:2433/log_userplatform?characterEncoding=utf8"));
//        System.out.println(DesUtils.encrypt("root"));
//        System.out.println(DesUtils.encrypt("jeremytest"));

//        System.out.println(DesUtils.encrypt("jdbc:mysql://10.10.4.26:3306/db_secureshell?characterEncoding=utf8"));
//        System.out.println(DesUtils.encrypt("root"));
//        System.out.println(DesUtils.encrypt("mysql"));
        System.out.println(DesUtils.encrypt("jdbc:mysql://127.0.0.1:2433/log_userplatform?characterEncoding=utf8"));
        System.out.println(DesUtils.encrypt("up-program"));
        System.out.println(DesUtils.encrypt("MMO!J@v#r3dbME"));


        System.out.println("url: "+DesUtils.encrypt("jdbc:mysql://127.0.0.1:2433/db_userplatform?characterEncoding=utf8"));
        System.out.println("userName: "+DesUtils.encrypt("up-program"));
        System.out.println("password: "+DesUtils.encrypt("MMO!J@v#r3dbME"));

        System.out.println("url: "+DesUtils.decrypt("SPmS+WcH7TnWVhkdstPdSGdSVI6Wm9LLXW+8pMt8qhzDivbpcoiiGND0D9bpXFFL"));
        System.out.println("userName: "+DesUtils.decrypt("EFn3m/PtYwkZa+wLrswTig=="));
        System.out.println("password: "+DesUtils.decrypt("7t0acRAu5T1L9ln7EyHzXA=="));

    }
}
