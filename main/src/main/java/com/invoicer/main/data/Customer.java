package com.invoicer.main.data;

import com.invoicer.sql.AttributeGroup;

public class Customer extends StoredObject {

    public Customer(DataManager dataManager, AttributeGroup attributeGroup) {
        super(dataManager, attributeGroup);
    }

    public String getEmail() {
        return getAttributeGroup().getAttributes().get(2).getValue().toString();
    }
}
