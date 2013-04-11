package com.sz7road.userplatform.dao.jdbc;

import com.sz7road.userplatform.dao.InfoDao;
import com.sz7road.userplatform.pojos.InfoObject;
import com.sz7road.userplatform.pojos.SiteObject;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-15
 * Time: 上午11:25
 * 对客服信息接口的具体实现
 */
public class InfoDaoJdbcImpl extends JdbcDaoSupport<InfoObject> implements InfoDao {

    private static String CS_TELEPHONENUM = "cs_telephonenum";
    private static String CS_COMPLAINMAIL = "cs_complainmail";
    private static String CS_QQGROUP = "cs_qqgroup";
    private static String CS_ADPHOTO = "cs_adphoto";


    static final String TABLE = "`db_userplatform`.`dt_site_info`";
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;


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
