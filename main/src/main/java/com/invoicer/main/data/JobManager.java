package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.AttributeGroup;

public class JobManager extends Manager {

    public JobManager(DataManager dataManager, Config config) {
        super(dataManager, config);
    }

    @Override
    public StoredObject createObject(AttributeGroup attributeGroup) {
        return new Job(getDataManager(), attributeGroup);
    }
}
