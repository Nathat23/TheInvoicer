package com.invoicer.theinvoicer.gui;

import javafx.application.Application;
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

            }
        };
        dialog.showDialog();
    }
}
