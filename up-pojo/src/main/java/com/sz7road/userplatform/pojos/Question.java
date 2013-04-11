package com.sz7road.userplatform.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Author: jiangfan.zhou
 * 密保问题答案
 */
public class Question implements Serializable {
    private int id;
    private int userId;
    private String question;
    private String answer;
    private int status;
    private Timestamp addTime;

    public Question(){}

    public Question(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public Question(int userId, String question, String answer, int status){
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.status = status;
    }

    public Question(int userId, String question, String answer, int status, Timestamp addTime){
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.status = status;
        this.addTime = addTime;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }
}
