package com.invoicer.main.display;

import com.invoicer.gui.WideDialogElement;
import com.invoicer.main.GenericModifyDialog;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.Manager;
import com.invoicer.main.data.StorageManager;
import com.invoicer.main.data.StoredObject;
import com.invoicer.main.display.StoredObjectTable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import jfxtras.scene.layout.VBox;

import java.util.Collection;

public class StoredObjectViewer extends WideDialogElement {

    private VBox vBox;
    private final Collection<StoredObject> collection;
    private final Class<? extends StoredObject> clazz;
    private final DataManager dataManager;
    private final StorageManager storageManager;

    public StoredObjectViewer(StorageManager storageManager, DataManager dataManager, Collection<StoredObject> collection, Class<? extends StoredObject> clazz) {
        super("");
        this.collection = collection;
        this.dataManager = dataManager;
        this.clazz = clazz;
        this.storageManager = storageManager;
    }

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
        StoredObjectTable<StoredObject> storedObjectTable = new StoredObjectTable<>(dataManager.getManager(clazz), collection, true);
        ButtonBar buttonBar = new ButtonBar();
        Button add = new Button("Add");
        add.setOnMouseClicked(mouseEvent -> {
            Manager manager = dataManager.getManager(clazz);
            GenericModifyDialog genericModifyDialog = new GenericModifyDialog(manager, null);
            genericModifyDialog.showDialog(true);
            storedObjectTable.getItems().add(genericModifyDialog.getObject());
        });
        buttonBar.getButtons().add(add);
        Button modify = new Button("Modify");
        modify.setOnMouseClicked(event -> {
            StoredObject selected = storedObjectTable.getSelectionModel().getSelectedItem();
            Manager manager = dataManager.getManager(clazz);
            if (selected == null) {
                return;
            }
            GenericModifyDialog genericModifyDialog = new GenericModifyDialog(manager, selected);
            genericModifyDialog.showDialog(true);
            storedObjectTable.refresh();
        });
        buttonBar.getButtons().add(modify);
        Button delete = new Button("Delete");
        delete.setOnMouseClicked(event -> {
            StoredObject selected = storedObjectTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }
            storageManager.delete(selected);
            storedObjectTable.getItems().remove(selected);
        });
        buttonBar.getButtons().add(delete);
        vBox.getChildren().addAll(buttonBar, storedObjectTable);
        return vBox;
    }
}
