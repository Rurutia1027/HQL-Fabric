package com.hql.fabric.persistence.query;

import com.hql.fabric.persistence.query.builder.Condition;
import com.hql.fabric.persistence.query.builder.Join;
import com.hql.fabric.persistence.query.builder.SelectClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
}
