package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.sz7road.userplatform.dao.ItemDao;
import com.sz7road.userplatform.dao.sql.SqlConfig;
import com.sz7road.userplatform.pojos.Item;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-6-19
 * Time: 下午12:21
 * To change this template use File | Settings | File Templates.
 */
public class ItemDaoJdbcImpl extends JdbcDaoSupport<Item> implements ItemDao {

    static final String TABLE = "`conf_userplatform`.`conf_pay_table`";
    static final String SQL_BASE = "SELECT * FROM " + TABLE + " WHERE 1";
    static final String SQL_GETBY_USERNAME = SQL_BASE + " AND `username`=?";

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;


    /**
     * 拿到支付渠道数据
     *
     * @return
     */
    @Override
    public List<Item> getChannels() {
        List<Item> channelItems = new ArrayList<Item>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "select DISTINCT channelId,channelName from " + TABLE;

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item channel = new Item();
                channel.setLabel(rs.getString("channelName"));
                channel.setValue(rs.getString("channelId"));
                channelItems.add(channel);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return channelItems;


    }

    /**
     * 拿到支付渠道的支付方式数据
     *
     * @param channel 支付渠道
     * @return
     */
    @Override
    public List<Item> getSubTypes(String channel) {

        List<Item> subTypeItems = new ArrayList<Item>();
        try {

            String sql = "select DISTINCT subType,subTypeName  from " + TABLE + " where 1" + " AND channelId=?";
            if(Strings.isNullOrEmpty(channel))
            {
                sql="select DISTINCT subType,subTypeName  from " + TABLE;
            }
            conn = getQueryRunner().getDataSource().getConnection();


            stmt = conn.prepareStatement(sql);
             if(!Strings.isNullOrEmpty(channel))
             {
                 stmt.setString(1, channel);
             }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item subType = new Item();
                subType.setLabel(rs.getString("subTypeName"));
                subType.setValue(String.valueOf(rs.getInt("subType")));
                subTypeItems.add(subType);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return subTypeItems;

    }

    /**
     * 拿到支付渠道下，支付方式下的具体支付方式
     *
     * @param channel 支付渠道
     * @param subType 支付方式第一层
     * @return
     */
    @Override
    public List<Item> getSubTypeTags(String channel, String subType) {
        List<Item> subTypeTagItems = new ArrayList<Item>();
        try {

            String sql = "select DISTINCT subTag,subTagName  from " + TABLE + " where 1" + " AND channelId=? AND subType=?";

            if(Strings.isNullOrEmpty(channel)||Strings.isNullOrEmpty(subType))
            {
                sql="select DISTINCT subTag,subTagName  from " + TABLE;
            }

            conn = getQueryRunner().getDataSource().getConnection();
            stmt = conn.prepareStatement(sql);

            if(!Strings.isNullOrEmpty(channel))
            {
                stmt.setString(1, channel);
            }
            if(!Strings.isNullOrEmpty(subType))
            {
                stmt.setString(2, subType);
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item subTypeTag = new Item();
                subTypeTag.setLabel(rs.getString("subTagName"));
                subTypeTag.setValue(rs.getString("subTag"));
                subTypeTagItems.add(subTypeTag);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return subTypeTagItems;
    }

    /**
     * 拿到游戏的数据
     *
     * @return
     */
    @Override
    public List<Item> getGames() {

        List<Item> gameItems = new ArrayList<Item>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "select DISTINCT id,gameName from " + SqlConfig.GAME_TABLE;

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            log.info("初始化服务器列表sql：" + sql);
            while (rs.next()) {
                Item server = new Item();
                server.setLabel(rs.getString("gameName"));
                server.setValue(rs.getString("id"));
                gameItems.add(server);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return gameItems;

    }

    /**
     * 拿到服务器的数据
     *
     * @param gameId 游戏ID
     * @return
     */
    @Override
    public List<Item> getServers(String gameId) {
        List<Item> serverItems = new ArrayList<Item>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "select DISTINCT id,serverName  from " + SqlConfig.SERVER_TABLE + " where 1" + " AND gameId=?  order by serverNo ";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, gameId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item subType = new Item();
                subType.setLabel(rs.getString("serverName"));
                subType.setValue(String.valueOf(rs.getInt("id")));
                serverItems.add(subType);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return serverItems;
    }

    /**
     * 拿到所有的常见问题分类
     *
     * @return
     */
    @Override
    public List<Item> getFaqKind() {
        List<Item> kinds = new ArrayList<Item>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "select DISTINCT id,name  from " + SqlConfig.DT_FAQ_KIND + " order by sortNo ";

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item kind = new Item();
                kind.setLabel(rs.getString("name"));
                kind.setValue(String.valueOf(rs.getInt("id")));
                kinds.add(kind);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return kinds;
    }

    /**
     * 拿到一级分类
     *
     * @return
     */
    @Override
    public List<Item> getFaqKindOfParent() {
        List<Item> kinds = new ArrayList<Item>();
        try {
            conn = getQueryRunner().getDataSource().getConnection();

            String sql = "select DISTINCT id,name  from " + SqlConfig.DT_FAQ_KIND + " where parentId=0  order by sortNo ";

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item kind = new Item();
                kind.setLabel(rs.getString("name"));
                kind.setValue(String.valueOf(rs.getInt("id")));
                kinds.add(kind);
            }
        } catch (final SQLException e) {
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return kinds;
    }

}
