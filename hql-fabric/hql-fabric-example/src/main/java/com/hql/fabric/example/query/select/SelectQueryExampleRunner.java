package com.hql.fabric.example.query.select;

import com.hql.fabric.example.entity.permission.User;
import com.hql.fabric.persistence.query.builder.HqlQueryBuilder;
import com.hql.fabric.persistence.service.impl.HqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
public class SelectQueryExampleRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SelectQueryExampleRunner.class);

    @Autowired
    private HqlQueryService hqlQueryService;

    @Autowired
    private DataSource dataSource;

    private void selectUserExample_1() {
        HqlQueryBuilder builder = new HqlQueryBuilder();
        builder.fromAs(User.class, "user")
                .distinct();

        builder.selectCount("user");
        Map<String, Object> params = builder.getInjectionParameters();
        String hql = builder.build();

        LOG.info("query parameters cnt {}", params.size());
        LOG.info("HQL: '{}'", hql);

        Long userCnt = (Long) hqlQueryService.query(hql, params).get(0);
        LOG.info("selectUserExample_1# userCnt {}", userCnt);
    }


    @Override
    public void run(String... args) throws Exception {
        selectUserExample_1();
    }
}
