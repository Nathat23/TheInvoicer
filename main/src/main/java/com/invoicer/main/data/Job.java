package com.invoicer.main.data;

import com.invoicer.sql.DateTimeAttribute;
import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.AttributeGroup;
import com.invoicer.sql.StringAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Job extends StoredObject {

    public Job(DataManager dataManager, AttributeGroup attributeGroup) {
        super(dataManager, attributeGroup);
    }

    public int getCustomerId() {
        return ((IntAttribute) getAttributeGroup().getAttributes().get(0)).getValue();
    }

    public LocalDateTime getStartDateTime() {
        return ((DateTimeAttribute) getAttributeGroup().getAttributes().get(1)).getValue();
    }

    public LocalDateTime getEndDateTime() {
        return ((DateTimeAttribute) getAttributeGroup().getAttributes().get(2)).getValue();
    }

    public String getName() {
        return ((StringAttribute) getAttributeGroup().getAttributes().get(3)).getValue();
    }

    public String getDescription() {
        return ((StringAttribute) getAttributeGroup().getAttributes().get(4)).getValue();
    }

    public void setCustomerId(int customerId) {
        getAttributeGroup().getAttributes().get(0).setValue(customerId);
    }

    public void setStartDateTime(LocalDateTime value) {
        getAttributeGroup().getAttributes().get(1).setValue(value);
    }

    public void setEndDateTime(LocalDateTime value) {
        getAttributeGroup().getAttributes().get(2).setValue(value);
    }

    public void setName(String name) {
        getAttributeGroup().getAttributes().get(3).setValue(name);
    }

    public void setDescription(String description) {
        getAttributeGroup().getAttributes().get(4).setValue(description);
    }

    public List<JobItem> getJobItems() {
        List<JobItem> jobItems = new ArrayList<>();
        for (StoredObject storedObject : getDataManager().getManager(JobItem.class).getStoredObjects()) {
            JobItem jobItem = (JobItem) storedObject;
            if (jobItem.getJobId() == getId()) {
                jobItems.add(jobItem);
            }
        }
        return jobItems;
    }
}
