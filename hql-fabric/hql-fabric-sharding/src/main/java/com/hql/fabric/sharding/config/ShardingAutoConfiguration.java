package com.hql.fabric.sharding.config;

import com.hql.fabric.persistence.config.HqlFabricAutoConfiguration;
import com.hql.fabric.sharding.router.RouterRegistry;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@ConditionalOnProperty(name = "hql.fabric.sharding.enabled", havingValue = "true")
@AutoConfigureAfter(HqlFabricAutoConfiguration.class)
public class ShardingAutoConfiguration {

    private final ShardingProperties shardingProperties;

    public ShardingAutoConfiguration(ShardingProperties shardingProperties) {
        this.shardingProperties = shardingProperties;
    }

    @Bean
    public Map<String, SessionFactoryImplementor> shardSessionFactoryMap() {
        Map<String, DataSource> dataSourceMap = shardingProperties.buildDataSourceMap();
        Map<String, SessionFactoryImplementor> factoryMap = new HashMap<>();

        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            String shardKey = entry.getKey();
            DataSource dataSource = entry.getValue();

            LocalContainerEntityManagerFactoryBean emfBean =
                    new LocalContainerEntityManagerFactoryBean();
            emfBean.setDataSource(dataSource);
            emfBean.setPackagesToScan("com.hql.fabric.domain");
            emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            emfBean.setPersistenceUnitName("shard-" + shardKey);

            EntityManagerFactory emf = emfBean.getObject();
            if (Objects.isNull(emf)) {
                throw new IllegalStateException("EntityManagerFactory is null for shard: " + shardKey);
            }

            SessionFactoryImplementor sessionFactory =
                    emf.unwrap(SessionFactoryImplementor.class);
            factoryMap.put(shardKey, sessionFactory);
        }

        return factoryMap;
    }

}
