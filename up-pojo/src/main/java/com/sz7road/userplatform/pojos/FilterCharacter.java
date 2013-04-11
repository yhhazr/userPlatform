package com.sz7road.userplatform.pojos;

import java.io.Serializable;

/**
 * @author leo.liao
 */


public class FilterCharacter implements Serializable {

    private int id;
    private String content;

    public FilterCharacter() {

    }

    public FilterCharacter(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
