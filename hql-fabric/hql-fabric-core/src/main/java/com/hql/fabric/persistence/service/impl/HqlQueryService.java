package com.hql.fabric.persistence.service.impl;

import com.hql.fabric.persistence.entity.BaseEntity;
import com.hql.fabric.persistence.processor.IQueryPostProcessor;
import com.hql.fabric.persistence.query.HqlQueryRequest;
import com.hql.fabric.persistence.service.IHqlQueryService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("hqlQueryService")
public class HqlQueryService implements IHqlQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(HqlQueryService.class);
    private final SessionFactoryImplementor sessionFactory;

    public HqlQueryService(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
    }


    @Override
    public Session openSession() {
        return null;
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql) {
        return List.of();
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql, Object... params) {
        return List.of();
    }

    @Override
    public List query(String hql, IQueryPostProcessor post, Object... params) {
        return List.of();
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql, Map<String, Object> namedParams, IQueryPostProcessor post) {
        return List.of();
    }

    @Override
    public <T extends BaseEntity> List<T> pagedQuery(String hql, Map<String, Object> namedParameters, Integer pageStart, Integer pageSize) {
        return List.of();
    }

    @Override
    public <T extends BaseEntity> List<T> pagedQuery(String hql, Map<String, Object> namedParameters, Integer pageStart, Integer pageSize, IQueryPostProcessor post) {
        return List.of();
    }

    @Override
    public <T extends BaseEntity> T save(T item) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T save(T item, boolean saveOrUpdate) throws HibernateException {
        return null;
    }

    @Override
    public <T extends BaseEntity> T delete(T item) throws HibernateException {
        return null;
    }

    @Override
    public <T extends BaseEntity> List<T> saveAll(List<T> itemList) {
        return List.of();
    }

    @Override
    public <T extends BaseEntity> List<T> mergeAll(List<T> itemList) throws HibernateException {
        return List.of();
    }

    @Override
    public  <T extends BaseEntity> List<T> sqlQuery(String sql, Object... params) {
        return List.of();
    }

    @Override
    public  <T extends BaseEntity> List<T> sqlQueryLimit(String sql, int limit, Object... params) {
        return List.of();
    }

    @Override
    public List<Object[]> sqlQueryArray(String sql, Object... params) {
        return List.of();
    }

    @Override
    public int sqlUpdate(String sql, Object... params) {
        return 0;
    }

    @Override
    public <T extends BaseEntity> T findObjectByName(Class<T> clazz, String name) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findSimpleObjectById(Class<T> clazz, String objId, String typeName) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findObjectByName(Class<T> clazz, String name, IQueryPostProcessor post) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findObjectByIdOrName(Class<T> clazz, String idName, IQueryPostProcessor post) {
        return null;
    }

    @Override
    public Object querySingle(String hql) {
        return null;
    }

    @Override
    public Object querySingle(String hql, Map<String, Object> params) {
        return null;
    }

    @Override
    public int executeQuery(String hql, Map<String, Object> params) {
        return 0;
    }

    @Override
    public Object querySingle(String hql, Map<String, Object> params, IQueryPostProcessor post) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findOrSave(String hql, Map<String, Object> params, T item) {
        return null;
    }

    @Override
    public <T extends BaseEntity> List<T> query(HqlQueryRequest request) {
        return this.query(request.getHql(), request.getParameters());
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql, Map<String, Object> params) {
        return List.of();
    }

    // -- getter && setter --
    public SessionFactoryImplementor getSessionFactory() {
        return sessionFactory;
    }
}
