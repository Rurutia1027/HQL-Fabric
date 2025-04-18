package com.hql.fabric.persistence.service.impl;

import com.hql.fabric.HqlFabricCoreTestApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HqlFabricCoreTestApp.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HqlQueryServiceTest {
    @Autowired
    private HqlQueryService hqlQueryService;

    @Test
    public void initTest() {
        Assertions.assertNotNull(hqlQueryService);
    }
}