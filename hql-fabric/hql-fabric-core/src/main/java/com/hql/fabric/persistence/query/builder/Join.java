package com.hql.fabric.persistence.query.builder;

/**
 * Internal container for Join operations of an HQL query.
 */
public class Join {
    private JoinEnum joinType;
    private String fromTable;
    private String toTable;
    private boolean fetch;
    private String withLeft;
    private WithOperator withOpt;
    private String withRight;
}
