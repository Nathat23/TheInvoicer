package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.AttributeGroup;

public class JobRateManager extends Manager {

    public JobRateManager(DataManager dataManager, Config config) {
        super(dataManager, config);
    }

    @Override
    public StoredObject createObject(AttributeGroup attributeGroup) {
        return new JobRate(getDataManager(), attributeGroup);
    }
}
