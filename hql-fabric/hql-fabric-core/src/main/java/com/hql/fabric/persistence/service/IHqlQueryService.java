package com.hql.fabric.persistence.service;

import com.hql.fabric.persistence.entity.Artifact;
import com.hql.fabric.persistence.query.HqlQueryRequest;

import java.util.List;
import java.util.Map;

public interface IHqlQueryService {

    <T extends Artifact> List<T> query(String hql, Map<String, Object> params);

    <T extends Artifact> List<T> query(HqlQueryRequest request);
}
