package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.IntegerTextFieldElement;
import com.invoicer.gui.StringTextFieldElement;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.JobItem;
import com.invoicer.main.data.JobItemManager;
import com.invoicer.main.data.JobRate;
import com.invoicer.main.data.JobRateManager;

public class EditJobItemDialog extends Dialog {

    private JobItem jobItem;
    private final DataManager dataManager;
    private boolean newItem;

    public EditJobItemDialog(DataManager dataManager, JobItem jobItem) {
        super("Editing Job Item", DialogSize.SMALL);
        this.dataManager = dataManager;
        this.jobItem = jobItem;
    }

    @Override
    public void populate() {
        JobItemManager jobItemManager = (JobItemManager) dataManager.getManager(JobItem.class);
        JobRateManager jobRateManager = (JobRateManager) dataManager.getManager(JobRate.class);
        if (jobItem == null) {
            jobItem = (JobItem) jobItemManager.createObject();
            newItem = true;
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
        jobRateManager.getStoreableObjects().forEach(object -> boxElement.addItem((JobRate) object));
        dialogPage.addElement(boxElement);
        IntegerTextFieldElement units = new IntegerTextFieldElement("Units");
        units.getContent().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!units.validate()) {
                return;
            }
            jobItem.setUnits(units.getValue());
        });
        dialogPage.addElement(units);
        if (!newItem) {
            boxElement.getContent().getSelectionModel().select((JobRate) jobRateManager.getStoreableObject(jobItem.getRateId()));
            description.getContent().setText(jobItem.getDescription());
            name.getContent().setText(jobItem.getName());
            units.getContent().setText(jobItem.getUnits() + "");
        }
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {
        if (!newItem) {
            return;
        }
        JobItemManager jobItemManager = (JobItemManager) dataManager.getManager(JobItem.class);
        jobItemManager.addStoreableObject(jobItem);
    }
}
