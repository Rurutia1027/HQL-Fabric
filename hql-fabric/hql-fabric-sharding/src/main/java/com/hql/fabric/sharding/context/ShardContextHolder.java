package com.hql.fabric.sharding.context;

import com.hql.fabric.persistence.entity.NamedArtifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShardContextHolder {
    private static final Logger LOG = LoggerFactory.getLogger(ShardContextHolder.class);

    private static final ThreadLocal<NamedArtifact> CONTEXT = new ThreadLocal<>();

    public static void setCurrentEntity(NamedArtifact entity) {
        CONTEXT.set(entity);
    }

    public static NamedArtifact getCurrentEntity() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
