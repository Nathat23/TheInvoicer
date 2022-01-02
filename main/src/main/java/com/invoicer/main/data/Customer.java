package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StoreableObjectData;

import java.util.Collection;

@StoreableObjectData(tableName = "customers")
public class Customer extends StoreableObject {

    public Customer(int id, Config config) {
        super(id, config);
    }

    public Customer(int id, Config config, Collection<Attribute> attributes) {
        super(id, config, attributes);
    }


}
