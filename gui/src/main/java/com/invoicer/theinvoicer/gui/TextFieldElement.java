package com.invoicer.theinvoicer.gui;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public abstract class TextFieldElement extends StandardDialogElement implements EditableElement {

    private TextField textArea;
    private final boolean passwordField;

    public TextFieldElement(String name, boolean passwordField) {
        super(name);
        this.passwordField = passwordField;
    }

    @Override
    public TextField getContent() {
        if (textArea != null) {
            return textArea;
        }
        if (passwordField) {
            textArea = new PasswordField();
            return textArea;
        }
        textArea = new TextField();
        return textArea;
    }

}
