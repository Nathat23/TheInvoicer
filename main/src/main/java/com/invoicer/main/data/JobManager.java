package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;

public class JobManager extends Manager {

    public JobManager(Config config) {
        super(config);
    }

    @Override
    public StoreableObject createObject(int id) {
        return new Job(id, getConfig());
    }
}
