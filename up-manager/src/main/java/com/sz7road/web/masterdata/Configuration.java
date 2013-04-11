package com.sz7road.web.masterdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午4:00
 * 配置文件帮助类
 */
public class Configuration {

    private static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        Properties pro = new Properties();
        try {
            InputStream in = Configuration.class.getResourceAsStream("/properties/masterdata.properties");
            pro.load(in);
            Enumeration<?> keys = pro.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = pro.getProperty(key);
                map.put(key, value);
            }
            in.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("init properties file error!");
        } catch (Exception e) {
            LOGGER.error("init properties file error!");
        }
    }

    public static String getValue(String key) {
        return map.get(key);
    }

    public static void setValue(String key, String value) {
        map.put(key, value);
    }

    public static void main(String[] args) {
        System.out.println(Configuration.getValue("host"));
    }


}
