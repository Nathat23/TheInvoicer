package com.invoicer.theinvoicer.gui;

import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Dialog dialog = new Dialog("Hi", Dialog.DialogSize.SMALL) {
            @Override
            public void populate() {
                DialogPage page = new DialogPage("Beans");
                page.addElement(new StringTextFieldElement("Ohh"));
                page.addElement(new LabelElement("Oh!"));
                page.addElement(new StringTextFieldElement("Neat"));
                DialogPage page1 = new DialogPage("Boo");
                page1.addElement(new StringTextFieldElement("Happy birthday"));
                page1.addElement(new StringTextFieldElement("It is the day"));
                addPage(page);
                addPage(page1);
            }
        };
        dialog.showDialog();
    }
}
