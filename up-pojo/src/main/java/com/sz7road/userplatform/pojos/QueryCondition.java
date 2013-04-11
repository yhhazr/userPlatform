package com.sz7road.userplatform.pojos;

/**
 * User: leo.liao
 * Date: 12-6-13
 * Time: 上午11:20
 */
public class QueryCondition {

    private String field;

    private String value;

    private String symbol;

    public QueryCondition(String field, String symbol, String value) {
        this.field = field;
        this.symbol = symbol;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
