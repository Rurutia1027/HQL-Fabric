package com.hql.fabric.persistence.service;

import com.hql.fabric.persistence.query.HqlQueryRequest;

import java.util.List;
import java.util.Map;

public interface IHqlQueryService {

    <T> List<T> query(String hql, Map<String, Object> params);
    <T> List<T> query(HqlQueryRequest request);
}
