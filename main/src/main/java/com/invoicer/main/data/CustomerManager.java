package com.invoicer.main.data;

import com.invoicer.sql.AttributeGroup;
import com.invoicer.sql.Config;

public class CustomerManager extends Manager {

    public CustomerManager(DataManager dataManager, Config config) {
        super(dataManager, config);
    }

    @Override
    public StoredObject createObject(AttributeGroup attributeGroup) {
        return new Customer(getDataManager(), attributeGroup);
    }
}
