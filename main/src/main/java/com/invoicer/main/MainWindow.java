package com.invoicer.main;

import com.invoicer.gui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
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

        if (!password.getDialogPage().isUserValidated()) {
            return;
        }

        System.out.println("sfafjk");
        Label test = new Label("asd");
        Scene scene = new Scene(test, 1000, 100);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
