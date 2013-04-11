package com.sz7road.web.manager;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午11:07
 */
public class SessionManager {

    final static Map<String, Map<String, Map<String, Object>>> sessions = Maps.newHashMap();

    public static void add(String sid, Map<String, Map<String, Object>> user) {
        sessions.put(sid, user);
    }

    public static Map<String, Map<String, Object>> get(String sid) {
        return sessions.get(sid);
    }

    public static void remove(String sid) {
        sessions.remove(sid);
    }
}
