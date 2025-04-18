package com.hql.fabric.persistence.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class BaseExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(BaseExecutor.class);

    protected void close(final Statement stmt) {
        if (Objects.nonNull(stmt)) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.error("SQLException got during closing Statement", e);
            }
        }
    }

    protected void close(final ResultSet rs) {
        if (Objects.nonNull(rs)) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error("SQLException got during closing JDBC ResultSet", e);
            }
        }
    }

    // operate JDBC connection execute rollback operation
    protected void rollback(final Connection conn) {
        if (Objects.nonNull(conn)) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                LOG.error("SQLException during executing JDBC rollback operaiton", e);
            }
        }
    }

    /**
     * Retrieve column names from ResultSet and return in lowercase in String array.
     *
     * @param rs
     * @return String array
     */
    protected String[] columnNames(final ResultSet rs) throws SQLException {
        final String[] cols = getColumnNames(rs);
        for (int i = 0; i < cols.length; i++) {
            cols[i] = cols[i].toLowerCase();
        }
        return cols;
    }


    /**
     * Parse column names from ResultSet and return in String array.
     *
     * @param rs ResultSet
     * @return column names organized in String array
     */
    protected String[] getColumnNames(final ResultSet rs) throws SQLException {
        final ResultSetMetaData meta = rs.getMetaData();
        final String[] names = new String[meta.getColumnCount()];
        for (int i = 0; i < names.length; i++) {
            names[i] = meta.getColumnName(i + 1);
        }
        return names;
    }
}
