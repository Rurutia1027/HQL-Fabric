package com.hql.fabric.example.query.select;

import com.hql.fabric.example.entity.permission.User;
import com.hql.fabric.example.loader.DbRiderDatasetLoader;
import com.hql.fabric.persistence.query.builder.HqlQueryBuilder;
import com.hql.fabric.persistence.service.impl.HqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SelectQueryExampleRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SelectQueryExampleRunner.class);

    @Autowired
    private HqlQueryService hqlQueryService;

    @Autowired
    private DataSource dataSource;


    @Override
    public void run(String... args) throws Exception {
        DbRiderDatasetLoader loader = new DbRiderDatasetLoader(dataSource);
        loader.loadDataset("datasets/002_select_dataset.yml");
        LOG.info("Example datasets loaded!");
        HqlQueryBuilder hqlQueryBuilder = new HqlQueryBuilder();
        String hql = hqlQueryBuilder.fromAs(User.class, "user")
                .eq("user.id", "1")
                .and()
                .eq("user.displayName", "John Doe")
                .build();
        Map<String, Object> params = hqlQueryBuilder.getInjectionParameters();
        LOG.info("Hql parameters len {}", params.size());
        try {
            List<User> userList = hqlQueryService.query(hql, params);
            if (Objects.nonNull(userList) && userList.size() > 0) {
                LOG.info("User list len {}", userList.size());
                User user = userList.get(0);
                LOG.info("User displayName is '{}' and userId is '{}'",
                        user.getDisplayName(), user.getId());
            }
        } catch (ClassCastException e) {
            LOG.error("Hql execution got class cast exception", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Hql execution got exception", e);
            throw e;
        }
    }
}
