package com.invoicer.theinvoicer.gui;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public abstract class TextFieldElement extends StandardDialogElement implements EditableElement {

    private TextField textArea;

    public TextFieldElement(String name) {
        super(name);
    }

    @Override
    public TextField getContent() {
        if (textArea == null) {
            textArea = new TextField();
        }
        return textArea;
    }

}
