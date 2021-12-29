package com.invoicer.sql;

public class IntAttribute extends Attribute {

    public IntAttribute(String name, int value) {
        super(name, value);
    }

    @Override
    public Integer getValue() {
        return (int) super.getValue();
    }
}
