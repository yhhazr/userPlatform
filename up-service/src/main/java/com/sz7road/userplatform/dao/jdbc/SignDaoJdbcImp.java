package com.sz7road.userplatform.dao.jdbc;

import com.google.common.base.Strings;
import com.sz7road.userplatform.dao.SignDao;
import com.sz7road.userplatform.ws.sign.ScoreRuleAndMoveByte;
import com.sz7road.userplatform.ws.sign.Sign;
import com.sz7road.userplatform.ws.sign.SignObject;
import com.sz7road.utils.CommonDateUtils;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-18
 * Time: 上午11:01
 * 调用存储过程，实现签到功能
 */
public class SignDaoJdbcImp extends JdbcDaoSupport<SignObject> implements SignDao {

    private final static Logger log = LoggerFactory.getLogger(SignDaoJdbcImp.class);

    private Connection conn = null;

    @Override
    public Sign querySign(int uid, int gid) {
        final Sign sign = new Sign();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            preparedStatement = conn.prepareStatement(" select * from db_userplatform.dt_sign where userid=? and gameid=? ;");

            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, gid);

            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                sign.setCode(200);
                sign.setMsg("成功的查询到签到信息!");
                sign.setContinueSignCount(rs.getInt("signCount"));
                sign.setTotalScore(rs.getInt("integration"));
                final long date = rs.getDate("lastModifyTime").getTime();
//                log.info("date:"+date);
                sign.setLastModifyDate(new Timestamp(date));
                sign.setSignHistory(rs.getLong("signHistory"));
            } else {
                sign.setCode(300);
                sign.setMsg("该用户从来没有签过到！");
            }

        } catch (SQLException e) {
            sign.setCode(404);
            sign.setMsg("平台或者db异常！");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            try {
                DbUtils.close(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbUtils.closeQuietly(conn);
        }
        return sign;
    }


    @Override
    public Sign signThenReturn(int uid, int gid) {
        Sign sign = new Sign();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null, executePreparedStatement = null;
        SignObject signObject = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            preparedStatement = conn.prepareStatement(" select * from db_userplatform.dt_sign where userid=? and gameid=? ;");
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, gid);

            rs = preparedStatement.executeQuery();
            if (rs.next()) {//查到了更新
                signObject = new SignObject();
                signObject.setId(rs.getInt("id"));
                signObject.setUid(rs.getInt("userid"));
                signObject.setGid(rs.getInt("gameid"));
                signObject.setSignCount(rs.getInt("signCount"));
                signObject.setIntegration(rs.getInt("integration"));
                signObject.setLastModifyTime(new Timestamp(rs.getDate("lastModifyTime").getTime()));
                signObject.setSignHistory(rs.getLong("signHistory"));
                signObject.setExt(rs.getString("ext"));


                Timestamp lastModifyTimeStamp = signObject.getLastModifyTime();
                Timestamp todayStartTimeStamp = CommonDateUtils.getTodayStartTimeStamp();
                if (todayStartTimeStamp.after(lastModifyTimeStamp)) {//今天没有签过到
                    long missDays = (System.currentTimeMillis() - signObject.getLastModifyTime().getTime()) / (24 * 60 * 60 * 1000);
                    int newSignCount = signObject.getSignCount();
                    String newExt = "签到";
                    if (missDays == 1) {  //连续签到，加分，连续签到次数增加1 ，签到历史移动一位
                        newSignCount += 1;
                    } else {//不连续签到，加分，连续签到次数为1，签到历史移动missDays位
                        newSignCount = 1;
                    }
                    if (newSignCount >= 91) { //签到超过90天，连续签到次数重置为1
                        newSignCount = 1;
                        newExt = "连续签到天数重置为1，时间：" + CommonDateUtils.getDate(System.currentTimeMillis());
                    }
                    if (missDays >= 42) missDays = 0;
                    final long newSignHistory = ScoreRuleAndMoveByte.moveByte(signObject.getSignHistory(), missDays);
                    final int newIntegration = signObject.getIntegration() + ScoreRuleAndMoveByte.getScoreByRule(newSignCount);
                    executePreparedStatement = conn.prepareStatement(" update db_userplatform.dt_sign set signCount=? , integration=? , signHistory=? , lastModifyTime=? , ext=? where id=?; ");
                    executePreparedStatement.setInt(1, newSignCount);
                    executePreparedStatement.setInt(2, newIntegration);
                    executePreparedStatement.setLong(3, newSignHistory);
                    final long signDate = System.currentTimeMillis();
                    executePreparedStatement.setDate(4, new java.sql.Date(signDate));
                    executePreparedStatement.setString(5, newExt);
                    executePreparedStatement.setInt(6, signObject.getId());

                    int effectRows = executePreparedStatement.executeUpdate();

                    if (effectRows >= 1) {
                        sign.setCode(206);
                        sign.setMsg("签到成功！成功更新数据!");
                        sign.setContinueSignCount(newSignCount);
                        sign.setLastModifyDate(new Timestamp(signDate));
                        sign.setTotalScore(newIntegration);
                        sign.setSignHistory(newSignHistory);
                    } else {
                        sign.setCode(208);
                        sign.setMsg("签到失败，更新数据失败!");
                        sign.setContinueSignCount(signObject.getSignCount());
                        sign.setLastModifyDate(signObject.getLastModifyTime());
                        sign.setSignHistory(signObject.getSignHistory());
                        sign.setTotalScore(signObject.getIntegration());
                    }
                } else {//今天已经签过到了
                    sign.setCode(300);
                    sign.setMsg("该用户今天已经签过到了!");
                    sign.setLastModifyDate(signObject.getLastModifyTime());
                    sign.setContinueSignCount(signObject.getSignCount());
                    sign.setSignHistory(signObject.getSignHistory());
                    sign.setTotalScore(signObject.getIntegration());
                }

            } else {//没查到，插入
                executePreparedStatement = conn.prepareStatement(" insert into db_userplatform.dt_sign(userid,gameid,signCount,integration,lastModifyTime,signHistory,ext) values(?,?,1,1,?,1,?); ");
                executePreparedStatement.setInt(1, uid);
                executePreparedStatement.setInt(2, gid);
                final long nowTime = System.currentTimeMillis();
                final java.sql.Date insertDate = new java.sql.Date(nowTime);
                executePreparedStatement.setDate(3, insertDate);
                executePreparedStatement.setString(4, "首次签到,时间:" + insertDate);
                int effectRows = executePreparedStatement.executeUpdate();

                if (effectRows >= 1) {
                    sign.setCode(200);
                    sign.setMsg("该用户第一次签到！成功插入数据!");
                    sign.setContinueSignCount(1);
                    sign.setLastModifyDate(new Timestamp(nowTime));
                    sign.setTotalScore(1);
                    sign.setSignHistory(1);
                } else {
                    sign.setCode(204);
                    sign.setMsg("该用户第一次签到，插入数据失败!");
                    sign.setContinueSignCount(signObject.getSignCount());
                    sign.setLastModifyDate(signObject.getLastModifyTime());
                    sign.setSignHistory(signObject.getSignHistory());
                    sign.setTotalScore(signObject.getIntegration());

                }

            }
        } catch (SQLException e) {
            sign.setCode(404);
            sign.setMsg("平台或者db异常！");
            sign.setContinueSignCount(signObject.getSignCount());
            sign.setLastModifyDate(signObject.getLastModifyTime());
            sign.setSignHistory(signObject.getSignHistory());
            sign.setTotalScore(signObject.getIntegration());
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            try {
                DbUtils.close(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbUtils.closeQuietly(conn);
        }
        return sign;
    }

    @Override
    public Sign getGiftPackThenReturn(int uid, int gid, int giftPackScore) {
        Sign sign = new Sign();
        ResultSet rs = null;
        SignObject signObject = null;
        PreparedStatement preparedStatement = null, executePreparedStatement = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            preparedStatement = conn.prepareStatement(" select * from db_userplatform.dt_sign where userid=? and gameid=?  ;");

            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, gid);

            rs = preparedStatement.executeQuery();
            if (rs.next()) { //如果查到了减去积分
                signObject = new SignObject();

                signObject.setId(rs.getInt("id"));
                signObject.setUid(rs.getInt("userid"));
                signObject.setGid(rs.getInt("gameid"));
                signObject.setSignCount(rs.getInt("signCount"));
                signObject.setIntegration(rs.getInt("integration"));
                signObject.setLastModifyTime(new Timestamp(rs.getDate("lastModifyTime").getTime()));
                signObject.setSignHistory(rs.getLong("signHistory"));
                signObject.setExt(rs.getString("ext"));

                if(signObject.getIntegration() - giftPackScore>=0)
                {
                executePreparedStatement = conn.prepareStatement(" update db_userplatform.dt_sign set integration=? where id=? ;");
                executePreparedStatement.setInt(1, signObject.getIntegration() - giftPackScore);
                executePreparedStatement.setInt(2, signObject.getId());

                int effectRows = executePreparedStatement.executeUpdate();

                if (effectRows >= 1) {
                    sign.setCode(200);
                    sign.setMsg("成功领取礼包，积分消耗" + giftPackScore);
                    sign.setLastModifyDate(signObject.getLastModifyTime());
                    sign.setContinueSignCount(signObject.getSignCount());
                    sign.setSignHistory(signObject.getSignHistory());
                    sign.setTotalScore(signObject.getIntegration() - giftPackScore);
                } else { //减去积分失败
                    sign.setCode(400);
                    sign.setMsg("领取礼包失败,积分没有减去！");
                    sign.setContinueSignCount(signObject.getSignCount());
                    sign.setLastModifyDate(signObject.getLastModifyTime());
                    sign.setSignHistory(signObject.getSignHistory());
                    sign.setTotalScore(signObject.getIntegration());
                }
                }
           else { //，说明积分不够 返回300
                sign.setCode(300);
                sign.setMsg("积分不够领取礼包！");
                sign.setContinueSignCount(signObject.getSignCount());
                sign.setLastModifyDate(signObject.getLastModifyTime());
                sign.setSignHistory(signObject.getSignHistory());
                sign.setTotalScore(signObject.getIntegration());
            }
            }
        } catch (Exception e) {//发生异常则是404
            sign.setCode(404);
            sign.setMsg("平台或db异常");
            sign.setContinueSignCount(signObject.getSignCount());
            sign.setLastModifyDate(signObject.getLastModifyTime());
            sign.setSignHistory(signObject.getSignHistory());
            sign.setTotalScore(signObject.getIntegration());
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            try {
                DbUtils.close(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbUtils.closeQuietly(conn);
        }
        return sign;
    }

    @Override
    public int modifySignTime(int uid, int gid, int days) {
        int rel = 0;
        Sign sign = new Sign();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null, executePreparedStatement = null;
        try {
            conn = getQueryRunner().getDataSource().getConnection();
            preparedStatement = conn.prepareStatement(" update  db_userplatform.dt_sign set lastModifyTime=? where userid=? and gameid=?  ;");

            java.sql.Date date = new java.sql.Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * days);
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, uid);
            preparedStatement.setInt(3, gid);

            rel = preparedStatement.executeUpdate();

        } catch (Exception e) {//发生异常则是404
            sign.setCode(404);
            sign.setMsg("平台或db异常");
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            try {
                DbUtils.close(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbUtils.closeQuietly(conn);
        }
        return rel;
    }
}
