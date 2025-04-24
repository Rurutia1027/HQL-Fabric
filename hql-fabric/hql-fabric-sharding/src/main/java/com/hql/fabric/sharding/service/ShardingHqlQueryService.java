package com.hql.fabric.sharding.service;

import com.hql.fabric.persistence.entity.BaseEntity;
import com.hql.fabric.persistence.processor.IQueryPostProcessor;
import com.hql.fabric.persistence.query.builder.ArrayRowBuilder;
import com.hql.fabric.persistence.query.builder.HqlQueryRequest;
import com.hql.fabric.persistence.query.builder.MapRowBuilder;
import com.hql.fabric.persistence.service.IHqlQueryService;
import com.hql.fabric.sharding.router.IShardRouter;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("shardingHqlQueryService")
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
public class ShardingHqlQueryService implements IHqlQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(ShardingHqlQueryService.class);

    private final Map<String, SessionFactory> sessionFactoryMap;
    private final IShardRouter shardingRouter;
    private final MapRowBuilder mapRowBuilder;
    private final ArrayRowBuilder arrayRowBuilder;


    public ShardingHqlQueryService(Map<String, EntityManagerFactory> emfMap,
                                   IShardRouter shardingRouter) {

        // extract corresponding data sources (configured different dbs) to unwrap their
        // inner Session Factory instances to init the sessionFactoryMap
        LOG.info("ShardingQueryService initializing with EntityManager cnt {}", emfMap.size());
        this.sessionFactoryMap = emfMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        item -> item.getValue().unwrap(SessionFactoryImplementor.class)));
        this.shardingRouter = shardingRouter;
        this.mapRowBuilder = new MapRowBuilder();
        this.arrayRowBuilder = new ArrayRowBuilder();
        LOG.info("ShardingQueryService initializing finished, with session factory instances" +
                " {}", this.sessionFactoryMap.size());
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
    public <T extends BaseEntity> List<T> query(String hql, IQueryPostProcessor post, Object... params) {
        return List.of();
    }

    @Override
    public List query(String hql, Map<String, Object> params) {
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
    public <T extends BaseEntity> List<T> mergeAll(List<T> itemList) {
        return List.of();
    }

    @Override
    public List sqlQuery(String sql, Object... params) {
        return List.of();
    }

    @Override
    public List sqlQueryLimit(String sql, int limit, Object... params) {
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
    public <T extends BaseEntity> T findObjectById(Class<T> clazz, String id, IQueryPostProcessor post) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findObjectByName(Class<T> clazz, String name, IQueryPostProcessor post) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findObjectByIdOrName(Class<T> clazz, String idOrName) {
        return null;
    }

    @Override
    public <T extends BaseEntity> T findObjectByIdOrName(Class<T> clazz, String idOrName, IQueryPostProcessor post) {
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
        return List.of();
    }
}
