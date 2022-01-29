package com.invoicer.main;

import com.invoicer.main.data.DataManager;
import com.invoicer.main.data.Manager;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.StoreableObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class StoreableObjectTable<T extends StoreableObject> extends TableView<T> {

    private final Collection<T> storeableObjects;
    private Manager manager;

    public StoreableObjectTable(Manager manager, Collection<T> storeableObjects, boolean showId) {
        this.storeableObjects = storeableObjects;
        this.manager = manager;
        init(showId);
    }

    public void init(boolean showId) {
        Optional<T> optionalT = storeableObjects.stream().findFirst();
        if (optionalT.isEmpty()) {
            return;
        }
        T object = optionalT.get();
        TableColumn<T, Integer> id = new TableColumn<>("Identifier: ");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (showId) {
            getColumns().add(id);
        }
        for (Attribute attribute : object.getAttributes()) {
            TableColumn<T, String> column = new TableColumn<>(attribute.getAttributeConfig().getHuman() + ": ");
            column.setCellValueFactory(as -> {
                for (Attribute attribute2 : as.getValue().getAttributes()) {
                    if (attribute.getName().equals(attribute2.getName())) {
                        return new SimpleStringProperty(String.valueOf(attribute2.getValue()));
                    }
                }
                return new SimpleStringProperty("invalid");
            });
            getColumns().add(column);
        }
        setItems(FXCollections.observableArrayList(storeableObjects));
        id.setSortType(TableColumn.SortType.ASCENDING);
        getSortOrder().add(id);
        sort();
        setRowFactory(param -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() != 2) {
                    return;
                }
                GenericModifyDialog modifyDialog = new GenericModifyDialog(manager, row.getItem());
                modifyDialog.showDialog(true);
                refresh();
            });
            return row;
        });
    }
}
