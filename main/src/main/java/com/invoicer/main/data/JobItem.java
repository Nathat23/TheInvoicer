package com.invoicer.main.data;

import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.AttributeGroup;
import com.invoicer.sql.StringAttribute;

import java.time.Duration;
import java.time.LocalDateTime;

public class JobItem extends StoredObject {

    private Job job;

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

    public Job getJob() {
        if (job == null) {
            for (StoredObject storedObject : getDataManager().getManager(Job.class).getStoredObjects()) {
                if (storedObject.getId() == getJobId()) {
                    job = (Job) storedObject;
                }
            }
        }
        return job;
    }

    public double calculateCost() {
        double cost = getJobRate().getRate() * getUnits();
        if (!getJobRate().isHourly()) {
            return cost;
        }
        LocalDateTime start = getJob().getStartDateTime();
        LocalDateTime end = getJob().getEndDateTime();
        Duration duration = Duration.between(start.toLocalTime(), end.toLocalTime());
        double hours = duration.toHours();
        double quarts = duration.toMinutesPart() % 15;
        double totalTime = hours + quarts;
        return totalTime * getJobRate().getRate();
    }
}
