package com.hql.fabric.persistence.query;

import java.util.Map;

public class HqlQueryRequest {
    private final String hql;
    private final Map<String, Object> parameters;

    public HqlQueryRequest(String hql, Map<String, Object> parameters) {
        this.hql = hql;
        this.parameters = parameters;
    }

    public static HqlQueryRequest from(HqlQueryBuilder builder) {
        // return new HqlQueryRequest(builder.build(), builder.getInjectionParameters());
        return null;
    }

    // -- getter && setter --
    public String getHql() {
        return hql;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}
