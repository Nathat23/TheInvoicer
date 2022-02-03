package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;

import java.util.List;

public class Customer extends StoreableObject {

    public Customer(int id, Config config) {
        super(id, config);
    }

    public Customer(int id, Config config, List<Attribute> attributes) {
        super(id, config, attributes);
    }


}
