package com.invoicer.main;

import com.invoicer.gui.WideDialogElement;
import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.Manager;
import com.invoicer.sql.StoreableObject;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import jfxtras.scene.layout.VBox;

import java.util.Collection;

public class StoreableObjectViewer<T extends StoreableObject> extends WideDialogElement {

    private VBox vBox;
    private Collection<StoreableObject> collection;
    private Class<T> clazz;
    private DataManager dataManager;

    public StoreableObjectViewer(DataManager dataManager, Collection<StoreableObject> collection, Class<T> clazz) {
        super("");
        this.collection = collection;
        this.dataManager = dataManager;
        this.clazz = clazz;
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
        StoreableObjectTable<StoreableObject> storeableObjectTable = new StoreableObjectTable<>(dataManager.getManager(clazz), collection, true);
        ButtonBar buttonBar = new ButtonBar();
        Button add = new Button("Add");
        add.setOnMouseClicked(mouseEvent -> {
            Manager manager = dataManager.getManager(clazz);
            GenericModifyDialog genericModifyDialog = new GenericModifyDialog(manager, null);
            genericModifyDialog.showDialog(true);
            storeableObjectTable.getItems().add(genericModifyDialog.getObject());
        });
        buttonBar.getButtons().add(add);
        Button modify = new Button("Modify");
        modify.setOnMouseClicked(event -> {
            StoreableObject selected = storeableObjectTable.getSelectionModel().getSelectedItem();
            Manager manager = dataManager.getManager(clazz);
            if (selected == null) {
                return;
            }
            GenericModifyDialog genericModifyDialog = new GenericModifyDialog(manager, selected);
            genericModifyDialog.showDialog(true);
            storeableObjectTable.refresh();
        });
        buttonBar.getButtons().add(modify);
        vBox.getChildren().addAll(buttonBar, storeableObjectTable);
        return vBox;
    }
}
