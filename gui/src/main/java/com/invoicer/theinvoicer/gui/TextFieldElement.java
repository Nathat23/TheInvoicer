package com.invoicer.theinvoicer.gui;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TextFieldElement<T> extends StandardDialogElement implements EditableElement {

    private TextField textArea;
    private Class<T> tClass;

    public TextFieldElement(String name, Class<T>) {
        super(name);
    }

    @Override
    public Node getContent() {
        if (textArea == null) {
            textArea = new TextField();
        }
        return textArea;
    }

    @Override
    public boolean validate() {
        try {
            Integer.valueOf()
        } catch (NumberFormatException e)
        return false;
    }
}
