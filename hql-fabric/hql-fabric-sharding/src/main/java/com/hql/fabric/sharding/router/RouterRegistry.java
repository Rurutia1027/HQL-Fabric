package com.hql.fabric.sharding.router;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
public class RouterRegistry {
    private final List<TableAwareShardRouter<?>> routers;

    public RouterRegistry(List<TableAwareShardRouter<?>> routers) {
        this.routers = routers;
    }

    public Optional<TableAwareShardRouter<?>> getRouterForTable(String tableName) {
        return routers.stream()
                .filter(r -> r.supportsTable(tableName))
                .findFirst();
    }
}
