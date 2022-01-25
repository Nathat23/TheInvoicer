package com.invoicer.sql;

public class DoubleAttribute extends Attribute {

    public DoubleAttribute(AttributeConfig attributeConfig, String name, double value) {
        super(attributeConfig, name, value);
    }

    @Override
    public Double getValue() {
        return (Double) super.getValue();
    }
}
