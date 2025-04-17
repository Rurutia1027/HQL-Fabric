package com.hql.fabric.persistence.service.impl;

import com.hql.fabric.persistence.query.HqlQueryRequest;
import com.hql.fabric.persistence.service.IHqlQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("hqlQueryService")
public class HqlQueryService implements IHqlQueryService {
    @Override
    public <T> List<T> query(String hql, Map<String, Object> params) {
        return List.of();
    }

    @Override
    public <T> List<T> query(HqlQueryRequest request) {
        return List.of();
    }
}
