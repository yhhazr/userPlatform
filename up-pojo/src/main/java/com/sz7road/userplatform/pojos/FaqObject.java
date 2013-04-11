package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-14
 * Time: 下午6:19
 * 常见问题实体类
 */
public class FaqObject implements Serializable {
    //主键标识ID
    private int id;
    //种类ID
    private int cid;
    //问题
    private String question;
    //答案
    private String answer;
    //点击次数
    private int visitSum;
    // 排序数字
    private int sortNum;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public int getVisitSum() {
        return visitSum;
    }

    public void setVisitSum(int visitSum) {
        this.visitSum = visitSum;
    }


    @Override
    public String toString() {
        return "{ id: " + id + " cid: " + cid + " question: " + question +
                " answer: " + answer + " visitSum: " + visitSum + " sortNum:" + sortNum + " }";
    }
}
