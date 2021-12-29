package com.invoicer.sql;

import java.sql.Types;

public enum DataType {
    STRING(String.class, Types.VARCHAR, "varchar(255)"),
    INTEGER(int.class, Types.INTEGER, "int");

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
