package com.invoicer.main;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {

    private final List<PageElement> pageElementList;
    private final String name;

    public Page(String name) {
        this.pageElementList = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPageElement(PageElement element) {
        pageElementList.add(element);
    }

    public List<PageElement> getPageElementList() {
        return pageElementList;
    }

    abstract public Node generate();

    public Node generatePage() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        Label label = new Label(getName());
        label.setId("big-text");
        vBox.getChildren().addAll(label, generate());
        return vBox;
    }

}
