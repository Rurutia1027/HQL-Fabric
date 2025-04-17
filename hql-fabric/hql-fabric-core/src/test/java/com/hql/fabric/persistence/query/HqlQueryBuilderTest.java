package com.hql.fabric.persistence.query;


import com.hql.fabric.HqlFabricCoreTestApp;
import com.hql.fabric.persistence.entity.Order;
import com.hql.fabric.persistence.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void testSubQuery() {
        HqlQueryBuilder subHqlQueryBuilder = new HqlQueryBuilder();
        subHqlQueryBuilder.from("product.id")
                .selectCount()
                .eq("field1_key", "testKey")
                .and()
                .eq("field1_value", "testValue");
        String subHqlQuery = subHqlQueryBuilder.build();
        Assertions.assertEquals("SELECT COUNT (*) FROM " +
                "product.id WHERE field1_key = :_0 " +
                "and field1_value = :_1", subHqlQuery);
        Map<String, Object> subQueryParamMap = subHqlQueryBuilder.getInjectionParameters();
        Assertions.assertEquals("testKey", subQueryParamMap.get("_0"));
        Assertions.assertEquals("testValue", subQueryParamMap.get("_1"));

        assertNull(subQueryParamMap.get("sub_0"));
        assertNull(subQueryParamMap.get("sub_1"));

        // Add sub query to primary query
        hqlQueryBuilder.fromAs(Order.class, "order")
                .leftJoin("order.orderNo", "orderNo")
                .select("user")
                .isNull("user.deleted")
                .and()
                .eq("order.id", "testOrderId")
                .and()
                .isNull("user.deleted")
                .and()
                .open()
                .open()
                .subQuery(subHqlQueryBuilder)
                .gt(0)
                .close();

        String hql = hqlQueryBuilder.build();
        Map<String, Object> hqlParams = hqlQueryBuilder.getInjectionParameters();

        Assertions.assertTrue(StringUtils.isNotBlank(hql));
        Assertions.assertEquals(4, hqlParams.size());
        Assertions.assertEquals("testKey", hqlParams.get("sub_1"));
        Assertions.assertEquals("testValue", hqlParams.get("sub_2"));
        assertNull(hqlParams.get("_2"));
        assertNull(hqlParams.get("_1"));
    }

    @Test
    public void testGreaterThan() {
        String hql = hqlQueryBuilder.fromAs(User.class, "user").selectCount().eq(
                "user.name", "testName").gt(0).build();
        Assertions.assertEquals("SELECT COUNT (*) " +
                "FROM com.hql.fabric.persistence.entity.User as user " +
                "WHERE user.name = :_0 > :_1", hql);
    }


    @Test
    public void testLessThan() {
        String hql = hqlQueryBuilder.fromAs(User.class,
                "user").selectCount().eq(
                "user.name", "testName").lt(0).build();

        Assertions.assertEquals("SELECT COUNT (*) " +
                "FROM com.hql.fabric.persistence.entity.User as user " +
                "WHERE user.name = :_0 < :_1", hql);
    }

    @Test
    public void testGtGe() {

    }
}