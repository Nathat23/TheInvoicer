package com.invoicer.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class LabelElement extends WideDialogElement {

    private final String text;

    public LabelElement(String text) {
        super("Label");
        this.text = text;
    }

    @Override
    public Node getContent() {
        return createElement();
    }

    @Override
    public Node createElement() {
        return new Label(text);
    }
}
