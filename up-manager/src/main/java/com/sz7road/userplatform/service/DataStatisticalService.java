package com.sz7road.userplatform.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.dao.DataStatisticalDao;
import com.sz7road.userplatform.pojo.DataStatisticalObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-17
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class DataStatisticalService {
    @Inject
    private Provider<DataStatisticalDao> dataStatisticalDaoProvider;

    public List<Map<String, Object>> getBasicDataStatistical() {
        DataStatisticalDao dataStatisticalDao = dataStatisticalDaoProvider.get();

        return dataStatisticalDao.getBasicDataStatistical();
    }

    /**
     * 根据查询条件拿到统计的信息，包括表头和数据
     *
     * @param conditions
     * @return
     */
    public DataStatisticalObject getDataStatisticalByCustomize(Map<String, Object> conditions) {

        DataStatisticalObject dataStatisticalInfo = new DataStatisticalObject();

        DataStatisticalDao dataStatisticalDao = dataStatisticalDaoProvider.get();

        List<Map<String, Object>> dataContents = dataStatisticalDao.getDataStatisticalByCustomize(conditions);

        if (dataContents != null && dataContents.size() > 0) {
            dataStatisticalInfo.setMessage("查询统计数据成功！");
            dataStatisticalInfo.setOverAll(getOverAllInfo(conditions));
            dataStatisticalInfo.setHead(getHeads(conditions));
            dataStatisticalInfo.setDataContent(dataContents);
        } else {
            dataStatisticalInfo.setMessage("查询统计数据失败!");
        }
        return dataStatisticalInfo;
    }

    /**
     * 根据查询条件拿到分页的统计的信息，包括表头和数据
     *
     * @param conditions
     * @return
     */
    public DataStatisticalObject getDataStatisticalByCustomizeByPage(Map<String, Object> conditions) {

        DataStatisticalObject dataStatisticalInfo = new DataStatisticalObject();

        DataStatisticalDao dataStatisticalDao = dataStatisticalDaoProvider.get();

        List<Map<String, Object>> dataContents = dataStatisticalDao.getDataStatisticalByCustomizeByPage(conditions);
        int total = dataStatisticalDao.getTotalCount(conditions);
        if (dataContents != null && dataContents.size() > 0) {
            dataStatisticalInfo.setMessage("查询统计数据成功！");
            dataStatisticalInfo.setOverAll(getOverAllInfo(conditions));
            dataStatisticalInfo.setHead(getHeads(conditions));
            dataStatisticalInfo.setDataContent(dataContents);
            dataStatisticalInfo.setTotal(total);
        } else {
            dataStatisticalInfo.setMessage("查询统计数据失败!");
        }
        return dataStatisticalInfo;
    }

    /**
     * 根据条件得到表头
     *
     * @param conditions
     * @return
     */
    private List<String> getHeads(Map<String, Object> conditions) {

        List<String> heads = new ArrayList<String>();
        if (conditions != null && !conditions.isEmpty()) {
            if (conditions.get("gameName") != null && !Strings.isNullOrEmpty(conditions.get("gameName").toString()))
                heads.add("游戏名称");
            if (conditions.get("serverName") != null && !Strings.isNullOrEmpty(conditions.get("serverName").toString()))
                heads.add("服务器名称");
            if (conditions.get("channelId") != null && !Strings.isNullOrEmpty(conditions.get("channelId").toString()))
                heads.add("支付网关");
            if (conditions.get("subType") != null && !Strings.isNullOrEmpty(conditions.get("subType").toString()))
                heads.add("支付方式");
            if (conditions.get("bank") != null && !Strings.isNullOrEmpty(conditions.get("bank").toString()))
                heads.add("支付渠道");
            if (conditions.get("status") != null && !Strings.isNullOrEmpty(conditions.get("status").toString())) {
                String statuHead = null;
                if ("success".equals(conditions.get("status").toString())) statuHead = "支付成功订单数";
                if ("empty".equals(conditions.get("status").toString())) {
                    statuHead = "订单数";
                }
                if ("failed".equals(conditions.get("status").toString())) {
                    statuHead = "支付失败订单数";
                }

                heads.add(statuHead);

            }
//            if (conditions.get("amount") != null && !Strings.isNullOrEmpty(conditions.get("amount").toString()))
            heads.add("充值金额");
            if (conditions.get("gold") != null && !Strings.isNullOrEmpty(conditions.get("gold").toString()))
                heads.add("游戏币");
            if (conditions.get("userAmount") != null && !Strings.isNullOrEmpty(conditions.get("userAmount").toString()))
                heads.add("用户数");
            if (conditions.get("playerAmount") != null && !Strings.isNullOrEmpty(conditions.get("playerAmount").toString()))
                heads.add("玩家数");
            if (conditions.get("statusAmount") != null && !Strings.isNullOrEmpty(conditions.get("statusAmount").toString()))
                heads.add("支付数");
        }
        return heads;
    }

    /**
     * 拿到整体的时间统计信息
     *
     * @param conditions
     * @return
     */
    private String getOverAllInfo(Map<String, Object> conditions) {
        StringBuffer sb = null;
        if (conditions != null && !conditions.isEmpty()) {
            sb = new StringBuffer("以下是");
            if (checkNull("$payStartTime", conditions)) sb.append("提交时间从" + conditions.get("$payStartTime"));

            if (checkNull("$payEndTime", conditions)) sb.append("到" + conditions.get("$payEndTime"));

            if (checkNull("$assertStartTime", conditions)) sb.append("确认时间从" + conditions.get("$assertStartTime"));

            if (checkNull("$assertEndTime", conditions)) sb.append("到" + conditions.get("$assertEndTime"));

            if (!(checkNull("$payStartTime", conditions) || checkNull("$payEndTime", conditions) || checkNull("$assertStartTime", conditions) || checkNull("$assertEndTime", conditions)))
                sb.append("不限时间");
            sb.append("的统计数据");
        }
        return sb.toString();
    }

    /**
     * 检查conditon中是不是含有field对应的值
     *
     * @param field
     * @param conditions
     * @return
     */
    private boolean checkNull(String field, Map<String, Object> conditions) {
        return conditions.get(field) != null && !Strings.isNullOrEmpty(conditions.get(field).toString().trim());
    }

}

