package com.hql.fabric.persistence.service.impl;

import com.hql.fabric.persistence.entity.Artifact;
import com.hql.fabric.persistence.query.HqlQueryRequest;
import com.hql.fabric.persistence.service.IHqlQueryService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("hqlQueryService")
public class HqlQueryService implements IHqlQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(HqlQueryService.class);
    private final SessionFactoryImplementor sessionFactory;

    public HqlQueryService(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
    }

    @Override
    public <T extends Artifact> List<T> query(String hql, Map<String, Object> params) {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery(hql);
            if (Objects.nonNull(params) && !CollectionUtils.isEmpty(params)) {
                params.forEach(query::setParameter);
            }
            return query.list();
        }
    }

    @Override
    public <T extends Artifact> List<T> query(HqlQueryRequest request) {
        return this.query(request.getHql(), request.getParameters());
    }

    // -- getter && setter --
    public SessionFactoryImplementor getSessionFactory() {
        return sessionFactory;
    }
}
