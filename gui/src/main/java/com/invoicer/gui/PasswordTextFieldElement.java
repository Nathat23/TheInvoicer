package com.invoicer.gui;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordTextFieldElement extends StringTextFieldElement {

    private PasswordField field;

    public PasswordTextFieldElement(String name) {
        super(name);
    }

    @Override
    public TextField getContent() {
        if (field == null) {
            field = new PasswordField();
        }
        return field;
    }
}
