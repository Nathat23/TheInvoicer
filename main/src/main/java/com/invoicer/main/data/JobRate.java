package com.invoicer.main.data;

import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.DoubleAttribute;
import com.invoicer.sql.IntAttribute;
import com.invoicer.sql.StoreableObject;
import com.invoicer.sql.StoreableObjectData;
import com.invoicer.sql.StringAttribute;

import java.util.List;

@StoreableObjectData(tableName = "job_rates")
public class JobRate extends StoreableObject {

    public JobRate(int id, Config config) {
        super(id, config);
    }

    public JobRate(int id, Config config, List<Attribute> attributes) {
        super(id, config, attributes);
    }

    public String getName() {
        return ((StringAttribute) getAttributes().get(0)).getValue();
    }

    public double getRate() {
        return ((DoubleAttribute) getAttributes().get(2)).getValue();
    }
}
