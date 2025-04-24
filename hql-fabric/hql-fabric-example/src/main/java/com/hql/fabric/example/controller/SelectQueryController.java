package com.hql.fabric.example.controller;

import com.hql.fabric.domain.permission.User;
import com.hql.fabric.example.dto.ApiResponse;
import com.hql.fabric.example.loader.ExampleDatasetLoader;
import com.hql.fabric.persistence.query.builder.HqlQueryBuilder;
import com.hql.fabric.persistence.service.impl.HqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class SelectQueryController {
    private static final Logger LOG = LoggerFactory.getLogger(SelectQueryController.class);

    @Autowired
    private ExampleDatasetLoader exampleDatasetLoader;

    @Autowired
    private HqlQueryService hqlQueryService;

    /**
     * Query User directly via HqlQueryService + HqlQueryBuilder
     */
    @GetMapping("/query")
    public ResponseEntity selectUser() throws Exception {
        HqlQueryBuilder hqlQueryBuilder = new HqlQueryBuilder();
        String hql = hqlQueryBuilder
                .fromAs(User.class, "user")
                .eq("user.id", "1")
                .and()
                .eq("user.displayName", "John Doe")
                .build();

        Map<String, Object> params = hqlQueryBuilder.getInjectionParameters();
        LOG.info("HQL: {}", hql);
        LOG.info("Hql parameters len: {}", params.size());

        List<User> userList = hqlQueryService.query(hql, params);
        if (userList != null && !userList.isEmpty()) {
            LOG.info("User list len: {}", userList.size());
            User user = userList.get(0);
            LOG.info("User displayName is '{}' and userId is '{}'",
                    user.getDisplayName(), user.getId());
        }
        return ResponseEntity.ok(ApiResponse.success(userList));
    }
}
