package com.invoicer.theinvoicer.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public abstract class Dialog implements AbstractDialog {

    private final String name;
    private GridPane gridPane;
    private Stage stage;
    private Scene scene;
    private final DialogSize dialogSize;
    private final Set<DialogElement> dialogElement;

    public Dialog(String name, DialogSize dialogSize) {
        this.name = name;
        this.dialogSize = dialogSize;
        this.dialogElement = new HashSet<>();
    }

    public void showDialog() {
        if (name == null) {
            throw new UnsupportedOperationException("No dialog name");
        }
        if (dialogSize == null) {
            throw new UnsupportedOperationException("No dialog size specified");
        }
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setId("header-box");
        Label label = new Label(name);
        label.setId("big-text");
        vBox.getChildren().add(label);
        borderPane.topProperty().setValue(vBox);
        gridPane = new GridPane();
        borderPane.centerProperty().setValue(gridPane);

        populate();
        stage = new Stage();
        scene = new Scene(borderPane, dialogSize.getWidth(), dialogSize.getHeight());
        scene.getStylesheets().add("dialog.css");
        stage.setScene(scene);
        stage.show();
    }

    public void addElement(DialogElement element) {
        dialogElement.add(element);
    }

    enum DialogSize {
        SMALL(700, 200),
        MEDIUM(500, 400),
        LARGE(700, 400);

        int width;
        int height;
        DialogSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
