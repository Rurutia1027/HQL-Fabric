package com.hql.fabric.persistence.processor.impl;

import com.hql.fabric.persistence.processor.IQueryPostProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Post-processes find and list results to ensure that lazy fetched attributes are resolved.
 */
public class DefaultQueryPostProcessor implements IQueryPostProcessor {
    /**
     * Post-processes the result of looking up a single object.
     *
     * @param entity the entity found by the lookup
     * @return the entity
     */
    @Override
    public <T> T processFindResult(T entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        if (!(entity instanceof T)) {
            return entity;
        }

        // do extra init or business logic here
        return entity;
    }


    /**
     * Post-processes the result of listing multiple objects, yielding a list.
     *
     * @param collection the collection of entities queried from DB.
     * @param <T>        the data type of the entity
     * @return the entities
     */
    @Override
    public <T> List<T> processListResult(Collection<T> collection) {
        if (Objects.isNull(collection)) {
            return null;
        }

        for (T entity : collection) {
            if (!(entity instanceof T)) {
                continue;
            }

        }

        // we can do other logic operations here
        return new ArrayList<>(collection);
    }
}
