package com.hql.fabric.routing.controller;

import com.hql.fabric.persistence.service.impl.HqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/shards")
public class RoutingController {
    private static final Logger LOG = LoggerFactory.getLogger(RoutingController.class);
    @Autowired
    private HqlQueryService hqlQueryService;

    @GetMapping("/query")
    public ResponseEntity testQuery() throws Exception {
        LOG.info("testQuery controller invoked, and inner hql query service {}",
                Objects.nonNull(hqlQueryService));
        List<Object> retList = List.of("hello", "routing-controller",
                "endpoint", "works", "ok");
        return ResponseEntity.ok(retList);
    }
}
