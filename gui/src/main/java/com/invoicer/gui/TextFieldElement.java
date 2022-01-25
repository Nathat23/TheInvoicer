package com.invoicer.gui;

import javafx.scene.control.TextField;

public abstract class TextFieldElement extends StandardDialogElement implements EditableElement {

    private TextField textArea;

    public TextFieldElement(String name) {
        super(name);
    }

    @Override
    public TextField getContent() {
        if (textArea == null) {
            textArea = createElement();
        }
        return textArea;
    }

    @Override
    public TextField createElement() {
        return new TextField();
    }
}
