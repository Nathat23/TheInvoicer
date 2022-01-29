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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableRow;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ViewRateDialog extends Dialog {

    private final DataManager dataManager;

    public ViewRateDialog(DataManager dataManager) {
        super("Rates", DialogSize.MEDIUM);
        this.dataManager = dataManager;
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
                StoreableObjectViewer<JobRate> table = new StoreableObjectViewer<>(dataManager, jobRateManager.getStoreableObjects(), JobRate.class);
                vBox.getChildren().add(table.getContent());
                return vBox;
            }
        };
        dialogPage.addElement(wideDialogElement);
        addPage(dialogPage);
    }
}
