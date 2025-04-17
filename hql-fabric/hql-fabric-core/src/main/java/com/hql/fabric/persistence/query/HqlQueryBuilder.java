package com.hql.fabric.persistence.query;

import com.hql.fabric.persistence.query.builder.Condition;
import com.hql.fabric.persistence.query.builder.Join;
import com.hql.fabric.persistence.query.builder.JoinEnum;
import com.hql.fabric.persistence.query.builder.SelectClause;
import com.hql.fabric.persistence.query.builder.WhereClause;
import com.hql.fabric.persistence.query.builder.WithOperator;
import com.hql.fabric.persistence.query.exception.HqlBuildException;
import io.micrometer.common.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Builder class for building HQL queries.
 * Builder components can be called order independent and will be constructed correctly.
 * Examples can be found in the associated test classes.
 */
public class HqlQueryBuilder {
    private Map<String, String> fromMap = new LinkedHashMap<>();
    private List<Join> joins = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private Map<String, String> orderBy = new LinkedHashMap<>();
    private SelectClause selectClause = SelectClause.SELECT;
    private String operatorField;
    private Map<String, Object> injectionParameters = new HashMap<>();
    private Integer tokenCount = 0;
    private boolean distinct = false;
    private String groupBy;
    private String having;

    public Map<String, Object> getInjectionParameters() {
        return new HashMap<>(injectionParameters);
    }

    /**
     * Used to register a Class type with the builder for a query.
     *
     * @param clazz Class type to access.
     * @return builder instance
     */
    public HqlQueryBuilder from(Class clazz) {
        fromMap.put(clazz.getName(), null);
        return this;
    }

    /**
     * Used to register a Class type with the builder by name.
     *
     * @param clazz Name of the class to access
     * @return builder instance
     */
    public HqlQueryBuilder from(String clazz) {
        fromMap.put(clazz, null);
        return this;
    }

    /**
     * Used to register a Class with the builder as an alias.
     * Later calls to elements of the class may use this alias.
     *
     * @param clazz Type of class to access.
     * @param alias Alias for the class to be used in queries.
     * @return builder
     */
    public HqlQueryBuilder fromAs(Class clazz, String alias) {
        fromMap.put(clazz.getName(), alias);
        return this;
    }

    /**
     * Used to register a Class type with the builder by name and associate it to an alias
     *
     * @param clazz Name of the class to be access
     * @param alias alias for that resource
     * @return builder
     */
    public HqlQueryBuilder fromAs(String clazz, String alias) {
        fromMap.put(clazz, alias);
        return this;
    }

    /**
     * Used to create a left join of the registered entity with provided alias
     *
     * @param entity name of the registered entity to be joined.
     * @param alias  Alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder leftJoin(String entity, String alias) {
        joins.add(new Join(JoinEnum.LEFT_OUTER, entity, alias));
        return this;
    }

    /**
     * Used to create a left join of the registered entity with provided alias
     *
     * @param clazz Class of the registered entity to be joined.
     * @param alias Alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder leftJoin(Class clazz, String alias) {
        joins.add(new Join(JoinEnum.LEFT_OUTER, clazz.getName(), alias));
        return this;
    }

    /**
     * Creates a left join of the given entity with the given alias, forcing a fetch of the
     * joined result.
     *
     * @param entity the entity
     * @param alias  the alias
     * @return the builder with the join added
     */
    public HqlQueryBuilder leftJoinFetch(String entity, String alias) {
        joins.add(new Join(JoinEnum.LEFT_OUTER, entity, alias, true));
        return this;
    }

    /**
     * Creates a left join of the given entity with the given alias,forcing a fetch of the joined result.
     *
     * @param clazz the class of the entity to join.
     * @param alias the alias
     * @return the builder with the join added.
     */
    public HqlQueryBuilder leftJoinFetch(Class clazz, String alias) {
        joins.add(new Join(JoinEnum.LEFT_OUTER, clazz.getName(), alias, true));
        return this;
    }

