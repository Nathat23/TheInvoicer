package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StoreableObjectData;

import java.util.Collection;

@StoreableObjectData(tableName = "customers")
public class Customer extends StoreableObject {

    public Customer(int id, Collection<Attribute<String>> attributes) {
        super(id, attributes);
    }


}
