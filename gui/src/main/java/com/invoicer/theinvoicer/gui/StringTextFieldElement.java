package com.invoicer.theinvoicer.gui;

public class StringTextFieldElement extends TextFieldElement {

    public StringTextFieldElement(String name, boolean passwordField) {
        super(name, passwordField);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getValue() {
        return getContent().getText();
    }

}
