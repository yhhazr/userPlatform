package com.sz7road.userplatform.ws.mbk;

import com.google.common.base.Objects;
import com.google.common.collect.Table;
import com.sz7road.userplatform.Constant;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-3-6
 * Time: 下午3:15
 * 密保卡实体
 */
public class PswSafeCard implements Serializable {

    //用户id
    private int userId;
    //序列号
    private int sequenceNum;
    //密保卡
    private Table content;

    public PswSafeCard() {
    }

    public PswSafeCard(int userId, int sequenceNum, Table content) {
        this.userId = userId;
        this.sequenceNum = sequenceNum;
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public Table getContent() {
        return content;
    }

    public void setContent(Table content) {
        this.content = content;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer=new StringBuffer();
        if(content!=null&&!content.isEmpty())
        {
            for(int i=0;i<=9;i++)
            {
                for(String str: Constant.MBK_CHAR_LIST)
                {
                   stringBuffer.append("mbk["+i+"]["+str+"]="+content.get(i,str)+" ; ");
                }
            }
        }

        return Objects.toStringHelper(this).add("userId", userId)
                .add("sequenceNum", sequenceNum).add("content", stringBuffer.toString()).toString();
    }
}
