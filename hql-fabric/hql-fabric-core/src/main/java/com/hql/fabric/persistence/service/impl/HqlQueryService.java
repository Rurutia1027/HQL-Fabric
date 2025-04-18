package com.hql.fabric.persistence.service.impl;

import com.hql.fabric.persistence.entity.BaseEntity;
import com.hql.fabric.persistence.executor.LimitExecutor;
import com.hql.fabric.persistence.executor.UpdateExecutor;
import com.hql.fabric.persistence.processor.IQueryPostProcessor;
import com.hql.fabric.persistence.query.builder.ArrayRowBuilder;
import com.hql.fabric.persistence.query.builder.HqlQueryRequest;
import com.hql.fabric.persistence.query.builder.MapRowBuilder;
import com.hql.fabric.persistence.query.builder.RowBuilder;
import com.hql.fabric.persistence.service.IHqlQueryService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.mapping.EntityMappingType;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("hqlQueryService")
public class HqlQueryService implements IHqlQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(HqlQueryService.class);
    private static final Object[] EMPTY = {};

    private final SessionFactoryImplementor sfi;
    private final MapRowBuilder mapRowBuilder;
    private final ArrayRowBuilder arrayRowBuilder;

    public HqlQueryService(EntityManagerFactory entityManagerFactory) {
        super();
        this.sfi = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        this.mapRowBuilder = new MapRowBuilder();
        this.arrayRowBuilder = new ArrayRowBuilder();
    }

    @Override
    public Session openSession() {
        return this.sfi.openSession();
    }

    public void shutdown() {
        if (!this.sfi.isClosed()) {
            this.sfi.close();
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

    /**
     * Save item to corresponding db table.
     *
     * @param item
     * @param saveOrUpdate saveOrUpdate = true, then save this record, and the record will be saved in detachedstatus.
     *                     saveOrUpdate = false, then create a new record, with allocating a new identifier ID.
     * @param <T>          data type should be subclass of {@link BaseEntity}
     * @return saved entity
     */
    @Override
    public <T extends BaseEntity> T save(T item, boolean saveOrUpdate) throws HibernateException {
        Session session = null;
        Transaction trx = null;
        T ret = null;

        try {
            session = openSession();
            trx = session.beginTransaction();
            if (saveOrUpdate) {
                ret = (T) session.merge(item);
            } else {
                session.persist(item);
                ret = item;
            }
            trx.commit();
            return ret;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while executing saving item with saveOrUpdate " +
                        "status {}, gonna rollback", saveOrUpdate, e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while executing saving item with saveOrUpdate " +
                        "status {}, gonna rollback", saveOrUpdate, e);
            }
            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
    }

    @Override
    public <T extends BaseEntity> T delete(T item) throws HibernateException {
        Session session = null;
        Transaction trx = null;

        try {
            session = openSession();
            trx = session.beginTransaction();
            session.remove(item);
            trx.commit();
            return item;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.info("JDBCException while deleting item, gonna rollback!", e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while deleting item, gonna rollback!", e);
            }
            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
    }

    /**
     * Persist a list of items of type T to the data store.
     *
     * @param itemList list of objects in type of T that gonna to be persisted to db
     * @param <T>      data type
     * @return list of item in type of T
     */
    @Override
    public <T extends BaseEntity> List<T> saveAll(List<T> itemList) {
        Session session = null;
        Transaction trx = null;
        try {
            session = openSession();
            trx = session.beginTransaction();
            for (T save : itemList) {
                session.persist(save);
            }
            trx.commit();
            return itemList;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while saving all items to db, gonna rollback!", e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while saving all items to db, gonna rollback!", e);
            }

            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
    }

    /**
     * Persist a list of item of type T to the data store.
     *
     * @param itemList list of objects to be merged to data store
     * @param <T>      the type of each item in list
     * @return list of entities stored to db successfully
     */
    @Override
    public <T extends BaseEntity> List<T> mergeAll(List<T> itemList) {
        Session session = null;
        Transaction trx = null;
        try {
            session = openSession();
            trx = session.beginTransaction();
            for (T save : itemList) {
                session.merge(save);
            }
            trx.commit();
            return itemList;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException during merge items to database, gonna rollback", e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException during merge items to db, gonna rollback", e);
            }
            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
    }


    @Override
    public List sqlQuery(String sql, Object... params) {
        return sqlQueryExecute(sql, 0, params, mapRowBuilder);
    }

    @Override
    public List sqlQueryLimit(String sql, int limit, Object... params) {
        return sqlQueryExecute(sql, limit, params, mapRowBuilder);
    }

    @Override
    public List<Object[]> sqlQueryArray(String sql, Object... params) {
        return sqlQueryExecute(sql, 0, params, arrayRowBuilder);
    }

    @Override
    public int sqlUpdate(String sql, Object... params) {
        Session session = null;
        try {
            session = openSession();
            return session.doReturningWork(new UpdateExecutor(sql, params));
        } catch (Exception e) {
            for (Object param : params) {
                sql = sql + ", " + param.toString();
            }
            LOG.error("Failed to execute sql {}, with exception {}",
                    sql, e.getMessage());
            throw e;
        } finally {
            close(session);
        }
    }

    @Override
    public <T extends BaseEntity> T findObjectByName(Class<T> clazz, String name) {
        return findObjectByName(clazz, name, null);
    }

    /**
     * Finds an object with the given unique identifier.
     *
     * @param clazz the data type of the object to search for
     * @param id    the unique identifier of the object
     * @param post  the query post-processor to run on the object (may be {@code null})
     * @param <T>   The data type of the object
     * @return the fully loaded object that corresponds to the given unique identifier, or {@code null} if no
     * such object can be found.
     */
    @Override
    public <T extends BaseEntity> T findObjectById(Class<T> clazz,
                                                   String id, IQueryPostProcessor post) {
        if (Objects.isNull(id)) {
            return null;
        }

        // build base HQL
        String hql = "from " + clazz.getName() + " where id =?";

        // run the query
        List<T> found = query(hql, post, id);
        return !found.isEmpty() ? found.get(0) : null;
    }


    /**
     * Finds an object with the given name
     *
     * @param clazz the data type of the object to search for
     * @param name  the name or unique identifier of the object
     * @param post  the query post-processor to run on the object
     * @param <T>   the data type of the object
     * @return the fully loaded object that associated with the given name or unique
     * identifier.
     */
    @Override
    public <T extends BaseEntity> T findObjectByName(Class<T> clazz, String name, IQueryPostProcessor post) {
        if (Objects.isNull(name)) {
            return null;
        }

        // Get the mapping metamodel
        EntityMappingType mappingType = sfi.getRuntimeMetamodels()
                .getMappingMetamodel()
                .findEntityDescriptor(clazz.getName());

        // build base HQL
        StringBuilder hql =
                new StringBuilder("from " + clazz.getName() + " where lower(name) = lower(?)");

        // check if 'deleted' property exists
        if (mappingType.findAttributeMapping("deleted") != null) {
            hql.append(" and delete is null");
        }

        // run the query
        List<T> found = query(hql.toString(), post, name);
        return !found.isEmpty() ? found.get(0) : null;
    }

    @Override
    public <T extends BaseEntity> T findObjectByIdOrName(Class<T> clazz, String idOrName) {
        return findObjectByIdOrName(clazz, idOrName, null);
    }

    @Override
    public <T extends BaseEntity> T findObjectByIdOrName(Class<T> clazz, String idOrName, IQueryPostProcessor post) {
        if (Objects.isNull(idOrName)) {
            return null;
        }
        String hql = "from " + clazz.getName() +
                " where (id=?0 or lower(name)=lower(?1))";
        List<T> found = query(hql, post, idOrName, idOrName);
        return found.isEmpty() ? null : found.get(0);
    }

    @Override
    public Object querySingle(String hql) {
        Map<String, Object> namedParameters = new HashMap<>();
        return querySingle(hql, namedParameters);
    }

    @Override
    public Object querySingle(String hql, Map<String, Object> params) {
        return querySingle(hql, params, null);
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE statement
     *
     * @param hql    hql statement to be executed
     * @param params parameters of the statement
     * @return the number of rows effected by the query
     */
    @Override
    public int executeQuery(String hql, Map<String, Object> params) {
        Session session = null;
        Transaction trx = null;

        if (Objects.isNull(params)) {
            LOG.error("query parameters is required, but not provided");
            return -1;
        }

        try {
            session = openSession();
            Query query = session.createQuery(hql);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query = query.setParameter(entry.getKey(), entry.getValue());
            }
            trx = session.beginTransaction();
            int result = query.executeUpdate();
            trx.commit();
            return result;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException while executing hql {} with params count {}, gonna " +
                        "rollback", hql, params.size(), e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException while executing hql {} with params count {}, " +
                        "gonna rollback", hql, params.size(), e);
            }

            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
    }

    /**
     * Performs a query for a single object
     *
     * @param hql    the HQL query text
     * @param params the parameters of the query
     * @param post   the post processor that handles the result of the query
     */
    @Override
    public Object querySingle(String hql, Map<String, Object> params, IQueryPostProcessor post) {
        Session session = null;
        if (Objects.isNull(params)) {
            LOG.info("query parameters are required, but not provided!");
            return null;
        }

        try {
            session = openSession();
            Query query = session.createQuery(hql);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query = query.setParameter(entry.getKey(), entry.getValue());
            }
            Object result = query.uniqueResult();
            if (Objects.isNull(post)) {
                return post.processFindResult(result);
            } else {
                return result;
            }

        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException during executing hql {}, with parameter cnt {}",
                        hql, params.size(), e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException during executing hql {}, with params count {}",
                        hql, params.size(), e);
            }
            throw e;
        } finally {
            close(session);
        }

    }

    @Override
    public <T extends BaseEntity> T findOrSave(String hql, Map<String, Object> params, T item) {
        Session session = null;
        Transaction trx = null;
        if (Objects.isNull(params)) {
            LOG.error("Query parameters are required, but not provided!");
            return null;
        }

        try {
            session = openSession();
            Query query = session.createQuery(hql);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query = query.setParameter(entry.getKey(), entry.getValue());
            }
            T found = (T) query.uniqueResult();
            trx = session.beginTransaction();
            item.setModifiedDate(new Date());
            session.saveOrUpdate(item);
            trx.commit();
            return item;
        } catch (Exception e) {
            if (e instanceof JDBCException) {
                LOG.error("JDBCException executing query '{}'. " +
                                "Database may be down or unavailable. " +
                                "Gonna rollback transaction!",
                        hql, e);
            } else if (e instanceof HibernateException) {
                LOG.error("HibernateException executing query '{}'. " +
                        "Gonna rollback transaction!", hql, e);
            }
            rollback(trx);
            throw e;
        } finally {
            close(session);
        }
    }

    @Override
    public <T extends BaseEntity> List<T> query(HqlQueryRequest request) {
        return this.query(request.getHql(), request.getParameters());
    }



    // -- getter && setter --
    public SessionFactoryImplementor getSfi() {
        return sfi;
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

    private List sqlQueryExecute(String sql, int limit,
                                 Object[] params,
                                 RowBuilder builder) {
        Session session = null;
        try {
            session = openSession();
            // return session.doReturningWork(());
            return session.doReturningWork(
                    new LimitExecutor(sql, limit, params, builder));
        } catch (HibernateException e) {
            LOG.error("HibernateException during executing sql {} limit {} with params num " +
                    "{}", sql, limit, params == null ? 0 : params.length, e);
            throw e;
        } finally {
            close(session);
        }
    }
}
