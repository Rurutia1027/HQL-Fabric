package com.hql.fabric.persistence.service.impl;

import com.hql.fabric.persistence.entity.BaseEntity;
import com.hql.fabric.persistence.processor.IQueryPostProcessor;
import com.hql.fabric.persistence.query.builder.ArrayRowBuilder;
import com.hql.fabric.persistence.query.builder.HqlQueryRequest;
import com.hql.fabric.persistence.query.builder.MapRowBuilder;
import com.hql.fabric.persistence.service.IHqlQueryService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("hqlQueryService")
public class HqlQueryService implements IHqlQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(HqlQueryService.class);
    private static final Object[] EMPTY = {};

    private final SessionFactoryImplementor sessionFactory;
    private final MapRowBuilder mapRowBuilder;
    private final ArrayRowBuilder arrayRowBuilder;

    public HqlQueryService(EntityManagerFactory entityManagerFactory) {
        super();
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        this.mapRowBuilder = new MapRowBuilder();
        this.arrayRowBuilder = new ArrayRowBuilder();
    }

    @Override
    public Session openSession() {
        return this.sessionFactory.openSession();
    }

    public void shutdown() {
        if (!this.sessionFactory.isClosed()) {
            this.sessionFactory.close();
        }
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql) {
        return this.query(hql, EMPTY);
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql, Object... params) {
        return query(hql, null, params);
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql, IQueryPostProcessor post, Object... params) {
        Session session = null;

        try {
            session = openSession();
            Query<T> query = session.createQuery(hql);
            for (int i = 0; i < params.length; i++) {
                query = query.setParameter(i, params[i]);
            }

            List<T> result = query.list();
            if (Objects.nonNull(post)) {
                return post.processListResult(result);
            } else {
                return result;
            }

        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while executing query {}", hql, e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while executing query {}", hql, e);
            }
            throw e;
        } finally {
            close(session);
        }
    }


    @Override
    public <T extends BaseEntity> List<T> query(String hql, Map<String, Object> params) {
        return this.query(hql, params, null);
    }

    @Override
    public <T extends BaseEntity> List<T> query(String hql, Map<String, Object> namedParams, IQueryPostProcessor post) {
        Session session = null;
        if (Objects.isNull(namedParams)) {
            LOG.debug("Expect query parameters, but not provided");
            return null;
        }
        try {
            session = openSession();
            Query<T> query = session.createQuery(hql);
            for (Map.Entry<String, Object> entry : namedParams.entrySet()) {
                query = query.setParameter(entry.getKey(), entry.getValue());
            }

            List<T> result = query.list();

            if (Objects.isNull(post)) {
                return post.processListResult(result);
            } else {
                return result;
            }

        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while executing hql {}", hql, e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while executing hql {}", hql, e);
            }
            throw e;
        } finally {
            close(session);
        }
    }

    @Override
    public <T extends BaseEntity> List<T> pagedQuery(String hql, Map<String, Object> namedParameters, Integer pageStart, Integer pageSize) {
        return pagedQuery(hql, namedParameters, pageStart, pageSize, null);
    }

    @Override
    public <T extends BaseEntity> List<T> pagedQuery(String hql, Map<String, Object> namedParameters, Integer pageStart, Integer pageSize, IQueryPostProcessor post) {
        Session session = null;
        if (Objects.isNull(pageStart) || Objects.isNull(pageSize)) {
            LOG.debug("Both pageStart and pageSize are required, but not provided.");
            return null;
        }
        try {
            session = openSession();
            Query<T> query = session.createQuery(hql);
            for (Map.Entry<String, Object> entry : namedParameters.entrySet()) {
                query = query.setParameter(entry.getKey(), entry.getValue());
            }
            query.setFirstResult(pageStart);
            query.setMaxResults(pageSize);

            List<T> result = query.list();

            if (Objects.nonNull(post)) {
                return post.processListResult(result);
            } else {
                return result;
            }
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while executing hql {}", hql, e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while executing hql {}", hql, e);
            }
            throw e;
        } finally {
            close(session);
        }
    }

    @Override
    public <T extends BaseEntity> T save(T item) {
        Session session = null;
        Transaction trx = null;

        try {
            session = openSession();
            trx = session.beginTransaction();
            session.persist(item);
            trx.commit();
            return item;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while executing saving item {}, gonna rollback", item,
                        e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while executing saving item {}, gonna rollback",
                        item, e);
            }

            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
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



    // -- getter && setter --
    public SessionFactoryImplementor getSessionFactory() {
        return sessionFactory;
    }

    private void rollback(Transaction trx) {
        if (Objects.nonNull(trx)) {
            try {
                trx.rollback();
            } catch (HibernateException e) {
                LOG.error("Error rolling back Transaction", e);
            }
        }
    }

    private void close(Session session) {
        if (Objects.nonNull(session)) {
            try {
                session.close();
            } catch (HibernateException e) {
                LOG.error("Error closing Session", e);
            }
        }
    }
}
