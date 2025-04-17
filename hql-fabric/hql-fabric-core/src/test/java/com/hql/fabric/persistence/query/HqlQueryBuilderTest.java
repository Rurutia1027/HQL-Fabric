package com.hql.fabric.persistence.query;


import com.hql.fabric.HqlFabricCoreTestApp;
import com.hql.fabric.persistence.entity.Order;
import com.hql.fabric.persistence.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest(classes = HqlFabricCoreTestApp.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HqlQueryBuilderTest {
    private HqlQueryBuilder hqlQueryBuilder;

    @BeforeAll
    @Transactional
    public void initTest() throws Exception {
        hqlQueryBuilder = new HqlQueryBuilder();
        Assertions.assertNotNull(hqlQueryBuilder);
    }

    @AfterEach
    public void refreshStatus() {
        this.hqlQueryBuilder.clear();
    }

    @Test
    public void testFromClass() {
        String hql = hqlQueryBuilder.from(Order.class).build();
        Assertions.assertEquals("FROM com.hql.fabric.persistence.entity.Order", hql);
    }

    @Test
    public void testFromClassSelect() {
        String hql = hqlQueryBuilder
                .fromAs(User.class, "u")
                .select("o.name").build();
        Assertions.assertEquals("SELECT o.name FROM com.hql.fabric.persistence.entity.User as u", hql);
    }

    @Test
    public void testWhereClause() {
        String hql = hqlQueryBuilder
                .fromAs(User.class, "u")
                .eq("o.name", "name_string")
                .build();
        Assertions.assertEquals("FROM com.hql.fabric.persistence.entity.User as u WHERE o.name = :_0", hql);
        Assertions.assertEquals(1, hqlQueryBuilder.getInjectionParameters().size());
        hqlQueryBuilder.clear();
        Assertions.assertEquals(0, hqlQueryBuilder.getInjectionParameters().size());
    }

    @Test
    public void testSelectMapWhere() {
        Map<String, String> stringMap = new LinkedHashMap<>();
    }
}