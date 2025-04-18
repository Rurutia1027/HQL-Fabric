package com.hql.fabric.persistence.query.operator;

/**
 * Represents an HQL operator used in a with clause (part of a join operation)
 */
public enum WithOperator {
    EQUAL("="),
    NOT_EQUAL("<>");

    /**
     * The HQL operator represented by this enumeration.
     */
    private final String hql;

    /**
     * Creates a new instance of WithOperator
     *
     * @param hql the HQL used to implement the operator represented by the enumeration.
     */
    WithOperator(String hql) {
        this.hql = hql;
    }

    /**
     * Gets the HQL operator associated with this enumeration.
     *
     * @return the HQL operator
     */
    public String getHql() {
        return this.hql;
    }

}
