package com.hql.fabric.sharding.router;

public interface TableAwareShardRouter<T> {
    /**
     * @return Whether support this db table sharding logic
     */
    boolean supportsTable(String tableName);

    /**
     * @return shard name like (shard_0, shard_1, ...).
     * Depending on context info.
     */
    String routeShard(String tableName);
}