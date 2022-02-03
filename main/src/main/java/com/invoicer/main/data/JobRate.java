package com.invoicer.main.data;

import com.invoicer.sql.DoubleAttribute;
import com.invoicer.sql.AttributeGroup;
import com.invoicer.sql.StringAttribute;

public class JobRate extends StoredObject {

    public JobRate(DataManager dataManager, AttributeGroup attributeGroup) {
        super(dataManager, attributeGroup);
    }

    public String getName() {
        return ((StringAttribute) getAttributeGroup().getAttributes().get(0)).getValue();
    }

    public double getRate() {
        return ((DoubleAttribute) getAttributeGroup().getAttributes().get(2)).getValue();
    }
}
