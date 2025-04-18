package com.hql.fabric.persistence.query.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.hql.fabric.persistence.utils.SqlStatementUtils.readColumnValue;

public class MapRowBuilder implements RowBuilder{
    @Override
    public Object buildRow(ResultSet rs, String[] colNames) throws SQLException {
        Map row = new HashMap();
        for (int i = 0; i < colNames.length; i++) {
            row.put(colNames[i], readColumnValue(rs, i));
        }
        return row;
    }
}
