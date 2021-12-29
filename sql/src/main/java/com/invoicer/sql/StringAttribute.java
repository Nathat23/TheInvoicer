package com.invoicer.sql;

public class StringAttribute extends Attribute {

    public StringAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public String getValue() {
        return (String) super.getValue();
    }
}
