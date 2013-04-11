package com.sz7road.userplatform.dao.sql;

import com.google.common.base.Strings;
import com.sz7road.web.utils.DataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-20
 * Time: 上午11:49
 * 放一些比较重要的sql语句
 */
public class SqlConfig {

    public static final String ORDER_TABLE = "`db_userplatform`.`dt_order`";

    public static final String USER_TABLE = "`db_userplatform`.`dt_user`";

    public static final String ACCOUNT_TABLE = "`db_userplatform`.`dt_account`";

    public static final String SERVER_TABLE = "`conf_userplatform`.`conf_server_table`";

    public static final String GAME_TABLE = "`conf_userplatform`.`conf_game_table`";

    public static final String PAY_TABLE = "`conf_userplatform`.`conf_pay_table`";

    public static final String DT_FAQS = "`db_userplatform`.`dt_faqs`";

    public static final String DT_FAQ_KIND = "`db_userplatform`.`dt_faq_kind`";

    public static final String DT_SITE_INFO = "`db_userplatform`.`dt_site_info`";

    public static final String LOG_CHARGE_ORDER = "`log_userplatform`.`log_charge_order`";

    public static final String ORDER_COUNT_SQ = "" +
            "SELECT COUNT(*) " +
            "FROM db_userplatform.dt_order as od " +
            "left join db_userplatform.dt_account as u on od.userId=u.id " +
            "join conf_userplatform.conf_game_table as gt on od.gameId=gt.id " +
            "join conf_userplatform.conf_server_table as st on od.zoneId=st.id  and od.gameId=st.gameId  " +
            "join conf_userplatform.conf_pay_table as pt on od.channelId=pt.channelId and od.subType=pt.subType and od.subTag=pt.subTag ";

    public static final String ORDER_COUNT_NOTFORSQ = "" +
            "SELECT COUNT(*) " +
            "FROM db_userplatform.dt_order as od " +
            "left join db_userplatform.dt_account as u on od.userId=u.id " +
            "join conf_userplatform.conf_game_table as gt on od.gameId=gt.id " +
            "join conf_userplatform.server_table as st on od.zoneId=st.serverId  and od.gameId=st.gameId  " +
            "join conf_userplatform.conf_pay_table as pt on od.channelId=pt.channelId and od.subType=pt.subType and od.subTag=pt.subTag ";

    public static final String ORDER_SELECT_SQ = "" +
            "SELECT od.id AS orderId,od.userId AS userId,u.userName AS userName,od.playerId AS playerId," +
            "od.gameId as gameId, gt.gameName AS gameName,gt.goldName AS gameGoldName,st.serverName AS serverName,pt.channelName AS channelName," +
            "pt.subTypeName AS subTypeName,pt.subTagName AS subTypeTagName,od.endOrder AS endOrder,od.amount AS amount," +
            "od.gold AS gold,od.`status` AS status,od.payTime AS payTime,od.assertTime AS assertTime " +
            "FROM db_userplatform.dt_order as od " +
            "left join db_userplatform.dt_account as u on od.userId=u.id " +
            "join conf_userplatform.conf_game_table as gt on od.gameId=gt.id " +
            "join conf_userplatform.conf_server_table as st on od.zoneId=st.id  and od.gameId=st.gameId " +
            "join conf_userplatform.conf_pay_table as pt on od.channelId=pt.channelId and od.subType=pt.subType and od.subTag=pt.subTag ";

    public static final String ORDER_SELECT_NOTFORSQ = "" +
            "SELECT od.id AS orderId,od.userId AS userId,u.userName AS userName,od.playerId AS playerId," +
            "od.gameId as gameId, gt.gameName AS gameName,gt.goldName AS gameGoldName,st.serverName AS serverName,pt.channelName AS channelName," +
            "pt.subTypeName AS subTypeName,pt.subTagName AS subTypeTagName,od.endOrder AS endOrder,od.amount AS amount," +
            "od.gold AS gold,od.`status` AS status,od.payTime AS payTime,od.assertTime AS assertTime " +
            "FROM db_userplatform.dt_order as od " +
            "left join db_userplatform.dt_account as u on od.userId=u.id " +
            "join conf_userplatform.conf_game_table as gt on od.gameId=gt.id " +
            "join conf_userplatform.server_table as st on od.zoneId=st.serverId  and od.gameId=st.gameId " +
            "join conf_userplatform.conf_pay_table as pt on od.channelId=pt.channelId and od.subType=pt.subType and od.subTag=pt.subTag ";

    //订单统计的sql
    public static final String ORDER_STATISTIC_SQ = "" +
            "FROM db_userplatform.dt_order as od " +
            "left join db_userplatform.dt_account as u on od.userId=u.id " +
            "join conf_userplatform.conf_game_table as gt on od.gameId=gt.id " +
            "join conf_userplatform.conf_server_table as st on od.zoneId=st.id  and od.gameId=st.gameId " +
            "join conf_userplatform.conf_pay_table as pt on od.channelId=pt.channelId and od.subType=pt.subType and od.subTag=pt.subTag ";


    //订单统计的sql
    public static final String ORDER_STATISTIC_NOTFORSQ = "" +
            "FROM db_userplatform.dt_order as od " +
            "left join db_userplatform.dt_account as u on od.userId=u.id " +
            "join conf_userplatform.conf_game_table as gt on od.gameId=gt.id " +
            "join conf_userplatform.server_table as st on od.zoneId=st.serverId " +
            "join conf_userplatform.conf_pay_table as pt on od.channelId=pt.channelId and od.subType=pt.subType and od.subTag=pt.subTag ";




