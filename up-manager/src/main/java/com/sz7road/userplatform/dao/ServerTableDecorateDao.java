package com.sz7road.userplatform.dao;

import com.sz7road.userplatform.pojo.GameIdAndServerId;
import com.sz7road.userplatform.pojo.ZTreeObject;
import com.sz7road.userplatform.pojos.ServerEntity;
import com.sz7road.userplatform.pojos.ServerTable;
import com.sz7road.userplatform.pojos.ServerTableNotForSq;
import com.sz7road.utils.ListData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-19
 * Time: 上午10:45
 */
public interface ServerTableDecorateDao extends ServerTableDao {
    int updateField(Map<String, Object> fieldValueMap, int id) throws SQLException;

    int batchUpdate(ServerTable.ServerEntry[] entries) throws SQLException;

    ListData<ServerTable.ServerEntry> list(int gameId, String order, int sort, int start, int limit, Map<String, Object> queryMap) throws SQLException;

    public int batchUpdateServerFromAllToStatus(int all, int status) throws SQLException;

    /**
     * 拿到状态不是-1的服务器列表
     *
     * @return
     * @throws SQLException
     */

    ServerTable.ServerEntry getServerEntryById(int id);

    Map<Integer, ServerTable.ServerEntry> getNonCloseServers() throws SQLException;

    Map<Integer, ServerEntity> getNonCloseServerEntities() throws SQLException;

    Map<Integer, ServerEntity> getAllServers();

    int bathUpdateStatus(List<Integer> list, int status) throws SQLException;

    /**
     * 合区功能
     *
     * @param serverNo 主区的服务区编号
     * @param ids      从区的服务区id数组
     * @return
     */
    public boolean mergerServers(String serverNo, int[] ids);

    /**
     * 分区或者取消合区功能
     *
     * @param ids
     * @return
     */
    public boolean splitServers(int[] ids);

    /**
     * 从服务区表和服务区配置表中拿到合区的服务区树形结构数据。
     *
     * @return
     */
    public List<ZTreeObject> getServerTreeDataStruct();

    /**
     * 从服务区表和服务区配置表中拿到合区的主服务区树形结构数据。
     *
     * @return
     */
    public List<ZTreeObject> getServerTreeDataStructOfMain();

    public List<ZTreeObject> getServerTreeDataStructOfCancell();

    public int bathUpdateRecommendFiledBeforeToday() throws SQLException;

    public int addNotForSq(ServerTableNotForSq.ServerEntryNotForSq entry);

    public int batchUpdateNotForSq(ServerTableNotForSq.ServerEntryNotForSq[] entries);

    public ListData<ServerTableNotForSq.ServerEntryNotForSq> listNotForSq(int gameId, String order, int sort, int start, int limit, Map<String, Object> queryMap);

    public int bathUpdateStatusNotForSq(List<Integer> serverIds, int status,int gameId)  throws Exception;

    public int bathUpdateStatusNotForSq(List<Integer> ids, int status)throws Exception;

   public int bathUpdateStatusForMaintain(List<GameIdAndServerId> serverIdListTOMaintain, int status) throws  Exception;
}
