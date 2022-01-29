package com.invoicer.gui;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

public class CheckBoxElement extends WideDialogElement implements EditableElement {

    private CheckBox radioButton;

    public CheckBoxElement(String name) {
        super(name);
    }

    @Override
    public CheckBox getContent() {
        if (radioButton == null) {
            radioButton = createElement();
        }
        return radioButton;
    }

    @Override
    public CheckBox createElement() {
        return new CheckBox(getName());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Boolean getValue() {
        return getContent().isSelected();
    }
}
