package com.hql.fabric.example.config;

import com.hql.fabric.example.loader.DbRiderDatasetLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasetLoaderConfig {
    private static final Logger LOG = LoggerFactory.getLogger(DatasetLoaderConfig.class);

    @Bean
    public DbRiderDatasetLoader dbRiderDatasetLoader(DataSource dataSource) {
        DbRiderDatasetLoader dbRiderDatasetLoader = new DbRiderDatasetLoader(dataSource);
        try {
            dbRiderDatasetLoader.loadDataset("datasets/query_datasets.yml");
        } catch (Exception e) {
            LOG.error("Failed to load example datasets to db");
            throw new RuntimeException(e);
        }
        return dbRiderDatasetLoader;
    }
}
