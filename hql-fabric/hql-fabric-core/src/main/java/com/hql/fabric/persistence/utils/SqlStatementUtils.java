package com.hql.fabric.persistence.utils;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlStatementUtils {
    /**
     * Returns the given column value from the ResultSet after applying
     * common CLOB and Timestamp handling.
     */
    public static Object readColumnValue(ResultSet rs, int i) throws SQLException {
        Object val = rs.getObject(i + 1);
        // Clobs are not Serializable
        if (val instanceof Clob) {
            Clob clob = (Clob) val;
            val = clob.getSubString(1, (int) clob.length());
        }

        if (isOracleTimestamp(val)) {
            val = rs.getTimestamp(i + 1);
        }
        return val;
    }


    /**
     * Returns true if the input Object is Oracle's oracle.sql.TIMESTAMP
     * class, which does not extend java.utilities.Date or java.sql.Timestamp.
     */
    private static boolean isOracleTimestamp(Object val) {
        return (val != null && "oracle.sql.TIMESTAMP".equals(val.getClass()
                .getName()));
    }
}
