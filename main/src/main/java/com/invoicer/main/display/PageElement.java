package com.invoicer.main.display;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class PageElement implements AbstractPageElement {

    private final VBox vBox;
    private String name;

    public PageElement(String name) {
        this();
        this.name = name;
    }

    public PageElement() {
        this.vBox = new VBox();
    }

    public String getName() {
        return name;
    }

    public void generateElement() {
        if (getName() != null && getName().length() > 0) {
            Label label = new Label(getName());
            label.setId("medium-text");
            vBox.getChildren().add(label);
        }
        generate();
    }

    public VBox getContainer() {
        return vBox;
    }

    public void addElement(Node element) {
        vBox.getChildren().add(element);
    }
}
