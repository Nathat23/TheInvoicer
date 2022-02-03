package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StringAttribute;

import java.util.List;

public class JobItem extends StoreableObject {

    public JobItem(int id, Config config) {
        super(id, config);
    }

    public JobItem(int id, Config config, List<Attribute> attributes) {
        super(id, config, attributes);
    }

    public int getJobId() {
        return ((IntAttribute) getAttributes().get(0)).getValue();
    }

    public int getRateId() {
        return ((IntAttribute) getAttributes().get(1)).getValue();
    }

    public String getName() {
        return ((StringAttribute) getAttributes().get(2)).getValue();
    }

    public String getDescription() {
        return ((StringAttribute) getAttributes().get(3)).getValue();
    }

    public int getUnits() {
        return ((IntAttribute) getAttributes().get(4)).getValue();
    }

    public void setJobId(int newJobId) {
        getAttributes().get(0).setValue(newJobId);
    }

    public void setRateId(int newRateId) {
        getAttributes().get(1).setValue(newRateId);
    }

    public void setName(String newName) {
        getAttributes().get(2).setValue(newName);
    }

    public void setDescription(String newDescription) {
        getAttributes().get(3).setValue(newDescription);
    }

    public void setUnits(int newUnits) {
        getAttributes().get(4).setValue(newUnits);
    }
}
