package com.invoicer.main.data;

import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.AttributeGroup;
import com.invoicer.sql.StringAttribute;

public class JobItem extends StoredObject {

    public JobItem(DataManager dataManager, AttributeGroup attributeGroup) {
        super(dataManager, attributeGroup);
    }

    public int getJobId() {
        return ((IntAttribute) getAttributeGroup().getAttributes().get(0)).getValue();
    }

    public int getRateId() {
        return ((IntAttribute) getAttributeGroup().getAttributes().get(1)).getValue();
    }

    public String getName() {
        return ((StringAttribute) getAttributeGroup().getAttributes().get(2)).getValue();
    }

    public String getDescription() {
        return ((StringAttribute) getAttributeGroup().getAttributes().get(3)).getValue();
    }

    public int getUnits() {
        return ((IntAttribute) getAttributeGroup().getAttributes().get(4)).getValue();
    }

    public void setJobId(int newJobId) {
        getAttributeGroup().getAttributes().get(0).setValue(newJobId);
    }

    public void setRateId(int newRateId) {
        getAttributeGroup().getAttributes().get(1).setValue(newRateId);
    }

    public void setName(String newName) {
        getAttributeGroup().getAttributes().get(2).setValue(newName);
    }

    public void setDescription(String newDescription) {
        getAttributeGroup().getAttributes().get(3).setValue(newDescription);
    }

    public void setUnits(int newUnits) {
        getAttributeGroup().getAttributes().get(4).setValue(newUnits);
    }

    public JobRate getJobRate() {
        for (StoredObject storedObject : getDataManager().getManager(JobRate.class).getStoredObjects()) {
            if (storedObject.getId() == getRateId()) {
                return (JobRate) storedObject;
            }
        }
        return null;
    }
}
