package com.hql.fabric.example.loader;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class DbRiderDatasetLoader {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DbRiderDatasetLoader(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void loadDataset(String resourcePath) throws Exception {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) throw new RuntimeException("YAML file not found: " + resourcePath);
            Map<String, List<Map<String, Object>>> dataset = yaml.load(in);

            for (Map.Entry<String, List<Map<String, Object>>> table : dataset.entrySet()) {
                String tableName = table.getKey();
                List<Map<String, Object>> rows = table.getValue();

                for (Map<String, Object> row : rows) {
                    insertRow(tableName, row);
                }
            }
        }
    }


    private void insertRow(String table, Map<String, Object> values) {
        // Preprocess values
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof String && "[NOW]".equals(val)) {
                entry.setValue(Timestamp.from(Instant.now()));
            }
        }

        String columns = String.join(", ", values.keySet());
        String placeholders = ":" + String.join(", :", values.keySet());

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns, placeholders);
        jdbcTemplate.update(sql, values);
    }
}
