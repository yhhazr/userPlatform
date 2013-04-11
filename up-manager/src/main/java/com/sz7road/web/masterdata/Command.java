package com.sz7road.web.masterdata;

import org.apache.mina.core.session.IoSession;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-6
 * Time: 下午3:35
 * 命令接口
 */
public interface Command {

    public void readPacket(IoSession session, Object message);

    public void writePacket(IoSession session);

    public void setUpdateHandler(ListenerEvent event);

    public void setPassword(String password);

    public void setUserName(String userName);

    public String getPassword();

    public String getUserName();

    public Map<String, Map<String, Object>> getMasterDataObject();

}