    public HqlQueryBuilder leftJoinWithEq(String entity, String alias, String lhs,
                                          String rhs) {
        String token = getNextToken();
        injectionParameters.put(token, rhs);
        joins.add(new Join(JoinEnum.LEFT_OUTER, entity, alias, lhs, WithOperator.EQUAL,
                ":" + token));
        return this;
    }

    /**
     * Used to create a right join of the registered entity with provided alias.
     *
     * @param entity name of the registered entity to be joined.
     * @param alias  alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder rightJoin(String entity, String alias) {
        joins.add(new Join(JoinEnum.RIGHT_OUTER, entity, alias));
        return this;
    }

    /**
     * Used to create a right join of the registered entity with provided alias.
     *
     * @param clazz Class of the registered entity to be joined.
     * @param alias alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder rightJoin(Class clazz, String alias) {
        joins.add(new Join(JoinEnum.RIGHT_OUTER, clazz.getName(), alias));
        return this;
    }

    /**
     * Used to create an inner join of the registered entity with provided alias.
     *
     * @param entity name of the registered entity to be joined.
     * @param alias  alias of the joined element
     * @return builder
     */
    public HqlQueryBuilder innerJoin(String entity, String alias) {
        joins.add(new Join(JoinEnum.INNER, entity, alias));
        return this;
    }

    /**
     * Used to create an inner join of the registered entity with provided alias
     *
     * @param clazz Class of the registered entity to be joined
     * @param alias alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder innerJoin(Class clazz, String alias) {
        joins.add(new Join(JoinEnum.INNER, clazz.getName(), alias));
        return this;
    }

    /**
     * Used to create a full join of the registered entity with provided alias.
     *
     * @param entity name of the registered entity to joined.
     * @param alias  alias of the joined element.
     * @return build
     */
    public HqlQueryBuilder fullJoin(String entity, String alias) {
        joins.add(new Join(JoinEnum.FULL, entity, alias));
        return this;
    }

    /**
     * Used to create a full join of the registered entity with provided alias.
     */

    public HqlQueryBuilder like(String field, String value) {
        String token = getNextToken();
        injectionParameters.put(token, value);
        conditions.add(new Condition(field, WhereClause.LIKE, ":" + token));
        return this;
    }

    /**
     * Used to set the operator for the query to SELECT
     *
     * @param field Additional fields required for the select.
     * @return builder
     */
    public HqlQueryBuilder select(String field) {
        selectClause = SelectClause.SELECT;
        operatorField = field;
        return this;
    }

    /**
     * User to set the operator for the query to DELETE
     *
     * @return builder
     */
    public HqlQueryBuilder delete() {
        selectClause = SelectClause.DELETE;
        operatorField = "";
        return this;
    }

    /**
     * Used to add a conditional where a field is equal to a value
     *
     * @param field field on which equality will be evaluted
     * @param value value the equality expects
     * @return builder
     */
    public HqlQueryBuilder eq(String field, String value) {
        String token = getNextToken();
        injectionParameters.put(token, value);
        conditions.add(new Condition(field, WhereClause.EQUALS, ":" + token));
        return this;
    }


    /////////////////////////// --- common functions --- ////////////////////////

