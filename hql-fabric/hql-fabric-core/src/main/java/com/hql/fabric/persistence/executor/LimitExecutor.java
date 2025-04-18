package com.hql.fabric.persistence.executor;

import org.hibernate.jdbc.ReturningWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LimitExecutor extends BaseExecutor implements ReturningWork<List<Object>> {
    @Override
    public List<Object> execute(Connection connection) throws SQLException {
        return null;
    }
}
