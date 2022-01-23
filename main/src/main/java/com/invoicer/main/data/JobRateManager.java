package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;

public class JobRateManager extends Manager {

    public JobRateManager(Config config) {
        super(config);
    }

    @Override
    public StoreableObject createObject(int id) {
        return new JobRate(id, getConfig());
    }
}