    /**
     * Used to create the HQL statement with the previous given information.
     *
     * @return HQL String
     * @throws HqlBuildException
     */
    public String build() throws HqlBuildException {
        StringBuilder stringBuilder = new StringBuilder();
        if (Objects.nonNull(operatorField)) {
            switch (selectClause) {
                case SELECT_MAP:
                    stringBuilder.append("SELECT NEW MAP");
                    break;
                case COUNT:
                    stringBuilder.append("SELECT COUNT");
                    break;
                case DELETE:
                    stringBuilder.append("DELETE");
                    break;
                default:
                    stringBuilder.append(selectClause.name());
            }
            stringBuilder.append(" ");
            if (selectClause.isFn()) {
                stringBuilder.append("(");
            }
            if (distinct && !operatorField.equals("*")) {
                stringBuilder.append("DISTINCT ");
            }
            stringBuilder.append(operatorField);
            if (selectClause.isFn()) {
                stringBuilder.append(")");
            }
            stringBuilder.append(" ");
        }

        stringBuilder.append("FROM ");
        String delimiter = "";

        for (Map.Entry<String, String> fromEntry : fromMap.entrySet()) {
            stringBuilder.append(delimiter).append(fromEntry.getKey());
            if (fromEntry.getValue() != null) {
                stringBuilder.append(" as ").append(fromEntry.getValue());
            }
            delimiter = ", ";
        }

        for (Join join : joins) {
            join.append(stringBuilder);
        }

        if (!conditions.isEmpty()) {
            stringBuilder.append(" WHERE ");
            for (Condition condition : conditions) {
                if (condition.getField() != null) {
                    stringBuilder.append(condition.getField());
                }
                switch (condition.getOperator()) {
                    case EQUALS:
                        stringBuilder.append(" = ").append(condition.getValue());
                        break;
                    case NOT_EQUAL:
                        stringBuilder.append(" <> ").append(condition.getValue());
                        break;
                    case IN:
                        stringBuilder.append(" in (").append(condition.getValue()).append(")");
                        break;
                    case IN_NO_PARENS:
                        stringBuilder.append(" in ").append(condition.getValue());
                        break;
                    case LIKE:
                        stringBuilder.append(" like ").append(condition.getValue());
                        break;
                    case AND:
                        stringBuilder.append(" and ");
                        break;
                    case NOT:
                        stringBuilder.append(" not ");
                        break;
                    case OR:
                        stringBuilder.append(" or ");
                        break;
                    case OPEN_SCOPE:
                        stringBuilder.append(" (");
                        break;
                    case CLOSE_SCOPE:
                        stringBuilder.append(") ");
                        break;
                    case NULL:
                        stringBuilder.append(" is null ");
                        break;
                    case NOT_NULL:
                        stringBuilder.append(" is not null ");
                        break;
                    case MAP:
                        stringBuilder.append("[").append(condition.getValue()).append("] = ").append(condition.getMapValue());
                        break;
                    case SUB_QUERY:
                        stringBuilder.append(" (").append(condition.getSubQuery()).append(") ");
                        break;
                    case GREATER_THAN:
                        stringBuilder.append(" > ").append(condition.getValue());
                        break;
                    case LESS_THAN:
                        stringBuilder.append(" < ").append(condition.getValue());
                        break;
                    case GREATER_EQUAL_THAN:
                        stringBuilder.append(" >= ").append(condition.getValue());
                        break;
                    case LESS_EQUAL_THAN:
                        stringBuilder.append(" <= ").append(condition.getValue());
                        break;
                    default:
                        throw new HqlBuildException("ERROR: syntax error at or near \"WHERE\". Invalid operator found for condition: " + condition.getField());
                }
            }
        }

        if (!orderBy.isEmpty()) {
            stringBuilder.append(" ORDER BY ");
            delimiter = "";
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                stringBuilder.append(delimiter).append(entry.getKey()).append(" ").append(entry.getValue());
                delimiter = ", ";
            }
        }

        if (!StringUtils.isEmpty(groupBy)) {
            stringBuilder.append(" GROUP BY " + groupBy);
        }
        if (!StringUtils.isEmpty(having)) {
            stringBuilder.append(" having " + having);
        }

        return stringBuilder.toString();
    }

    /**
     * This function should be invoked each time after calling build to reset the builder
     * for use in the same scope.
     */
    public void clear() {
        this.fromMap.clear();
        this.joins.clear();
        this.conditions.clear();
        this.orderBy.clear();
        this.selectClause = SelectClause.SELECT;
        this.operatorField = null;
        this.injectionParameters.clear();
        this.tokenCount = 0;
    }

    private String getNextToken() {
        return "_" + tokenCount++;
    }
}
