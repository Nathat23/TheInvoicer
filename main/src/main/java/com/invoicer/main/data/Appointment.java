package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StoreableObjectData;

import java.util.Collection;

@StoreableObjectData(tableName = "appointments")
public class Appointment extends StoreableObject {

    public Appointment(int id, Config config) {
        super(id, config);
    }

    public Appointment(int id, Config config, Collection<Attribute> attributeCollection) {
        super(id, config, attributeCollection);
    }

}
