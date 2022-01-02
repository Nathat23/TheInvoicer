package com.invoicer.main;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ListPage extends Page {

    public ListPage(String name) {
        super(name);
    }

    @Override
    public Node generate() {
        VBox vBox = new VBox();
        for (PageElement pageElement : getPageElementList()) {
            pageElement.generateElement();
            vBox.getChildren().add(pageElement.getContainer());
        }
        return vBox;
    }
}
