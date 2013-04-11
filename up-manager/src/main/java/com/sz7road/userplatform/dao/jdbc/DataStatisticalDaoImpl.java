package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.sz7road.userplatform.dao.DataStatisticalDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojo.OrderViewObject;
import com.sz7road.utils.VerifyFormItem;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-7-17
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class DataStatisticalDaoImpl extends JdbcDaoSupport<HashMap<String, Object>> implements DataStatisticalDao {

    static final String VIEW = "`db_usermanager`.`v_orderview`";
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    /**
     * 拿到一个最基本的数据统计信息，这里就不搞实体了，放到map里算了。
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getBasicDataStatistical() {

        List<Map<String, Object>> dataStatistics = new ArrayList<Map<String, Object>>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = " select DISTINCT channelName ,sum(amount) as sumMoney ," +
                    " sum(gold) as  gameGoldCount , COUNT(orderId) as  orderCount , count( DISTINCT userId) as userCount ,   count( DISTINCT playerId) as playerCount from " + VIEW +
                    " where status=1  group by channelName";

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> dataStatistic = new HashMap<String, Object>();
                dataStatistic.put("channelName", rs.getString("channelName"));
                dataStatistic.put("sumMoney", rs.getFloat("sumMoney"));
                dataStatistic.put("gameGoldCount", rs.getFloat("gameGoldCount"));
                dataStatistic.put("orderCount", rs.getInt("orderCount"));
                dataStatistic.put("userCount", rs.getInt("userCount"));
                dataStatistic.put("playerCount", rs.getInt("playerCount"));
                dataStatistics.add(dataStatistic);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return dataStatistics;


    }

    /**
     * 根据条件拿到统计数据
     *
     * @param conditions 条件
     * @return
     */
    @Override
    public List<Map<String, Object>> getDataStatisticalByCustomize(Map<String, Object> conditions) {
        List<Map<String, Object>> dataStatistics = new ArrayList<Map<String, Object>>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = transToSql(conditions);

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            //根据条件组装数据
            while (rs.next()) {
                Map<String, Object> dataStatistic = new HashMap<String, Object>();

                if (conditions.get("gameName") != null && !Strings.isNullOrEmpty(conditions.get("gameName").toString())) {
                    dataStatistic.put("gameName", rs.getString("gameName"));
                }

                if (conditions.get("serverName") != null && !Strings.isNullOrEmpty(conditions.get("serverName").toString())) {
                    dataStatistic.put("serverName", rs.getString("serverName"));
                }
                if (conditions.get("channelId") != null && !Strings.isNullOrEmpty(conditions.get("channelId").toString())) {
                    dataStatistic.put("channelName", rs.getString("channelName"));
                }
                if (conditions.get("subType") != null && !Strings.isNullOrEmpty(conditions.get("subType").toString())) {
                    dataStatistic.put("subTypeName", rs.getString("subTypeName"));
                }
                if (conditions.get("bank") != null && !Strings.isNullOrEmpty(conditions.get("bank").toString())) {
                    dataStatistic.put("subTagName", rs.getString("subTagName").length() == 0 ? "无" : rs.getString("subTagName"));
                }
                if (conditions.get("status") != null && !Strings.isNullOrEmpty(conditions.get("status").toString())) {
                    dataStatistic.put("status", rs.getInt("okOrderNum"));
                }
                if (conditions.get("userAmount") != null && !Strings.isNullOrEmpty(conditions.get("userAmount").toString())) {
                    dataStatistic.put("userSum", rs.getString("userSum"));
                }
                if (conditions.get("playerAmount") != null && !Strings.isNullOrEmpty(conditions.get("playerAmount").toString())) {
                    dataStatistic.put("playerSum", rs.getString("playerSum"));
                }
                dataStatistic.put("sumMoney", rs.getFloat("sumMoney"));
                if (conditions.get("gold") != null && !Strings.isNullOrEmpty(conditions.get("gold").toString())) {
                    dataStatistic.put("goldSum", rs.getFloat("goldSum"));
                }
                dataStatistics.add(dataStatistic);
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return dataStatistics;

    }

    public List<Map<String, Object>> getDataStatisticalByCustomizeByPage(Map<String, Object> conditions) {
        List<Map<String, Object>> dataStatistics = new ArrayList<Map<String, Object>>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = transToSql(conditions);

            int start = 0;
            int count = 0;
            if (conditions.get("page") != null && !Strings.isNullOrEmpty(conditions.get("page").toString()) &&
                    VerifyFormItem.isInteger(conditions.get("page").toString())) {
                count = Integer.parseInt(conditions.get("page").toString());
            }
            if (conditions.get("pageCount") != null && !Strings.isNullOrEmpty(conditions.get("pageCount").toString()) &&
                    VerifyFormItem.isInteger(conditions.get("pageCount").toString())) {

                start = (count - 1) * Integer.parseInt(conditions.get("pageCount").toString());
                count = Integer.parseInt(conditions.get("pageCount").toString());
            } else {
                start = 0;
                count = 15;
            }
            sql += " limit " + start + " , " + count;
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            //根据条件组装数据
            while (rs.next()) {
                Map<String, Object> dataStatistic = new HashMap<String, Object>();

                if (conditions.get("gameName") != null && !Strings.isNullOrEmpty(conditions.get("gameName").toString())) {
                    dataStatistic.put("gameName", rs.getString("gameName"));
                }

                if (conditions.get("serverName") != null && !Strings.isNullOrEmpty(conditions.get("serverName").toString())) {
                    dataStatistic.put("serverName", rs.getString("serverName"));
                }
                if (conditions.get("channelId") != null && !Strings.isNullOrEmpty(conditions.get("channelId").toString())) {
                    dataStatistic.put("channelName", rs.getString("channelName"));
                }
                if (conditions.get("subType") != null && !Strings.isNullOrEmpty(conditions.get("subType").toString())) {
                    dataStatistic.put("subTypeName", rs.getString("subTypeName"));
                }
                if (conditions.get("bank") != null && !Strings.isNullOrEmpty(conditions.get("bank").toString())) {
                    dataStatistic.put("subTagName", rs.getString("subTagName").length() == 0 ? "无" : rs.getString("subTagName"));
                }
                if (conditions.get("status") != null && !Strings.isNullOrEmpty(conditions.get("status").toString())) {
                    dataStatistic.put("status", rs.getInt("okOrderNum"));
                }
                if (conditions.get("userAmount") != null && !Strings.isNullOrEmpty(conditions.get("userAmount").toString())) {
                    dataStatistic.put("userSum", rs.getString("userSum"));
                }
                if (conditions.get("playerAmount") != null && !Strings.isNullOrEmpty(conditions.get("playerAmount").toString())) {
                    dataStatistic.put("playerSum", rs.getString("playerSum"));
                }
//                if (conditions.get("amount") != null && !Strings.isNullOrEmpty(conditions.get("amount").toString())) {
                dataStatistic.put("sumMoney", rs.getFloat("sumMoney"));
//                }
                if (conditions.get("gold") != null && !Strings.isNullOrEmpty(conditions.get("gold").toString())) {
                    dataStatistic.put("goldSum", rs.getFloat("goldSum"));
                }
                dataStatistics.add(dataStatistic);
            }
        } catch (final SQLException e) {
            log.error("查询订单统计数据异常!");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return dataStatistics;

    }

    /**
     * @return
     */
    public int getTotalCount(Map<String, Object> conditions) {
        int total = 0;
        try {
            final String gameId=conditions.get("gameName").toString();
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = transToSqlForCount(conditions);

            StringBuffer sb = new StringBuffer(" select count(od.id) " +(gameId.equals("神曲")?SqlConfig.ORDER_STATISTIC_SQ:SqlConfig.ORDER_STATISTIC_NOTFORSQ)  + sql);
            stmt = conn.prepareStatement(sb.toString());

            ResultSet rs = stmt.executeQuery();

            rs.getMetaData().getColumnCount();

            rs.last();

            total = rs.getRow();

        } catch (final Exception ex) {
            log.error("查询统计数据数目异常!");
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return total;

    }


    /**
     * 把条件转换成sql
     *
     * @param conditions
     * @return
     */
    private String transToSql(Map<String, Object> conditions) {
        //select 条件
        StringBuffer stringBuffer = new StringBuffer(" select distinct ");
        //where 条件
        StringBuffer whereBuffer = new StringBuffer();
        // group条件
        StringBuffer groupBuffer = new StringBuffer();


        if (conditions != null && !conditions.isEmpty()) {

            if (conditions.get("$payStartTime") != null && !Strings.isNullOrEmpty(conditions.get("$payStartTime").toString())) {
                whereBuffer.append(" od.payTime >= '").append(conditions.get("$payStartTime").toString()).append("' and");
            }

            if (conditions.get("$payEndTime") != null && !Strings.isNullOrEmpty(conditions.get("$payEndTime").toString())) {
                whereBuffer.append(" od.payTime <='").append(conditions.get("$payEndTime").toString()).append("' and");
            }

            if (conditions.get("$assertStartTime") != null && !Strings.isNullOrEmpty(conditions.get("$assertStartTime").toString())) {
                whereBuffer.append(" od.assertTime >='").append(conditions.get("$assertStartTime").toString()).append("' and");
            }

            if (conditions.get("$assertEndTime") != null && !Strings.isNullOrEmpty(conditions.get("$assertEndTime").toString())) {
                whereBuffer.append(" od.assertTime <='").append(conditions.get("$assertEndTime").toString()).append("' and");
            }

            if (conditions.get("gameName") != null && !Strings.isNullOrEmpty(conditions.get("gameName").toString())) {
                stringBuffer.append(" gt.gameName ,");
                if (!"all".equals(conditions.get("gameName").toString())) {
                    whereBuffer.append(" gt.gameName = '").append(conditions.get("gameName").toString()).append("' and");
                }
                groupBuffer.append(" gt.gameName ,");

            }

            if (conditions.get("serverName") != null && !Strings.isNullOrEmpty(conditions.get("serverName").toString())) {
                stringBuffer.append(" st.serverName ,");
                if (!"all".equals(conditions.get("serverName").toString())) {
                    whereBuffer.append(" st.serverName = '").append(conditions.get("serverName").toString()).append("' and");
                }
                groupBuffer.append(" st.serverName,");
            }
            if (conditions.get("channelId") != null && !Strings.isNullOrEmpty(conditions.get("channelId").toString())) {
                stringBuffer.append(" pt.channelName ,");
                if (!"all".equals(conditions.get("channelId").toString())) {
                    whereBuffer.append(" pt.channelName = '").append(conditions.get("channelId").toString()).append("' and");
                }
                groupBuffer.append(" pt.channelName,");
            }
            if (conditions.get("subType") != null && !Strings.isNullOrEmpty(conditions.get("subType").toString())) {
                stringBuffer.append(" pt.subTypeName ,");
                if (!"all".equals(conditions.get("subType").toString())) {
                    whereBuffer.append(" pt.subTypeName = '").append(conditions.get("subType").toString()).append("' and");
                }
                groupBuffer.append(" pt.subTypeName,");

            }
            if (conditions.get("bank") != null && !Strings.isNullOrEmpty(conditions.get("bank").toString())) {
                stringBuffer.append(" pt.subTagName ,");
                if (!"all".equals(conditions.get("bank").toString())) {
                    whereBuffer.append(" pt.subTagName = '").append(conditions.get("bank").toString()).append("' and");
                }
                groupBuffer.append(" pt.subTagName,");
            }

            if (conditions.get("status") != null && !Strings.isNullOrEmpty(conditions.get("status").toString())) {
                stringBuffer.append(" count(od.id) as okOrderNum ,");
                if (!"empty".equals(conditions.get("status").toString())) {
                    whereBuffer.append(" od.status = ").append("success".equals(conditions.get("status").toString()) ? 1 : 0).
                            append(" and");
                }
            }
            if (conditions.get("gold") != null && !Strings.isNullOrEmpty(conditions.get("gold").toString()))
                stringBuffer.append(" sum(od.gold) as goldSum ,");
            if (conditions.get("userAmount") != null && !Strings.isNullOrEmpty(conditions.get("userAmount").toString()))
                stringBuffer.append(" count(DISTINCT od.userId) as userSum ,");
            if (conditions.get("playerAmount") != null && !Strings.isNullOrEmpty(conditions.get("playerAmount").toString()))
                stringBuffer.append(" count(DISTINCT od.playerId) as playerSum ,");

            stringBuffer.append("sum(od.amount) as sumMoney ,");

        }
        ////去掉末尾的and 或者逗号 然后拼接sql
        final String gameId=conditions.get("gameName").toString();
        StringBuffer selectBf = new StringBuffer(trimAndorComma(stringBuffer)).append(gameId.equals("神曲")?SqlConfig.ORDER_STATISTIC_SQ:SqlConfig.ORDER_STATISTIC_NOTFORSQ);
        if (!Strings.isNullOrEmpty(whereBuffer.toString()))
            selectBf.append(" where ").append(trimAndorComma(whereBuffer));
        if (!Strings.isNullOrEmpty(groupBuffer.toString()))
            selectBf.append(" group by ").append(trimAndorComma(groupBuffer)).toString();
        String sql = selectBf.toString();
//        log.info("统计的sql是：" + sql);
        return sql;
    }


    /**
     * 把条件转换成sql
     *
     * @param conditions
     * @return
     */
    private String transToSqlForCount(Map<String, Object> conditions) {

        //where 条件
        StringBuffer whereBuffer = new StringBuffer();
        // group条件
        StringBuffer groupBuffer = new StringBuffer();


        if (conditions != null && !conditions.isEmpty()) {

            if (conditions.get("$payStartTime") != null && !Strings.isNullOrEmpty(conditions.get("$payStartTime").toString())) {
                whereBuffer.append(" od.payTime >= '").append(conditions.get("$payStartTime").toString()).append("' and");
            }

            if (conditions.get("$payEndTime") != null && !Strings.isNullOrEmpty(conditions.get("$payEndTime").toString())) {
                whereBuffer.append(" od.payTime <='").append(conditions.get("$payEndTime").toString()).append("' and");
            }

            if (conditions.get("$assertStartTime") != null && !Strings.isNullOrEmpty(conditions.get("$assertStartTime").toString())) {
                whereBuffer.append(" od.assertTime >='").append(conditions.get("$assertStartTime").toString()).append("' and");
            }

            if (conditions.get("$assertEndTime") != null && !Strings.isNullOrEmpty(conditions.get("$assertEndTime").toString())) {
                whereBuffer.append(" od.assertTime <='").append(conditions.get("$assertEndTime").toString()).append("' and");
            }

            if (conditions.get("gameName") != null && !Strings.isNullOrEmpty(conditions.get("gameName").toString())) {
                if (!"all".equals(conditions.get("gameName").toString())) {
                    whereBuffer.append(" gt.gameName = '").append(conditions.get("gameName").toString()).append("' and");
                }
                groupBuffer.append(" gt.gameName ,");

            }

            if (conditions.get("serverName") != null && !Strings.isNullOrEmpty(conditions.get("serverName").toString())) {
                if (!"all".equals(conditions.get("serverName").toString())) {
                    whereBuffer.append(" st.serverName = '").append(conditions.get("serverName").toString()).append("' and");
                }
                groupBuffer.append(" st.serverName,");
            }
            if (conditions.get("channelId") != null && !Strings.isNullOrEmpty(conditions.get("channelId").toString())) {
                if (!"all".equals(conditions.get("channelId").toString())) {
                    whereBuffer.append("  pt.channelName = '").append(conditions.get("channelId").toString()).append("' and");
                }
                groupBuffer.append("  pt.channelName,");
            }
            if (conditions.get("subType") != null && !Strings.isNullOrEmpty(conditions.get("subType").toString())) {
                if (!"all".equals(conditions.get("subType").toString())) {
                    whereBuffer.append(" pt.subTypeName = '").append(conditions.get("subType").toString()).append("' and");
                }
                groupBuffer.append(" pt.subTypeName,");

            }
            if (conditions.get("bank") != null && !Strings.isNullOrEmpty(conditions.get("bank").toString())) {
                if (!"all".equals(conditions.get("bank").toString())) {
                    whereBuffer.append(" pt.subTagName = '").append(conditions.get("bank").toString()).append("' and");
                }
                groupBuffer.append(" pt.subTagName,");
            }
            if (conditions.get("status") != null && !Strings.isNullOrEmpty(conditions.get("status").toString())) {
                if (!"empty".equals(conditions.get("status").toString())) {
                    whereBuffer.append(" od.status = ").append("success".equals(conditions.get("status").toString()) ? 1 : 0).append(" and");
                }
            }
        }
        //去掉末尾的and 或者逗号 然后拼接sql

        StringBuffer selectBf = new StringBuffer();
        if (!Strings.isNullOrEmpty(whereBuffer.toString()))
            selectBf.append(" where ").append(trimAndorComma(whereBuffer));
        if (!Strings.isNullOrEmpty(groupBuffer.toString()))
            selectBf.append(" group by ").append(trimAndorComma(groupBuffer)).toString();
        String sql = selectBf.toString();
        return sql;
    }

    //去掉末尾的and 或者逗号
    private String trimAndorComma(StringBuffer stringBuffer) {
        String resl = null;
        String sql = stringBuffer.toString();
        if (!Strings.isNullOrEmpty(sql)) {
            if (sql.endsWith("and")) {
                resl = sql.substring(0, sql.length() - 3);
            } else {
                resl = sql.substring(0, sql.length() - 1);
            }
        }

        return resl;
    }

}
