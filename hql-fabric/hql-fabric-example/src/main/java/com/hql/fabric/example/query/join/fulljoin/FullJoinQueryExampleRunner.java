package com.hql.fabric.example.query.join.fulljoin;

import com.hql.fabric.example.entity.permission.UserProfile;
import com.hql.fabric.example.loader.DbRiderDatasetLoader;
import com.hql.fabric.persistence.query.builder.HqlQueryBuilder;
import com.hql.fabric.persistence.service.impl.HqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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
        DbRiderDatasetLoader loader = new DbRiderDatasetLoader(dataSource);
        loader.loadDataset("datasets/001_full_join_dataset.yml");
        LOG.info("Example datasets loaded!");
        HqlQueryBuilder builder = new HqlQueryBuilder();
        String hql = builder.fromAs(UserProfile.class, "up")
                .innerJoin("up.user", "user")
                .build();

        LOG.info("HQL: {}", hql);
//        List<UserProfile> ret = hqlQueryService.query(hql);
    }
}
