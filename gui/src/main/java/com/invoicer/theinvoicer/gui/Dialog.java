package com.invoicer.theinvoicer.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public abstract class Dialog implements AbstractDialog {

    private final String name;
    private Stage stage;
    private Scene scene;
    private final DialogSize dialogSize;
    private final List<DialogPage> pageList;
    private int currentPage;
    private BorderPane borderPane;
    private Label pageLabel;
    private VBox progressBox;

    public Dialog(String name, DialogSize dialogSize) {
        this.name = name;
        this.dialogSize = dialogSize;
        this.pageList = new ArrayList<>();
        this.borderPane = new BorderPane();
    }

    public void showDialog() {
        if (name == null) {
            throw new UnsupportedOperationException("No dialog name");
        }
        if (dialogSize == null) {
            throw new UnsupportedOperationException("No dialog size specified");
        }
        populate();
        if (getPageList().size() == 0) {
            throw new UnsupportedOperationException("No pages have been added to dialog!");
        }
        // Top bar
        VBox vBox = new VBox();
        vBox.setId("header-box");
        pageLabel = new Label(name);
        pageLabel.setId("big-text");
        vBox.getChildren().add(pageLabel);
        borderPane.topProperty().setValue(vBox);
        // Main content
        for (DialogPage page : pageList) {
            page.generate();
        }
        borderPane.centerProperty().setValue(getDialogPage().getContents());
        // Left bar
        progressBox = new VBox();
        progressBox.setId("progress-vbar");
        updateStatus();
        borderPane.leftProperty().setValue(progressBox);
        // Bottom bar
        HBox hBox = new HBox();
        hBox.setId("button-box");
        Button button = new Button("Next");
        button.setOnAction(actionEvent -> {
            nextPage();
        });
        hBox.getChildren().add(button);
        borderPane.bottomProperty().setValue(hBox);

        stage = new Stage();
        stage.setTitle(name);
        scene = new Scene(borderPane, dialogSize.getWidth(), dialogSize.getHeight());
        scene.getStylesheets().add("dialog.css");
        stage.setScene(scene);
        stage.show();
    }

    public int getCurrentPageId() {
        return currentPage;
    }

    public DialogPage getDialogPage() {
        return getPageList().get(getCurrentPageId());
    }

    public List<DialogPage> getPageList() {
        return pageList;
    }

    public void addPage(DialogPage page) {
        getPageList().add(page);
    }

    public void nextPage() {
        currentPage = getCurrentPageId() == getPageList().size() - 1 ? 0 : getCurrentPageId() + 1;
        update();
    }

    public void update() {
        borderPane.centerProperty().setValue(getDialogPage().getContents());
        pageLabel.setText(getDialogPage().getPageTitle());
        updateStatus();
    }

    private void updateStatus() {
        GridPane gridPane = new GridPane();
        gridPane.setId("progress-grid");
        int i = 0;
        for (DialogPage page : getPageList()) {
            if (i < getCurrentPageId()) {
                gridPane.add(new Label("\u2713"), 0, i);
            }
            Label label = new Label(page.getPageTitle());
            if (i == getCurrentPageId()) {
                gridPane.add(new Label("\u2794"), 0, i);
                label.setFont(Font.font(label.getFont().getFamily(), FontWeight.findByWeight(750), label.getFont().getSize()));
            }
            gridPane.add(label, 1, i);
            i++;
        }
        gridPane.getColumnConstraints().add(new ColumnConstraints(15));
        if (progressBox.getChildren().isEmpty()) {
            progressBox.getChildren().add(gridPane);
            return;
        }
        progressBox.getChildren().set(0, gridPane);
    }

    enum DialogSize {
        SMALL(500, 300),
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
