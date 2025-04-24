package com.hql.fabric.sharding.resolver;

import com.hql.fabric.sharding.context.ShardContextHolder;
import com.hql.fabric.sharding.router.RouterRegistry;
import com.hql.fabric.sharding.router.TableAwareShardRouter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Component
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
public class ShardedSessionResolver {
    private static final Logger LOG = LoggerFactory.getLogger(ShardedSessionResolver.class);


    private final RouterRegistry routerRegistry;
    private final Map<String, SessionFactoryImplementor> shardSessionFactoryMap;


    @Value("${hql.fabric.sharding.shard-count}")
    private Integer shardCount;

    public ShardedSessionResolver(RouterRegistry routerRegistry,
                                  Map<String, SessionFactoryImplementor> shardSessionFactoryMap) {
        this.routerRegistry = routerRegistry;
        this.shardSessionFactoryMap = shardSessionFactoryMap;
    }

    public Session resolveSession(String hql) {
        String table = extractTableName(hql);
        Optional<TableAwareShardRouter<?>> router = routerRegistry.getRouterForTable(table);

        if (router.isPresent()) {
            String shard = router.get().routeShard(table);
            return shardSessionFactoryMap.get(shard).openSession();
        } else {
            int randomId = new Random().nextInt();
            int shardIndex = Math.abs(randomId % shardSessionFactoryMap.size());
            String shardKey = "shard_" + shardIndex;
            return shardSessionFactoryMap.get(shardKey).openSession();
        }
    }

    private String extractTableName(String hql) {
        // todo: implement HqlQueryBuilder#getTableName()
        return hql.split("\\s+")[1]; // "from User u" -> "User"
    }
}
