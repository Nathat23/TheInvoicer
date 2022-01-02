package com.invoicer.sql;

public class Attribute {

    private final String name;
    private boolean modified;
    private final AttributeConfig attributeConfig;
    private Object value;

    public Attribute(AttributeConfig attributeConfig, String name, Object value) {
        this.attributeConfig = attributeConfig;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        this.modified = true;
    }

    void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isModified() {
        return modified;
    }

    public AttributeConfig getAttributeConfig() {
        return attributeConfig;
    }
}
