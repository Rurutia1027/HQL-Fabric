package com.hql.fabric.example.query.join.fulljoin;

import com.hql.fabric.persistence.service.impl.HqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Objects;

@Component
public class FullJoinQueryExampleRunner implements CommandLineRunner {
    private static final Logger LOG =
            LoggerFactory.getLogger(FullJoinQueryExampleRunner.class);
    @Autowired
    private HqlQueryService hqlQueryService;

    @Autowired
    private DataSource dataSource;


    @Override
    public void run(String... args) throws Exception {
        // example-1: show simple full join usage
        System.out.println(Objects.nonNull(hqlQueryService));
        //  System.out.println(Objects.nonNull(flyway));
        // hqlQueryService.query()
    }
}
