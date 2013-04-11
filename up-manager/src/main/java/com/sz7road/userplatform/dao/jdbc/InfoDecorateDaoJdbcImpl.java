package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.InfoDecorateDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.JqObject;
import com.sz7road.userplatform.pojos.RowObject;
import com.sz7road.userplatform.pojos.SiteObject;
import com.sz7road.web.utils.ServletUtil;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 上午11:25
 * 对客服信息接口的具体实现
 */
public class InfoDecorateDaoJdbcImpl extends JdbcDaoSupport<InfoObject> implements InfoDecorateDao {

    private static String CS_TELEPHONENUM = "cs_telephonenum";
    private static String CS_COMPLAINMAIL = "cs_complainmail";
    private static String CS_QQGROUP = "cs_qqgroup";
    private static String CS_ADPHOTO = "cs_adphoto";


    static final String TABLE = "`db_userplatform`.`dt_site_info`";
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    /**
     * 更改客服信息
     *
     * @param info
     * @return
     */
    @Override
    public int updateInfo(InfoObject info) {
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String UPDATEINFOSQL = " update " + TABLE + " set  name=? , text=? , ext=?  where id=? ";
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(UPDATEINFOSQL);
            stmt.setString(1, info.getName());
            stmt.setString(2, info.getText());
            stmt.setString(3, info.getExt());
            stmt.setInt(4, info.getId());
            rel = stmt.executeUpdate();
            log.info("成功更新一条客服信息:" + info.toString());
        } catch (Exception ex) {
            log.info("更新客服信息异常：" + ex.getMessage());
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
     * 增加轮播图片或者QQ群
     *
     * @param info
     * @return
     */
    @Override
    public int addQQGroupOrAdPhoto(InfoObject info) {
        Connection connection = null;
        PreparedStatement stmt = null;
        //根据条件，组合不同的sql
        String ADDINFOSQL = " insert into " + TABLE + "(name,text,ext) values (?,?,?) ";
        int rel = 0;
        try {
            connection = getQueryRunner().getDataSource().getConnection();
            stmt = connection.prepareStatement(ADDINFOSQL);
            stmt.setString(1, info.getName());
            stmt.setString(2, info.getText());
            stmt.setString(3, info.getExt());
            rel = stmt.executeUpdate();
            log.info("成功增加一条客服信息：" + info.toString());
        } catch (Exception ex) {
            log.info("增加客服信息异常：" + ex.getMessage());
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
     * 批量删除轮播图片或者QQ群，当多于4个的时候
     *
     * @param ids
     * @return
     */
    @Override
    public int BatchDeleteQQGroupOrAdPhoto(int[] ids) {

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
            log.error("批量删除客服信息异常：" + ex.getMessage());
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
     * 显示并查询客服信息
     *
     * @param conditions
     * @param start
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public JqObject queryCsInfo(Map<String, Object> conditions, int start, int pageSize, String sortField, String sortOrder) {
        JqObject jqObject = new JqObject();
        Connection conn = null;
        Map<String, String> countAndResultsql = SqlConfig.getCountAndResultSqlByJq(conditions, start, pageSize, sortField, sortOrder, TABLE);
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
                log.error("查询客服信息出现异常{}", ex.getMessage());
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

    /**
     * 对外接口返回客服信息
     *
     * @return
     */
    @Override
    public SiteObject getCsInfo() {
        StringBuffer GETCSINFO = new StringBuffer(" select * from ").append(TABLE).
                append(" where name in ('").append(CS_TELEPHONENUM).append("','").
                append(CS_COMPLAINMAIL).append("')");
        StringBuffer GETQQGROUP = new StringBuffer(" select * from ").append(TABLE).
                append(" where name like '").append(CS_QQGROUP).append("%'");

        SiteObject siteObject = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //拿到数据
            PreparedStatement preparedStatement = conn.prepareStatement(GETCSINFO.toString());
            ResultSet rs = preparedStatement.executeQuery();
            siteObject = transResultToInfoList(rs);

            PreparedStatement prepared = conn.prepareStatement(GETQQGROUP.toString());
            ResultSet resultSet = prepared.executeQuery();
            List<String> qqList = transReultToQQList(resultSet);

            siteObject.setCs_playqqgroup(qqList);

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
        return siteObject;
    }

    private List<String> transReultToQQList(ResultSet resultSet) {
        List<String> qqlist = new ArrayList<String>();
        try {
            while (resultSet != null && resultSet.next()) {
                qqlist.add(resultSet.getString("text"));
            }
        } catch (Exception ex) {
            log.error("rs提取QQ群列表异常！" + ex.getMessage());
            ex.printStackTrace();
        }
        return qqlist;
    }

    /**
     * 对外接口返回轮播图片信息
     *
     * @return
     */
    @Override
    public List<InfoObject> getAdPhotos() {
        StringBuffer GETQQGROUP = new StringBuffer(" select * from ").append(TABLE).
                append(" where name like '").append(CS_ADPHOTO).append("%'");
        List<InfoObject> photoList = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            PreparedStatement prepared = conn.prepareStatement(GETQQGROUP.toString());
            ResultSet resultSet = prepared.executeQuery();
            photoList = transReultToPhotoList(resultSet);
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
        return photoList;
    }

    @Override
    public InfoObject getCsInfoById(int id) {
        StringBuffer GETCSINFO = new StringBuffer(" select * from ").append(TABLE).
                append(" where id= ").append(id);
        InfoObject infoObject = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            //拿到数据
            PreparedStatement preparedStatement = conn.prepareStatement(GETCSINFO.toString());
            ResultSet rs = preparedStatement.executeQuery();
            infoObject = transReultToInfoObject(rs);
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
        return infoObject;
    }

    @Override
    public JqObject queryCsInfoById(int id) {
        JqObject jqObject = new JqObject();
        Connection conn = null;

        if (true) {
            try {
                conn = getQueryRunner().getDataSource().getConnection();
                //拿到数据条数
                PreparedStatement prepared = conn.prepareStatement(" select * from " + SqlConfig.DT_SITE_INFO);
                ResultSet preparedRs = prepared.executeQuery();
                int count = 0;
                while (preparedRs.next()) {
                    count = preparedRs.getInt(1);
                }
                jqObject.setTotal(ServletUtil.getTotalPages(count, 10));
                jqObject.setRecords(count);
                jqObject.setPage(+1);
                //拿到数据
                PreparedStatement preparedStatement = conn.prepareStatement("select * from " + SqlConfig.DT_SITE_INFO + " where id=? ");
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                jqObject.setRows(transResultToRowObjectList(rs));
            } catch (Exception ex) {
                log.error("查询客服信息出现异常{}", ex.getMessage());
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

    private InfoObject transReultToInfoObject(ResultSet rs) {

        InfoObject infoObject = null;
        try {
            while (rs != null && rs.next()) {
                infoObject = new InfoObject();
                infoObject.setId(rs.getInt("id"));
                infoObject.setName(rs.getString("name"));
                infoObject.setText(rs.getString("text"));
                infoObject.setExt(rs.getString("ext"));

            }
        } catch (Exception ex) {
            log.error("rs转换成InfoObject实体异常！" + ex.getMessage());
            ex.printStackTrace();
        }
        return infoObject;
    }

    private List<InfoObject> transReultToPhotoList(ResultSet rs) {
        List<InfoObject> photoList = new LinkedList<InfoObject>();
        try {
            while (rs != null && rs.next()) {
                InfoObject infoObject = new InfoObject();
                infoObject.setName(rs.getString("name"));
                infoObject.setText(rs.getString("text"));
                infoObject.setExt(rs.getString("ext"));
                photoList.add(infoObject);
            }
        } catch (Exception ex) {
            log.error("rs转换成InfoObject实体异常！" + ex.getMessage());
            ex.printStackTrace();
        }
        return photoList;
    }

    /**
     * 把结果集转换成jq需要的对象
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
                cells.add(rs.getString("text"));
                cells.add(rs.getString("ext"));
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
     * 把结果集转换成客户信息列表
     *
     * @param rs
     * @return
     */
    private SiteObject transResultToInfoList(ResultSet rs) {
        SiteObject siteObject = new SiteObject();
        try {
            while (rs != null && rs.next()) {
                String name = rs.getString("name");

                String text = rs.getString("text");
                if (CS_TELEPHONENUM.equals(name)) {
                    siteObject.setCs_telephonenum(text);
                }
                if (CS_COMPLAINMAIL.equals(name)) {
                    siteObject.setCs_complainmail(text);
                }
            }
        } catch (Exception ex) {
            log.error("rs转换成InfoObject实体异常！" + ex.getMessage());
            ex.printStackTrace();
        }
        return siteObject;
    }

}
