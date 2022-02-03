package com.invoicer.sql;

import java.util.ArrayList;
import java.util.List;

public class StoredObjectConfig {

    private String tableName;
    private List<AttributeConfig> list;

    public List<AttributeConfig> getList() {
        return list;
    }

    public void setList(List<AttributeConfig> list) {
        this.list = list;
    }

    public List<Attribute> load() {
        List<Attribute> attributes = new ArrayList<>();
        for (AttributeConfig attribute : getList()) {
            switch (attribute.getType()) {
                case STRING:
                    attributes.add(new StringAttribute(attribute, attribute.getName(), null));
                    continue;
                case INTEGER:
                    attributes.add(new IntAttribute(attribute, attribute.getName(), 0));
                    continue;
                case DATETIME:
                    attributes.add(new DateTimeAttribute(attribute, attribute.getName(), null));
                    continue;
                case BOOLEAN:
                    attributes.add(new BooleanAttribute(attribute, attribute.getName(), false));
                    continue;
                case DOUBLE:
                    attributes.add(new DoubleAttribute(attribute, attribute.getName(), 0));
                    continue;
            }
            throw new UnsupportedOperationException("I don't know how to handle " + attribute.getType());
        }
        return attributes;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
