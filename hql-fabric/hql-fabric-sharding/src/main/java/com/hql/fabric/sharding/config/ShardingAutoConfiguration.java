package com.hql.fabric.sharding.config;

import com.hql.fabric.persistence.config.HqlFabricAutoConfiguration;
import com.hql.fabric.persistence.service.IHqlQueryService;
import com.hql.fabric.sharding.router.IShardRouter;
import com.hql.fabric.sharding.service.ShardingHqlQueryService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
@AutoConfigureAfter(HqlFabricAutoConfiguration.class)
public class ShardingAutoConfiguration {
    @Bean
    public IHqlQueryService shardingHqlQueryService(IShardRouter shardRouter) {
        return new ShardingHqlQueryService(null, shardRouter);
    }
}
