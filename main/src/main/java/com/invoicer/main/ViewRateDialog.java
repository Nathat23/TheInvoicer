package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.StringTextFieldElement;
import com.invoicer.gui.WideDialogElement;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.JobRate;
import com.invoicer.main.data.JobRateManager;
import com.invoicer.sql.StoreableObject;
import javafx.scene.Node;
import javafx.scene.control.TableRow;

import java.util.ArrayList;
import java.util.List;

public class ViewRateDialog extends Dialog {

    private DataManager dataManager;

    public ViewRateDialog(DataManager dataManager) {
        super("Rates", DialogSize.MEDIUM);
        this.dataManager = dataManager;
    }

    @Override
    public void populate() {
        JobRateManager jobRateManager = (JobRateManager) dataManager.getManager(JobRate.class);
        DialogPage dialogPage = new DialogPage("Rates");
        List<JobRate> jobRateList = new ArrayList<>();
        for (StoreableObject storeableObject : jobRateManager.getStoreableObjects()) {
            jobRateList.add((JobRate) storeableObject);
        }
        WideDialogElement wideDialogElement = new WideDialogElement("") {
            StoreableObjectTable<JobRate> jobRateStoreableObjectTable;

            @Override
            public Node getContent() {
                if (jobRateStoreableObjectTable == null) {
                    createElement();
                }
                return jobRateStoreableObjectTable;
            }

            @Override
            public Node createElement() {
                jobRateStoreableObjectTable = new StoreableObjectTable<>(jobRateList, true);
                jobRateStoreableObjectTable.setRowFactory(param -> {
                    TableRow<JobRate> jobRateTableRow = new TableRow<>();

                    return jobRateTableRow;
                });
                return jobRateStoreableObjectTable;
            }
        };
        dialogPage.addElement(wideDialogElement);
        addPage(dialogPage);
    }
}
