/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.utils;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author jeremy
 */
public class BackendTest {

    @Test
    public void testGet() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("userName", "hello");
        map.put("passWord", "hello");
        Backend.BackendResponse response =
//                Backend.post("www.badiu.com",map);
                Backend.get("http://www.badiu.com", map);
        if (response.getResponseCode() == 200) {
            System.out.println(response.getResponseContent());
        }
    }
}
