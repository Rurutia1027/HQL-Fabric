package com.hql.fabric.persistence.query.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.hql.fabric.persistence.utils.SqlStatementUtils.readColumnValue;

public class ArrayRowBuilder implements RowBuilder {
    @Override
    public Object buildRow(ResultSet rs, String[] colNames)
            throws SQLException {
        Object[] row = new Object[colNames.length];
        for (int i = 0; i < colNames.length; i++) {
            row[i] = readColumnValue(rs, i);
        }
        return row;
    }
}
