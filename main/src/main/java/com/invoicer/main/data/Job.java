package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.DateTimeAttribute;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StoreableObjectData;
import com.invoicer.sql.StringAttribute;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@StoreableObjectData(tableName = "jobs")
public class Job extends StoreableObject {

    public Job(int id, Config config) {
        super(id, config);
    }

    public Job(int id, Config config, List<Attribute> attributeCollection) {
        super(id, config, attributeCollection);
    }

    public LocalDateTime getStartDateTime() {
        return ((DateTimeAttribute) getAttributes().get(1)).getValue();
    }

    public LocalDateTime getEndDateTime() {
        return ((DateTimeAttribute) getAttributes().get(2)).getValue();
    }

    public String getName() {
        return ((StringAttribute) getAttributes().get(3)).getValue();
    }
}
