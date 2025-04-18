package com.hql.fabric.persistence.executor;

import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExecutor extends BaseExecutor implements ReturningWork<Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateExecutor.class);

    private final String sql;
    private final Object[] params;

    public UpdateExecutor(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
    }

    @Override
    public Integer execute(Connection connection) throws SQLException {
        PreparedStatement statement = null;
        boolean autoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            int rows = statement.executeUpdate();
            connection.commit();
            return rows;
        } catch (SQLException e) {
            LOG.error("SQLException execute sql {} with params len {} got exception, gonna " +
                    "rollback!", sql, params.length, e);
            rollback(connection);
            throw e;
        } finally {
            close(statement);

            // recover previous config
            connection.setAutoCommit(autoCommit);
        }
    }
}
