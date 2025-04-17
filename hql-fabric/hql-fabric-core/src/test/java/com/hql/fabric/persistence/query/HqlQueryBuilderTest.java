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
    public void testJoin() {
        Map<String, String> stringMap = new LinkedHashMap<>();
        stringMap.put("u.name", "name_str");
        stringMap.put("u.email", "email@email.com");
        stringMap.put("com.hql.fabric.persistence.entity.User.groupInfo", "groupInfo");
        String hql = hqlQueryBuilder.selectMap(stringMap)
                .fromAs(User.class, "u")
                .leftJoin("group.roles", "role")
                .eq("u.name", "name_str_2")
                .and()
                .eq("role.type", "type1")
                .build();
        Assertions.assertEquals("SELECT NEW MAP (u.name as name_str, u.email as email@email.com, com.hql.fabric.persistence.entity.User.groupInfo as groupInfo) " +
                "FROM com.hql.fabric.persistence.entity.User as u LEFT JOIN group.roles role " +
                "WHERE u.name = :_0 and role.type = :_1", hql);
    }

    @Test
    public void testOrderBy() {
        Map<String, String> stringMap = new LinkedHashMap<>();
        stringMap.put("u.name", "foo");
        stringMap.put("u.email", "aa@alipay.com");
        stringMap.put("com.hql.fabric.persistence.entity.User.groupInfo", "groupInfo");
        String hql = hqlQueryBuilder
                .selectMap(stringMap)
                .fromAs(User.class, "u")
                .orderBy("u.displayName", true)
                .orderBy("u.securityLevel", false)
                .fromAs(Order.class, "order")
                .leftJoin("order.id", "id")
                .eq("u.orderNo", "order_no_target")
                .and()
                .eq("order.type", "type1")
                .build();
        Assertions.assertEquals("SELECT NEW MAP (u.name as foo, u.email as aa@alipay.com, " +
                "com.hql.fabric.persistence.entity.User.groupInfo as groupInfo) " +
                "FROM com.hql.fabric.persistence.entity.User as u, " +
                "com.hql.fabric.persistence.entity.Order as order " +
                "LEFT JOIN order.id id " +
                "WHERE u.orderNo = :_0 and order.type = :_1 " +
                "ORDER BY u.displayName asc, u.securityLevel desc", hql);
        Map<String, Object> hqlParameters = hqlQueryBuilder.getInjectionParameters();
        hqlQueryBuilder.clear();
        Assertions.assertEquals(hqlParameters.size(), 2);
        Assertions.assertEquals(hqlQueryBuilder.getInjectionParameters().size(), 0);


    }
}