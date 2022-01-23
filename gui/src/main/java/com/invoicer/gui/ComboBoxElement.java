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
            comboBox = new ComboBox<>();
            comboBox.setEditable(false);
        }
        return comboBox;
    }

    public void addItems(List<T> items) {
        getContent().getItems().addAll(items);
    }

    public void addItem(T item) {
        getContent().getItems().add(item);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public T getValue() {
        return comboBox.getValue();
    }
}
