package com.invoicer.main;

import com.invoicer.gui.Dialog;
import com.invoicer.gui.DialogPage;
import com.invoicer.gui.StringTextFieldElement;
import com.invoicer.gui.WideDialogElement;
import com.invoicer.main.data.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;

import java.util.ArrayList;
import java.util.List;

public class EditJobDialog extends Dialog {

    private Job job;
    private Customer customer;
    private final Agenda.Appointment appointment;
    private final DataManager dataManager;
    private boolean newJob;
    private final List<JobItem> jobItem;

    public EditJobDialog(DataManager dataManager, Agenda.Appointment appointment, Job job) {
        super("Edit Job", DialogSize.LARGE);
        this.appointment = appointment;
        this.dataManager = dataManager;
        this.job = job;
        this.jobItem = new ArrayList<>();
    }

    @Override
    public void populate() {
        CustomerManager customerManager = (CustomerManager) dataManager.getManager(Customer.class);
        JobManager jobManager = (JobManager) dataManager.getManager(Job.class);
        if (job == null) {
            newJob = true;
            job = (Job) jobManager.createObject();
            job.setStartDateTime(appointment.getStartLocalDateTime());
            job.setEndDateTime(appointment.getEndLocalDateTime());
        }
        DialogPage dialogPage = new DialogPage("Editing Job");
        StoredObjectBoxElement<Customer> comboBoxElement = new StoredObjectBoxElement<>("Customer");
        for (StoredObject customer : customerManager.getStoredObjects()) {
            comboBoxElement.getContent().getItems().add((Customer) customer);
        }
        comboBoxElement.getContent().getSelectionModel().selectedItemProperty().addListener((observableValue, customer1, t1) -> {
            job.setCustomerId(t1.getId());
        });
        dialogPage.addElement(comboBoxElement);
        StringTextFieldElement nameElement = new StringTextFieldElement("Name");
        nameElement.getContent().textProperty().addListener((observable, oldValue, newValue) -> {
            appointment.setSummary(newValue);
            job.setName(newValue);
        });
        dialogPage.addElement(nameElement);
        StringTextFieldElement description = new StringTextFieldElement("Description");
        description.getContent().textProperty().addListener((observable, oldValue, newValue) -> {
            appointment.setDescription(newValue);
            job.setDescription(newValue);
        });
        dialogPage.addElement(description);
        if (!newJob) {
            comboBoxElement.getContent().getSelectionModel().select((Customer) customerManager.getStoredObject(job.getCustomerId()));
            nameElement.getContent().setText(job.getName());
            description.getContent().setText(job.getDescription());
        }
        WideDialogElement wideDialogElement = new WideDialogElement("Item"){
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
                GridPane guide = new GridPane();
                guide.setPadding(new Insets(0, 5, 0, 5));
                guide.addRow(0, new Label("Name"), new Label("Units"), new Label("Cost Per Unit"));
                ColumnConstraints wide = new ColumnConstraints();
                wide.setPercentWidth(50);
                ColumnConstraints thin = new ColumnConstraints();
                thin.setPercentWidth(20);
                guide.getColumnConstraints().addAll(wide, thin, thin);
                vBox.getChildren().add(guide);
                ListView<JobItem> jobItemListView = new ListView<>();
                jobItemListView.setCellFactory(new Callback<>() {
                    @Override
                    public ListCell<JobItem> call(ListView<JobItem> param) {
                        ListCell<JobItem> listCell = new ListCell<>() {
                            @Override
                            public void updateItem(JobItem jobItem, boolean empty) {
                                super.updateItem(jobItem, empty);
                                if (jobItem == null) {
                                    return;
                                }
                                GridPane gridPane = new GridPane();
                                VBox name = new VBox();
                                Label nameLabel = new Label(jobItem.getName());
                                nameLabel.setFont(Font.font(nameLabel.getFont().getFamily(), FontWeight.BOLD, nameLabel.getFont().getSize()));
                                Label desc = new Label(jobItem.getDescription());
                                name.getChildren().addAll(nameLabel, desc);
                                Label units = new Label(jobItem.getUnits() + "");
                                JobRate rate = jobItem.getJobRate();
                                Label rateLabel = new Label(rate.getRate() + "");
                                gridPane.addRow(0, name, units, rateLabel);
                                gridPane.getColumnConstraints().addAll(guide.getColumnConstraints());
                                setGraphic(gridPane);
                            }
                        };
                        listCell.setOnMouseClicked(event -> {
                            EditJobItemDialog editJobItemDialog = new EditJobItemDialog(dataManager, job, listCell.getItem());
                            editJobItemDialog.showDialog(true);
                            jobItemListView.refresh();
                        });
                        return listCell;
                    }
                });
                for (JobItem jobItem : job.getJobItems()) {
                    jobItemListView.getItems().add(jobItem);
                }
                vBox.getChildren().add(jobItemListView);
                Button addButton = new Button("Add");
                addButton.setOnMouseClicked(mouseEvent -> {
                    EditJobItemDialog editJobItemDialog = new EditJobItemDialog(dataManager, job, null);
                    editJobItemDialog.showDialog(true);
                    if (!editJobItemDialog.isNaturalClosure()) {
                        return;
                    }
                    jobItem.add(editJobItemDialog.getJobItem());
                    jobItemListView.getItems().add(editJobItemDialog.getJobItem());
                });
                vBox.getChildren().add(addButton);
                return vBox;
            }
        };
        dialogPage.addElement(wideDialogElement);
        addPage(dialogPage);
    }

    @Override
    public void onClosure() {
        JobManager jobManager = (JobManager) dataManager.getManager(Job.class);
        jobManager.addStoredObject(job);
        JobItemManager jobItemManager = (JobItemManager) dataManager.getManager(JobItem.class);
        for (JobItem jItem : jobItem) {
            jobItemManager.addStoredObject(jItem);
        }
    }
}
