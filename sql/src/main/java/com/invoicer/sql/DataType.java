package com.invoicer.sql;

public enum DataType {
    STRING(String.class),
    INTEGER(int.class);

    Class<?> type;
    DataType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
