package com.invoicer.gui;

public class StringTextFieldElement extends TextFieldElement {

    public StringTextFieldElement(String name) {
        super(name);
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
