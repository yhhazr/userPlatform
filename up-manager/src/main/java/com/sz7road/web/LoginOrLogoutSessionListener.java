package com.sz7road.web;

import com.sz7road.web.manager.SessionManager;
import com.sz7road.web.pojos.User;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-12
 * Time: 上午11:19
 */
public class LoginOrLogoutSessionListener implements HttpSessionBindingListener {

    String sid;
    User user;
    Map<String, Map<String, Object>> masterUser;

    public LoginOrLogoutSessionListener(String sid, Map<String, Map<String, Object>> masterUser) {
        this.sid = sid;
        this.masterUser = masterUser;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        SessionManager.add(sid, masterUser);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LoginOrLogoutSessionListener session = (LoginOrLogoutSessionListener) event.getValue();
        if (session != null) {
            SessionManager.remove(session.sid);
        }
    }
}
