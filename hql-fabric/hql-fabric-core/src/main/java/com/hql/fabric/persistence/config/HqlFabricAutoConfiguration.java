package com.hql.fabric.persistence.config;

import com.hql.fabric.persistence.service.IHqlQueryService;
import com.hql.fabric.persistence.service.impl.HqlQueryService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(EntityManagerFactory.class)
public class HqlFabricAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public IHqlQueryService hqlQueryService(EntityManagerFactory entityManagerFactory) {
        return new HqlQueryService(entityManagerFactory);
    }
}
