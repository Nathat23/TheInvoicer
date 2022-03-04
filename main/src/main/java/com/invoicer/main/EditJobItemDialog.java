package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.IntegerTextFieldElement;
import com.invoicer.gui.StringTextFieldElement;
import com.invoicer.main.data.*;
import com.invoicer.main.display.StoredObjectBoxElement;

public class EditJobItemDialog extends Dialog {

    private JobItem jobItem;
    private final Job job;
    private final DataManager dataManager;
    private boolean newItem;

    public EditJobItemDialog(DataManager dataManager, Job job, JobItem jobItem) {
        super("Editing Job Item", DialogSize.SMALL);
        this.dataManager = dataManager;
        this.jobItem = jobItem;
        this.job = job;
    }

    @Override
    public void populate() {
        JobItemManager jobItemManager = (JobItemManager) dataManager.getManager(JobItem.class);
        JobRateManager jobRateManager = (JobRateManager) dataManager.getManager(JobRate.class);
        if (jobItem == null) {
            jobItem = (JobItem) jobItemManager.createObject();
            newItem = true;
            jobItem.setJobId(job.getId());
        }
        DialogPage dialogPage = new DialogPage("Editing Job Item");
        StringTextFieldElement name = new StringTextFieldElement("Name");
        name.getContent().textProperty().addListener((observable, oldValue, newValue) -> {
            jobItem.setName(newValue);
        });
        dialogPage.addElement(name);
        StringTextFieldElement description = new StringTextFieldElement("Description");
        description.getContent().textProperty().addListener((observable, oldValue, newValue) -> {
            jobItem.setDescription(newValue);
        });
        dialogPage.addElement(description);
        StoredObjectBoxElement<JobRate> boxElement = new StoredObjectBoxElement<>("Rate");
        jobRateManager.getStoredObjects().forEach(object -> boxElement.addItem((JobRate) object));
        dialogPage.addElement(boxElement);
        boxElement.getContent().valueProperty().addListener((observableValue, jobRate, t1) -> {
            if (jobRate == null) {
                return;
            }
            jobItem.setRateId(t1.getId());
        });
        IntegerTextFieldElement units = new IntegerTextFieldElement("Units");
        units.getContent().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!units.validate()) {
                return;
            }
            jobItem.setUnits(units.getValue());
        });
        dialogPage.addElement(units);
        if (!newItem) {
            boxElement.getContent().getSelectionModel().select((JobRate) jobRateManager.getStoredObject(jobItem.getRateId()));
            description.getContent().setText(jobItem.getDescription());
            name.getContent().setText(jobItem.getName());
            units.getContent().setText(jobItem.getUnits() + "");
        }
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {

    }

    public JobItem getJobItem() {
        return jobItem;
    }
}
