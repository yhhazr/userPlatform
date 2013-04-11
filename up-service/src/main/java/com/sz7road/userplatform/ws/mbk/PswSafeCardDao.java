package com.sz7road.userplatform.ws.mbk;

import com.google.common.collect.Table;
import com.sz7road.userplatform.dao.Dao;
import com.sz7road.userplatform.dao.jdbc.JdbcDaoSupport;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-6
 * Time: 下午3:32
 * 密保卡接口
 */
public interface PswSafeCardDao{

    /**
     * 生成用户的密保卡
      * @param userId
     * @return
     */
  public PswSafeCard generateCard(int userId);

    /**
     * 根据序列号查询用户的密保卡信息
      * @param sequenceNum
     * @return
     */
  public PswSafeCard queryCardBySequenceNum(int sequenceNum);

    /**
     *  根据徐磊好查询用户的密保卡信息
     * @param userId
     * @return
     */
    public PswSafeCard queryCardByUserId(int userId);

    /**
     * 更换用户的密保卡
      * @param userId
     * @return
     */
  public PswSafeCard swapCard(int userId);

    /**
     * 验证用户的密保卡
     * @param checkItems
     * @param userId
     * @return
     */
  public boolean  checkCard(Table checkItems ,int userId);

}
