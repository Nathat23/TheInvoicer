package com.invoicer.main;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class PageElement implements AbstractPageElement {

    private final VBox vBox;
    private String name;
    private final List<Node> nodes;

    public PageElement(String name) {
        this();
        this.name = name;
    }

    public PageElement() {
        this.vBox = new VBox();
        this.nodes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void generateElement() {
        Label label = new Label(getName());
        label.setId("medium-text");
        vBox.getChildren().add(label);
        vBox.getChildren().addAll(nodes);
        generate();
    }

    public VBox getContainer() {
        return vBox;
    }

    public void addElement(Node element) {
        vBox.getChildren().add(element);
    }
}
