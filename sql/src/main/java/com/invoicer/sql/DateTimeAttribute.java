package com.invoicer.sql;

import java.time.LocalDateTime;

public class DateTimeAttribute extends Attribute {

    public DateTimeAttribute(AttributeConfig attributeConfig, String name, LocalDateTime value) {
        super(attributeConfig, name, value);
    }

    @Override
    public LocalDateTime getValue() {
        return (LocalDateTime) super.getValue();
    }
}
