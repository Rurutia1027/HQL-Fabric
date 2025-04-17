package com.hql.fabric.persistence.query;


import com.hql.fabric.HqlFabricCoreTestApp;
import com.hql.fabric.persistence.entity.Order;
import com.hql.fabric.persistence.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}