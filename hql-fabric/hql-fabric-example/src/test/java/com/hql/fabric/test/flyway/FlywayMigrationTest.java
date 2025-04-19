package com.hql.fabric.test.flyway;

import com.hql.fabric.HqlFabricExampleTestApplication;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.util.List;


@SpringBootTest(classes = HqlFabricExampleTestApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FlywayMigrationTest {

    @TestConfiguration
    static class FlywayTestConfig {
        @Bean(initMethod = "migrate")
        public Flyway flyway(@Autowired DataSource dataSource) {
            return Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration/test")
                    .baselineOnMigrate(true)
                    .load();
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void initTest() {
        Assertions.assertNotNull(jdbcTemplate);
        List<String> names = jdbcTemplate.queryForList("SELECT email FROM test_users",
                String.class);
        Assertions.assertNotNull(names);
        Assertions.assertEquals(names.size(), 3);
    }
}