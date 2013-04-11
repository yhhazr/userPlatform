package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojos.Msg;
import com.sz7road.userplatform.pojos.ServerMaintain;
import com.sz7road.userplatform.pojos.ServerMaintain2;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * User: leo.liao
 * Date: 12-7-9
 * Time: 下午12:32
 */
public interface ServerMaintainDecorateDao {
    /**
     * 批量增加维护服务区
     *
     * @param entitys 维护服务区列表
     * @return 结果
     * @throws SQLException
     */
    public Msg addMaintainServer(List<ServerMaintain> entitys) throws SQLException;

    /**
     * 拿到到时间的维护列表,即已经维护结束的的维护信息
     *
     * @param current
     * @return
     */
    public List<ServerMaintain> getMaintainClosedServerFromTime(Timestamp current);

    /**
     * 拿到正在维护和即将维护的维护信息
     * @param current
     * @return
     */
    public List<ServerMaintain> getMaintainingServerFromTime(Timestamp current);

    /**
     * 批量修改数据的信息   信息都放在了对象里面
     *
     * @param maintainInfo 信息封装在这个对象里
     * @return
     * @throws java.sql.SQLException
     */

    public Msg batchUpdateMaintainServers(ServerMaintain maintainInfo, List<Integer> ids) throws SQLException;

    public Msg batchUpdateMaintainServersNotForSq(ServerMaintain2 maintainInfo, List<Integer> ids) throws SQLException;


    /**
     * 批量删除还没到时间的维护信息
     * @param serverIdArray 服务器id数组
     * @return
     */
   public   Msg batchDeleteFutureMaintainInfo(int[] serverIdArray);

   public Msg batchDeleteFutureMaintainInfoByGameId(int[] serverIdArray, int gameId);

   public   Msg addMaintainServerNotForSq(List<ServerMaintain2> needMaintainServers) throws Exception ;

   public Msg batchUpdateMaintainServersNotForSqById(ServerMaintain2 maintainInfo, List<Integer> ids)  throws SQLException;
}
