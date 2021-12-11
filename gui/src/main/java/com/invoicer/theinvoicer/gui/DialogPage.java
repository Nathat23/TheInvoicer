package com.invoicer.theinvoicer.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class DialogPage {

    private final List<DialogElement> elementList;
    private final String pageTitle;
    private GridPane contents;

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
        }
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
