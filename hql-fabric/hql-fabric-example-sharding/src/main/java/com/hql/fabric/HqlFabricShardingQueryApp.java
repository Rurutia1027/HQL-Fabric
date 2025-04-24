package com.hql.fabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {"com.hql.fabric.domain", "com.hql.fabric.sharding", "com.hql.fabric.example"},
        exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
public class HqlFabricShardingQueryApp {
    public static void main(String[] args) {
        SpringApplication.run(HqlFabricShardingQueryApp.class, args);
    }
}