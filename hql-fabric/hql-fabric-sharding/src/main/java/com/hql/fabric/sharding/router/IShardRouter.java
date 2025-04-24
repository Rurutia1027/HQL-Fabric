package com.hql.fabric.sharding.router;

public interface IShardRouter {
    String routeShardKey();
    String shardRouterName();
}
