package com.invoicer.sql;

public class AttributeConfig {

    private DataType type;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Class<?> getTypeClass() {
        return this.type.getType();
    }
}
