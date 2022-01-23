package com.invoicer.sql;

import java.util.ArrayList;
import java.util.List;

public class StoredObjectConfig {

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
            if (attribute.getType() == DataType.STRING) {
                attributes.add(new StringAttribute(attribute, attribute.getName(), null));
                continue;
            }
            if (attribute.getType() == DataType.INTEGER) {
                attributes.add(new IntAttribute(attribute, attribute.getName(), 0));
                continue;
            }
            if (attribute.getType() == DataType.DATETIME) {
                attributes.add(new DateTimeAttribute(attribute, attribute.getName(), null));
            }
        }
        return attributes;
    }
}
