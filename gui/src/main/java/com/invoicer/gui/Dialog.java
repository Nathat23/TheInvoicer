package com.invoicer.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class Dialog implements AbstractDialog {

    private final String name;
    private Stage stage;
    private Scene scene;
    private final DialogSize dialogSize;
    private final List<DialogPage> pageList;
    private int currentPage;
    private final BorderPane borderPane;
    private Label pageLabel;
    private VBox progressBox;
    private Button nextButton;
    private Button cancelButton;
    private boolean naturalClosure;
    private PageChange pageChange;

    public Dialog(String name, DialogSize dialogSize) {
        this.name = name;
        this.dialogSize = dialogSize;
        this.pageList = new ArrayList<>();
        this.borderPane = new BorderPane();
    }

    @Override
    public void showDialog() {
        showDialog(false);
    }

    @Override
    public void onClosure() {

    }

    public void showDialog(boolean wait) {
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
        stage = new Stage();
        stage.setTitle(name);
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
        ButtonBar hBox = new ButtonBar();
        hBox.setId("button-box");
        Label label = new Label();
        label.setId("error-text");
        cancelButton = new Button("Cancel");
        cancelButton.setOnMouseClicked(mouseEvent -> {
            stage.close();
        });
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        hBox.getButtons().add(cancelButton);
        nextButton = new Button("Next");
        nextButton.setOnAction(actionEvent -> {
            if (getDialogPage().getValidation() != null) {
                CustomValidation validation = getDialogPage().getValidation();
                CustomValidation.ValidationResult validationResult = validation.validate();
                if (!validationResult.isValid()) {
                    label.setText(validationResult.getErrorMessage());
                    return;
                }
            }
            if (currentPage == getPageList().size() - 1) {
                onClosure();
                stage.close();
                naturalClosure = true;
            }
            nextPage();
        });
        nextButton.setText(getCurrentPageId() == getPageList().size() - 1 ? "Exit" : "Next");
        nextButton.setDisable(true);
        ButtonBar.setButtonData(nextButton, ButtonBar.ButtonData.NEXT_FORWARD);

        for (DialogPage dialogPage : pageList) {
            dialogPage.getContents().setOnMouseMoved(event -> nextButton.setDisable(!dialogPage.validateContents()));
        }
        hBox.getButtons().addAll(label, nextButton);
        borderPane.bottomProperty().setValue(hBox);

        scene = new Scene(borderPane, dialogSize.getWidth(), dialogSize.getHeight());
        scene.getStylesheets().add("dialog.css");
        stage.setScene(scene);
        if (wait) {
            stage.showAndWait();
            return;
        }
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
        if (pageChange != null) {
            pageChange.onPageChange(getDialogPage());
        }
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

    public boolean isNaturalClosure() {
        return naturalClosure;
    }

    public void setPageChange(PageChange pageChange) {
        this.pageChange = pageChange;
    }

    public Window getWindow() {
        return stage;
    }

    public enum DialogSize {
        SMALL(500, 300),
        MEDIUM(500, 400),
        LARGE(700, 400),
        LONG(700, 600);

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

    public interface PageChange {
        void onPageChange(DialogPage newPage);
    }
}