    public static final String SELECTFAQSQL = " select faq.id , faqk.name , faq.question , faq.answer , faq.visitSum , faq.sortNum , faq.cid  from " + DT_FAQS + " faq ," + DT_FAQ_KIND + " faqk where faq.cid = faqk.id ";

    public static final String SELECTFAQSQLCOUNT = " select count(*)  from " + DT_FAQS + " faq ," + DT_FAQ_KIND + " faqk where faq.cid = faqk.id ";

    public static final String SELECTFAQKINDSQL = " select faq.id , faqk.name , faq.question , faq.answer , faq.visitSum , faq.sortNum , faq.cid  from " + DT_FAQS + " faq ," + DT_FAQ_KIND + " faqk where faq.cid = faqk.id ";

    public static final String SELECTFAQKINDSQLCOUNT = " select count(*)  from " + DT_FAQS + " faq ," + DT_FAQ_KIND + " faqk where faq.cid = faqk.id ";


    /**
     * 处理之后的jq查询sql
     *
     * @param conditions 查询条件
     * @param start      开始位置
     * @param pageSize   记录条数
     * @param sortField  排序列名
     * @param sortOrder  排序命令
     * @param table      表名
     * @return
     */
    public static Map<String, String> getCountAndResultSqlByJq(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder, String table) {
        Map<String, String> CountAndResultSql = new HashMap<String, String>();

        StringBuffer sql = new StringBuffer(" select * from " + table);
        StringBuffer CountSql = new StringBuffer(" select count(*) from " + table);

        String sqlTemp = null;
        String CountSqlTemp = null;

        if (conditions != null && !conditions.isEmpty()) {
            sql.append(" where ");
            CountSql.append(" where ");
            String groupOp = conditions.get("groupOp").toString();

            for (Map<String, Object> searchObject : (List<Map>) conditions.get("rules")) {
                sql.append(" " + searchObject.get("field")).append(" ").
                        append(DataUtil.transOperateToSqlTag(searchObject.get("op").toString())).append(" ").
                        append(searchObject.get("data")).append(" " + groupOp);
                CountSql.append(" " + searchObject.get("field")).append(" ").
                        append(DataUtil.transOperateToSqlTag(searchObject.get("op").toString())).append(" ").
                        append(searchObject.get("data")).append(" " + groupOp);
            }

            sqlTemp = sql.substring(0, sql.length() - groupOp.length());
            CountSqlTemp = CountSql.substring(0, CountSql.length() - groupOp.length());
        }

        StringBuffer sqlBuffer = new StringBuffer();
        StringBuffer CountSqlBuffer = new StringBuffer();

        if (sqlTemp != null && !Strings.isNullOrEmpty(sqlTemp)) {
            sqlBuffer.append(sqlTemp);
        } else {
            sqlBuffer.append(sql);
        }
        if (CountSqlTemp != null && !Strings.isNullOrEmpty(CountSqlTemp)) {
            CountSqlBuffer.append(CountSqlTemp);
        } else {
            CountSqlBuffer.append(CountSql);
        }

        sqlBuffer.append(" order by ").append(sortField).append(" ").append(sortOrder).
                append(" limit ").append(start).append(" , ").append(pageSize);
//        CountSqlBuffer.append(" order by ").append(sortField).append(" ").append(sortOrder);
//                .
//                append(" limit ").append(start).append(" , ").append(pageSize);

        CountAndResultSql.put("sql", sqlBuffer.toString());
        CountAndResultSql.put("countSql", CountSqlBuffer.toString());

        return CountAndResultSql;
    }

    public static Map<String, String> getCountAndResultSqlByJqSql(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder, String sqlStr, String countSqlStr) {
        Map<String, String> CountAndResultSql = new HashMap<String, String>();

        StringBuffer sql = new StringBuffer(sqlStr);
        StringBuffer CountSql = new StringBuffer(countSqlStr);

        String sqlTemp = null;
        String CountSqlTemp = null;

        if (conditions != null && !conditions.isEmpty()) {
            String groupOp = conditions.get("groupOp").toString();

            for (Map<String, Object> searchObject : (List<Map>) conditions.get("rules")) {
                sql.append(" and " + searchObject.get("field")).append(" ").
                        append(DataUtil.transOperateToSqlTag(searchObject.get("op").toString())).append(" ").
                        append(searchObject.get("data")).append(" " + groupOp);
                CountSql.append(" and " + searchObject.get("field")).append(" ").
                        append(DataUtil.transOperateToSqlTag(searchObject.get("op").toString())).append(" ").
                        append(searchObject.get("data")).append(" " + groupOp);
            }

            sqlTemp = sql.substring(0, sql.length() - groupOp.length());
            CountSqlTemp = CountSql.substring(0, CountSql.length() - groupOp.length());
        }

        StringBuffer sqlBuffer = new StringBuffer();
        StringBuffer CountSqlBuffer = new StringBuffer();

        if (sqlTemp != null && !Strings.isNullOrEmpty(sqlTemp)) {
            sqlBuffer.append(sqlTemp);
        } else {
            sqlBuffer.append(sql);
        }
        if (CountSqlTemp != null && !Strings.isNullOrEmpty(CountSqlTemp)) {
            CountSqlBuffer.append(CountSqlTemp);
        } else {
            CountSqlBuffer.append(CountSql);
        }

        sqlBuffer.append(" order by ").append(sortField).append(" ").append(sortOrder).
                append(" limit ").append(start).append(" , ").append(pageSize);
//        CountSqlBuffer.append(" order by ").append(sortField).append(" ").append(sortOrder);
//                .
//                append(" limit ").append(start).append(" , ").append(pageSize);

        CountAndResultSql.put("sql", sqlBuffer.toString());
        CountAndResultSql.put("countSql", CountSqlBuffer.toString());

        return CountAndResultSql;
    }
}
