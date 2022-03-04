package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.WideDialogElement;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.JobRate;
import com.invoicer.main.data.JobRateManager;
import com.invoicer.main.data.StorageManager;
import com.invoicer.main.display.StoredObjectViewer;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ViewRateDialog extends Dialog {

    private final DataManager dataManager;
    private final StorageManager storageManager;

    public ViewRateDialog(StorageManager storageManager, DataManager dataManager) {
        super("Rates", DialogSize.MEDIUM);
        this.dataManager = dataManager;
        this.storageManager = storageManager;
    }

    @Override
    public void populate() {
        JobRateManager jobRateManager = (JobRateManager) dataManager.getManager(JobRate.class);
        DialogPage dialogPage = new DialogPage("Rates");
        WideDialogElement wideDialogElement = new WideDialogElement("") {
            VBox vBox;

            @Override
            public Node getContent() {
                if (vBox == null) {
                    vBox = createElement();
                }
                return vBox;
            }

            @Override
            public VBox createElement() {
                VBox vBox = new VBox();
                StoredObjectViewer table = new StoredObjectViewer(storageManager, dataManager, jobRateManager.getStoredObjects(), JobRate.class);
                vBox.getChildren().add(table.getContent());
                return vBox;
            }
        };
        dialogPage.addElement(wideDialogElement);
        addPage(dialogPage);
    }
}
