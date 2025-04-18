package com.hql.fabric.persistence.query.operator;

public enum WhereClause {
    EQUALS,
    IN,
    /**
     * This gonna be used in other types of 'in', like elements/index querying.
     */
    IN_NO_PARENS,
    LIKE,
    AND,
    OR,
    OPEN_SCOPE,
    CLOSE_SCOPE,
    NULL,
    NOT_NULL,
    MAP,
    SUB_QUERY,
    GREATER_THAN,
    LESS_THAN,
    NOT_EQUAL,
    GREATER_EQUAL_THAN,
    NOT,
    LESS_EQUAL_THAN
}
