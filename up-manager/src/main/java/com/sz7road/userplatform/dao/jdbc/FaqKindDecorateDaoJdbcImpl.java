package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sz7road.userplatform.dao.FaqKindDecorateDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojos.FaqKindObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.RowObject;
import com.sz7road.web.utils.DataUtil;
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
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-2
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public class FaqKindDecorateDaoJdbcImpl extends JdbcDaoSupport<FaqKindObject> implements FaqKindDecorateDao {
    @Inject
    private Injector injector;

    static final String TABLE = "`db_userplatform`.dt_faq_kind";

    /**
     * 根据条件查询faq问题分类信息
     *
     * @param conditions 搜索关键词 对象存放位置
     * @param start      开始位置
     * @param pageSize   每页的记录数
     * @param sortField  排序的字段
     * @param sortOrder  排序的命令 asc or desc
     * @return
     */
    @Override
    public JqObject query(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder) {
        JqObject jqObject = new JqObject();
        Connection conn = null;
        String sql = "";
        String countsql = "";


        Map<String, String> countAndResultsql = SqlConfig.getCountAndResultSqlByJqSql(conditions, start, pageSize, sortField, sortOrder, sql, countsql);
        if (countAndResultsql != null && !countAndResultsql.isEmpty()) {
            try {
                conn = getQueryRunner().getDataSource().getConnection();
                //拿到数据条数
                PreparedStatement prepared = conn.prepareStatement(countAndResultsql.get("countSql"));
                ResultSet preparedRs = prepared.executeQuery();
                int count = 0;
                while (preparedRs.next()) count = preparedRs.getInt(1);
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
                }
            }
        }
        return jqObject;
    }

    /**
     * 把rs转换才RowObject实体方法
     *
     * @param rs
     * @return
     */
    private List<RowObject> transResultToRowObjectList(ResultSet rs) {
        List<RowObject> rowObjectList = new LinkedList<RowObject>();
        try {
            if (rs != null) while (rs.next()) {
                RowObject rowObject = new RowObject();
                List<Object> cells = new LinkedList<Object>();
                cells.add(rs.getInt("id"));
                cells.add(rs.getString("name"));
                cells.add(rs.getString("ext"));
                cells.add(rs.getInt("sortNo"));
                cells.add(rs.getInt("parentId"));
                rowObject.setId(rs.getInt("id"));
                rowObject.setCell(cells);
                rowObjectList.add(rowObject);
            }
        } catch (Exception ex) {
            log.error("rs转换成实体出错！");
        }

        return rowObjectList;
    }

    private List<RowObject> transResultToRowObjectListByTree(ResultSet rs) {
        List<RowObject> rowObjectList = new LinkedList<RowObject>();
        try {
            if (rs != null) while (rs.next()) {
                RowObject rowObject = new RowObject();
                List<Object> cells = new LinkedList<Object>();
                cells.add(rs.getInt("id"));
                cells.add(rs.getString("name"));
                cells.add(rs.getString("ext"));
                cells.add(rs.getInt("sortNo"));
                cells.add(rs.getInt("parentId"));
                //层级
                int level = 0;
                String isLeaf = null;
                String isExpand = null;
                int parent = rs.getInt("parentId");
                if (parent == 0) {
                    level = 0;

                } else {
                    level = 1;
                }

                List<FaqKindObject> faqKindObjects = getChildFaqKindByParentId(rs.getInt("id"));

                if (faqKindObjects == null || faqKindObjects.isEmpty()) {
                    isLeaf = "true";
                    isExpand = "true";
                } else {
                    isLeaf = "false";
                    isExpand = "false";
                }
                cells.add(level);
                cells.add(rs.getInt("parentId"));
                cells.add(isExpand);
                rowObject.setId(rs.getInt("id"));
                rowObject.setCell(cells);
                rowObjectList.add(rowObject);
            }
            if (rowObjectList.size() == 1) {
                List<Object> onlyCell = (List<Object>) rowObjectList.get(0).getCell();
                onlyCell.remove(onlyCell.size() - 1);
                onlyCell.add("true");
            }
        } catch (Exception ex) {
            log.error("rs转换成实体出错！");
        }

        return rowObjectList;
    }


    /**
     * 把rs转换才faqkind实体方法
     *
     * @param rs
     * @return
     */
    private List<FaqKindObject> transResultToFaqkindList(ResultSet rs) {
        List<FaqKindObject> faqKindObjectList = new LinkedList<FaqKindObject>();
        try {
            if (rs != null) while (rs.next()) {
                FaqKindObject faqKindObject = new FaqKindObject();
                faqKindObject.setId(rs.getInt("id"));
                faqKindObject.setName(rs.getString("name"));
                faqKindObject.setSortNo(rs.getInt("sortNo"));
                faqKindObject.setExt(rs.getString("ext"));
                faqKindObject.setParentId(rs.getInt("parentId"));
                faqKindObjectList.add(faqKindObject);
            }
        } catch (Exception ex) {
            log.error("rs转换成实体出错！");
        }

        return faqKindObjectList;
    }


    /**
     * 通过Id拿到种类
     *
     * @param id
     * @return
     */
    @Override
    public FaqKindObject getFaqKindById(int id) {
        FaqKindObject faqKindObject = null;
        Connection conn = null;
        try {
            String sql = " select * from " + TABLE + " where id=? ";
            conn = getQueryRunner().getDataSource().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            List<FaqKindObject> faqKindObjectList = transResultToFaqkindList(rs);
            if (faqKindObjectList != null && !faqKindObjectList.isEmpty()) faqKindObject = faqKindObjectList.get(0);
        } catch (Exception ex) {
            log.error("查询faqKind出现异常{}", ex.getMessage());
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return faqKindObject;
    }

    /**
     * 通过Id拿到子种类
     *
     * @param parentId
     * @return
     */
    @Override
    public List<FaqKindObject> getChildFaqKindByParentId(int parentId) {
        List<FaqKindObject> faqKindObjectList = new LinkedList<FaqKindObject>();
        Connection conn = null;
        try {
            String sql = "select * from " + TABLE + " where parentId=? ";
            conn = getQueryRunner().getDataSource().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, parentId);
            ResultSet rs = preparedStatement.executeQuery();
            faqKindObjectList = transResultToFaqkindList(rs);
        } catch (Exception ex) {
            log.error("查询faqKind出现异常{}", ex.getMessage());
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return faqKindObjectList;
    }

    /**
     * 删除指定ID的faqkind，如果有子节点，顺带删除子节点
     *
     * @param id
     * @return
     */
    @Override
    public int deleteFaqKindById(int id) {
        int result = 0;
        //拿到该记录树的ID列表
        List<Integer> faqKindObjectIdList = getFaqKindTreeIdList(id);
        Connection conn = null;
        PreparedStatement preparedStatement=null;
        try {
            String deleteSql = " delete from " + TABLE + " where id=? ";
            conn = getQueryRunner().getDataSource().getConnection();
            conn.setAutoCommit(false);
             preparedStatement = conn.prepareStatement(deleteSql);
            if (faqKindObjectIdList != null && !faqKindObjectIdList.isEmpty()) {
                for (Integer faqKindID : faqKindObjectIdList) {
                    preparedStatement.setInt(1, faqKindID.intValue());
                    preparedStatement.addBatch();
                }
            }
            result = preparedStatement.executeUpdate();
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                log.error("回滚事物失败！");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            log.error("删除faqKind树出现异常{}", ex.getMessage());
        } finally {
            try {
                conn.commit();
                conn.setAutoCommit(true);
                   preparedStatement.close();
            } catch (SQLException e) {
                log.error("提交事物失败！");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public int batchDeleteFaqKindByIds(int[] ids) {
        int result = 0;
        //拿到该记录树的ID列表
        List<Integer> faqKindObjectIdList = new LinkedList<Integer>();
        for (int id : ids) {
            faqKindObjectIdList.addAll(getFaqKindTreeIdList(id));
        }
        Connection conn = null;
        PreparedStatement preparedStatement=null;
        try {
            String deleteSql = " delete from " + TABLE + " where id=? ";
            conn = getQueryRunner().getDataSource().getConnection();
            conn.setAutoCommit(false);
             preparedStatement = conn.prepareStatement(deleteSql);
            if (faqKindObjectIdList != null && !faqKindObjectIdList.isEmpty()) {
                for (Integer faqKindID : faqKindObjectIdList) {
                    preparedStatement.setInt(1, faqKindID.intValue());
                    preparedStatement.addBatch();
                }
                result = preparedStatement.executeBatch().length;
            }
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                log.error("回滚事物失败！");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            log.error("删除faqKind树出现异常{}", ex.getMessage());
        } finally {
            try {
                conn.commit();
                 conn.setAutoCommit(true);
                preparedStatement.close();
            } catch (SQLException e) {
                log.error("提交事物失败！");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    @Override
    public JqObject queryTree(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder, int nodeId) {

        JqObject jqObject = new JqObject();
        Connection conn = null;
        try {
            StringBuffer sql = new StringBuffer(" select * from " + TABLE + " where parentId=").append(nodeId).append(" and");
            StringBuffer CountSql = new StringBuffer(" select count(*) from " + TABLE + " where parentId=").append(nodeId).append(" and");
            String sqlTemp = null;
            String CountSqlTemp = null;
            if (conditions != null && !conditions.isEmpty()) {
//                sql.append(" where parentId=0 and");
//                CountSql.append(" where parentId=0  and");
                String groupOp = conditions.get("groupOp").toString();

                for (Map<String, Object> searchObject : (List<Map>) conditions.get("rules")) {
                    sql.append(" " + searchObject.get("field")).append(" ").
                            append(DataUtil.transOperateToSqlTag(searchObject.get("op").toString())).append(" ").append(searchObject.get("data")).append(" " + groupOp);
                    CountSql.append(" " + searchObject.get("field")).append(" ").
                            append(DataUtil.transOperateToSqlTag(searchObject.get("op").toString())).append(" ").append(searchObject.get("data")).append(" " + groupOp);
                }

                sqlTemp = sql.substring(0, sql.length() - groupOp.length());
                CountSqlTemp = CountSql.substring(0, CountSql.length() - groupOp.length());
            }

            StringBuffer sqlBuffer = new StringBuffer();
            StringBuffer CountSqlBuffer = new StringBuffer();

            if (sqlTemp != null && !Strings.isNullOrEmpty(sqlTemp)) {
                sqlBuffer.append(sqlTemp);
            } else {
                sqlBuffer.append(sql.substring(0, sql.length() - "and".length()));
            }
            if (CountSqlTemp != null && !Strings.isNullOrEmpty(CountSqlTemp)) {
                CountSqlBuffer.append(CountSqlTemp);
            } else {
                CountSqlBuffer.append(CountSql.substring(0, CountSql.length() - "and".length()));
            }

            sqlBuffer.append(" order by ").append(sortField).append(" ").append(sortOrder).append(" limit ").append(start).append(" , ").append(pageSize);
            CountSqlBuffer.append(" order by ").append(sortField).append(" ").append(sortOrder);

            conn = getQueryRunner().getDataSource().getConnection();
            //拿到数据条数
            PreparedStatement prepared = conn.prepareStatement(CountSqlBuffer.toString());
            ResultSet preparedRs = prepared.executeQuery();
            int count = 0;
            while (preparedRs.next()) {
                count = preparedRs.getInt(1);
            }
            jqObject.setTotal(ServletUtil.getTotalPages(count, pageSize));
            jqObject.setRecords(count);
            jqObject.setPage(start / pageSize + 1);
            //拿到数据
            PreparedStatement preparedStatement = conn.prepareStatement(sqlBuffer.toString());
            ResultSet rs = preparedStatement.executeQuery();
            jqObject.setRows(transResultToRowObjectListByTree(rs));
        } catch (Exception ex) {
            log.error("查询faqKind出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return jqObject;
    }

    @Override
    public JqObject queryFaqKindById(int id) {
        JqObject jqObject = new JqObject();
        Connection conn = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //拿到数据条数
            PreparedStatement prepared = conn.prepareStatement(" select count(*) from " + SqlConfig.DT_FAQ_KIND);
            ResultSet preparedRs = prepared.executeQuery();
            int count = 0;
            while (preparedRs.next()) {
                count = preparedRs.getInt(1);
            }
            jqObject.setTotal(ServletUtil.getTotalPages(count, 10));
            jqObject.setRecords(count);
            jqObject.setPage(1);
            //拿到数据
            PreparedStatement preparedStatement = conn.prepareStatement(" select * from " + SqlConfig.DT_FAQ_KIND + " where id=? ");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            jqObject.setRows(transResultToRowObjectListByTree(rs));
        } catch (Exception ex) {
            log.error("查询faqKind出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return jqObject;
    }

    //通过ID拿到这棵树所有的ID,子ID在前，父ID在后
    private List<Integer> getFaqKindTreeIdList(int id) {
        List<Integer> faqKindTreeIdList = new LinkedList<Integer>();

        FaqKindObject faqKindObject = getFaqKindById(id);
        if (faqKindObject != null) {
            List<FaqKindObject> faqKindObjectChildList = getChildFaqKindByParentId(id);
            if (faqKindObjectChildList != null && !faqKindObjectChildList.isEmpty()) {
                for (FaqKindObject faqKind : faqKindObjectChildList) {
                    faqKindTreeIdList.addAll(getFaqKindTreeIdList(faqKind.getId()));
                }
                faqKindTreeIdList.add(id);
            } else {
                faqKindTreeIdList.add(id);
            }
        }
        return faqKindTreeIdList;
    }

    /**
     * 更新数据表对象。更新之前，先拿到这个数据。
     *
     * @param entity 用作更新的数据对象
     * @return true or false
     */
    @Override
    public int update(FaqKindObject entity) throws SQLException {
        int result = 0;
        Connection conn = null;
        try {
            String sql = " update " + TABLE + " set name=? , sortNo=? , ext=? , parentId=?  where Id=? ";
            FaqKindObject faqKindObject = getFaqKindById(entity.getParentId());
            if (faqKindObject != null || entity.getParentId() == 0) {
                conn = getQueryRunner().getDataSource().getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setInt(2, entity.getSortNo());
                preparedStatement.setString(3, entity.getExt());
                preparedStatement.setInt(4, entity.getParentId());
                preparedStatement.setInt(5, entity.getId());
                result = preparedStatement.executeUpdate();
            } else {
                log.info("更新的faqKind父记录不存在！");
            }
        } catch (Exception ex) {
            log.error("更新faqKind出现异常{}", ex.getMessage());
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return result;
    }


    /**
     * 添加数据表对象。
     *
     * @param entity 待添加的数据对象
     * @return true or false
     */
    @Override
    public int add(FaqKindObject entity) throws SQLException {
        int result = 0;
        Connection conn = null;
        try {
            String sql = " insert  into " + TABLE + "(name,ext,sortNo,parentId)  values (?,?,?,?)";
            int parentId = entity.getParentId();

            conn = getQueryRunner().getDataSource().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getExt());
            preparedStatement.setInt(3, entity.getSortNo());
            if (parentId > 0) {
                FaqKindObject faqKindObject = getFaqKindById(entity.getParentId());
                if (faqKindObject != null) {
                    preparedStatement.setInt(4, parentId);
                    result = preparedStatement.executeUpdate();
                } else {
                    log.info("增加的faqKind的parentId非法");
                }
            } else {
                preparedStatement.setInt(4, 0);
                result = preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            log.error("增加faqKind出现异常{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) try {
                DbUtils.close(conn);
            } catch (Exception e) {
                log.error("关闭连接出现异常{}", e.getMessage());
            }
        }
        return result;
    }
}
