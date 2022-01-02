package com.invoicer.sql;

public class AttributeConfig {

    private DataType type;
    private String name;
    private String human;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public String getHuman() {
        return human;
    }

    public void setHuman(String human) {
        this.human = human;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Class<?> getTypeClass() {
        return this.type.getType();
    }
}
