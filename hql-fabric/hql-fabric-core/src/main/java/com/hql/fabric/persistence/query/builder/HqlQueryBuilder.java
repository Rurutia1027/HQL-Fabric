package com.hql.fabric.persistence.query.builder;

import com.hql.fabric.persistence.query.operator.Condition;
import com.hql.fabric.persistence.query.operator.Join;
import com.hql.fabric.persistence.query.operator.JoinEnum;
import com.hql.fabric.persistence.query.operator.SelectClause;
import com.hql.fabric.persistence.query.operator.WhereClause;
import com.hql.fabric.persistence.query.operator.WithOperator;
import com.hql.fabric.persistence.query.exception.HqlBuildException;
import io.micrometer.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
     * Used to create a full join of the registered entity with provided alias
     *
     * @param entity name of the registered entity to be joined
     * @param alias  Alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder fullJoin(String entity, String alias) {
        joins.add(new Join(JoinEnum.FULL, entity, alias));
        return this;
    }

    /**
     * Used to create a full join of the registered entity with provided alias
     *
     * @param clazz Class of the registered entity to be joined
     * @param alias Alias of the joined element.
     * @return builder
     */
    public HqlQueryBuilder fullJoin(Class clazz, String alias) {
        joins.add(new Join(JoinEnum.FULL, clazz.getName(), alias));
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
     * Used to add a conditional where a field must be in a list of values.
     *
     * @param field  field to look for values
     * @param values list of values to check for field
     * @return builder
     */
    public HqlQueryBuilder in(String field, Object... values) {
        StringBuilder stringBuilder = new StringBuilder();
        String delimiter = "";
        for (Object value : values) {
            String token = getNextToken();
            injectionParameters.put(token, value);
            stringBuilder.append(delimiter).append(":").append(token);
            delimiter = ", ";
        }
        conditions.add(new Condition(field, WhereClause.IN, stringBuilder.toString()));
        return this;
    }

    /**
     * Used to add a conditional where a field must be in a collection of values.
     *
     * @param field  field to look for in values
     * @param values a collection of values to check for field
     * @return builder
     */
    public HqlQueryBuilder in(String field, Collection<String> values) {
        StringBuilder stringBuilder = new StringBuilder();
        String delimiter = "";
        for (Object value : values) {
            String token = getNextToken();
            injectionParameters.put(token, value);
            stringBuilder.append(delimiter).append(":").append(token);
            delimiter = ", ";
        }
        conditions.add(new Condition(field, WhereClause.IN,
                stringBuilder.toString()));
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

    /**
     * Used to add a conditional where a field is not equal to a value.
     *
     * @param field field on which equality will be evaluated.
     * @param value value the equality expects
     * @return builder
     */
    public HqlQueryBuilder neq(String field, Object value) {
        String token = getNextToken();
        injectionParameters.put(token, value);
        conditions.add(new Condition(field, WhereClause.NOT_EQUAL, ":" + token));
        return this;
    }

    public HqlQueryBuilder neqField(String field1, String field2) {
        conditions.add(new Condition(field1, WhereClause.NOT_EQUAL, field2));
        return this;
    }


    /**
     * Used to add a logical conditional and between conditional statements
     * Must be added between two conditions
     *
     * @return builder
     */
    public HqlQueryBuilder and() {
        conditions.add(new Condition(null, WhereClause.AND, null));
        return this;
    }


    /**
     * Used to set the operator for the query to SELECT NEW MAP
     *
     * @param fieldMap Mapping of entries to their alias
     * @return builder
     */
    public HqlQueryBuilder selectMap(Map<String, String> fieldMap) {
        selectClause = SelectClause.SELECT_MAP;

        StringBuilder stringBuilder = new StringBuilder();
        String delimiter = "";
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            stringBuilder.append(delimiter).append(entry.getKey())
                    .append(" as ")
                    .append(entry.getValue());
            delimiter = ", ";
        }
        operatorField = stringBuilder.toString();
        return this;
    }

    /**
     * Used to select the count an entity.
     *
     * @param entity entity to count by name
     * @return builder
     */
    public HqlQueryBuilder selectCount(String entity) {
        selectClause = SelectClause.COUNT;
        operatorField = entity;
        return this;
    }

    /**
     * Used to select the count of an entity.
     *
     * @param clazz Class of the entity to select the count o n
     * @return builder
     */
    public HqlQueryBuilder selectCount(Class clazz) {
        selectClause = SelectClause.COUNT;
        operatorField = clazz.getName();
        return this;
    }

    /**
     * Used to select the count of a default entity.
     *
     * @return builder
     */
    public HqlQueryBuilder selectCount() {
        selectClause = SelectClause.COUNT;
        operatorField = "*";
        return this;
    }

    /**
     * Used to assign ordering by which the query will be returned.
     * The order in which fields are added to the builder is the order by which they will be
     * created.
     *
     * @param field     name of the field to order on
     * @param ascending true to return ascending by the field, false for descending.
     * @return builder
     */
    public HqlQueryBuilder orderBy(String field, boolean ascending) {
        orderBy.put(field, ascending ? "asc" : "desc");
        return this;
    }

    public HqlQueryBuilder groupBy(String field) {
        this.groupBy = field;
        return this;
    }

    public HqlQueryBuilder having(String operator) {
        this.having = operator;
        return this;
    }


    /**
     * Used to add a query to a query so you can query while you query data.
     * Adds a subQuery to the conditions of the current query.
     *
     * @param subHqlQueryBuilder HqlQueryBuilder containing the parts of the subQuery.
     * @return builder without modifying the subQueryBuilder.
     */
    public HqlQueryBuilder subQuery(HqlQueryBuilder subHqlQueryBuilder) {
        Map<String, String> tokensToReplace = new HashMap<>();

        for (Map.Entry<String, Object> subQueryParameter :
                subHqlQueryBuilder.getInjectionParameters().entrySet()) {
            String token = "sub" + getNextToken();
            injectionParameters.put(token, subQueryParameter.getValue());
            tokensToReplace.put(subQueryParameter.getKey(), token);
        }

        String subQuery = subHqlQueryBuilder.build();
        for (Map.Entry<String, String> tokenToReplace : tokensToReplace.entrySet()) {

            subQuery = subQuery.replaceAll(":" + tokenToReplace.getKey(), ":" + tokenToReplace.getValue());
        }
        conditions.add(new Condition(subQuery, WhereClause.SUB_QUERY));
        return this;
    }

    /**
     * Used to add a conditional greater than and value to the last field added to the
     * builder.
     *
     * @param value Value to evaluate greater than against.
     * @return builder
     */
    public HqlQueryBuilder gt(Integer value) {
        String token = getNextToken();
        injectionParameters.put(token, value.intValue());
        conditions.add(new Condition(null, WhereClause.GREATER_THAN, ":" + token));
        return this;
    }

    /**
     * Used to add a conditional greater than and value to the field.
     *
     * @param field field on which equality will be evaluated.
     * @param value value to evaluate greater than against
     * @return builder
     */
    public HqlQueryBuilder gt(String field, Integer value) {
        String token = getNextToken();
        injectionParameters.put(token, value.intValue());
        conditions.add(new Condition(field, WhereClause.GREATER_THAN, ":" + token));
        return this;
    }

    /**
     * Used to add a conditional greater and equal than and value to the field;
     *
     * @param field field on which equality will be evaluated.
     * @param value value to evaluate greater than against
     * @return builder
     */
    public HqlQueryBuilder ge(String field, Integer value) {
        String token = getNextToken();
        injectionParameters.put(token, value.intValue());
        conditions.add(new Condition(field, WhereClause.GREATER_EQUAL_THAN, ":" + token));
        return this;
    }

    /**
     * Used to add an is null condition statement.
     *
     * @param field condition is null checked on this field
     * @return builder
     */
    public HqlQueryBuilder isNull(String field) {
        conditions.add(new Condition(field, WhereClause.NULL, null));
        return this;
    }

    /**
     * Used to add an is not condition statement
     *
     * @return builder
     */
    public HqlQueryBuilder not() {
        conditions.add(new Condition(null, WhereClause.NOT, null));
        return this;
    }


    /**
     * Used to add a conditional less than and value to the last field added to the builder.
     *
     * @param value value to evaluate less than against
     * @return builder
     */
    public HqlQueryBuilder lt(Integer value) {
        String token = getNextToken();
        this.injectionParameters.put(token, value.intValue());
        conditions.add(new Condition(null, WhereClause.LESS_THAN,
                ":" + token));
        return this;
    }

    /**
     * Adds a conditional less than the given data
     *
     * @param field the field to compare
     * @param value the value to compar the field to
     * @return builder
     */
    public HqlQueryBuilder lt(String field, Date value) {
        String token = getNextToken();
        injectionParameters.put(token, value);
        conditions.add(new Condition(field, WhereClause.LESS_THAN, ":" + token));
        return this;
    }

    /**
     * Adds a conditional after (greater than) the given date
     *
     * @param field the field to compare
     * @param value the value to compare the field to
     * @return builder
     */
    public HqlQueryBuilder gt(String field, Date value) {
        String token = getNextToken();
        injectionParameters.put(token, value);
        conditions.add(new Condition(field, WhereClause.GREATER_THAN, ":" + token));
        return this;
    }

    /**
     * Adds a conditional greater or equal than the given date
     *
     * @param field the field to compare
     * @param value the value to compare the field to
     * @return builder
     */
    public HqlQueryBuilder ge(String field, Date value) {
        String token = getNextToken();
        injectionParameters.put(token, value);
        conditions.add(new Condition(field, WhereClause.GREATER_THAN,
                ":" + token));
        return this;
    }

    /**
     * Used to add a conditional less and equal than the value to the field.
     *
     * @param field field on which equality will be evaluated.
     * @param value value to evaluate less than against
     * @return builder
     */
    public HqlQueryBuilder le(String field, Integer value) {
        String token = getNextToken();
        injectionParameters.put(token, value.intValue());
        conditions.add(new Condition(field, WhereClause.LESS_EQUAL_THAN, ":" + token));
        return this;
    }

    /**
     * Used to add a conditional less than and value to the field.
     *
     * @param field field on which equality will be evaluated
     * @param value value to evaluate less than against
     * @return builder
     */
    public HqlQueryBuilder lt(String field, Integer value) {
        String token = getNextToken();
        injectionParameters.put(token, value.intValue());
        conditions.add(new Condition(field, WhereClause.LESS_THAN, ":" + token));
        return this;
    }

    /**
     * Used to add a logical conditional or between conditional statements
     * Must be added between to conditions
     *
     * @return builder
     */
    public HqlQueryBuilder or() {
        conditions.add(new Condition(null, WhereClause.OR, null));
        return this;
    }


    /**
     * Used to add an is not null conditional statement
     *
     * @param field condition is not null checked on this field
     * @return builder
     */
    public HqlQueryBuilder isNotNul(String field) {
        conditions.add(new Condition(field, WhereClause.NOT_NULL, null));
        return this;
    }

    /**
     * Used to add a logical open scope "(" to a set of condition statements.
     *
     * @return builder
     */
    public HqlQueryBuilder open() {
        conditions.add(new Condition(null, WhereClause.OPEN_SCOPE,
                null));
        return this;
    }

    /**
     * Used to add a logical close scope ")" to a set of conditional statements.
     *
     * @return builder
     */
    public HqlQueryBuilder close() {
        conditions.add(new Condition(null, WhereClause.CLOSE_SCOPE, null));
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
