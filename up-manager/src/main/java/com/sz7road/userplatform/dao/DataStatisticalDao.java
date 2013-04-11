package com.sz7road.userplatform.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-17
 * Time: 下午4:18
 * 数据统计接口
 */
public interface DataStatisticalDao extends Dao<HashMap<String, Object>> {
    /**
     * 拿到一个最基本的数据统计信息，这里就不搞实体了，放到map里算了。
     *
     * @return
     */
    public List<Map<String, Object>> getBasicDataStatistical();

    /**
     * 根据条件拿到统计的数据
     *
     * @param conditions 条件
     * @return 数据
     */
    public List<Map<String, Object>> getDataStatisticalByCustomize(Map<String, Object> conditions);

    /**
     * 根据条件拿到分页的统计的数据
     *
     * @param conditions
     * @return
     */
    public List<Map<String, Object>> getDataStatisticalByCustomizeByPage(Map<String, Object> conditions);

    /**
     * 拿到统计的总数目
     *
     * @param conditions
     * @return
     */
    public int getTotalCount(Map<String, Object> conditions);

}
