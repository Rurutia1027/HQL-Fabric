package com.hql.fabric.sharding.router;

import com.hql.fabric.persistence.entity.NamedArtifact;
import com.hql.fabric.sharding.context.ShardContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
public class DefaultHashShardRouter implements TableAwareShardRouter<NamedArtifact> {

    @Value("${hql.fabric.sharding.shard-count}")
    private Integer shardCount;

    @Override
    public boolean supportsTable(String tableName) {
        return true;
    }

    @Override
    public String routeShard(String tableName) {
        NamedArtifact entity = ShardContextHolder.getCurrentEntity();
        int idHash = entity.getId().hashCode();
        return "shard_" + (idHash % shardCount); //
    }
}
