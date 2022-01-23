package com.invoicer.gui;

import javafx.geometry.HPos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class DialogPage {

    private final List<DialogElement> elementList;
    private final String pageTitle;
    private GridPane contents;
    private CustomValidation validation;

    public DialogPage(String pageTitle) {
        this.pageTitle = pageTitle;
        this.elementList = new ArrayList<>();
    }

    public void addElement(DialogElement element) {
        elementList.add(element);
    }

    public List<DialogElement> getElementList() {
        return elementList;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public GridPane getContents() {
        return contents;
    }

    public void generate() {
        if (contents != null) {
            throw new UnsupportedOperationException("Page has already been generated!");
        }
        contents = new GridPane();
        contents.setId("grid");
        for (AbstractElement element : elementList) {
            if (element instanceof WideDialogElement) {
                contents.add(element.getContent(), 0, contents.getRowCount(), 2, 1);
                continue;
            }
            Label name = new Label(element.getName() + ":");
            contents.addRow(contents.getRowCount(), name, element.getContent());
            ((Region) element.getContent()).setMaxWidth(Double.MAX_VALUE);
        }
        contents.getColumnConstraints().add(new ColumnConstraints());
        contents.getColumnConstraints().add(new ColumnConstraints(200, Control.USE_PREF_SIZE, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
    }

    public void setValidation(CustomValidation validation) {
        this.validation = validation;
    }

    public CustomValidation getValidation() {
        return validation;
    }

    public boolean isCustomValidated() {
        if (getValidation() == null) {
            return true;
        }
        if (getValidation().getValidationResult() == null) {
            return false;
        }
        return getValidation().getValidationResult().isValid();
    }

    public boolean validateContents() {
        for (DialogElement dialogElement : getElementList()) {
            if (!(dialogElement instanceof EditableElement)) {
                continue;
            }
            if (!((EditableElement) dialogElement).validate()) {
                return false;
            }
            if (dialogElement instanceof TextFieldElement) {
                if (((TextFieldElement) dialogElement).getContent().getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
