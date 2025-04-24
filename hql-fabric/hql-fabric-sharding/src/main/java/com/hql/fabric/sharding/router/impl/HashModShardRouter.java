package com.hql.fabric.sharding.router.impl;

import com.hql.fabric.sharding.router.IShardRouter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
public class HashModShardRouter implements IShardRouter {
    private static final String ROUTER_NAME = "HASH_MOD_SHARD_ROUTER";

    @Override
    public String routeShardKey() {
        Integer userId = 0;
        // ShardContextHolder.getCurrentUserId();
        int shardIndex = userId % 3;
        return "shard_" + shardIndex;
    }

    @Override
    public String shardRouterName() {
        return ROUTER_NAME;
    }
}
