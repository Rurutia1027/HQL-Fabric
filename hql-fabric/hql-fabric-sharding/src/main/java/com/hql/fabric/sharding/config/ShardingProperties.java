package com.hql.fabric.sharding.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "hql.fabric.sharding")
public class ShardingProperties {
    private boolean enabled;
    private int shardCount;
    private Map<String, DataSourceProperties> datasources = new HashMap<>();


    public Map<String, DataSource> buildDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        for (Map.Entry<String, DataSourceProperties> entry : datasources.entrySet()) {
            String shardName = entry.getKey();
            DataSourceProperties properties = entry.getValue();
            DataSource dataSource = properties.initializeDataSourceBuilder().build();
            result.put(shardName, dataSource);
        }
        return result;
    }

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getShardCount() {
        return shardCount;
    }

    public void setShardCount(int shardCount) {
        this.shardCount = shardCount;
    }

    public Map<String, DataSourceProperties> getDatasources() {
        return datasources;
    }

    public void setDatasources(Map<String, DataSourceProperties> datasources) {
        this.datasources = datasources;
    }
}