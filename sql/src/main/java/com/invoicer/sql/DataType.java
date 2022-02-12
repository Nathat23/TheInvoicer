package com.invoicer.sql;

import java.sql.Types;
import java.time.LocalDateTime;

public enum DataType {
    STRING(String.class, Types.VARCHAR, "varchar(255)"),
    INTEGER(int.class, Types.INTEGER, "int"),
    DATETIME(LocalDateTime.class, Types.DATE, "DATETIME"),
    BOOLEAN(boolean.class, Types.BIT, "BIT(1)"),
    DOUBLE(double.class, Types.DOUBLE, "double");

    private final Class<?> type;
    private final int sqlType;
    private final String strType;

    DataType(Class<?> type, int sqlType, String strType) {
        this.type = type;
        this.sqlType = sqlType;
        this.strType = strType;
    }

    public Class<?> getType() {
        return type;
    }

    public int getSqlType() {
        return sqlType;
    }

    public String getStrType() {
        return strType;
    }


}
