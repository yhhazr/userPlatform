package com.sz7road.userplatform.dao;

import com.google.common.collect.Table;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.pojos.ServerMaintain2;

import java.sql.Timestamp;
import java.util.List;

/**
 * User: leo.liao
 * Date: 12-7-9
 * Time: 下午12:32
 */
public interface ServerMaintainDao extends Dao<ServerMaintain> {
    /**
     * 拿到正在维护的维护信息列表
     * @param current
     * @return
     */
    public List<ServerMaintain> getMaintainServerFromTime(Timestamp current);


  public   List<ServerMaintain> getMaintainServerFromTimeAll(Timestamp current);

    /**************************多游戏的维护接口 不影响神曲*********************************/



    public   List<ServerMaintain2> getMaintainServerFromTimeAndGameId(Timestamp current,int gameId);

    public Table<Integer,Integer,ServerMaintain2> getMaintainServerFromTime2(Timestamp current);
}
