package com.invoicer.gui;

import javafx.scene.control.ComboBox;

import java.util.List;

public class ComboBoxElement<T> extends StandardDialogElement implements EditableElement {

    private ComboBox<T> comboBox;

    public ComboBoxElement(String name) {
        super(name);
    }

    @Override
    public ComboBox<T> getContent() {
        if (comboBox == null) {
            comboBox = createElement();
            comboBox.setEditable(false);
        }
        return comboBox;
    }

    @Override
    public ComboBox<T> createElement() {
        return new ComboBox<>();
    }

    public void addItems(List<T> items) {
        getContent().getItems().addAll(items);
    }

    public void addItem(T item) {
        getContent().getItems().add(item);
    }

    @Override
    public boolean validate() {
        return !getContent().getSelectionModel().isEmpty();
    }

    @Override
    public T getValue() {
        return comboBox.getValue();
    }
}
