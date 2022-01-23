package com.invoicer.main.data;

import com.invoicer.sql.Config;
import com.invoicer.sql.StoreableObject;

public class AppointmentManager extends Manager {

    public AppointmentManager(Config config) {
        super(config);
    }

    @Override
    public StoreableObject createObject(int id) {
        return new Appointment(id, getConfig());
    }
}
