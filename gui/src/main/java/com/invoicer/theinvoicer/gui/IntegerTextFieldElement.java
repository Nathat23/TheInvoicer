package com.invoicer.theinvoicer.gui;

public class IntegerTextFieldElement extends TextFieldElement {

    public IntegerTextFieldElement(String name) {
        super(name);
    }

    @Override
    public boolean validate() {
        try {
            Integer.valueOf(getContent().getText());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Integer getValue() {
        return Integer.valueOf(getContent().getText());
    }
}
