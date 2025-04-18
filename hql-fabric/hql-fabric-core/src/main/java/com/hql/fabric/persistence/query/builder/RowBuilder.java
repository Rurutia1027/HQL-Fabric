package com.hql.fabric.persistence.query.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

// interface for pluggable row handling strategies below.
public interface RowBuilder {
    Object buildRow(ResultSet rs, String[] colNames) throws SQLException;
}
