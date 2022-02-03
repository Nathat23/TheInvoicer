package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.AttributeGroup;

import java.util.ArrayList;
import java.util.List;

public class JobItemManager extends Manager {

    public JobItemManager(DataManager dataManager, Config config) {
        super(dataManager, config);
    }

    @Override
    public StoredObject createObject(AttributeGroup attributeGroup) {
        return new JobItem(getDataManager(), attributeGroup);
    }
}
