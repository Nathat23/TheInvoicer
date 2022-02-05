package com.invoicer.main;

import com.invoicer.main.data.Job;
import com.invoicer.main.data.JobItem;
import com.invoicer.main.data.JobItemManager;
import com.invoicer.main.data.JobManager;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.CustomerManager;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.JobRate;
import com.invoicer.main.data.JobRateManager;
import com.invoicer.main.data.StorageManager;
import com.invoicer.main.email.EmailConfig;
import com.invoicer.main.email.EmailHandler;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.Config;
import com.invoicer.sql.SqlHandler;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class TheInvoicer {

    private DataManager dataManager;
    private StorageManager storageManager;
    private EmailHandler emailHandler;

    public void init() {
        dataManager = new DataManager();
        InputStream inputStream = TheInvoicer.class.getResourceAsStream("/customer_structure.yml");
        dataManager.addManager(Customer.class, new CustomerManager(dataManager, new Config(inputStream)));
        InputStream inputStream2 = TheInvoicer.class.getResourceAsStream("/job_structure.yml");
        dataManager.addManager(Job.class, new JobManager(dataManager, new Config(inputStream2)));
        InputStream inputStream4 = TheInvoicer.class.getResourceAsStream("/job_rate_structure.yml");
        dataManager.addManager(JobRate.class, new JobRateManager(dataManager, new Config(inputStream4)));
        InputStream inputStream3 = TheInvoicer.class.getResourceAsStream("/job_item_structure.yml");
        dataManager.addManager(JobItem.class, new JobItemManager(dataManager, new Config(inputStream3)));

        SqlHandler sqlHandler = new SqlHandler("jdbc:mariadb://localhost:3306/invoicer", "new_user" , "new_password");
        sqlHandler.initialise();
        storageManager = new StorageManager(sqlHandler, dataManager);
        storageManager.init();

        Yaml yaml = new Yaml(new Constructor(EmailConfig.class));
        InputStream inputStream1 = TheInvoicer.class.getResourceAsStream("/email_config.yml");
        emailHandler = new EmailHandler(yaml.load(inputStream1));
        emailHandler.init();
    }

    public static void main(String[] args) {
        TheInvoicer theInvoicer = new TheInvoicer();
        theInvoicer.init();

        CustomerManager customerManager = (CustomerManager) theInvoicer.dataManager.getManager(Customer.class);
        Customer customer = (Customer) customerManager.createAndStore();
        for (Attribute attribute : customer.getAttributeGroup().getAttributes()) {
            if (attribute != null) {
                attribute.setValue("sda");
            }
        }
        JobRateManager jobRateManager = (JobRateManager) theInvoicer.dataManager.getManager(JobRate.class);
        JobRate jobRate = (JobRate) jobRateManager.createAndStore();
        jobRate.getAttributeGroup().getAttributes().get(0).setValue("SingleTimeRate");
        jobRate.getAttributeGroup().getAttributes().get(1).setValue(true);
        jobRate.getAttributeGroup().getAttributes().get(2).setValue(1.23);

        theInvoicer.storageManager.commitChanges();

        MainWindow.main(args);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public EmailHandler getEmailHandler() {
        return emailHandler;
    }
}
