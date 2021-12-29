package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;

public class CustomerManager extends Manager {


    public CustomerManager(Config config) {
        super(config);
    }

    @Override
    public StoreableObject createObject(int id) {
        return new Customer(id, getConfig().getStoredObjectConfig().load());
    }
}
