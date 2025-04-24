package com.hql.fabric.example.controller.reconciliation;

import com.hql.fabric.persistence.service.IHqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/reconciliation")
public class HelloTestController {
    @Autowired
    private IHqlQueryService hqlQueryService;

    private static final Logger LOG = LoggerFactory.getLogger(HelloTestController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello() {
        return String.format("hello-status-of-hql-query-service-%s",
                Objects.nonNull(hqlQueryService));
    }
}
