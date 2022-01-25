package com.invoicer.sql;

public class BooleanAttribute extends Attribute {

    public BooleanAttribute(AttributeConfig attributeConfig, String name, boolean value) {
        super(attributeConfig, name, value);
    }

    @Override
    public Boolean getValue() {
        return (boolean) super.getValue();
    }
}
