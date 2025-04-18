package com.hql.fabric.persistence.processor;

import java.util.Collection;
import java.util.List;

/**
 * Defines the public interface for classes that post-process queries in the IHqlQueryService.
 */
public interface IQueryPostProcessor {
    /**
     * Post-processes the result of looking up a single object.
     *
     * @param entity the entity found by the lookup
     * @return the entity
     */
    <T> T processFindResult(T entity);

    /**
     * Post-processes the result of listing multiple objects, yielding a list.
     *
     * @param collection the collection of entities found by the lookup
     * @param <T>        the data type of the entities
     * @return the entities
     */
    <T> List<T> processListResult(Collection<T> collection);
}
