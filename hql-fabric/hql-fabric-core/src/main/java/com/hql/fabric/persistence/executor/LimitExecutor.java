package com.hql.fabric.persistence.executor;

import com.hql.fabric.persistence.query.builder.RowBuilder;
import org.hibernate.jdbc.ReturningWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LimitExecutor extends BaseExecutor implements ReturningWork<List<Object>> {
    private final String sql;
    private final int limit;
    private final Object[] params;
    private final RowBuilder builder;

    public LimitExecutor(String sql, int limit, Object[] params, RowBuilder builder) {
        this.sql = sql;
        this.limit = limit;
        this.params = params;
        this.builder = builder;
    }

    @Override
    public List<Object> execute(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(this.sql);
            stmt.setMaxRows(this.limit);
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    stmt.setNull(i + 1, Types.VARCHAR);
                } else {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();

            List<Object> found = new ArrayList<>();

            while (rs.next()) {
                found.add(builder.buildRow(rs, columnNames(rs)));
            }

            // reset in case the stmt is reused by pooling
            stmt.setMaxRows(0);
            return found;
        } finally {
            super.close(rs);
            super.close(stmt);
        }
    }
}
