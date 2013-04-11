package com.sz7road.userplatform.ws.mbk;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.sz7road.userplatform.Constant;
import com.sz7road.userplatform.dao.jdbc.JdbcDaoSupport;
import org.apache.commons.dbutils.ResultSetHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-6
 * Time: 下午3:55
 * 密保卡接口实现类
 */
public class PswSafeCardDaoImp extends JdbcDaoSupport<PswSafeCard> implements PswSafeCardDao {

    private final String DT_MBK_TABLE = "`db_userplatform`.`dt_pswSafeCard`";
    private final String DT_MBK_INSERT = " insert into " + DT_MBK_TABLE + " (`userId`,`content`) values(?,?) ;";
    private final String DT_MBK_QUERY_BY_USER_ID = " select * from " + DT_MBK_TABLE + " where userId=? ;";
    private final String DT_MBK_QUERY_BY_SEQUENCE_NUM = " select * from " + DT_MBK_TABLE + " where sequeceNum=? ;";

    @Override
    public PswSafeCard generateCard(int userId) {
        //随机生成密保卡
        Table<Integer, String, Integer> content = HashBasedTable.create();
        for (int i = 0; i <= 9; i++) {
            for (String lie : Constant.MBK_CHAR_LIST) {
                content.put(i, lie, (new Random().nextInt(899)) + 100);
            }
        }
        //插入db
        try {
            getQueryRunner().update(DT_MBK_INSERT, userId, content);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PswSafeCard queryCardBySequenceNum(int sequenceNum) {
        return null;
    }

    @Override
    public PswSafeCard queryCardByUserId(int userId) {
        try {
            return getQueryRunner().query(DT_MBK_QUERY_BY_USER_ID, new ResultSetHandler<PswSafeCard>() {
                @Override
                public PswSafeCard handle(ResultSet rs) throws SQLException {
                    PswSafeCard pswSafeCard = new PswSafeCard();
                    while (rs != null && rs.next()) {
                        pswSafeCard.setUserId(rs.getInt("userId"));
                        pswSafeCard.setSequenceNum(rs.getInt("sequeceNum"));
                        Blob blob = rs.getBlob("content");
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(blob.getBinaryStream());
                            Table content = (Table<Integer, String, Integer>) objectInputStream.readObject();
                            pswSafeCard.setContent(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    return pswSafeCard;
                }
            }, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PswSafeCard swapCard(int userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean checkCard(Table checkItems, int userId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
