package com.invoicer.sql;

public class StringAttribute extends Attribute {

    public StringAttribute(AttributeConfig attributeConfig, String name, String value) {
        super(attributeConfig, name, value);
    }

    @Override
    public String getValue() {
        return (String) super.getValue();
    }
}
