package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.FaqDecorateDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojos.FaqObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.RowObject;
import com.sz7road.web.utils.ServletUtil;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-14
 * Time: 下午6:26
 * 常见问题后台的管理实现
 */
public class FaqDecorateDaoJdbcImpl extends JdbcDaoSupport<FaqObject> implements FaqDecorateDao {

    static final String TABLE = "`db_userplatform`.`dt_faqs`";
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    /**
     * 增加Faq
     *
     * @param faqObject
     * @return
     */
    @Override
    public int addFaq(FaqObject faqObject) {
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String ADDINFOSQL = " insert into " + TABLE + "(cid,question,answer,sortNum) values (?,?,?,?) ";
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(ADDINFOSQL);
            stmt.setInt(1, faqObject.getCid());
            stmt.setString(2, faqObject.getQuestion());
            stmt.setString(3, faqObject.getAnswer());
            stmt.setInt(4, faqObject.getSortNum());
            rel = stmt.executeUpdate();
            log.info("成功增加一条常见问题：" + faqObject.toString());
        } catch (Exception ex) {
            log.info("增加常见问题异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                DbUtils.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return rel;
    }

    /**
     * 批量删除Faq
     *
     * @param ids
     * @return
     */
    @Override
    public int batchDeleteFaq(int[] ids) {
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String DELETESQL = " delete from  " + TABLE + " where id=? ";
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(DELETESQL);
            for (Integer id : ids) {
                stmt.setInt(1, id);
                stmt.addBatch();
            }
            rel = stmt.executeBatch().length;
        } catch (Exception ex) {
            log.error("批量删除常见问题异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                connection.commit();
                connection.setAutoCommit(true);
                stmt.close();
                DbUtils.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return rel;
    }

    /**
     * 更新faq
     *
     * @param faqObject
     * @return
     */
    @Override
    public int updateFaq(FaqObject faqObject) {
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String UPDATEINFOSQL = " update " + TABLE + " set  cid=? , question=? , " +
                "answer=? , visitSum=? , sortNum=? where id=? ";
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(UPDATEINFOSQL);
            stmt.setInt(1, faqObject.getCid());
            stmt.setString(2, faqObject.getQuestion());
            stmt.setString(3, faqObject.getAnswer());
            stmt.setInt(4, faqObject.getVisitSum());
            stmt.setInt(5, faqObject.getSortNum());
            stmt.setInt(6, faqObject.getId());
            rel = stmt.executeUpdate();
            log.info("成功更新一条常见问题:" + faqObject.toString());
        } catch (Exception ex) {
            log.info("更新常见问题异常：" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                DbUtils.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return rel;
    }

    /**
     * 更新点击次数
     *
     * @param ids
     * @return
     */
    @Override
    public boolean updateVisitSum(int[] ids) {
        String updateSql = " update " + TABLE + " set visitSum=visitSum+1  where id=? ";
        int rel = 0;
        PreparedStatement prepared=null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            conn.setAutoCommit(false);
             prepared = conn.prepareStatement(updateSql);
            for (int id : ids) {
                prepared.setInt(1, id);
                prepared.addBatch();
            }
            rel = prepared.executeBatch().length;
            log.info("成功更新" + rel + "条常见问题的点击次数!");
        } catch (Exception ex) {
            log.error("更新点击常见问题的点击次数出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                conn.commit();
                conn.setAutoCommit(true);
                prepared.close();
                if (conn != null){
                    DbUtils.close(conn);
                }
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return rel == ids.length;
    }

    private List<FaqObject> transResultToFaqList(ResultSet rs) {
        List<FaqObject> faqObjectList = new LinkedList<FaqObject>();
        try {
            if (rs != null) while (rs.next()) {
                FaqObject faqObject = new FaqObject();
                faqObject.setId(rs.getInt("id"));
                faqObject.setCid(rs.getInt("cid"));
                faqObject.setQuestion(rs.getString("question"));
                faqObject.setAnswer(rs.getString("answer"));
                faqObject.setVisitSum(rs.getInt("visitSum"));
                faqObject.setSortNum(rs.getInt("sortNum"));
                faqObjectList.add(faqObject);
            }
        } catch (Exception ex) {
            log.error("rs转换成实体出错！");
        }
        return faqObjectList;
    }

    /**
     * 查询faq
     *
     * @param conditions
     * @param start
     * @param pageSize
     * @param sortFiled
     * @param sortOrder
     * @return
     */
    @Override
    public JqObject queryFaq(Map conditions, int start, int pageSize, String sortFiled, String sortOrder) {
        JqObject jqObject = new JqObject();

        Map<String, String> countAndResultsql = SqlConfig.getCountAndResultSqlByJqSql(conditions, start, pageSize, sortFiled, sortOrder, SqlConfig.SELECTFAQSQL, SqlConfig.SELECTFAQSQLCOUNT);
        Connection conn = null;
        if (countAndResultsql != null && !countAndResultsql.isEmpty()) {
            try {
                conn = getQueryRunner().getDataSource().getConnection();
                //拿到数据条数
                PreparedStatement prepared = conn.prepareStatement(countAndResultsql.get("countSql"));
                ResultSet preparedRs = prepared.executeQuery();
                int count = 0;
                while (preparedRs.next()) {
                    count = preparedRs.getInt(1);
                }
                jqObject.setTotal(ServletUtil.getTotalPages(count, pageSize));
                jqObject.setRecords(count);
                jqObject.setPage(start / pageSize + 1);
                //拿到数据
                PreparedStatement preparedStatement = conn.prepareStatement(countAndResultsql.get("sql"));
                ResultSet rs = preparedStatement.executeQuery();
                jqObject.setRows(transResultToRowObjectList(rs));
            } catch (Exception ex) {
                log.error("查询faqKind出现异常{}", ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (conn != null) try {
                    DbUtils.close(conn);
                } catch (Exception e) {
                    log.error("关闭连接出现异常{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return jqObject;
    }

    private List<RowObject> transResultToRowObjectList(ResultSet rs) {
        List<RowObject> rowObjectList = new LinkedList<RowObject>();
        try {
            if (rs != null) while (rs.next()) {
                RowObject rowObject = new RowObject();
                List<Object> cells = new LinkedList<Object>();

                cells.add(rs.getInt("id"));
                cells.add(rs.getString("name"));
                cells.add(rs.getString("question"));
                cells.add(rs.getString("answer"));
                cells.add(rs.getInt("visitSum"));
                cells.add(rs.getInt("sortNum"));
                cells.add(rs.getInt("cid"));
                rowObject.setId(rs.getInt("id"));
                rowObject.setCell(cells);
                rowObjectList.add(rowObject);
            }
        } catch (Exception ex) {
            log.error("rs转换成实体出错！");
            ex.printStackTrace();
        }
        return rowObjectList;
    }

    /**
     * 全文搜索Faq
     *
     * @param searchWord
     * @return todo 全文检索
     */
    @Override
    public List<FaqObject> fullTextQuery(String searchWord) {

        List<FaqObject> faqObjectList = null;

        Connection conn = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //拿到数据
            PreparedStatement preparedStatement = conn.prepareStatement(" select * from " + TABLE);
            ResultSet rs = preparedStatement.executeQuery();

            faqObjectList = transResultToFaqList(rs);

        } catch (Exception ex) {
            log.error("查询faqKind出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return faqObjectList;
    }

    /**
     * 按照id搜索faq
     *
     * @param id
     * @return
     */
    @Override
    public JqObject queryFaqById(int id) {
        JqObject jqObject = new JqObject();

        Connection conn = null;
        if (true) {
            try {
                conn = getQueryRunner().getDataSource().getConnection();
                //拿到数据条数
                PreparedStatement prepared = conn.prepareStatement(SqlConfig.SELECTFAQSQLCOUNT);
                ResultSet preparedRs = prepared.executeQuery();
                int count = 0;
                while (preparedRs.next()) {
                    count = preparedRs.getInt(1);
                }
                jqObject.setTotal(ServletUtil.getTotalPages(count, 10));
                jqObject.setRecords(count);
                jqObject.setPage(1);
                //拿到数据
                PreparedStatement preparedStatement = conn.prepareStatement(SqlConfig.SELECTFAQSQL + " and faq.id=? ");
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                jqObject.setRows(transResultToRowObjectList(rs));
            } catch (Exception ex) {
                log.error("查询faqKind出现异常{}", ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (conn != null) try {
                    DbUtils.close(conn);
                } catch (Exception e) {
                    log.error("关闭连接出现异常{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return jqObject;
    }
}
