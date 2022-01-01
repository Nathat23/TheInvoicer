package com.invoicer.main;

import com.invoicer.gui.*;
import com.invoicer.gui.Dialog;
import com.invoicer.main.data.Customer;
import com.invoicer.main.data.CustomerManager;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.AttributeConfig;
import com.invoicer.sql.StoreableObject;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainWindow extends Application {

    private final List<Page> pages;
    private TheInvoicer theInvoicer;

    public MainWindow() {
        this.pages = new ArrayList<>();
    }

    @Override
    public void start(Stage stage) {
        theInvoicer = new TheInvoicer();
        theInvoicer.init();
        Dialog password = new Dialog("Login", Dialog.DialogSize.SMALL) {
            @Override
            public void populate() {
                DialogPage dialogPage = new DialogPage("Login");
                StringTextFieldElement username = new StringTextFieldElement("Username");
                StringTextFieldElement password = new PasswordTextFieldElement("Password");
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
        CustomerManager customerManager = (CustomerManager) theInvoicer.getDataManager().getManager(Customer.class);
        Page customers = new Page("Customers");
        customers.addPageElement(new PageElement() {
            @Override
            public void generate() {
                StoreableObjectTable<StoreableObject> table = new StoreableObjectTable<>(customerManager.getStoreableObjects());
                table.setRowFactory(param -> {
                    TableRow<StoreableObject> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        row.getItem().getAttributes().forEach(attribute -> attribute.setValue("bean"));
                        table.refresh();
                    });
                    return row;
                });

                addElement(table);
            }
        });
        pages.add(customers);

        int preferredColumns = 2;
        for (Page page : pages) {
            Tab tab = new Tab(page.getName());
            GridPane gridPane = new GridPane();
            gridPane.setGridLinesVisible(true);
            int size = page.getPageElementList().size();
            int actualColumns = Math.min(size, preferredColumns);
            for (int i = 0; i < size; i++) {
                int row = i / actualColumns;
                int column = i % actualColumns;
                PageElement pageElement = page.getPageElementList().get(i);
                pageElement.generateElement();
                gridPane.add(page.getPageElementList().get(i).getContainer(), column, row);
            }
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100D / actualColumns);
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
