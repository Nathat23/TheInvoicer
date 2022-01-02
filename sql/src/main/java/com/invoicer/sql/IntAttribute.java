package com.invoicer.sql;

public class IntAttribute extends Attribute {

    public IntAttribute(AttributeConfig config, String name, int value) {
        super(config, name, value);
    }

    @Override
    public Integer getValue() {
        return (int) super.getValue();
    }
}
