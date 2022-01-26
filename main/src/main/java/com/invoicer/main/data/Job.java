package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.DateTimeAttribute;
import com.invoicer.sql.IntAttribute;
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

    public int getCustomerId() {
        return ((IntAttribute) getAttributes().get(0)).getValue();
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

    public String getDescription() {
        return ((StringAttribute) getAttributes().get(4)).getValue();
    }

    public void setCustomerId(int customerId) {
        getAttributes().get(0).setValue(customerId);
    }

    public void setStartDateTime(LocalDateTime value) {
        getAttributes().get(1).setValue(value);
    }

    public void setEndDateTime(LocalDateTime value) {
        getAttributes().get(2).setValue(value);
    }

    public void setName(String name) {
        getAttributes().get(3).setValue(name);
    }

    public void setDescription(String description) {
        getAttributes().get(4).setValue(description);
    }
}
