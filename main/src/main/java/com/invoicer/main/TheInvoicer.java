package com.invoicer.main;

import com.invoicer.main.data.Appointment;
import com.invoicer.main.data.AppointmentManager;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.CustomerManager;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.StorageManager;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.SqlHandler;
import com.invoicer.sql.SqlTable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TheInvoicer {

    private DataManager dataManager;
    private StorageManager storageManager;

    public void init() {
        dataManager = new DataManager();
        InputStream inputStream = TheInvoicer.class.getResourceAsStream("/customer_structure.yml");
        dataManager.addManager(Customer.class, new CustomerManager(new Config(inputStream)));
        InputStream inputStream2 = TheInvoicer.class.getResourceAsStream("/appointment_structure.yml");
        dataManager.addManager(Appointment.class, new AppointmentManager(new Config(inputStream2)));

        SqlHandler sqlHandler = new SqlHandler("jdbc:mariadb://localhost:3306/invoicer", "new_user" , "new_password");
        sqlHandler.initialise();
        storageManager = new StorageManager(sqlHandler, dataManager);
        storageManager.init();
    }

    public static void main(String[] args) {
        TheInvoicer theInvoicer = new TheInvoicer();
        theInvoicer.init();

        CustomerManager customerManager = (CustomerManager) theInvoicer.dataManager.getManager(Customer.class);
        customerManager.getStoreableObjects().forEach(storeableObject -> storeableObject.getAttributes().forEach(stringAttribute -> System.out.println(stringAttribute.getName() + "," + stringAttribute.getValue())));
        Customer customer = (Customer) customerManager.create();
        for (Attribute attribute : customer.getAttributes()) {
            if (attribute != null) {
                attribute.setValue("sda");
            }
        }
        customerManager.getStoreableObjects().forEach(storeableObject -> storeableObject.getAttributes().forEach(stringAttribute -> System.out.println(stringAttribute.getName() + "," + stringAttribute.getValue())));


        theInvoicer.storageManager.commitChanges();

        MainWindow.main(args);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
