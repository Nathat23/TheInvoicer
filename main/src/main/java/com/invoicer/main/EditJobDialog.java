package com.invoicer.main;

import com.invoicer.gui.ComboBoxElement;
import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.StringTextFieldElement;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.CustomerManager;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.Job;
import com.invoicer.main.data.JobManager;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.StoreableObject;
import jfxtras.scene.control.agenda.Agenda;

import java.util.HashMap;

public class EditJobDialog extends Dialog {

    private Job job;
    private Customer customer;
    private Agenda.Appointment appointment;
    private final DataManager dataManager;

    public EditJobDialog(DataManager dataManager, Agenda.Appointment appointment) {
        super("Edit Job", DialogSize.LARGE);
        this.appointment = appointment;
        this.dataManager = dataManager;
    }

    @Override
    public void populate() {
        CustomerManager customerManager = (CustomerManager) dataManager.getManager(Customer.class);
        DialogPage dialogPage = new DialogPage("Editing Job");
        ComboBoxElement<String> comboBoxElement = new ComboBoxElement<>("Customer");
        HashMap<String, Customer> customerHashMap = new HashMap<>();
        for (StoreableObject customer : customerManager.getStoreableObjects()) {
            StringBuilder builder = new StringBuilder();
            builder.append("ID: ").append(customer.getId()).append(" (");
            for (Attribute attribute : customer.getAttributes()) {
                builder.append(attribute.getValue()).append(",");
            }
            builder.setCharAt(builder.length() - 1, ')');
            comboBoxElement.addItem(builder.toString());
            customerHashMap.put(builder.toString(), (Customer) customer);
        }
        comboBoxElement.getContent().getSelectionModel().selectedIndexProperty().addListener(observable -> {
            customer = customerHashMap.get(comboBoxElement.getContent().getSelectionModel().getSelectedItem());
        });
        dialogPage.addElement(comboBoxElement);
        StringTextFieldElement textFieldElement = new StringTextFieldElement("Name");
        textFieldElement.getContent().textProperty().addListener((observable, oldValue, newValue) -> appointment.setSummary(newValue));
        dialogPage.addElement(textFieldElement);
        StringTextFieldElement description = new StringTextFieldElement("Description");
        description.getContent().textProperty().addListener((observable, oldValue, newValue) -> appointment.setDescription(newValue));
        dialogPage.addElement(description);
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {
        JobManager jobManager = (JobManager) dataManager.getManager(Job.class);
        Job job = (Job) jobManager.create();
        job.setCustomerId(customer.getId());
        job.setStartDateTime(appointment.getStartLocalDateTime());
        job.setEndDateTime(appointment.getEndLocalDateTime());
        job.setName(appointment.getSummary());
    }
}
