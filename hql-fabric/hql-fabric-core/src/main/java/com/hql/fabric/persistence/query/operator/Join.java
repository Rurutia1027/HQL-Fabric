package com.hql.fabric.persistence.query.operator;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Internal container for Join operations of an HQL query.
 */
public class Join {
    private JoinEnum joinType;
    private String fromTable;
    private String toTable;
    private boolean fetch;
    private String withLeft;
    private WithOperator withOp;
    private String withRight;

    /**
     * Creates a new instance of Join.
     *
     * @param joinType  the join type
     * @param fromTable the table that is the left side of the join operation.
     * @param toTable   the table that is the right side of the join operation
     */
    public Join(JoinEnum joinType, String fromTable, String toTable) {
        this(joinType, fromTable, toTable, false);
    }

    /**
     * Creates a new instance of this.
     *
     * @param joinType  the join type
     * @param fromTable the table that is the left side of the join operation
     * @param toTable   the table that is the right side of the join operation
     * @param fetch     a flag that indicates whether to force fetch the right side table
     */
    public Join(JoinEnum joinType, String fromTable, String toTable, boolean fetch) {
        this(joinType, fromTable, toTable, fetch, null, null, null);
    }

    /**
     * Creates a join that includes a with condition
     *
     * @param joinType  the join type
     * @param fromTable the table on the left side of the join operation.
     * @param toTable   the table on the right side of the join operation
     * @param withLeft  the table on the left side of the with condition
     * @param withOp    the with operator
     * @param withRight the value on the right side of the with condition
     */
    public Join(JoinEnum joinType, String fromTable, String toTable, String withLeft,
                WithOperator withOp, String withRight) {
        this(joinType, fromTable, toTable, false, withLeft, withOp, withRight);
    }

    /**
     * Creates a join that includes a with condition
     *
     * @param joinType  the join type
     * @param fromTable the table on the left side of the join operation
     * @param toTable   the table on the right side of the join operation
     * @param fetch     a flag that indicates whether to force fetch the right side table
     * @param withLeft  the table on the left side of the with condition
     * @param withOp    the with operator
     * @param withRight the value on the right side of the with condition
     */
    public Join(JoinEnum joinType, String fromTable, String toTable, boolean fetch, String withLeft, WithOperator withOp, String withRight) {
        this.joinType = joinType;
        this.fromTable = fromTable;
        this.toTable = toTable;
        this.fetch = fetch;
        this.withLeft = withLeft;
        this.withOp = withOp;
        this.withRight = withRight;
    }

    /**
     * Determines whether this join has a valid with condition
     *
     * @return {@code true} if this join has a valid with condition (left side, operator,
     * and right side) {@code false} otherwise.
     */
    public boolean isWithConditionValid() {
        return (StringUtils.hasText(this.withLeft) && Objects.nonNull(this.withOp)
                && StringUtils.hasText(this.withRight));
    }

    /**
     * Appends the HQL representation of this join operation to the given StringBuilder.
     *
     * @param builder the StringBuilder where the HQL query is being accumulated.
     */
    public void append(StringBuilder builder) {
        builder.append(" ").append(this.joinType.getHql()).append(" ");
        if (isFetch()) {
            builder.append("FETCH ");
        }
        builder.append(this.fromTable).append(" ").append(this.toTable);
        if (isWithConditionValid()) {
            builder.append(" WITH ").append(this.withLeft).append(" ").append(this.withOp.getHql()).
                    append(" ").append(this.withRight);
        }
    }

    // -- getter && setter --
    public JoinEnum getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinEnum joinType) {
        this.joinType = joinType;
    }

    public String getFromTable() {
        return fromTable;
    }

    public void setFromTable(String fromTable) {
        this.fromTable = fromTable;
    }

    public String getToTable() {
        return toTable;
    }

    public void setToTable(String toTable) {
        this.toTable = toTable;
    }

    public boolean isFetch() {
        return fetch;
    }

    public void setFetch(boolean fetch) {
        this.fetch = fetch;
    }

    public String getWithLeft() {
        return withLeft;
    }

    public void setWithLeft(String withLeft) {
        this.withLeft = withLeft;
    }

    public WithOperator getWithOp() {
        return withOp;
    }

    public void setWithOp(WithOperator withOp) {
        this.withOp = withOp;
    }

    public String getWithRight() {
        return withRight;
    }

    public void setWithRight(String withRight) {
        this.withRight = withRight;
    }
}
