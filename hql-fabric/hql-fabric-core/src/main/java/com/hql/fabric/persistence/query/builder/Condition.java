package com.hql.fabric.persistence.query.builder;

/**
 * HqlQueryBuilder internal container for conditional elements of an HQL query.
 */
public class Condition {
    private String field;
    private String value;
    private String mapValue;
    private String subQuery;
    private WhereClause operator;

    public Condition(String field, WhereClause operator, String value) {
        this.field = field;
        this.value = value;
        this.operator = operator;
    }

    public Condition(String field, WhereClause operator, String value, String mapValue) {
        this.field = field;
        this.value = value;
        this.mapValue = mapValue;
        this.operator = operator;
    }

    public Condition(String subQuery, WhereClause operator) {
        this.subQuery = subQuery;
        this.operator = operator;
    }

    // -- getter && setter --

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMapValue() {
        return mapValue;
    }

    public void setMapValue(String mapValue) {
        this.mapValue = mapValue;
    }

    public String getSubQuery() {
        return subQuery;
    }

    public void setSubQuery(String subQuery) {
        this.subQuery = subQuery;
    }

    public WhereClause getOperator() {
        return operator;
    }

    public void setOperator(WhereClause operator) {
        this.operator = operator;
    }
}
