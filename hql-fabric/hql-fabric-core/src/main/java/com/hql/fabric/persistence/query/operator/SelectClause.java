package com.hql.fabric.persistence.query.operator;

public enum SelectClause {
    SELECT,
    SELECT_MAP(true),
    UPDATE,
    INSERT,
    DELETE,
    AVERAGE(true),
    COUNT(true),
    MAX(true),
    MIN(true);

    private boolean fn = false;

    SelectClause(boolean fn) {
        this.fn = fn;
    }

    SelectClause() {
    }

    /**
     * @return Whether the selection clause is a function that
     * takes an operator field. E.g., "count(entity)" versus "select entity".
     */
    public boolean isFn() {
        return fn;
    }
}
