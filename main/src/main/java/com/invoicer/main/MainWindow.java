package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.gui.Dialog;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Application {

    private List<Page> pages;

    public MainWindow() {
        this.pages = new ArrayList<>();
    }

    @Override
    public void start(Stage stage) {
        Dialog password = new Dialog("Login", Dialog.DialogSize.SMALL) {
            @Override
            public void populate() {
                DialogPage dialogPage = new DialogPage("Login");
                StringTextFieldElement username = new StringTextFieldElement("Username", false);
                StringTextFieldElement password = new StringTextFieldElement("Password", true);
                dialogPage.addElement(username);
                dialogPage.addElement(password);
                dialogPage.setValidation(new CustomValidation() {
                    @Override
                    public ValidationResult validatePage() {
                       if (username.getContent().getText().equals("Beans") && password.getContent().getText().equals("Had")) {
                           return new AbstractCustomValidation.ValidationResult(true, "");
                       }
                       return new AbstractCustomValidation.ValidationResult(false, "Incorrect password!");
                    }
                });
                addPage(dialogPage);
            }
        };
        password.showDialog(true);

        if (!password.getDialogPage().isCustomValidated()) {
            return;
        }

        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Help");
        menuBar.getMenus().add(menu);
        borderPane.topProperty().setValue(menuBar);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
        tabPane.setTabMinHeight(30);
        tabPane.setTabMinWidth(50);

        Page overview = new Page("Overview");
        overview.addPageElement(new PageElement("Nice") {
            @Override
            public void generate() {
                addElement(new Label("Haha"));
            }
        });
        overview.addPageElement(new PageElement("But") {
            @Override
            public void generate() {
                addElement(new Label("Oh"));
            }
        });
        overview.addPageElement(new PageElement("Da") {
            @Override
            public void generate() {
                addElement(new Label("Sure"));
            }
        });
        Page timetable = new Page("Timetable");
        timetable.addPageElement(new PageElement("Nice") {
            @Override
            public void generate() {
                addElement(new Label("Haha"));
            }
        });
        timetable.addPageElement(new PageElement("But") {
            @Override
            public void generate() {
                addElement(new Label("Oh"));
            }
        });
        timetable.addPageElement(new PageElement("Da") {
            @Override
            public void generate() {
                addElement(new Label("Sure"));
            }
        });
        pages.add(overview);
        pages.add(timetable);

        int rows = 2;
        for (Page page : pages) {
            Tab tab = new Tab(page.getName());
            GridPane gridPane = new GridPane();
            int size = page.getPageElementList().size();
            for (int i = 0; i < size; i++) {
                int row = i / rows;
                int remainder = i % rows;
                PageElement pageElement = page.getPageElementList().get(i);
                pageElement.generateElement();
                gridPane.add(page.getPageElementList().get(i).getContainer(), row, remainder);
            }
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100D / rows);
            for (int i = 0; i < gridPane.getColumnCount(); i++) {
                gridPane.getColumnConstraints().add(columnConstraints);
            }
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(10));
            Label label = new Label(page.getName());
            label.setId("big-text");
            vBox.getChildren().addAll(label, gridPane);
            tab.setContent(vBox);
            tabPane.getTabs().add(tab);
        }

        borderPane.centerProperty().setValue(tabPane);

        Scene scene = new Scene(borderPane, 1200, 700);
        scene.getStylesheets().add("global.css");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
