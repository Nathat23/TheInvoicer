package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.StoreableObject;

import java.util.ArrayList;
import java.util.List;

public class JobItemManager extends Manager {

    public JobItemManager(Config config) {
        super(config);
    }

    @Override
    public StoreableObject createObject(int id) {
        return new JobItem(id, getConfig());
    }

    public List<JobItem> findItemsForJob(Job job) {
        List<JobItem> jobs = new ArrayList<>();
        for (StoreableObject storeableObject : getStoreableObjects()) {
            IntAttribute intAttribute = (IntAttribute) storeableObject.getAttributes().get(0);
            if (intAttribute.getValue() == job.getId()) {
                jobs.add((JobItem) storeableObject);
            }
        }
        return jobs;
    }
}
